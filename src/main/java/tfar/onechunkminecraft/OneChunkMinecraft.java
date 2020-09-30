package tfar.onechunkminecraft;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.play.server.SChunkDataPacket;
import net.minecraft.network.play.server.SUnloadChunkPacket;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.TrackedEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerStoppingEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(OneChunkMinecraft.MODID)
public class OneChunkMinecraft {
    // Directly reference a log4j logger.

    public static final String MODID = "onechunkminecraft";

    public static final Map<ServerPlayerEntity,ChunkPos> prevPos = new HashMap<>();

    public OneChunkMinecraft() {
        MinecraftForge.EVENT_BUS.addListener(this::onPlayerTick);
        MinecraftForge.EVENT_BUS.addListener(this::stop);
    }

    public static boolean onTrack(Set set, ServerPlayerEntity player, Entity entity, TrackedEntity entry, Set<ServerPlayerEntity> trackingPlayers) {
        if (!new ChunkPos(player.getPosition()).equals(new ChunkPos(entity.getPosition()))) {
            entry.untrack(player);
            trackingPlayers.remove(player);
            return false;
        }
        return set.add(player);
    }

    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
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
