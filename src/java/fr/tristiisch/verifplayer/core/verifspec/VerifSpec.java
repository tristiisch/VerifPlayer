package fr.tristiisch.verifplayer.core.verifspec;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffectType;

import fr.tristiisch.verifplayer.VerifPlayerPlugin;
import fr.tristiisch.verifplayer.utils.PlayerContents;
import fr.tristiisch.verifplayer.utils.config.ConfigGet;

public class VerifSpec {

	private static Set<UUID> players = new HashSet<>();
	private static Map<OfflinePlayer, OfflinePlayer> knockback = new HashMap<>();

	public static void disable(Player player) {
		if (player.getGameMode() != GameMode.CREATIVE)
			player.setAllowFlight(false);

		PlayerContents playerContents = PlayerContents.fromDisk(player);
		if (player.isOnline())
			playerContents.returnHisInventory();

		VerifPlayerPlugin.getInstance().getVanishHandler().disable(player, false);

		player.removePotionEffect(PotionEffectType.SPEED);
		player.removePotionEffect(PotionEffectType.SLOW);
		player.removePotionEffect(PotionEffectType.NIGHT_VISION);

		remove(player);
		player.sendMessage(ConfigGet.MESSAGES_VERIFSPEC_DISABLE.getString());
	}

	public static void enable(Player player) {
		PlayerContents playerContents = new PlayerContents(player);
		players.add(player.getUniqueId());

		playerContents.saveToDisk();
		playerContents.clearInventory();

		VerifPlayerPlugin.getInstance().getVanishHandler().enable(player, false);
		setVerifSpecItems(player);

		player.setAllowFlight(true);

		player.sendMessage(ConfigGet.MESSAGES_VERIFSPEC_ENABLE.getString());
	}

	// Not used
	@Deprecated
	public static Player getClosestPlayer(Location loc) {
		return loc.getWorld().getPlayers().stream().min((o1, o2) -> {
			return Double.compare(o1.getLocation().distanceSquared(loc), o2.getLocation().distanceSquared(loc));
		}).orElse(null);
	}

	public static boolean isIn(Player player) {
		return players.stream().filter(uuid -> player.getUniqueId().equals(uuid)).findFirst().isPresent();
	}

	public static Set<UUID> getPlayersInVerif() {
		return players;
	}

	public static boolean remove(Player player) {
		return players.remove(player.getUniqueId());
	}

	protected static void setVerifSpecItems(Player player) {
		PlayerInventory playerInventory = player.getInventory();
		for (VerifSpecTool verifSpecItem : VerifSpecTool.values())
			if (verifSpecItem.isEnable())
				playerInventory.setItem(verifSpecItem.getSlot(), verifSpecItem.getItemStack());
	}

	public static void addKnockback(Player mod, Player target) {
		knockback.put(mod, target);
	}
}
