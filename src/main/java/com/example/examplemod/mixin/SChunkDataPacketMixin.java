package com.example.examplemod.mixin;

import com.example.examplemod.ChunkDataFibber;
import com.example.examplemod.Fibber;
import com.google.common.collect.Lists;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.LongArrayNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.SChunkDataPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkSection;
import net.minecraft.world.gen.Heightmap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Mixin(SChunkDataPacket.class)
public abstract class SChunkDataPacketMixin implements ChunkDataFibber {

    @Shadow
    private int chunkX;
    @Shadow private int chunkZ;
    @Shadow private int availableSections;
    @Shadow private CompoundNBT heightmapTags;
    @Shadow private int[] biomes;
    @Shadow private byte[] buffer;
    @Shadow private List<CompoundNBT> tileEntityTags;
    @Shadow private boolean fullChunk;

    @Shadow protected abstract int calculateChunkSize(Chunk chunk, int includedSectionsMark);
    @Shadow private ByteBuf getWriteBuffer() {return null;}
    @Shadow public abstract boolean isFullChunk();

    @Shadow public abstract int extractChunkData(PacketBuffer buf, Chunk chunkIn, int writeSkylight);

    private ServerPlayerEntity player;

    // We are abusing the empty constructor of ChunkDataS2CPacket HARD here. Basically just constructing it here instead
    @Override
    public void fix(Chunk chunk, int includedSectionsMask, ServerPlayerEntity player) {
        this.player = player;

        ChunkPos chunkPos = chunk.getPos();
        this.chunkX = chunkPos.x;
        this.chunkZ = chunkPos.z;
        this.fullChunk = includedSectionsMask == 65535;
        this.heightmapTags = new CompoundNBT();
        Iterator<Map.Entry<Heightmap.Type, Heightmap>> heightmaps = chunk.getHeightmaps().iterator();

        Map.Entry<Heightmap.Type, Heightmap> heightmap;
        while(heightmaps.hasNext()) {
            heightmap = heightmaps.next();
            if ((heightmap.getKey()).isUsageClient()) {
                this.heightmapTags.put((heightmap.getKey()).name(), new LongArrayNBT((heightmap.getValue()).getDataArray()));
            }
        }

        if (this.fullChunk) {
            this.biomes = chunk.getBiomes().getBiomeIds();
        }

        this.buffer = new byte[this.calculateChunkSize(chunk, includedSectionsMask)];
        this.availableSections = this.extractChunkData(new PacketBuffer(this.getWriteBuffer()), chunk, includedSectionsMask);

        this.tileEntityTags = Lists.newArrayList();

        for(Map.Entry<BlockPos, TileEntity> entry1 : chunk.getTileEntityMap().entrySet()) {
            BlockPos blockpos = entry1.getKey();
            TileEntity tileentity = entry1.getValue();
            int i = blockpos.getY() >> 4;
            if (this.isFullChunk() || (includedSectionsMask & 1 << i) != 0) {
                CompoundNBT compoundnbt = tileentity.getUpdateTag();
                this.tileEntityTags.add(compoundnbt);
            }
        }
    }

    @Inject(method = "calculateChunkSize", at = @At(value = "HEAD"))
    public void fixDataSize(Chunk chunk, int includedSectionsMark, CallbackInfoReturnable<Integer> cir) {
        for (ChunkSection chunkSection : chunk.getSections())
            Fibber.fix(chunkSection, player);
    }
}
