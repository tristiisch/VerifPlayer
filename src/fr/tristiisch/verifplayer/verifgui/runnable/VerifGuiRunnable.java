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
import org.bukkit.scheduler.BukkitRunnable;

import fr.tristiisch.verifplayer.utils.TaskManager;
import fr.tristiisch.verifplayer.verifgui.VerifGuiItem;
import fr.tristiisch.verifplayer.verifgui.VerifGuiItem.VerifGuiSlot;
import fr.tristiisch.verifplayer.verifgui.VerifGuiManager;

public class VerifGuiRunnable extends BukkitRunnable {

	private static String taskName = "verifgui";

	public static void start() {
		if(TaskManager.taskExist(taskName)) {
			return;
		}
		// 0.05 sec
		System.out.println("start VerifGuiRunnable");
		/*TaskManager.runTaskTimerAsynchronously(taskName, new VerifGuiRunnable(), 0, 1);*/
	}

	public static void stop() {
		TaskManager.cancelTaskByName(taskName);
	}

	@Override
	public void run() {
		final Set<Entry<UUID, Set<UUID>>> set = VerifGuiManager.getPlayersBeingChecked().entrySet();
		if(set.isEmpty()) {
			stop();
		}
		for(final Map.Entry<UUID, Set<UUID>> entry : set) {
			final UUID uuid = entry.getKey();
			final Set<UUID> viewers = entry.getValue();
			final Player player = Bukkit.getPlayer(uuid);
			if(player == null) {
				return;
			}
			final HashMap<Integer, ItemStack> items = new HashMap<>();

			// Head
			items.put(VerifGuiSlot.SKULL.getSlot(), VerifGuiItem.getSkull(player));

			// Effects
			items.put(VerifGuiSlot.EFFECTS.getSlot(), VerifGuiItem.getEffects(player));

			// CPS
			items.put(VerifGuiSlot.CPS.getSlot(), VerifGuiItem.getCps(player));

			// Ping + Tps
			items.put(VerifGuiSlot.PING_AND_TPS.getSlot(), VerifGuiItem.getPingAndTps(player));

			// Inv + armor + Holding slot
			items.putAll(VerifGuiItem.getInventory(player));

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

					if(entryItem != null && !entryItem.isSimilar(actuelItem)) {

						if(actuelItem.hasItemMeta() && entrySlot == VerifGuiSlot.SKULL.getSlot() && entryItem.getItemMeta().getLore().equals(actuelItem.getItemMeta().getLore())) {
							continue;
						}

						inventory.setItem(entrySlot, entryItem);
					}
				}
			}
		}
	}
}
