package fr.tristiisch.verifplayer.core.vanish;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;

@SuppressWarnings("deprecation")
public class VanishListener1_11 implements Listener {

	@EventHandler(ignoreCancelled = true)
	public void onPlayerPickupItem(final PlayerPickupItemEvent event) {
		final Player player = event.getPlayer();
		if(Vanish.isVanished(player)) {
			event.setCancelled(true);
		}
	}
}
