package fr.tristiisch.verifplayer.verifclick;

import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import fr.tristiisch.verifplayer.VerifPlayerData;
import fr.tristiisch.verifplayer.object.PlayerInfo;
import fr.tristiisch.verifplayer.utils.ConfigUtils;
import fr.tristiisch.verifplayer.utils.Reflection;
import fr.tristiisch.verifplayer.utils.TPS;

public class FastClickRunnable extends BukkitRunnable {

	public static final int sizeHistoryCps = 10;
	public static final int alertCPS = 18;
	private static final int timeBetwenAlert = 30;

	@Override
	public void run() {
		for(final Map.Entry<UUID, PlayerInfo> entry : VerifPlayerData.playersData.entrySet()) {
			final PlayerInfo playerInfo = entry.getValue();
			final UUID playerUuid = entry.getKey();
			final Player player = Bukkit.getPlayer(playerUuid);
			final int ping = Reflection.getPing(player);
			if(playerInfo.getClicksAir().size() >= FastClickRunnable.sizeHistoryCps) {
				playerInfo.getClicksAir().remove(0);
				playerInfo.getClicksEntity().remove(0);
			}

			playerInfo.getClicksAir().add(playerInfo.getClickAir());
			playerInfo.getClicksEntity().add(playerInfo.getClickEntity());
			final int click = playerInfo.getClickAir() + playerInfo.getClickEntity();
			if(click > playerInfo.getMaxCPS()) {
				playerInfo.setMaxCPS(click);
			}

			if(click >= FastClickRunnable.alertCPS) {
				final double tps = TPS.getTPS();
				int lagAlertCPS = (int) ((20.0 - tps) * 2.0);
				lagAlertCPS += ping / 50;
				if(click >= FastClickRunnable.alertCPS + lagAlertCPS) {
					playerInfo.addNbAlerts();
					final long timestamp = System.currentTimeMillis();
					if(playerInfo.getLastAlert() + FastClickRunnable.timeBetwenAlert * 1000L < timestamp) {
						playerInfo.setLastAlert(timestamp);
						final String msg = ConfigUtils.VERIF_SENDALERT.getString()
								.replace("%player%", player.getName())
								.replace("%cps%", String.valueOf(click))
								.replace("%ping%", String.valueOf(ping))
								.replace("%tps%", String.valueOf(tps));
						for(final Player players : Bukkit.getOnlinePlayers()) {
							if(players.hasPermission("verifcps.mod")) {
								players.sendMessage(msg);
							}
						}
					}
				}
			}
			playerInfo.resetClickAir();
			playerInfo.resetClickEntity();
		}
	}
}
