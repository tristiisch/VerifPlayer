package fr.tristiisch.verifplayer.core.verifspec.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import fr.tristiisch.verifplayer.core.verifspec.VerifSpec;
import fr.tristiisch.verifplayer.core.verifspec.VerifSpecTool;
import fr.tristiisch.verifplayer.utils.PlayerContents;

public class VerifSpecRestrictListener implements Listener {

	/*
	 * static { for(final Player player : Bukkit.getOnlinePlayers()) { final
	 * PlayerContents playerContents = PlayerContents.fromDisk(player);
	 * if(playerContents.hasData()) { playerContents.returnHisInventory(); } } }
	 */

	@EventHandler
	public void onEntityDamage(final EntityDamageEvent event) {
		if (!(event.getEntity() instanceof Player)) {
			return;
		}
		final Player player = (Player) event.getEntity();
		if (VerifSpec.isIn(player)) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onInventoryClick(final InventoryClickEvent event) {
		if (!(event.getWhoClicked() instanceof Player)) {
			return;
		}
		final Player player = (Player) event.getWhoClicked();
		if (VerifSpec.isIn(player)) {

			event.setCancelled(true);
			ItemStack item = event.getCurrentItem();
			if (item == null) {
				return;
			}
			VerifSpecTool tool = VerifSpecTool.getTool(item);
			if (tool != null && tool == VerifSpecTool.QUIT) {
				VerifSpec.disable(player);
			}
		}
	}

	@EventHandler
	public void onPlayerQuit(final PlayerQuitEvent event) {
		final Player player = event.getPlayer();
		if (VerifSpec.isIn(player)) {
			VerifSpec.disable(player);
		}
	}

	@EventHandler
	public void PlayerJoinEvent(final PlayerJoinEvent event) {
		final Player player = event.getPlayer();
		final PlayerContents playerContents = PlayerContents.fromDisk(player);
		if (!playerContents.hasData()) {
			return;
		}

		playerContents.returnHisInventory();
	}
}
