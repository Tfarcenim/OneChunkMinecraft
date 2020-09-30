package tfar.onechunkminecraft.mixin;


import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.IPacket;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.server.ChunkManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChunkManager.class)
public abstract class ThreadedAnvilChunkStorageMixin {

    @Inject(method = "setChunkLoadedAtClient", at = @At("HEAD"),cancellable = true)
    public void fixPackets(ServerPlayerEntity player, ChunkPos chunkPosIn, IPacket<?>[] packetCache, boolean wasLoaded, boolean load, CallbackInfo ci) {
        if (!chunkPosIn.equals(new ChunkPos(player.getPosition()))) {
            ci.cancel();
        }
    }
}
