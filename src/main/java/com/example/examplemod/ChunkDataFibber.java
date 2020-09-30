package com.example.examplemod;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.world.chunk.Chunk;

public interface ChunkDataFibber {
    static ChunkDataFibber fix(Object object) {
        return (ChunkDataFibber)object;
    }

    void fix(Chunk chunk, int includedSectionsMask, ServerPlayerEntity player);
}
