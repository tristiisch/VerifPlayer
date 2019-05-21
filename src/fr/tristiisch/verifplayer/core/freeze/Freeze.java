package fr.tristiisch.verifplayer.core.freeze;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import fr.tristiisch.verifplayer.utils.SpigotUtils;
import fr.tristiisch.verifplayer.utils.Title;

public class Freeze {

	private static final List<UUID> players = new ArrayList<>();

	public static void freeze(final Player player) {
		freeze(player, "&cVenez sur le ts &a&l&nts.emeraldmc.fr&c au plus vite");
	}

	public static void freeze(final Player player, final String motif) {
		player.setWalkSpeed(0);
		final Location underPlayer = SpigotUtils.getFirstBlockUnderPlayer(player);
		if(underPlayer != null) {
			player.teleport(new Location(underPlayer.getWorld(), underPlayer.getX() + 0.5, underPlayer.getY() + 1, underPlayer.getZ() + 0.5));
		} else {
			//			player.teleport(EmeraldSpigot.getSpawn());
		}
		players.add(player.getUniqueId());
		Title.sendTitle(player, "&4Vous avez été freeze", motif, 0, 100000000, 0);
	}

	public static boolean isFreeze(final Player player) {
		return players.contains(player.getUniqueId());
	}

	public static void unfreeze(final Player player) {
		player.setWalkSpeed(0.2f);
		players.remove(player.getUniqueId());
		Title.sendTitle(player, "&aVous n'êtes plus freeze", "");
	}
}
