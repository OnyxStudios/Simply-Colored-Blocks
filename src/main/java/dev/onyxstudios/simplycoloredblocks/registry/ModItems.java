package dev.onyxstudios.simplycoloredblocks.registry;

import dev.onyxstudios.simplycoloredblocks.SimplyColoredBlocks;
import dev.onyxstudios.simplycoloredblocks.client.container.ColorWheelContainer;
import dev.onyxstudios.simplycoloredblocks.items.ColorWheelInventory;
import dev.onyxstudios.simplycoloredblocks.items.ColorWheelItem;
import dev.onyxstudios.simplycoloredblocks.items.ColoredBlock;
import dev.onyxstudios.simplycoloredblocks.utils.ColorUtils;
import net.minecraft.block.Block;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.Map;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModItems {

    public static final DeferredRegister<Block> blockRegistry = DeferredRegister.create(ForgeRegistries.BLOCKS, SimplyColoredBlocks.MODID);
    public static final DeferredRegister<Item> itemRegistry = DeferredRegister.create(ForgeRegistries.ITEMS, SimplyColoredBlocks.MODID);
    public static final DeferredRegister<ContainerType<?>> containersRegistry = DeferredRegister.create(ForgeRegistries.CONTAINERS, SimplyColoredBlocks.MODID);
    public static Map<Integer, ColoredBlock> colors = new HashMap<>();

    public static RegistryObject<Item> colorWheel = itemRegistry.register("color_wheel", () -> new ColorWheelItem());
    public static RegistryObject<ContainerType<ColorWheelContainer>> colorWheelContainer = containersRegistry.register("color_wheel_container", () ->
            IForgeContainerType.create((windowId, inv, data) -> new ColorWheelContainer(windowId, inv, new ColorWheelInventory(inv.player), data.readItemStack())));

    public static  void registerBlocks() {
        for (int r = 0; r < 255; r+=16) {
            for (int g = 0; g < 255; g+=16) {
                for (int b = 0; b < 255; b+=16) {
                    int color = ColorUtils.createHex(r, g, b);
                    ColoredBlock block = new ColoredBlock(color);
                    blockRegistry.register("colored_block" + color, () -> block);
                    itemRegistry.register("colored_block" + color, () -> new BlockItem(block, new Item.Properties().group(SimplyColoredBlocks.TAB)));
                    colors.put(color, block);
                }
            }
        }
    }
}
