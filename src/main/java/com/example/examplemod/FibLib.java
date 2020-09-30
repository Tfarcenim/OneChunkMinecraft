package com.example.examplemod;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.ServerPlayerEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.HashMap;

@SuppressWarnings("unused")
public class FibLib {
	public static final String MOD_ID = "fiblib";
	private static final String MOD_NAME = "FibLib";

	private static final Logger LOGGER = LogManager.getLogger();
    public static boolean DEBUG = true;//FabricLoader.getInstance().isDevelopmentEnvironment();

	public static void log(String msg) {
		log("%s", msg);
	}

	public static void log(String format, Object... args) {
		LOGGER.info(String.format("[%s] %s", MOD_NAME, String.format(format, args)));
	}

	public static void debug(String msg) {
		debug("%s", msg);
	}

	public static void debug(String format, Object... args) {
		if (DEBUG) LOGGER.info(String.format("[%s] %s", MOD_NAME, String.format(format, args)));
	}


	public static class Blocks {
		private static final LookupTable LOOKUPS = new LookupTable();

		private static final HashMap<BlockState, BlockFib> FIBS = new HashMap<>();

		public static void tick() {
			LOOKUPS.update();
		}

		public static int getVersion() {return LOOKUPS.getVersion();}

		// API methods
		/**
		 * Use this function to register Fibs that aren't created with scripts.
		 *
		 * @param fib   the fib itself
		 */
		public static void register(BlockFib fib) {
			FIBS.put(fib.getInput(), fib);
			//dev.hephaestus.fiblib.FibLib.log("Registered a BlockFib for %s", fib.getInput().getBlock().getTranslationKey());
		}

		/**
		 * Returns the result of any fibs on a given BlockState
		 *
		 * @param state  the state of the block we're inquiring about. Note that because this is passed to a BlockFib, other
		 *               aspects of the state than the Block may be used in determining the output
		 * @param player the player who we will be fibbing to
		 * @param soft if the block fib is soft, and the caller is soft, don't fib
		 * @return the result of the fib. This is what the player will get told the block is
		 */
		public static BlockState get(BlockState state, @Nullable ServerPlayerEntity player, boolean soft) {
			if (!FIBS.containsKey(state)) return state;

			BlockFib fib = FIBS.get(state);

			if (soft && fib.isSoft()) {
				return state;
			}

			return LOOKUPS.get(fib, state, player);
		}

		/**
		 * Returns whether or not a fib exists for the given state.
		 *
		 * @param state the state to inquire about
		 * @return the result of the inquiry
		 */
		public static boolean contains(BlockState state) {
			return FIBS.containsKey(state);
		}
	}
}
