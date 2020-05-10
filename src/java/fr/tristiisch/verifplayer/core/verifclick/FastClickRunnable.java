package fr.tristiisch.verifplayer.core.verifclick;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import fr.tristiisch.verifplayer.VerifPlayerPlugin;
import fr.tristiisch.verifplayer.core.verifgui.runnable.VerifGuiRunnable;
import fr.tristiisch.verifplayer.playerinfo.PlayerInfo;
import fr.tristiisch.verifplayer.tps.TpsGetter;
import fr.tristiisch.verifplayer.utils.Reflection;
import fr.tristiisch.verifplayer.utils.Utils;
import fr.tristiisch.verifplayer.utils.config.ConfigGet;
import fr.tristiisch.verifplayer.utils.permission.Permission;

public class FastClickRunnable extends BukkitRunnable {

	private static String getTaskName() {
		return FastClickRunnable.class.getName();
	}

	private static boolean isRunning() {
		return VerifPlayerPlugin.getInstance().getTaskHandler().taskExist(getTaskName());
	}

	public static void start() {
		// 0.05 sec = 1; 1 sec = 20
		VerifPlayerPlugin.getInstance().getTaskHandler().scheduleSyncRepeatingTask(getTaskName(), new FastClickRunnable(), 0, 20);
	}

	public static void startIfNotRunning() {
		if (isRunning()) {
			return;
		}
		start();
	}

	public static void stop() {
		VerifPlayerPlugin.getInstance().getTaskHandler().cancelTask(getTaskName());
	}

	@Override
	public void run() {
		for (PlayerInfo playerInfo : VerifPlayerPlugin.getInstance().getPlayerInfoHandler().get()) {
			Player player = playerInfo.getPlayer();
			int ping = Reflection.getPing(player);

			double tps = new TpsGetter().getDouble();
			int maxCps = ConfigGet.SETTINGS_MAXCPS.getInt();

			while (playerInfo.getAirClicks().size() >= ConfigGet.SETTINGS_SIZEHISTORYCPS.getInt()) {
				playerInfo.getAirClicks().remove(0);
				playerInfo.getEntityClicks().remove(0);
			}

			playerInfo.getAirClicks().add(playerInfo.getClickAir());
			playerInfo.getEntityClicks().add(playerInfo.getClickEntity());
			int click = playerInfo.getClickAir() + playerInfo.getClickEntity();
			if (click > playerInfo.getMaxCPS()) {
				playerInfo.setMaxCPS(click);
			}

			if (click > maxCps) {
				int lagAlertCPS = (int) ((20.0 - tps) * 2.0);
				lagAlertCPS += ping / 50;
				if (click > maxCps + lagAlertCPS) {
					playerInfo.addAlert();
					long timestamp = Utils.getCurrentTimeinSeconds();
					playerInfo.addAlertHistory(timestamp, click);

					if (playerInfo.getLastAlert() + ConfigGet.SETTINGS_TIMEBETWEENALERTS.getInt() < timestamp) {
						playerInfo.setLastAlert(timestamp);
						String msg = ConfigGet.MESSAGES_VERIF_SENDALERT.getString().replace("%player%", player.getName()).replace("%cps%", String.valueOf(click)).replace("%ping%", String.valueOf(ping)).replace("%tps%",
								String.valueOf(tps));

						for (Player players : Bukkit.getOnlinePlayers()) {
							if (Permission.MODERATOR_RECEIVEALERT.hasPermission(players) && !player.getUniqueId().equals(players.getUniqueId())) {
								players.sendMessage(msg);
							}
						}
					}
				}
			}
			playerInfo.resetAirClick();
			playerInfo.resetEntityClick();
			// VerifPlayerPlugin.getInstance().getPlayerInfoHandler().set(player,
			// playerInfo);
		}
		VerifGuiRunnable.run();
	}
}
