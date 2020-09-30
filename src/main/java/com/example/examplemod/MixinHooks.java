package com.example.examplemod;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.IPacket;
import net.minecraft.util.math.ChunkPos;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public class MixinHooks {
    public static void sendChunkToClient(ServerPlayerEntity player, ChunkPos chunkPosIn, IPacket<?>[] packetCache, boolean wasLoaded, boolean load, CallbackInfo ci) {
        ChunkPos playerChunkPos = new ChunkPos(player.getPosition());
        if (!playerChunkPos.equals(chunkPosIn)) {
            ci.cancel();
        }
    }
}
