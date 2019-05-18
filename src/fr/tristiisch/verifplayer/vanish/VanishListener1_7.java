package fr.tristiisch.verifplayer.vanish;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;

@SuppressWarnings("deprecation")
public class VanishListener1_7 implements Listener {

	@EventHandler(ignoreCancelled = true)
	public void onPlayerPickupItem(final PlayerPickupItemEvent event) {
		final Player player = event.getPlayer();
		if(Vanish.isVanish(player)) {
			event.setCancelled(true);
		}
	}
}
