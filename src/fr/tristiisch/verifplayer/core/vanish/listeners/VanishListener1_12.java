package fr.tristiisch.verifplayer.core.vanish.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;

import fr.tristiisch.verifplayer.VerifPlayerPlugin;

public class VanishListener1_12 implements Listener {

	@EventHandler(ignoreCancelled = true)
	public void onPlayerPickupItem(final EntityPickupItemEvent event) {
		if (!(event.getEntity() instanceof Player)) {
			return;
		}
		final Player player = (Player) event.getEntity();
		if (VerifPlayerPlugin.getInstance().getVanishHandler().isVanished(player)) {
			event.setCancelled(true);
		}
	}
}
