package com.example.examplemod.mixin;


import com.example.examplemod.ChunkDataFibber;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.IPacket;
import net.minecraft.network.play.server.SChunkDataPacket;
import net.minecraft.network.play.server.SUpdateLightPacket;
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

    @Inject(method = "sendChunkData", at = @At("HEAD"))
    public void fixPackets(ServerPlayerEntity serverPlayerEntity_1, IPacket<?>[] packets_1, Chunk worldChunk_1, CallbackInfo ci) {
        if (packets_1[0] == null) {
            packets_1[0] = new SChunkDataPacket();
            ChunkDataFibber.fix(packets_1[0]).fix(worldChunk_1, 65535, serverPlayerEntity_1);
            // this new boolean is apparently an "is invalid" flag.
            packets_1[1] = new SUpdateLightPacket(worldChunk_1.getPos(), this.lightManager, true);
        }
    }
}
