package fr.tristiisch.verifplayer.gui.listener;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import fr.tristiisch.verifplayer.VerifPlayerPlugin;
import fr.tristiisch.verifplayer.gui.customevents.GuiClickEvent;
import fr.tristiisch.verifplayer.gui.customevents.GuiCloseEvent;
import fr.tristiisch.verifplayer.gui.objects.GuiCreator;

public class GuiListener implements Listener {

	@EventHandler(ignoreCancelled = true)
	public void onInventoryClick(InventoryClickEvent event) {
		if (!(event.getWhoClicked() instanceof Player)) {
			return;
		}
		Player player = (Player) event.getWhoClicked();
		GuiCreator gui = VerifPlayerPlugin.getInstance().getVerifGuiHandler().get(player);
		if (gui == null) {
			return;
		}
		GuiClickEvent guiClickEvent = new GuiClickEvent(player, gui, event);
		Bukkit.getPluginManager().callEvent(guiClickEvent);
		gui = guiClickEvent.getGui();

		if (gui.isMissClickClosing() && event.getClickedInventory() == null) {
			event.getWhoClicked().closeInventory();
			return;
		}
		if (!gui.isClickOnItems()) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onInventoryClose(InventoryCloseEvent event) {
		Player player = (Player) event.getPlayer();
		GuiCreator gui = VerifPlayerPlugin.getInstance().getVerifGuiHandler().remove(player);
		if (gui == null) {
			return;
		}
		GuiCloseEvent guiCloseEvent = new GuiCloseEvent(player, gui, event);
		Bukkit.getPluginManager().callEvent(guiCloseEvent);
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		VerifPlayerPlugin.getInstance().getVerifGuiHandler().remove(player);
	}
}
