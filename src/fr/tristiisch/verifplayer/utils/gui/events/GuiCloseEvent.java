package fr.tristiisch.verifplayer.utils.gui.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryCloseEvent;

import fr.tristiisch.verifplayer.utils.gui.GuiTool;

public class GuiCloseEvent extends Event {
	
	private InventoryCloseEvent inventoryCloseEvent;
    private GuiTool guiTool;
    private static final HandlerList HANDLERS_LIST = new HandlerList();

    public GuiCloseEvent(InventoryCloseEvent inventoryCloseEvent, GuiTool guiTool){
        this.inventoryCloseEvent = inventoryCloseEvent;
        this.guiTool = guiTool;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }

    public InventoryCloseEvent getInventoryCloseEvent() {
        return inventoryCloseEvent;
    }
    
    public GuiTool getGuiTool() {
		return guiTool;
	}
}