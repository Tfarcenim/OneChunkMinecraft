package com.example.examplemod;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.IPacket;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.TrackedEntity;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Set;

public class MixinHooks {

    public static boolean onTrack(Set set, ServerPlayerEntity player, Entity entity, TrackedEntity entry, Set<ServerPlayerEntity> trackingPlayers) {
        if (!new ChunkPos(player.getPosition()).equals(new ChunkPos(entity.getPosition()))) {
            entry.untrack(player);
            trackingPlayers.remove(player);
            return false;
        }
        return set.add(player);
    }
}
