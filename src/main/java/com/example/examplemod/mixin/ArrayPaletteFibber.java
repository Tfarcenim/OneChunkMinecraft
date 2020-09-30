package com.example.examplemod.mixin;


import com.example.examplemod.FibLib;
import com.example.examplemod.Fibber;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ObjectIntIdentityMap;
import net.minecraft.util.palette.ArrayPalette;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@SuppressWarnings("unchecked")
@Mixin(ArrayPalette.class)
public class ArrayPaletteFibber<T> implements Fibber {
    @Mutable
    @Final
    @Shadow
    private final ObjectIntIdentityMap<T> registry;
    public ArrayPaletteFibber(ObjectIntIdentityMap<T> registry) {
        this.registry = registry;
    }

    @Redirect(method = "write", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/ObjectIntIdentityMap;getId(Ljava/lang/Object;)I"))
    public int toPacketRedir(ObjectIntIdentityMap<T> idList, T object) {
        return idList.getId((T) FibLib.Blocks.get((BlockState) object, this.player, false));
    }

    @Redirect(method = "getSerializedSize", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/ObjectIntIdentityMap;getId(Ljava/lang/Object;)I"))
    public int getPacketSizeRedir(ObjectIntIdentityMap<T> idList, T object) {
        return idList.getId((T) FibLib.Blocks.get((BlockState) object, this.player, false));
    }

    private ServerPlayerEntity player;

    @Override
    public void fix(ServerPlayerEntity player) {
        this.player = player;
    }
}