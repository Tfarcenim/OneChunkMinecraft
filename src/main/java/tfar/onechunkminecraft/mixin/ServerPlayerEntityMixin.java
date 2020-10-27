package tfar.onechunkminecraft.mixin;


import com.mojang.authlib.GameProfile;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.IPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
abstract class ServerPlayerEntityMixin extends PlayerEntity {

    public ServerPlayerEntityMixin(World p_i241920_1_, BlockPos p_i241920_2_, float p_i241920_3_, GameProfile p_i241920_4_) {
        super(p_i241920_1_, p_i241920_2_, p_i241920_3_, p_i241920_4_);
    }

    @Inject(method = "sendChunkLoad", at = @At("HEAD"),cancellable = true)
    public void redirectPackets(ChunkPos chunkPosIn, IPacket<?> p_213844_2_, IPacket<?> p_213844_3_, CallbackInfo ci) {
        if (!chunkPosIn.equals(new ChunkPos(this.getPosition()))) {
            ci.cancel();
        }
    }
}
