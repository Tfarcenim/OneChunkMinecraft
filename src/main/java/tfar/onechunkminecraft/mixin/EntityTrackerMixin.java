package tfar.onechunkminecraft.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityTrackerEntry;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.ChunkPos;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityTrackerEntry.class)
public class EntityTrackerMixin {

    @Shadow @Final private Entity trackedEntity;

    @Inject(method = "isVisibleTo",at = @At("HEAD"),cancellable = true)
    private void no(EntityPlayerMP playerMP, CallbackInfoReturnable<Boolean> cir) {
        ChunkPos entityPos = new ChunkPos(trackedEntity.getPosition());
        ChunkPos playerPos = new ChunkPos(playerMP.getPosition());
        if (!entityPos.equals(playerPos)) {
            cir.setReturnValue(false);
        }
    }
}
