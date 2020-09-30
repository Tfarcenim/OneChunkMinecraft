package com.example.examplemod.mixin;

import com.example.examplemod.Fibber;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.palette.IPalette;
import net.minecraft.util.palette.PalettedContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(PalettedContainer.class)
public class PalettedContainerFibber<T> implements Fibber {
    @Shadow
    private IPalette<T> palette;

    @Override
    public void fix(ServerPlayerEntity player) {
        Fibber.fix(palette, player);
    }
}
