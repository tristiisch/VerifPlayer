package fr.tristiisch.verifplayer.verifspec.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;

import fr.tristiisch.verifplayer.verifspec.VerifSpec;

public class VerifSpecRestrictListener implements Listener {

	@EventHandler
	public void onEntityDamage(final EntityDamageEvent event) {
		if(!(event.getEntity() instanceof Player)) {
			return;
		}
		final Player player = (Player) event.getEntity();
		if(VerifSpec.isIn(player)) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onInventoryClick(final InventoryClickEvent event) {
		if(!(event.getWhoClicked() instanceof Player)) {
			return;
		}
		final Player player = (Player) event.getWhoClicked();
		if(VerifSpec.isIn(player)) {
			event.setCancelled(true);
		}
	}
}
