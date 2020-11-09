package fr.tristiisch.verifplayer.core.alert;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class AlertCpsListener implements Listener {

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		Alert.disableAlerts(player);
	}
}
