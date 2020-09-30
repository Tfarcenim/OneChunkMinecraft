package com.example.examplemod.mixin;


import com.example.examplemod.ChunkDataFibber;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.IPacket;
import net.minecraft.network.play.server.SChunkDataPacket;
import net.minecraft.network.play.server.SUpdateLightPacket;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.server.ChunkManager;
import net.minecraft.world.server.ServerWorldLightManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChunkManager.class)
public abstract class ThreadedAnvilChunkStorageMixin {
    @Shadow
    @Final
    private ServerWorldLightManager lightManager;

    @Inject(method = "setChunkLoadedAtClient", at = @At("HEAD"),cancellable = true)
    public void fixPackets(ServerPlayerEntity player, ChunkPos chunkPosIn, IPacket<?>[] packetCache, boolean wasLoaded, boolean load, CallbackInfo ci) {
        if (!chunkPosIn.equals(new ChunkPos(player.getPosition()))) {
            ci.cancel();
        }
    }
}
