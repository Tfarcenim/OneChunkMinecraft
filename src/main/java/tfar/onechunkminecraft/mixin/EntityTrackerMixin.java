package tfar.onechunkminecraft.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.world.TrackedEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import tfar.onechunkminecraft.OneChunkMinecraft;

import java.util.Set;

@Mixin(targets = "net.minecraft.world.server.ChunkManager$EntityTracker")
public class EntityTrackerMixin {

    @Shadow
    @Final
    private Entity entity;

    @Shadow
    @Final
    private TrackedEntity entry;

    @Shadow
    @Final
    private Set<ServerPlayerEntity> trackingPlayers;

    @Redirect(method = "updateTrackingState(Lnet/minecraft/entity/player/ServerPlayerEntity;)V", at = @At(value = "INVOKE", target = "Ljava/util/Set;add(Ljava/lang/Object;)Z"))
    private boolean no(Set set, Object e) {
        return OneChunkMinecraft.onTrack(set,(ServerPlayerEntity)e,entity,entry,trackingPlayers);
    }
}
