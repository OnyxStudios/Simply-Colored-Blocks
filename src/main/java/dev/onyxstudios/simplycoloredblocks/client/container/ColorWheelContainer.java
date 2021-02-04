package dev.onyxstudios.simplycoloredblocks.client.container;

import dev.onyxstudios.simplycoloredblocks.client.container.slots.LockedSlot;
import dev.onyxstudios.simplycoloredblocks.client.container.slots.SlotDye;
import dev.onyxstudios.simplycoloredblocks.client.container.slots.SlotStone;
import dev.onyxstudios.simplycoloredblocks.registry.ModItems;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

import java.util.List;

public class ColorWheelContainer extends Container {

    public ItemStackHandler inventory;

    public ColorWheelContainer(int id, PlayerInventory playerInventory, ItemStackHandler inventory, ItemStack stack) {
        super(ModItems.colorWheelContainer.get(), id);
        this.inventory = inventory;

        this.addSlot(new SlotStone(inventory, 0, 8, 8));
        this.addSlot(new SlotDye(inventory, 1, 8, 44));

        //Add player inventory slots
        for (int row = 0; row < 9; row++) {
            int x = 8 + row * 18;
            int y = 56 + 86;
            if (row == getSlotFor(playerInventory, stack)) {
                addSlot(new LockedSlot(playerInventory, row, x, y));
                continue;
            }

            addSlot(new Slot(playerInventory, row, x, y));
        }

        for (int row = 1; row < 4; row++) {
            for (int col = 0; col < 9; col++) {
                int x = 8 + col * 18;
                int y = row * 18 + (56 + 10);
                addSlot(new Slot(playerInventory, col + row * 9, x, y));
            }
        }
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
    }

    @Override
    public void onContainerClosed(PlayerEntity player) {
        super.onContainerClosed(player);

        if(!player.world.isRemote()) {
            for (int i = 0; i < inventory.getSlots(); i++) {
                if (!inventory.getStackInSlot(i).isEmpty()) {
                    ItemEntity item = new ItemEntity(player.world, player.getPosX(), player.getPosY(), player.getPosZ(), inventory.getStackInSlot(i));
                    item.setDefaultPickupDelay();
                    player.world.addEntity(item);
                }
            }
        }
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return true;
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        return handleShiftClick(playerIn, index);
    }

    public ItemStack handleShiftClick(PlayerEntity player, int slotIndex) {
        List<Slot> slots = inventorySlots;
        Slot sourceSlot = slots.get(slotIndex);
        ItemStack inputStack = sourceSlot.getStack();
        if (inputStack == ItemStack.EMPTY) {
            return ItemStack.EMPTY;
        }

        boolean sourceIsPlayer = sourceSlot.inventory == player.inventory;

        ItemStack copy = inputStack.copy();

        if (sourceIsPlayer) {
            if (!mergeStack(player.inventory, false, sourceSlot, slots, false)) {
                return ItemStack.EMPTY;
            } else {
                return copy;
            }
        } else {
            boolean isMachineOutput = !sourceSlot.canTakeStack(player);
            if (!mergeStack(player.inventory, true, sourceSlot, slots, !isMachineOutput)) {
                return ItemStack.EMPTY;
            } else {
                return copy;
            }
        }
    }

    private boolean mergeStack(PlayerInventory playerInv, boolean mergeIntoPlayer, Slot sourceSlot, List<Slot> slots, boolean reverse) {
        ItemStack sourceStack = sourceSlot.getStack();
        int originalSize = sourceStack.getCount();

        int len = slots.size();
        int idx;

        if (sourceStack.isStackable()) {
            idx = reverse ? len - 1 : 0;

            while (sourceStack.getCount() > 0 && (reverse ? idx >= 0 : idx < len)) {
                Slot targetSlot = slots.get(idx);
                if ((targetSlot.inventory == playerInv) == mergeIntoPlayer) {
                    ItemStack target = targetSlot.getStack();
                    if (ItemStack.areItemsEqual(sourceStack, target)) {
                        int targetMax = Math.min(targetSlot.getSlotStackLimit(), target.getMaxStackSize());
                        int toTransfer = Math.min(sourceStack.getCount(), targetMax - target.getCount());
                        if (toTransfer > 0) {
                            target.grow(toTransfer);
                            sourceSlot.decrStackSize(toTransfer);
                            targetSlot.onSlotChanged();
                        }
                    }
                }

                if (reverse) {
                    idx--;
                } else {
                    idx++;
                }
            }
            if (sourceStack.getCount() == 0) {
                sourceSlot.putStack(ItemStack.EMPTY);
                return true;
            }
        }

        idx = reverse ? len - 1 : 0;
        while (reverse ? idx >= 0 : idx < len) {
            Slot targetSlot = slots.get(idx);
            if ((targetSlot.inventory == playerInv) == mergeIntoPlayer && !targetSlot.getHasStack() && targetSlot.isItemValid(sourceStack)) {
                targetSlot.putStack(sourceStack);
                sourceSlot.putStack(ItemStack.EMPTY);
                return true;
            }

            if (reverse) {
                idx--;
            } else {
                idx++;
            }
        }

        if (sourceStack.getCount() != originalSize) {
            sourceSlot.onSlotChanged();
            return true;
        }

        return false;
    }

    public int getSlotFor(PlayerInventory inventory, ItemStack stack) {
        for (int i = 0; i < inventory.mainInventory.size(); ++i) {
            if (!inventory.mainInventory.get(i).isEmpty() && stackEqualExact(stack, inventory.mainInventory.get(i))) {
                return i;
            }
        }

        return -1;
    }

    private boolean stackEqualExact(ItemStack stack1, ItemStack stack2) {
        return stack1.getItem() == stack2.getItem() && ItemStack.areItemStackTagsEqual(stack1, stack2);
    }
}
