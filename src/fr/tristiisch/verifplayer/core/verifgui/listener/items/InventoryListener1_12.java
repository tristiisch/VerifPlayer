package fr.tristiisch.verifplayer.core.verifgui.listener.items;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;

public class InventoryListener1_12 implements Listener {

	@EventHandler(ignoreCancelled = true)
	public void onPlayerPickupItem(final EntityPickupItemEvent event) {
		if(!(event.getEntity() instanceof Player)) {
			return;
		}
		final Player player = (Player) event.getEntity();
		//		final PlayerContents playerContents = VerifGuiManager.getPlayersChecksInventoryContents(player.getUniqueId());
		//		if(playerContents != null) {
		//			event.setCancelled(true);
		//				} else {
		InventoryListener.updateInventory(player);
		//		}
	}
}
