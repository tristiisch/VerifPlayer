package fr.tristiisch.verifplayer.core.vanish.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;

import fr.tristiisch.verifplayer.VerifPlayerPlugin;

@SuppressWarnings("deprecation")
public class VanishListener1_11 implements Listener {

	@EventHandler(ignoreCancelled = true)
	public void onPlayerPickupItem(PlayerPickupItemEvent event) {
		Player player = event.getPlayer();
		if (VerifPlayerPlugin.getInstance().getVanishHandler().isVanished(player)) {
			event.setCancelled(true);
		}
	}
}
