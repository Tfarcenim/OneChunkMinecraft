package tfar.onechunkminecraft.mixin;


import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketChunkData;
import net.minecraft.server.management.PlayerChunkMapEntry;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.Chunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import javax.annotation.Nullable;

@Mixin(PlayerChunkMapEntry.class)
public abstract class ThreadedAnvilChunkStorageMixin {

    @Shadow @Nullable private Chunk chunk;

    @Redirect(method = "sendPacket", at = @At(value = "INVOKE",target = "Lnet/minecraft/network/NetHandlerPlayServer;sendPacket(Lnet/minecraft/network/Packet;)V"))
    public void fixPackets(NetHandlerPlayServer netHandlerPlayServer, Packet<?> packetIn) {
        if (packetIn instanceof SPacketChunkData) {
            ChunkPos playerPos = new ChunkPos(netHandlerPlayServer.player.getPosition());
            if (!playerPos.equals(chunk.getPos())) {
                return;
            } else {
                netHandlerPlayServer.sendPacket(packetIn);
            }
        }
    }

    @Redirect(method = "sendToPlayers",at = @At(value = "INVOKE",target = "Lnet/minecraft/network/NetHandlerPlayServer;sendPacket(Lnet/minecraft/network/Packet;)V"))
    private void noChunks(NetHandlerPlayServer netHandlerPlayServer, Packet<?> packetIn) {
        if (packetIn instanceof SPacketChunkData) {
            ChunkPos playerPos = new ChunkPos(netHandlerPlayServer.player.getPosition());
            if (!playerPos.equals(chunk.getPos())) {
                return;
            } else {
                netHandlerPlayServer.sendPacket(packetIn);
            }
        }
    }
}
