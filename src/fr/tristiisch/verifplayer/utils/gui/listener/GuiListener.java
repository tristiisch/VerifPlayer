package fr.tristiisch.verifplayer.utils.gui.listener;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import fr.tristiisch.verifplayer.utils.gui.GuiCreator;
import fr.tristiisch.verifplayer.utils.gui.GuiManager;
import fr.tristiisch.verifplayer.utils.gui.customevents.GuiClickEvent;
import fr.tristiisch.verifplayer.utils.gui.customevents.GuiCloseEvent;

public class GuiListener implements Listener {

	@EventHandler(ignoreCancelled = true)
	public void onInventoryClick(final InventoryClickEvent event) {
		if(!(event.getWhoClicked() instanceof Player)) {
			return;
		}
		final Player player = (Player) event.getWhoClicked();
		GuiCreator gui = GuiManager.get(player);
		if(gui == null) {
			return;
		}
		final GuiClickEvent guiClickEvent = new GuiClickEvent(player, gui, event);
		Bukkit.getPluginManager().callEvent(guiClickEvent);
		gui = guiClickEvent.getGui();
		if(gui.isMissClickClosing() && event.getClickedInventory() == null) {
			event.getWhoClicked().closeInventory();
			return;
		}
		if(!gui.isClickOnItems()) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onInventoryClose(final InventoryCloseEvent event) {
		final Player player = (Player) event.getPlayer();
		final GuiCreator gui = GuiManager.remove(player);
		if(gui == null) {
			return;
		}
		final GuiCloseEvent guiCloseEvent = new GuiCloseEvent(player, gui, event);
		Bukkit.getPluginManager().callEvent(guiCloseEvent);
	}

	@EventHandler
	public void onPlayerQuit(final PlayerQuitEvent event) {
		final Player player = event.getPlayer();
		GuiManager.remove(player);
	}
}
