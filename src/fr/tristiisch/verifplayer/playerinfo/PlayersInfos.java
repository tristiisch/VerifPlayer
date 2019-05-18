package fr.tristiisch.verifplayer.playerinfo;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Player;

import fr.tristiisch.verifplayer.verifclick.FastClickRunnable;

public class PlayersInfos {

	private static final Map<UUID, PlayerInfo> playersData = new HashMap<>();

	public static void addNewPlayerInfo(final Player player) {
		PlayersInfos.playersData.put(player.getUniqueId(), new PlayerInfo());
		FastClickRunnable.startIfNotRunning();
	}

	public static PlayerInfo getPlayerInfo(final Player player) {
		return PlayersInfos.playersData.get(player.getUniqueId());
	}

	public static PlayerInfo getPlayerInfo(final UUID uuid) {
		return PlayersInfos.playersData.get(uuid);
	}

	public static Map<UUID, PlayerInfo> getPlayersInfos() {
		return PlayersInfos.playersData;
	}

	public static void removePlayerInfo(final Player player) {
		PlayersInfos.playersData.remove(player.getUniqueId());
		if(playersData.isEmpty()) {
			FastClickRunnable.stop();
		}
	}

	public static void set(final Player player, final PlayerInfo playerInfo) {
		PlayersInfos.playersData.put(player.getUniqueId(), playerInfo);
	}
}
