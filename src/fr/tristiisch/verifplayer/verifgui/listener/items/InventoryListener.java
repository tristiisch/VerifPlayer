package fr.tristiisch.verifplayer.verifgui.listener.items;

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
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerEggThrowEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Repairable;

import fr.tristiisch.verifplayer.utils.TaskManager;
import fr.tristiisch.verifplayer.verifgui.VerifGuiItem;
import fr.tristiisch.verifplayer.verifgui.VerifGuiManager;

@SuppressWarnings("deprecation")
public class InventoryListener implements Listener {

	public static void updateInventory(final Player player) {
		final Set<UUID> viewers = VerifGuiManager.getViewers(player);
		if(viewers == null) {
			return;
		}

		TaskManager.runTask(() -> {

			final ConcurrentHashMap<Integer, ItemStack> items = VerifGuiItem.getInventory(player.getInventory());

			final Iterator<Entry<Integer, ItemStack>> iterator = items.entrySet().iterator();

			for(final UUID viewersUuid : viewers) {
				final Player viewer = Bukkit.getPlayer(viewersUuid);
				final InventoryView inventory = viewer.getOpenInventory();

				while(iterator.hasNext()) {
					final Entry<Integer, ItemStack> entry = iterator.next();

					final int itemSlot = entry.getKey();
					final ItemStack newItem = entry.getValue();
					final ItemStack actuelItem = inventory.getItem(itemSlot);

					if(actuelItem.isSimilar(newItem) && actuelItem.getAmount() == newItem.getAmount()) {
						items.remove(itemSlot);
					} else {
						inventory.setItem(itemSlot, newItem);
					}
				}
			}
		});
	}

	@EventHandler(ignoreCancelled = true)
	public void HangingBreakEvent(final HangingBreakByEntityEvent event) {
		if(!(event.getEntity() instanceof Player)) {
			return;
		}

		final Player player = (Player) event.getEntity();
		updateInventory(player);
	}

	@EventHandler(ignoreCancelled = true)
	public void HangingPlaceEvent(final HangingPlaceEvent event) {
		final Player player = event.getPlayer();
		updateInventory(player);
	}

	@EventHandler(ignoreCancelled = true)
	public void onBlockBreak(final BlockBreakEvent event) {
		final Player player = event.getPlayer();

		final ItemStack item = event.getPlayer().getItemInHand();
		if(item == null || !(item.getItemMeta() instanceof Repairable)) {
			return;
		}
		updateInventory(player);
	}

	@EventHandler(ignoreCancelled = true)
	public void onBlockPlace(final BlockPlaceEvent event) {
		final Player player = event.getPlayer();
		updateInventory(player);
	}

	@EventHandler(ignoreCancelled = true)
	public void onEntityDamageByEntity(final EntityDamageByEntityEvent event) {
		if(!(event.getEntity() instanceof Player)) {
			return;
		}
		final Player player = (Player) event.getEntity();
		updateInventory(player);
	}

	@EventHandler(ignoreCancelled = true)
	public void onInventoryClick(final InventoryClickEvent event) {
		if(!(event.getWhoClicked() instanceof Player)) {
			return;
		}
		final Player player = (Player) event.getWhoClicked();

		updateInventory(player);
	}

	@EventHandler(ignoreCancelled = true)
	public void onPlayerDropItem(final PlayerDropItemEvent event) {
		final Player player = event.getPlayer();
		updateInventory(player);
	}

	@EventHandler(ignoreCancelled = true)
	public void onPlayerEggThrow(final PlayerEggThrowEvent event) {
		final Player player = event.getPlayer();
		updateInventory(player);
	}

	@EventHandler(ignoreCancelled = true)
	public void onPlayerInteractAtEntity(final PlayerInteractEvent event) {
		final Player player = event.getPlayer();
		final ItemStack item;
		item = event.getPlayer().getItemInHand();

		if(item == null || !(item.getItemMeta() instanceof Repairable)) {
			return;
		}
		updateInventory(player);
	}

	@EventHandler(ignoreCancelled = true)
	public void onPlayerItemHeld(final PlayerItemHeldEvent event) {
		final Player player = event.getPlayer();

		updateInventory(player);
	}
}
