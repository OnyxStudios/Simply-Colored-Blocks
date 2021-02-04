package dev.onyxstudios.simplycoloredblocks.client.container.slots;

import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class SlotStone extends SlotItemHandler {

    public SlotStone(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        if(stack.isEmpty())
            return false;

        return stack.isItemEqualIgnoreDurability(Blocks.STONE.asItem().getDefaultInstance());
    }
}
