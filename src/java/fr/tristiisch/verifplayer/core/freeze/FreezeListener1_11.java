package fr.tristiisch.verifplayer.core.freeze;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;

@SuppressWarnings("deprecation")
public class FreezeListener1_11 implements Listener {

	@EventHandler(ignoreCancelled = true)
	public void onEntityPickupItem(PlayerPickupItemEvent event) {
		Player player = event.getPlayer();
		if (Freeze.isFreeze(player)) {
			event.setCancelled(true);
		}
	}
}
