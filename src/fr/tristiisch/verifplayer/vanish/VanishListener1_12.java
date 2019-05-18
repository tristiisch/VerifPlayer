package fr.tristiisch.verifplayer.vanish;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;

public class VanishListener1_12 implements Listener {

	@EventHandler(ignoreCancelled = true)
	public void onPlayerPickupItem(final EntityPickupItemEvent event) {
		if(!(event.getEntity() instanceof Player)) {
			return;
		}
		final Player player = (Player) event.getEntity();
		if(Vanish.isVanish(player)) {
			event.setCancelled(true);
		}
	}
}
