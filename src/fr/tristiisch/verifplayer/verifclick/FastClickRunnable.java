package fr.tristiisch.verifplayer.verifclick;

import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import fr.tristiisch.verifplayer.playerinfo.PlayerInfo;
import fr.tristiisch.verifplayer.playerinfo.PlayersInfos;
import fr.tristiisch.verifplayer.utils.Reflection;
import fr.tristiisch.verifplayer.utils.TPS;
import fr.tristiisch.verifplayer.utils.TaskManager;
import fr.tristiisch.verifplayer.utils.Utils;
import fr.tristiisch.verifplayer.utils.config.ConfigGet;
import fr.tristiisch.verifplayer.utils.permission.Permission;
import fr.tristiisch.verifplayer.verifgui.runnable.VerifGuiRunnable;

public class FastClickRunnable extends BukkitRunnable {

	private static String getTaskName() {
		return FastClickRunnable.class.getName();
	}

	private static boolean isRunning() {
		return TaskManager.taskExist(getTaskName());
	}

	public static void start() {
		// 0.05 sec = 1; 1 sec = 20
		TaskManager.scheduleSyncRepeatingTask(getTaskName(), new FastClickRunnable(), 0, 20);
	}

	public static void startIfNotRunning() {
		if(isRunning()) {
			return;
		}
		start();
	}

	public static void stop() {
		TaskManager.cancelTaskByName(getTaskName());
	}

	@Override
	public void run() {
		for(final Map.Entry<UUID, PlayerInfo> entry : PlayersInfos.getPlayersInfos().entrySet()) {
			final PlayerInfo playerInfo = entry.getValue();
			final UUID playerUuid = entry.getKey();
			final Player player = Bukkit.getPlayer(playerUuid);
			final int ping = Reflection.getPing(player);

			final double tps = TPS.getTPS();
			final int maxCps = ConfigGet.SETTINGS_MAXCPS.getInt();

			while(playerInfo.getClicksAir().size() >= ConfigGet.SETTINGS_SIZEHISTORYCPS.getInt()) {
				playerInfo.getClicksAir().remove(0);
				playerInfo.getClicksEntity().remove(0);
			}

			playerInfo.getClicksAir().add(playerInfo.getClickAir());
			playerInfo.getClicksEntity().add(playerInfo.getClickEntity());
			final int click = playerInfo.getClickAir() + playerInfo.getClickEntity();
			if(click > playerInfo.getMaxCPS()) {
				playerInfo.setMaxCPS(click);
			}

			if(click > maxCps) {
				int lagAlertCPS = (int) ((20.0 - tps) * 2.0);
				lagAlertCPS += ping / 50;
				if(click > maxCps + lagAlertCPS) {
					playerInfo.addNbAlerts();
					final long timestamp = Utils.getCurrentTimeinSeconds();
					playerInfo.putAlertHistory(timestamp, click);

					if(playerInfo.getLastAlert() + ConfigGet.SETTINGS_TIMEBETWEENALERTS.getInt() < timestamp) {
						playerInfo.setLastAlert(timestamp);
						final String msg = ConfigGet.MESSAGES_VERIF_SENDALERT.getString()
								.replace("%player%", player.getName())
								.replace("%cps%", String.valueOf(click))
								.replace("%ping%", String.valueOf(ping))
								.replace("%tps%", String.valueOf(tps));

						for(final Player players : Bukkit.getOnlinePlayers()) {
							if(Permission.MODERATOR_RECEIVEALERT.hasPermission(players) && !playerUuid.equals(players.getUniqueId())) {
								players.sendMessage(msg);
							}
						}
					}
				}
			}
			playerInfo.resetClickAir();
			playerInfo.resetClickEntity();
			PlayersInfos.set(player, playerInfo);
		}
		VerifGuiRunnable.run();
	}
}
