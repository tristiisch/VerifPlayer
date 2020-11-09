package fr.tristiisch.verifplayer.core.freeze;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import fr.tristiisch.verifplayer.utils.SpigotUtils;
import fr.tristiisch.verifplayer.utils.Title;
import fr.tristiisch.verifplayer.utils.config.ConfigGet;

public class Freeze {

	protected static Map<UUID, List<Player>> players = new HashMap<>();

	public static void freeze(Player player, Player author) {
		freeze(player, author, null);
	}

	public static void freeze(Player player, Player author, String reason) {
		player.setWalkSpeed(0);
		player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 128, false, false));
		player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 128, false, false));
		player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, Integer.MAX_VALUE, 128, false, false));
		Location underPlayer;
		if (!player.isOnGround() && (underPlayer = SpigotUtils.getFirstBlockUnderPlayer(player)) != null)
			player.teleport(new Location(underPlayer.getWorld(), underPlayer.getX() + 0.5, underPlayer.getY() + 1, underPlayer.getZ() + 0.5));

		List<Player> authors = new ArrayList<>();
		authors.add(author);
		players.put(player.getUniqueId(), authors);
		if (reason == null || reason.isEmpty())
			reason = ConfigGet.MESSAGES_FREEZE_SUBTITLE.getString();
		else
			reason = ConfigGet.MESSAGES_FREEZE_SUBTITLEREASON.getString() + reason;
		Title.sendTitle(player, ConfigGet.MESSAGES_FREEZE_TITLE.getString(), reason, 0, 100000000, 0);
	}

	public static boolean isFreeze(Player player) {
		return players.containsKey(player.getUniqueId());
	}

	public static void addAuthor(Player target, Player newAuthor) {
		List<Player> authors = players.get(target.getUniqueId());
		if (!authors.contains(newAuthor))
			authors.add(newAuthor);
	}

	public static boolean removeAuthor(Player target, Player author) {
		Set<Entry<UUID, List<Player>>> authors = players.entrySet().stream().filter(entry -> entry.getValue().contains(author)).collect(Collectors.toSet());
		if (!authors.isEmpty()) {
			authors.forEach(e -> players.get(e.getKey()).remove(author));
			return true;
		} else
			return false;
	}

	public static void unfreeze(Player player) {
		player.removePotionEffect(PotionEffectType.JUMP);
		player.removePotionEffect(PotionEffectType.SLOW);
		player.setWalkSpeed(0.2f);
		players.remove(player.getUniqueId());
		Title.sendTitle(player, ConfigGet.MESSAGES_FREEZE_NOLONGERFREEZETITLE.getString(), ConfigGet.MESSAGES_FREEZE_NOLONGERFREEZESUBTITLE.getString());
	}
}
