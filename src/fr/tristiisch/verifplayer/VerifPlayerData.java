package fr.tristiisch.verifplayer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import fr.tristiisch.verifplayer.object.PlayerInfo;
import fr.tristiisch.verifplayer.verifclick.FastClickRunnable;

public class VerifPlayerData {

	private static final Map<UUID, PlayerInfo> playersData = new HashMap<>();

	public static void addNewPlayerInfo(final Player player) {
		VerifPlayerData.playersData.put(player.getUniqueId(), new PlayerInfo());
		FastClickRunnable.startIfNotRunning();
	}

	public static ChatColor getIntervalChatColor(final int i, final int min, final int max) {
		if(i == 0) {
			return ChatColor.GRAY;
		}
		if(i < min) {
			return ChatColor.GREEN;
		}
		if(i < max) {
			return ChatColor.GOLD;
		}
		return ChatColor.RED;
	}

	public static int getIntervalGlassPaneColor(final int i, final int min, final int max) {
		if(i == 0) {
			return 0;
		}
		if(i < min) {
			return 5;
		}
		if(i < max) {
			return 1;
		}
		return 14;
	}

	public static PlayerInfo getPlayerInfo(final Player player) {
		return VerifPlayerData.playersData.get(player.getUniqueId());
	}

	public static PlayerInfo getPlayerInfo(final UUID uuid) {
		return VerifPlayerData.playersData.get(uuid);
	}

	public static Map<UUID, PlayerInfo> getPlayersInfos() {
		return VerifPlayerData.playersData;
	}

	public static void removePlayerInfo(final Player player) {
		VerifPlayerData.playersData.remove(player.getUniqueId());
		if(playersData.isEmpty()) {
			FastClickRunnable.stop();
		}
	}

	public static void set(final Player player, final PlayerInfo playerInfo) {
		VerifPlayerData.playersData.put(player.getUniqueId(), playerInfo);
	}
}
