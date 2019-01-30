package fr.tristiisch.verifplayer.utils.gui;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;

import fr.tristiisch.verifplayer.utils.gui.events.GuiClickEvent;
import fr.tristiisch.verifplayer.utils.gui.events.GuiCloseEvent;
import fr.tristiisch.verifplayer.utils.gui.events.GuiOpenEvent;

public class GuiToolsListener implements Listener {
	
	@EventHandler
	public void onInventoryOpen(InventoryOpenEvent event) {
		HumanEntity player = event.getPlayer();
		UUID playerUuid = player.getUniqueId();
		Inventory inventory = event.getInventory();
		GuiTool guiTool = GuiTools.get(playerUuid);
		
		if(guiTool == null) {
			GuiTools.add(playerUuid, new GuiTool(inventory));
		}
		Bukkit.getPluginManager().callEvent(new GuiOpenEvent(event, guiTool));
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		HumanEntity player = event.getWhoClicked();
		Inventory inventory = event.getInventory();
		GuiTool guiTool = GuiTools.get(player.getUniqueId());
		if(guiTool == null) {
			return;
		}
		Bukkit.getPluginManager().callEvent(new GuiClickEvent(event, guiTool));
		
		/*
		 * GuiName guiName = guiTool.getGuiName();
		 * 
		 * switch (guiName) { case VERIF_PLAYER: event.setCancelled(true); break;
		 * 
		 * default: break; }
		 */
	}

	@EventHandler
	public void onInventoryClose(InventoryCloseEvent event) {
		HumanEntity player = event.getPlayer();
		GuiTool guiTool = GuiTools.get(player.getUniqueId());
		Bukkit.getPluginManager().callEvent(new GuiCloseEvent(event, guiTool));
		GuiTools.remove(player.getUniqueId());
	}
	
}
