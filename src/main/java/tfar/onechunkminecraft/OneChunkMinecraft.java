package tfar.onechunkminecraft;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityTracker;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketChunkData;
import net.minecraft.network.play.server.SPacketUnloadChunk;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(modid = OneChunkMinecraft.MODID, name = "One Chunk Minecraft",version = "@VERSION@")
public class OneChunkMinecraft {
    // Directly reference a log4j logger.

    public static final String MODID = "onechunkminecraft";

    public static final Map<EntityPlayerMP,ChunkPos> prevPos = new HashMap<>();

    public OneChunkMinecraft() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    public static boolean onTrack(Set set, EntityPlayerMP player, Entity entity, EntityTracker entry, Set<EntityPlayerMP> trackingPlayers) {
        if (!new ChunkPos(player.getPosition()).equals(new ChunkPos(entity.getPosition()))) {
            entry.untrack(player);
            trackingPlayers.remove(player);
            return false;
        }
        return set.add(player);
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (!event.player.world.isRemote && event.phase == TickEvent.Phase.START) {
            EntityPlayerMP player = (EntityPlayerMP) event.player;
            World world = player.world;
            ChunkPos current = new ChunkPos(player.getPosition());
            ChunkPos previous = prevPos.get(player);

            if (!current.equals(previous)) {
                player.connection.sendPacket(new SPacketChunkData(world.getChunk(current.x,current.z),65535));
                if (previous != null) {
                    player.connection.sendPacket(new SPacketUnloadChunk(previous.x,previous.z));
                }
            }
            prevPos.put(player,current);
        }
    }

    @Mod.EventHandler
    private void stop(FMLServerStoppingEvent e) {
        prevPos.clear();
    }
}
