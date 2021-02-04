package dev.onyxstudios.simplycoloredblocks.client.container.slots;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Tags;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class SlotDye extends SlotItemHandler {

    public SlotDye(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        if(stack.isEmpty())
            return false;

        return Tags.Items.DYES.contains(stack.getItem());
    }
}
