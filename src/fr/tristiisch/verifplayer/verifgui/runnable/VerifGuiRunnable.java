package fr.tristiisch.verifplayer.verifgui.runnable;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import fr.tristiisch.verifplayer.verifgui.VerifGuiItem;
import fr.tristiisch.verifplayer.verifgui.VerifGuiItem.VerifGuiSlot;
import fr.tristiisch.verifplayer.verifgui.VerifGuiManager;

public class VerifGuiRunnable {

	public static void run() {
		for(final Entry<UUID, Set<UUID>> entry : VerifGuiManager.getPlayersBeingChecked().entrySet()) {
			final UUID uuid = entry.getKey();
			final Set<UUID> viewers = entry.getValue();
			final Player player = Bukkit.getPlayer(uuid);
			if(player == null) {
				return;
			}
			final Map<Integer, ItemStack> items = new HashMap<>();

			// Effects
			items.put(VerifGuiSlot.EFFECTS.getSlot(), VerifGuiItem.getEffects(player));

			// CPS
			items.put(VerifGuiSlot.CPS.getSlot(), VerifGuiItem.getCps(player));

			// Ping + Tps
			items.put(VerifGuiSlot.PING_AND_TPS.getSlot(), VerifGuiItem.getPingAndTps(player));

			/*			// Inv + armor + Holding slot
						items.putAll(VerifGuiItem.getInventory(player));*/

			for(final UUID viewersUuid : viewers) {
				final Player viewer = Bukkit.getPlayer(viewersUuid);
				/*				final GuiCreator gui = GuiManager.get(viewer);
								if(gui == null || !gui.getGuiPage().isSamePage(VerifGuiPage.HOME)) {
									System.out.println("BUG: VerifGuiManager.remove(viewer)");
									VerifGuiManager.remove(viewer);
								}*/
				final InventoryView inventory = viewer.getOpenInventory();
				for(final Map.Entry<Integer, ItemStack> entryItems : items.entrySet()) {
					final ItemStack entryItem = entryItems.getValue();
					final Integer entrySlot = entryItems.getKey();
					final ItemStack actuelItem = inventory.getItem(entrySlot);
					if(entryItem == null) {

						inventory.setItem(entrySlot, null);
					} else if(!entryItem.isSimilar(actuelItem)) {

						/*					if(actuelItem.hasItemMeta() && entrySlot == VerifGuiSlot.SKULL.getSlot() && entryItem.getItemMeta().getLore().equals(actuelItem.getItemMeta().getLore())) {
												continue;
											}*/

						inventory.setItem(entrySlot, entryItem);
					}
				}
			}
		}
	}
}
