package fr.tristiisch.verifplayer.listener;

import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

import fr.tristiisch.verifplayer.VerifPlayer;
import fr.tristiisch.verifplayer.utils.gui.GuiName;
import fr.tristiisch.verifplayer.utils.gui.GuiTool;
import fr.tristiisch.verifplayer.utils.gui.events.GuiClickEvent;
import fr.tristiisch.verifplayer.utils.gui.events.GuiCloseEvent;
import fr.tristiisch.verifplayer.utils.gui.events.GuiOpenEvent;

public class GuiListener implements Listener {

	@EventHandler
	public void onGuiOpen(GuiOpenEvent event) {
		GuiTool guiTool = event.getGuiTool();
		GuiName guiName = guiTool.getGuiName();
		if(guiName != GuiName.VERIF_PLAYER) {
			return;
		}
	}
	
	@EventHandler
	public void onGuiClick(GuiClickEvent event) {
		GuiTool guiTool = event.getGuiTool();
		GuiName guiName = guiTool.getGuiName();
		if(guiName != GuiName.VERIF_PLAYER) {
			return;
		}
		event.setCancelled(true);
	}
	
	@EventHandler
	public void onGuiClose(GuiCloseEvent event) {
		InventoryCloseEvent eventOpen = event.getInventoryCloseEvent();
		HumanEntity player = eventOpen.getPlayer();
		GuiTool guiTool = event.getGuiTool();
		GuiName guiName = guiTool.getGuiName();
		if(guiName != GuiName.VERIF_PLAYER) {
			return;
		}
		VerifPlayer.removeViewer(player.getUniqueId());
	}
}
