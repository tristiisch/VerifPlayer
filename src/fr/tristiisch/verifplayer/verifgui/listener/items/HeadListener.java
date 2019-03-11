package fr.tristiisch.verifplayer.verifgui.listener.items;

import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import fr.tristiisch.verifplayer.verifgui.VerifGuiItem;
import fr.tristiisch.verifplayer.verifgui.VerifGuiItem.VerifGuiSlot;
import fr.tristiisch.verifplayer.verifgui.VerifGuiManager;

public class HeadListener implements Listener {

	public static void updateHead(final Player player) {
		final Set<UUID> viewers = VerifGuiManager.getViewers(player);
		if(viewers == null) {
			return;
		}

		final ItemStack newItem = VerifGuiItem.getSkull(player);
		final int skullSlot = VerifGuiSlot.SKULL.getSlot();
		for(final UUID viewersUuid : viewers) {
			final Player viewer = Bukkit.getPlayer(viewersUuid);
			final InventoryView inventory = viewer.getOpenInventory();

			final ItemStack actuelItem = inventory.getItem(skullSlot);

			if(newItem.isSimilar(actuelItem)) {
				return;
			}
			inventory.setItem(skullSlot, VerifGuiItem.getSkull(player));

		}
	}

	@EventHandler(ignoreCancelled = true)
	public void onEntityDamage(final EntityDamageEvent event) {
		if(!(event.getEntity() instanceof Player)) {
			return;
		}
		final Player player = (Player) event.getEntity();
		HeadListener.updateHead(player);
	}

	@EventHandler(ignoreCancelled = true)
	public void onEntityRegainHealth(final EntityRegainHealthEvent event) {
		if(!(event.getEntity() instanceof Player)) {
			return;
		}
		System.out.println("regen");
		final Player player = (Player) event.getEntity();
		HeadListener.updateHead(player);
	}

	@EventHandler(ignoreCancelled = true)
	public void onEntityRegainHealth(final FoodLevelChangeEvent event) {
		if(!(event.getEntity() instanceof Player)) {
			return;
		}
		System.out.println("regen");
		final Player player = (Player) event.getEntity();
		HeadListener.updateHead(player);
	}

	@EventHandler(ignoreCancelled = true)
	public void onInventoryClose(final InventoryCloseEvent event) {
		final Player player = (Player) event.getPlayer();
		HeadListener.updateHead(player);
	}

	@EventHandler(ignoreCancelled = true)
	public void onInventoryClose(final InventoryOpenEvent event) {
		final Player player = (Player) event.getPlayer();
		HeadListener.updateHead(player);
	}

	@EventHandler(ignoreCancelled = true)
	public void onVehicleEnter(final VehicleEnterEvent event) {
		if(!(event.getEntered() instanceof Player)) {
			return;
		}
		final Player player = (Player) event.getEntered();
		HeadListener.updateHead(player);
	}

	@EventHandler(ignoreCancelled = true)
	public void onVehicleExit(final PlayerExpChangeEvent event) {
		final Player player = event.getPlayer();
		final int amount = event.getAmount();
		final float exp = player.getExp();
		final float expToLevel = player.getExpToLevel();
		final int level = player.getLevel();

		System.out.println("Amount: " + amount + " exp: " + exp + " expToLevel: " + expToLevel + " level: " + level);
		HeadListener.updateHead(player);
	}

	@EventHandler(ignoreCancelled = true)
	public void onVehicleExit(final VehicleExitEvent event) {
		if(!(event.getExited() instanceof Player)) {
			return;
		}
		final Player player = (Player) event.getExited();
		HeadListener.updateHead(player);
	}

}
