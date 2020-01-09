package fr.tristiisch.verifplayer.core.verifgui.listener.items;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerEggThrowEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Repairable;

import fr.tristiisch.verifplayer.VerifPlayerPlugin;
import fr.tristiisch.verifplayer.core.verifgui.VerifGuiItem;

@SuppressWarnings("deprecation")
public class InventoryListener implements Listener {

	public static void updateInventory(Player player) {
		Set<UUID> viewers = VerifPlayerPlugin.getInstance().getVerifGuiHandler().getViewers(player);
		if (viewers == null) {
			return;
		}

		VerifPlayerPlugin.getInstance().getTaskHandler().runTask(() -> {

			ConcurrentHashMap<Integer, ItemStack> items = VerifGuiItem.getInventory(player.getInventory());

			Iterator<Entry<Integer, ItemStack>> iterator = items.entrySet().iterator();

			for (UUID viewersUuid : viewers) {
				Player viewer = Bukkit.getPlayer(viewersUuid);
				InventoryView inventory = viewer.getOpenInventory();

				while (iterator.hasNext()) {
					Entry<Integer, ItemStack> entry = iterator.next();

					int itemSlot = entry.getKey();
					ItemStack newItem = entry.getValue();
					ItemStack actuelItem = inventory.getItem(itemSlot);

					if (actuelItem != null && newItem != null && actuelItem.isSimilar(newItem) && actuelItem.getAmount() == newItem.getAmount()) {
						items.remove(itemSlot);
					} else {
						inventory.setItem(itemSlot, newItem);
					}
				}
			}
		});
	}

	@EventHandler(ignoreCancelled = true)
	public void onBlockBreak(BlockBreakEvent event) {
		Player player = event.getPlayer();

		ItemStack item = event.getPlayer().getItemInHand();
		if (item == null || !(item.getItemMeta() instanceof Repairable)) {
			return;
		}
		updateInventory(player);
	}

	@EventHandler(ignoreCancelled = true)
	public void onBlockPlace(BlockPlaceEvent event) {
		Player player = event.getPlayer();
		updateInventory(player);
	}

	@EventHandler(ignoreCancelled = true)
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
		if (!(event.getEntity() instanceof Player)) {
			return;
		}
		Player player = (Player) event.getEntity();
		updateInventory(player);
	}

	@EventHandler(ignoreCancelled = true)
	public void onEntityShootBow(EntityShootBowEvent event) {
		if (!(event.getEntity() instanceof Player)) {
			return;
		}

		Player player = (Player) event.getEntity();
		updateInventory(player);
	}

	@EventHandler(ignoreCancelled = true)
	public void onHangingBreak(HangingBreakByEntityEvent event) {
		if (!(event.getEntity() instanceof Player)) {
			return;
		}

		Player player = (Player) event.getEntity();
		updateInventory(player);
	}

	@EventHandler(ignoreCancelled = true)
	public void onHangingPlace(HangingPlaceEvent event) {
		Player player = event.getPlayer();
		updateInventory(player);
	}

	@EventHandler
	public void onInventoryClose(InventoryCloseEvent event) {
		Player player = (Player) event.getPlayer();
		updateInventory(player);
	}

	@EventHandler(ignoreCancelled = true)
	public void onInventoryOpen(InventoryOpenEvent event) {
		Player player = (Player) event.getPlayer();
		updateInventory(player);
	}

	@EventHandler(ignoreCancelled = true)
	public void onPlayerDropItem(PlayerDropItemEvent event) {
		Player player = event.getPlayer();
		updateInventory(player);
	}

	@EventHandler(ignoreCancelled = true)
	public void onPlayerEggThrow(PlayerEggThrowEvent event) {
		Player player = event.getPlayer();
		updateInventory(player);
	}

	@EventHandler(ignoreCancelled = true)
	public void onPlayerInteractAtEntity(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		ItemStack item;
		item = event.getPlayer().getItemInHand();

		if (item == null || !(item.getItemMeta() instanceof Repairable)) {
			return;
		}
		updateInventory(player);
	}

	@EventHandler(ignoreCancelled = true)
	public void onPlayerItemHeld(PlayerItemHeldEvent event) {
		Player player = event.getPlayer();
		updateInventory(player);
	}
}
