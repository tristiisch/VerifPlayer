package fr.tristiisch.verifplayer.core.verifgui.runnable;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import fr.tristiisch.verifplayer.VerifPlayerPlugin;
import fr.tristiisch.verifplayer.core.verifgui.VerifGuiItem;
import fr.tristiisch.verifplayer.core.verifgui.VerifGuiItem.VerifGuiSlot;

public class VerifGuiRunnable {

	public static void run() {
		for (Entry<UUID, Set<UUID>> entry : VerifPlayerPlugin.getInstance().getVerifGuiHandler().getPlayersBeingChecked().entrySet()) {
			UUID uuid = entry.getKey();
			Set<UUID> viewers = entry.getValue();
			Player player = Bukkit.getPlayer(uuid);
			if (player == null) {
				return;
			}
			Map<Integer, ItemStack> items = new HashMap<>();

			// Effects
			items.put(VerifGuiSlot.EFFECTS.getSlot(), VerifGuiItem.getEffects(player));

			// CPS
			items.put(VerifGuiSlot.CPS.getSlot(), VerifGuiItem.getCps(player));

			// Ping + Tps
			items.put(VerifGuiSlot.PING_AND_TPS.getSlot(), VerifGuiItem.getPingAndTps(player));

			for (UUID viewersUuid : viewers) {
				Player viewer = Bukkit.getPlayer(viewersUuid);
				InventoryView inventory = viewer.getOpenInventory();
				for (Map.Entry<Integer, ItemStack> entryItems : items.entrySet()) {
					ItemStack entryItem = entryItems.getValue();
					Integer entrySlot = entryItems.getKey();
					ItemStack actuelItem = inventory.getItem(entrySlot);
					if (entryItem == null) {

						inventory.setItem(entrySlot, null);
					} else if (!entryItem.isSimilar(actuelItem)) {

						/*
						 * if(actuelItem.hasItemMeta() && entrySlot == VerifGuiSlot.SKULL.getSlot() &&
						 * entryItem.getItemMeta().getLore().equals(actuelItem.getItemMeta().getLore()))
						 * { continue; }
						 */

						inventory.setItem(entrySlot, entryItem);
					}
				}
			}
		}
	}
}