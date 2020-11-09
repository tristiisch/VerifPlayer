package fr.tristiisch.verifplayer.core.verifspec.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
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
	 * static { for(Player player : Bukkit.getOnlinePlayers()) { final
	 * PlayerContents playerContents = PlayerContents.fromDisk(player);
	 * if(playerContents.hasData()) { playerContents.returnHisInventory(); } } }
	 */

	@EventHandler
	public void onEntityDamage(EntityDamageEvent event) {
		if (!(event.getEntity() instanceof Player))
			return;
		Player player = (Player) event.getEntity();
		if (VerifSpec.isIn(player))
			event.setCancelled(true);
	}

	@EventHandler(ignoreCancelled = true)
	public void onBlockBreak(BlockBreakEvent event) {
		Player player = event.getPlayer();
		if (VerifSpec.isIn(player))
			event.setCancelled(true);
	}

	@EventHandler(ignoreCancelled = true)
	public void onBlockPlace(BlockPlaceEvent event) {
		Player player = event.getPlayer();
		if (VerifSpec.isIn(player))
			event.setCancelled(true);
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		if (!(event.getWhoClicked() instanceof Player))
			return;
		Player player = (Player) event.getWhoClicked();
		if (VerifSpec.isIn(player)) {

			event.setCancelled(true);
			ItemStack item = event.getCurrentItem();
			if (item == null)
				return;
			VerifSpecTool tool = VerifSpecTool.getTool(item);
			if (tool != null && tool == VerifSpecTool.QUIT)
				VerifSpec.disable(player);
		}
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		if (VerifSpec.isIn(player))
			VerifSpec.disable(player);
	}

	@EventHandler
	public void onPlayerJoinEvent(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		PlayerContents playerContents = PlayerContents.fromDisk(player);
		if (!playerContents.hasData())
			return;
		playerContents.returnHisInventory();
	}
}
