package com.example.examplemod.mixin;

import com.example.examplemod.Fibber;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.palette.PalettedContainer;
import net.minecraft.world.chunk.ChunkSection;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ChunkSection.class)
public class ChunkSectionFibber implements Fibber {
    @Mutable
    @Final
    @Shadow
    private PalettedContainer<BlockState> data;

    @Override
    public void fix(ServerPlayerEntity player) {
        Fibber.fix(data, player);
    }
}
