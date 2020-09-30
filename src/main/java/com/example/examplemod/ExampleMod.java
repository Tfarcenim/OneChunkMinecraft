package com.example.examplemod;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.play.server.SChunkDataPacket;
import net.minecraft.network.play.server.SUnloadChunkPacket;
import net.minecraft.util.ClassInheritanceMultiMap;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.server.ChunkManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(ExampleMod.MODID)
public class ExampleMod
{
    // Directly reference a log4j logger.

    public static final String MODID = "examplemod";

    public static final Map<ServerPlayerEntity,ChunkPos> prevPos = new HashMap<>();

    public ExampleMod() {
        MinecraftForge.EVENT_BUS.addListener(this::onServerStarting);
        MinecraftForge.EVENT_BUS.addListener(this::stop);
    }

    public void onServerStarting(TickEvent.PlayerTickEvent event) {
        if (!event.player.world.isRemote && event.phase == TickEvent.Phase.START) {
            ServerPlayerEntity player = (ServerPlayerEntity) event.player;
            World world = player.world;
            ChunkPos current = new ChunkPos(player.getPosition());
            ChunkPos previous = prevPos.get(player);

            if (!current.equals(previous)) {
                player.connection.sendPacket(new SChunkDataPacket(world.getChunk(current.x,current.z),65535));
                if (previous != null) {
                    player.connection.sendPacket(new SUnloadChunkPacket(previous.x,previous.z));
                }
            }
            prevPos.put(player,current);
        }
    }
    private void stop(FMLServerStoppingEvent e) {
        prevPos.clear();
    }
}
