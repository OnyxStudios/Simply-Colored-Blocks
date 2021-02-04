package dev.onyxstudios.simplycoloredblocks.items;

import dev.onyxstudios.simplycoloredblocks.SimplyColoredBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.text.*;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.ToolType;

import java.util.Arrays;
import java.util.List;

public class ColoredBlock extends Block {

    private int color;

    public ColoredBlock(int color) {
        super(Properties.create(Material.ROCK).hardnessAndResistance(1, 1).harvestTool(ToolType.PICKAXE).notSolid());
        this.color = color;
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
        return Arrays.asList(new ItemStack(this));
    }

    @Override
    public String getTranslationKey() {
        return Util.makeTranslationKey("block", new ResourceLocation(SimplyColoredBlocks.MODID, "colored_block"));
    }

    @Override
    public void addInformation(ItemStack stack, IBlockReader world, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, world, tooltip, flagIn);
        String hexColor = String.format("#%06X", (0xFFFFFF & getColor()));
        IFormattableTextComponent text = new StringTextComponent("Color: " + hexColor).setStyle(Style.EMPTY.setFormatting(TextFormatting.GOLD));
        tooltip.add(text);
    }

    public int getColor() {
        return color;
    }
}
