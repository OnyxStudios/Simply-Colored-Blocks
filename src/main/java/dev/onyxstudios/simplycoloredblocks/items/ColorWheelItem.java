package dev.onyxstudios.simplycoloredblocks.items;

import dev.onyxstudios.simplycoloredblocks.SimplyColoredBlocks;
import dev.onyxstudios.simplycoloredblocks.client.container.ColorWheelContainer;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import java.util.List;

public class ColorWheelItem extends Item {

    public ColorWheelItem() {
        super(new Item.Properties().group(SimplyColoredBlocks.TAB).maxStackSize(1));
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
        if(!world.isRemote()) {
            NetworkHooks.openGui((ServerPlayerEntity) player, new INamedContainerProvider() {
                @Override
                public ITextComponent getDisplayName() {
                    return player.getHeldItem(hand).getDisplayName();
                }

                @Override
                public Container createMenu(int windowId, PlayerInventory playerInventory, PlayerEntity playerEntity) {
                    return new ColorWheelContainer(windowId, playerInventory, new ColorWheelInventory(playerEntity), player.getHeldItem(hand));
                }
            }, buffer -> buffer.writeItemStack(player.getHeldItem(hand)));
        }

        return ActionResult.resultPass(player.getHeldItem(hand));
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        tooltip.add(new StringTextComponent("Color stone any color using dye types"));
    }

    @Override
    public Rarity getRarity(ItemStack stack) {
        return Rarity.EPIC;
    }
}
