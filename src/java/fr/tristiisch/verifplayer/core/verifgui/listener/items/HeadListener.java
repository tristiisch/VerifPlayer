package fr.tristiisch.verifplayer.core.verifgui.listener.items;

import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.tristiisch.verifplayer.VerifPlayerPlugin;
import fr.tristiisch.verifplayer.core.verifgui.VerifGuiItem;
import fr.tristiisch.verifplayer.core.verifgui.VerifGuiItem.VerifGuiSlot;

public class HeadListener implements Listener {

	public static void updateHead(Player player) {
		Set<UUID> viewers = VerifPlayerPlugin.getInstance().getVerifGuiHandler().getViewers(player);
		if (viewers == null)
			return;
		VerifPlayerPlugin.getInstance().getTaskHandler().runTask(() -> {
			ItemStack newItem = VerifGuiItem.getSkull(player);
			int skullSlot = VerifGuiSlot.SKULL.getSlot();
			for (UUID viewersUuid : viewers) {
				Player viewer = Bukkit.getPlayer(viewersUuid);
				InventoryView inventory = viewer.getOpenInventory();
				ItemStack actuelItem = inventory.getItem(skullSlot);
				if (newItem.isSimilar(actuelItem))
					return;
				if (actuelItem.getType().equals(newItem.getType())) {
					ItemMeta itemMeta = actuelItem.getItemMeta();
					itemMeta.setLore(newItem.getItemMeta().getLore());
					actuelItem.setItemMeta(itemMeta);
				} else
					inventory.setItem(skullSlot, newItem);

			}
		});
	}

	@EventHandler(ignoreCancelled = true)
	public void onEntityDamage(EntityDamageEvent event) {
		if (!(event.getEntity() instanceof Player))
			return;
		Player player = (Player) event.getEntity();
		HeadListener.updateHead(player);
	}

	@EventHandler(ignoreCancelled = true)
	public void onEntityRegainHealth(EntityRegainHealthEvent event) {
		if (!(event.getEntity() instanceof Player))
			return;
		Player player = (Player) event.getEntity();
		HeadListener.updateHead(player);
	}

	@EventHandler(ignoreCancelled = true)
	public void onFoodLevelChange(FoodLevelChangeEvent event) {
		if (!(event.getEntity() instanceof Player))
			return;
		Player player = (Player) event.getEntity();
		HeadListener.updateHead(player);
	}

	@EventHandler(ignoreCancelled = true)
	public void onInventoryClose(InventoryCloseEvent event) {
		Player player = (Player) event.getPlayer();
		HeadListener.updateHead(player);
	}

	@EventHandler(ignoreCancelled = true)
	public void onInventoryOpen(InventoryOpenEvent event) {
		Player player = (Player) event.getPlayer();
		HeadListener.updateHead(player);
	}

	@EventHandler(ignoreCancelled = true)
	public void onPlayerDeath(PlayerDeathEvent event) {
		Player player = event.getEntity();
		HeadListener.updateHead(player);
	}

	@EventHandler(ignoreCancelled = true)
	public void onPlayerExpChange(PlayerExpChangeEvent event) {
		Player player = event.getPlayer();
		// int amount = event.getAmount();
		// float exp = player.getExp();
		// float expToLevel = player.getExpToLevel();
		// int level = player.getLevel();
		//
		// System.out.println("Amount: " + amount + " exp: " + exp + " expToLevel: " +
		// expToLevel + " level: " + level);
		HeadListener.updateHead(player);
	}

	@EventHandler(ignoreCancelled = true)
	public void onVehicleEnter(VehicleEnterEvent event) {
		if (!(event.getEntered() instanceof Player))
			return;
		Player player = (Player) event.getEntered();
		HeadListener.updateHead(player);
	}

	@EventHandler(ignoreCancelled = true)
	public void onVehicleExit(VehicleExitEvent event) {
		if (!(event.getExited() instanceof Player))
			return;
		Player player = (Player) event.getExited();

		HeadListener.updateHead(player);
	}

}
