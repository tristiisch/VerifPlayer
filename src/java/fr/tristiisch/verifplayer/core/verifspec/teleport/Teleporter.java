package fr.tristiisch.verifplayer.core.verifspec.teleport;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import fr.tristiisch.verifplayer.VerifPlayerPlugin;
import fr.tristiisch.verifplayer.core.verifspec.VerifSpec;
import fr.tristiisch.verifplayer.playerinfo.PlayerInfo;
import fr.tristiisch.verifplayer.utils.Utils;
import fr.tristiisch.verifplayer.utils.config.ConfigGet;

public class Teleporter {

	private static Cache<UUID, Long> cooldown = CacheBuilder.newBuilder().expireAfterWrite(5, TimeUnit.SECONDS).build();

	public static void tp(Player player, Player target) {
		tp(player, VerifPlayerPlugin.getInstance().getPlayerInfoHandler().get(player), target);
	}

	public static void tp(Player player, PlayerInfo playerInfo, Player target) {
		List<Player> alreadyTeleportedPlayers = playerInfo.getAlreadyTeleportedPlayers();
		cooldown.put(player.getUniqueId(), Utils.getCurrentTimeinSeconds() + 5);
		player.teleport(target);
		player.sendMessage(ConfigGet.MESSAGES_VERIFSPEC_TELEPORTERMSG_SUCCES.getString().replace("%player%", target.getName()));
		alreadyTeleportedPlayers.add(target);
		playerInfo.setAlreadyTeleportedPlayers(alreadyTeleportedPlayers);
	}

	public static void tp(Player player) {
		tp(player, VerifPlayerPlugin.getInstance().getPlayerInfoHandler().get(player));
	}

	public static void tp(Player player, PlayerInfo playerInfo) {
		Long time;
		if ((time = cooldown.getIfPresent(player.getUniqueId())) != null) {
			player.sendMessage(ConfigGet.MESSAGES_VERIFSPEC_TELEPORTERMSG_WAIT.getString().replace("%time", Utils.timestampToDuration(time)));
			return;
		}
		List<Player> alreadyTeleportedPlayers = playerInfo.getAlreadyTeleportedPlayers();
		Location playerLocation = player.getLocation();
		List<Player> wortldAllTargets = playerLocation.getWorld().getPlayers();
		Collection<? extends Player> allTargets = Bukkit.getOnlinePlayers();
		List<Player> excludeWorldsTargets = allTargets.stream()
				.filter(p -> !wortldAllTargets.contains(p) && !VerifSpec.isIn(p) && !VerifPlayerPlugin.getInstance().getVanishHandler().isVanished(p))
				.collect(Collectors.toList());
		List<Player> wortldTargets = wortldAllTargets.stream()
				.filter(p -> !p.getUniqueId().equals(player.getUniqueId()) && !VerifSpec.isIn(p) && !VerifPlayerPlugin.getInstance().getVanishHandler().isVanished(p))
				.sorted(new SortByDistance(player.getLocation())).collect(Collectors.toList());
		Player target;
		if (wortldTargets == null || wortldTargets.isEmpty())
			if (excludeWorldsTargets.isEmpty()) {
				cooldown.put(player.getUniqueId(), Utils.getCurrentTimeinSeconds() + 5);
				player.sendMessage(ConfigGet.MESSAGES_VERIFSPEC_TELEPORTERMSG_NOPLAYERS.getString());
				return;
			} else
				target = excludeWorldsTargets.stream().filter(t -> !alreadyTeleportedPlayers.contains(t)).findFirst().orElse(null);
		else
			target = wortldTargets.stream().filter(t -> !alreadyTeleportedPlayers.contains(t)).findFirst().orElse(null);
		if (target == null) {
			playerInfo.clearAlreadyTeleportedPlayers();
			player.sendMessage(ConfigGet.MESSAGES_VERIFSPEC_TELEPORTERMSG_NEWLOOP.getString());
			tp(player, playerInfo);
			return;
		}
		tp(player, playerInfo, target);
	}
}
