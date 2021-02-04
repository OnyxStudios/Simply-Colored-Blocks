package dev.onyxstudios.simplycoloredblocks.network;

import dev.onyxstudios.simplycoloredblocks.SimplyColoredBlocks;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class ModPackets {

    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(SimplyColoredBlocks.MODID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public static void registerPackets() {
        INSTANCE.registerMessage(0, UpdateColorMessage.class, UpdateColorMessage::encode, UpdateColorMessage::decode, UpdateColorMessage::handleMessage);
        INSTANCE.registerMessage(1, ColorBlockMessage.class, ColorBlockMessage::encode, ColorBlockMessage::decode, ColorBlockMessage::handleMessage);
    }
}
