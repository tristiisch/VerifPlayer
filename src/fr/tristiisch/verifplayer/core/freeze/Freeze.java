package fr.tristiisch.verifplayer.core.freeze;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import fr.tristiisch.verifplayer.utils.SpigotUtils;
import fr.tristiisch.verifplayer.utils.Title;
import fr.tristiisch.verifplayer.utils.config.ConfigGet;

public class Freeze {

	private static final List<UUID> players = new ArrayList<>();

	public static void freeze(final Player player) {
		freeze(player, null);
	}

	public static void freeze(final Player player, String reason) {
		player.setWalkSpeed(0);
		final Location underPlayer = SpigotUtils.getFirstBlockUnderPlayer(player);
		if(underPlayer != null) {
			player.teleport(new Location(underPlayer.getWorld(), underPlayer.getX() + 0.5, underPlayer.getY() + 1, underPlayer.getZ() + 0.5));
		} else {
			//			player.teleport(EmeraldSpigot.getSpawn());
		}
		players.add(player.getUniqueId());

		if(reason == null || reason.isEmpty()) {
			reason = ConfigGet.MESSAGES_FREEZE_SUBTITLE.getString();
		} else {
			reason = ConfigGet.MESSAGES_FREEZE_SUBTITLEREASON.getString() + reason;
		}

		Title.sendTitle(player, ConfigGet.MESSAGES_FREEZE_TITLE.getString(), reason, 0, 100000000, 0);
	}

	public static boolean isFreeze(final Player player) {
		return players.contains(player.getUniqueId());
	}

	public static void unfreeze(final Player player) {
		player.setWalkSpeed(0.2f);
		players.remove(player.getUniqueId());
		Title.sendTitle(player, ConfigGet.MESSAGES_FREEZE_NOLONGERFREEZETITLE.getString(), ConfigGet.MESSAGES_FREEZE_NOLONGERFREEZESUBTITLE.getString());
	}
}
