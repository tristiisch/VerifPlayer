package fr.tristiisch.verifplayer.core.verifspec.teleport;

import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import fr.tristiisch.verifplayer.VerifPlayerPlugin;
import fr.tristiisch.verifplayer.core.verifspec.VerifSpec;
import fr.tristiisch.verifplayer.playerinfo.PlayerInfo;
import fr.tristiisch.verifplayer.utils.config.ConfigGet;

public class Teleporter {

	public static void tp(Player player) {
		tp(player, VerifPlayerPlugin.getInstance().getPlayerInfoHandler().get(player));
	}

	public static void tp(Player player, PlayerInfo playerInfo) {
		List<Player> alreadyTeleportedPlayers = playerInfo.getAlreadyTeleportedPlayers();

		Location spawn = Bukkit.getWorlds().get(0).getSpawnLocation();

		List<Player> allTargets = Bukkit.getOnlinePlayers().stream()
				.filter(playerOnline -> !playerOnline.getUniqueId().equals(player.getUniqueId()) && !VerifSpec.isIn(playerOnline) && !VerifPlayerPlugin.getInstance().getVanishHandler().isVanished(playerOnline))
				.sorted(new SortByDistance(spawn)).collect(Collectors.toList());

		if (allTargets == null || allTargets.isEmpty()) {
			player.sendMessage(ConfigGet.MESSAGES_VERIFSPEC_TELEPORTERMSG_NOPLAYERS.getString());
			return;
		}

		Player target = allTargets.stream().filter(t -> !alreadyTeleportedPlayers.contains(t)).findFirst().orElse(null);
		if (target == null) {
			playerInfo.clearAlreadyTeleportedPlayers();
			tp(player, playerInfo);
			return;
		}

		player.teleport(target);
		player.sendMessage(ConfigGet.MESSAGES_VERIFSPEC_TELEPORTERMSG_SUCCES.getString().replace("%player%", target.getName()));
		alreadyTeleportedPlayers.add(target);
		playerInfo.setAlreadyTeleportedPlayers(alreadyTeleportedPlayers);

	}
}
