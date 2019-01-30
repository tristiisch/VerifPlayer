package fr.tristiisch.verifplayer.runnable;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import fr.tristiisch.verifplayer.VerifPlayer;
import fr.tristiisch.verifplayer.object.PlayerInfo;
import fr.tristiisch.verifplayer.utils.ConfigUtils;
import fr.tristiisch.verifplayer.utils.Reflection;
import fr.tristiisch.verifplayer.utils.TPS;

public class FastClickRunnable extends BukkitRunnable {

	public static int sizeHistoryCps = 10;
	public static int alertCPS = 18;
	// en sec
	private final int timeBetwenAlert = 30;

	@Override
	public void run() {
		for(final PlayerInfo playerInfo : VerifPlayer.playersData) {
			final Player player = Bukkit.getPlayer(playerInfo.getUniqueId());
			final int ping = Reflection.getPing(player);

			if(playerInfo.getClicksAir().size() >= sizeHistoryCps) {
				playerInfo.getClicksAir().remove(0);
				playerInfo.getClicksEntity().remove(0);
			}

			playerInfo.getClicksAir().add(playerInfo.getClickAir());
			playerInfo.getClicksEntity().add(playerInfo.getClickEntity());

			final int click = playerInfo.getClickAir() + playerInfo.getClickEntity();

			if(click >= FastClickRunnable.alertCPS) {

				final double tps = TPS.getTPS();
				int lagAlertCPS = (int) ((20 - tps) * 2);
				lagAlertCPS += ping / 50;

				if(click >= FastClickRunnable.alertCPS + lagAlertCPS) {

					playerInfo.addNbAlerts();

					final long timestamp = System.currentTimeMillis();
					if(playerInfo.getLastAlert() + this.timeBetwenAlert * 1000L < timestamp) {

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
