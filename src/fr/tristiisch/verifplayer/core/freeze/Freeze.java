package fr.tristiisch.verifplayer.core.freeze;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import fr.tristiisch.verifplayer.utils.SpigotUtils;
import fr.tristiisch.verifplayer.utils.Title;
import fr.tristiisch.verifplayer.utils.config.ConfigGet;

public class Freeze {

	protected static List<UUID> players = new ArrayList<>();

	public static void freeze(Player player) {
		freeze(player, null);
	}

	public static void freeze(Player player, String reason) {
		player.setWalkSpeed(0);
		Location underPlayer = SpigotUtils.getFirstBlockUnderPlayer(player);
		if (underPlayer != null) {
			player.teleport(new Location(underPlayer.getWorld(), underPlayer.getX() + 0.5, underPlayer.getY() + 1, underPlayer.getZ() + 0.5));
		}
		player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 128, false, false));
		players.add(player.getUniqueId());

		if (reason == null || reason.isEmpty()) {
			reason = ConfigGet.MESSAGES_FREEZE_SUBTITLE.getString();
		} else {
			reason = ConfigGet.MESSAGES_FREEZE_SUBTITLEREASON.getString() + reason;
		}

		Title.sendTitle(player, ConfigGet.MESSAGES_FREEZE_TITLE.getString(), reason, 0, 100000000, 0);
	}

	public static boolean isFreeze(Player player) {
		return players.contains(player.getUniqueId());
	}

	public static void unfreeze(Player player) {
		player.setWalkSpeed(0.2f);
		player.removePotionEffect(PotionEffectType.JUMP);
		players.remove(player.getUniqueId());
		Title.sendTitle(player, ConfigGet.MESSAGES_FREEZE_NOLONGERFREEZETITLE.getString(), ConfigGet.MESSAGES_FREEZE_NOLONGERFREEZESUBTITLE.getString());
	}
}
