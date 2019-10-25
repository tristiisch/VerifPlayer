package fr.tristiisch.verifplayer.core.verifspec;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffectType;

import fr.tristiisch.verifplayer.VerifPlayerPlugin;
import fr.tristiisch.verifplayer.utils.PlayerContents;
import fr.tristiisch.verifplayer.utils.config.ConfigGet;

public class VerifSpec {

	private static Set<UUID> players = new HashSet<>();

	public static void disable(final Player player) {
		if (player.getGameMode() != GameMode.CREATIVE) {
			player.setAllowFlight(false);
		}

		final PlayerContents playerContents = PlayerContents.fromDisk(player);
		if (player.isOnline()) {
			playerContents.returnHisInventory();
		}

		VerifPlayerPlugin.getInstance().getVanishHandler().disable(player, false);

		player.removePotionEffect(PotionEffectType.SPEED);
		player.removePotionEffect(PotionEffectType.SLOW);

		remove(player);
		player.sendMessage(ConfigGet.MESSAGES_VERIFSPEC_DISABLE.getString());
	}

	public static void enable(final Player player) {
		final PlayerContents playerContents = new PlayerContents(player);
		players.add(player.getUniqueId());

		playerContents.saveToDisk();
		playerContents.clearInventory();

		VerifPlayerPlugin.getInstance().getVanishHandler().enable(player, false);
		setVerifSpecItems(player);

		player.setAllowFlight(true);

		player.sendMessage(ConfigGet.MESSAGES_VERIFSPEC_ENABLE.getString());
	}

	public static Player getClosestPlayer(final Location loc) {
		return loc.getWorld().getPlayers().stream().min((o1, o2) -> {
			return Double.compare(o1.getLocation().distanceSquared(loc), o2.getLocation().distanceSquared(loc));
		}).orElse(null);
	}

	public static boolean isIn(final Player player) {
		return players.stream().filter(uuid -> player.getUniqueId().equals(uuid)).findFirst().isPresent();
	}

	public static boolean remove(final Player player) {
		return players.remove(player.getUniqueId());
	}

	protected static void setVerifSpecItems(final Player player) {
		final PlayerInventory playerInventory = player.getInventory();
		for (final VerifSpecTool verifSpecItem : VerifSpecTool.values()) {
			if (verifSpecItem.isEnable()) {
				playerInventory.setItem(verifSpecItem.getSlot(), verifSpecItem.getItemStack());
			}
		}
	}
}
