package dev.onyxstudios.simplycoloredblocks.network;

import dev.onyxstudios.simplycoloredblocks.client.container.ColorWheelContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class ColorBlockMessage {

    public ItemStack stack;
    public int toCraft;
    public int dyeToTake;

    public ColorBlockMessage(ItemStack stack, int toCraft, int dyeToTake) {
        this.stack = stack;
        this.toCraft = toCraft;
        this.dyeToTake = dyeToTake;
    }

    public void encode(PacketBuffer buffer) {
        buffer.writeItemStack(stack);
        buffer.writeInt(toCraft);
        buffer.writeInt(dyeToTake);
    }

    public static ColorBlockMessage decode(PacketBuffer buffer) {
        return new ColorBlockMessage(buffer.readItemStack(), buffer.readInt(), buffer.readInt());
    }

    public static void handleMessage(ColorBlockMessage message, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            PlayerEntity player = ctx.get().getSender();

            if(player != null) {
                player.inventory.setItemStack(message.stack);

                if(player.openContainer instanceof ColorWheelContainer) {
                    ((ColorWheelContainer) player.openContainer).inventory.extractItem(0, message.toCraft, false);
                    ((ColorWheelContainer) player.openContainer).inventory.extractItem(1, message.dyeToTake, false);
                }
            }
        });
    }
}
