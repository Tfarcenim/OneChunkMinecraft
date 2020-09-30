package com.example.examplemod.mixin;

import com.example.examplemod.FibLib;
import com.example.examplemod.Fibber;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ObjectIntIdentityMap;
import net.minecraft.util.palette.IdentityPalette;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@SuppressWarnings("unchecked")
@Mixin(IdentityPalette.class)
public class IdListPaletteFibber<T> implements Fibber {
    @Redirect(method = "idFor", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/ObjectIntIdentityMap;getId(Ljava/lang/Object;)I"))
    public int getIndexRedir(ObjectIntIdentityMap<T> idList, T object) {
        return idList.getId((T) FibLib.Blocks.get((BlockState) object, this.player, false));
    }

    private ServerPlayerEntity player;

    @Override
    public void fix(ServerPlayerEntity player) {
        this.player = player;
    }
}
