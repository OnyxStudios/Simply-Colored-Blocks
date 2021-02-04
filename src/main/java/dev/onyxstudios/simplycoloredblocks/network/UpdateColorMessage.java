package dev.onyxstudios.simplycoloredblocks.network;

import dev.onyxstudios.simplycoloredblocks.client.gui.ColorWheelScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class UpdateColorMessage {

    public UpdateColorMessage() {
    }

    public void encode(PacketBuffer buffer) {
    }

    public static UpdateColorMessage decode(PacketBuffer buffer) {
        return new UpdateColorMessage();
    }

    public static void handleMessage(UpdateColorMessage message, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Minecraft minecraft = Minecraft.getInstance();

            if(minecraft.currentScreen instanceof ColorWheelScreen) {
                ((ColorWheelScreen) minecraft.currentScreen).updateColor();
            }
        });

        ctx.get().setPacketHandled(true);
    }
}
