package dev.onyxstudios.simplycoloredblocks.items;

import dev.onyxstudios.simplycoloredblocks.network.ModPackets;
import dev.onyxstudios.simplycoloredblocks.network.UpdateColorMessage;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.items.ItemStackHandler;

public class ColorWheelInventory extends ItemStackHandler {

    private PlayerEntity player;

    public ColorWheelInventory(PlayerEntity player) {
        super(2);
        this.player = player;
    }

    @Override
    protected void onContentsChanged(int slot) {
        super.onContentsChanged(slot);

        if(!player.world.isRemote()) {
            ModPackets.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) player), new UpdateColorMessage());
        }
    }
}
