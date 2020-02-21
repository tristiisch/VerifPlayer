package fr.tristiisch.verifplayer.core.freeze;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;

public class FreezeListener1_12 implements Listener {

	@EventHandler(ignoreCancelled = true)
	public void onEntityPickupItem(EntityPickupItemEvent event) {
		if (!(event.getEntity() instanceof Player)) {
			return;
		}
		Player player = (Player) event.getEntity();
		if (Freeze.isFreeze(player)) {
			event.setCancelled(true);
		}
	}
}
