package fr.tristiisch.verifplayer.verifgui.listener.items;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;

@SuppressWarnings("deprecation")
public class InventoryListener1_7 implements Listener {

	@EventHandler(ignoreCancelled = true)
	public void onPlayerPickupItem(final PlayerPickupItemEvent event) {
		final Player player = event.getPlayer();
		//				final PlayerContents playerContents = VerifGuiManager.getPlayersChecksInventoryContents(player.getUniqueId());
		//				if(playerContents != null) {
		//					event.setCancelled(true);
		//				} else {
		InventoryListener.updateInventory(player);
		//				}
	}
}
