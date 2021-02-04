package dev.onyxstudios.simplycoloredblocks;

import dev.onyxstudios.simplycoloredblocks.network.ModPackets;
import dev.onyxstudios.simplycoloredblocks.registry.ModItems;
import dev.onyxstudios.simplycoloredblocks.registry.ModClient;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(SimplyColoredBlocks.MODID)
public class SimplyColoredBlocks {

    public static final String MODID = "simplycoloredblocks";
    public static ItemGroup TAB = new ItemGroup(MODID) {
        @Override
        public ItemStack createIcon() {
            return ModItems.blockRegistry.getEntries().stream().findFirst().get().get().asItem().getDefaultInstance();
        }
    };

    public SimplyColoredBlocks() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::init);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::initClient);
        ModItems.blockRegistry.register(FMLJavaModLoadingContext.get().getModEventBus());
        ModItems.itemRegistry.register(FMLJavaModLoadingContext.get().getModEventBus());
        ModItems.containersRegistry.register(FMLJavaModLoadingContext.get().getModEventBus());
        ModItems.registerBlocks();
    }

    public void init(FMLCommonSetupEvent event) {
        ModPackets.registerPackets();
    }

    public void initClient(FMLClientSetupEvent event) {
        ModClient.register(event);
    }
}
