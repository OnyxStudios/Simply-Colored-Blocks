package dev.onyxstudios.simplycoloredblocks.registry;

import dev.onyxstudios.simplycoloredblocks.client.gui.ColorWheelScreen;
import dev.onyxstudios.simplycoloredblocks.items.ColoredBlock;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class ModClient {

    public static void register(FMLClientSetupEvent event) {
        ScreenManager.registerFactory(ModItems.colorWheelContainer.get(), ColorWheelScreen::new);

        Minecraft minecraft = event.getMinecraftSupplier().get();
        for (RegistryObject<Block> object : ModItems.blockRegistry.getEntries()) {
            if(object.get() instanceof ColoredBlock) {
                ColoredBlock block = (ColoredBlock) object.get();
                minecraft.getBlockColors().register((state, displayReader, pos, tintIndex) -> block.getColor(), block);
                minecraft.getItemColors().register((itemStack, tintIndex) -> block.getColor(), block);
            }
        }
    }
}
