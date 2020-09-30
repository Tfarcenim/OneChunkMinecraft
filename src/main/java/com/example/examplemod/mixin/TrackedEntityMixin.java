package com.example.examplemod.mixin;

import com.example.examplemod.MixinHooks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.TrackedEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Set;

@Mixin(targets = "net.minecraft.world.server.ChunkManager$EntityTracker")
public class TrackedEntityMixin {

    @Shadow
    @Final
    private Entity entity;

    @Shadow
    @Final
    private TrackedEntity entry;

    @Shadow
    @Final
    private Set<ServerPlayerEntity> trackingPlayers;

    /*@ModifyVariable(method = "updateTrackingState(Lnet/minecraft/entity/player/ServerPlayerEntity;)V",
            at = @At(value = "FIELD", shift = At.Shift.AFTER), ordinal = 0)
    private boolean modifyFlag(boolean old, ServerPlayerEntity playerEntity) {
        return old && new ChunkPos(playerEntity.getPosition()).equals(entity.getPosition());
    }*/


    @Redirect(method = "updateTrackingState(Lnet/minecraft/entity/player/ServerPlayerEntity;)V", at = @At(value = "INVOKE", target = "Ljava/util/Set;add(Ljava/lang/Object;)Z"))
    private boolean no(Set set, Object e) {
        return MixinHooks.onTrack(set,(ServerPlayerEntity)e,entity,entry,trackingPlayers);
    }
}
