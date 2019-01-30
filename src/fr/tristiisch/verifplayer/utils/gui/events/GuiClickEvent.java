package fr.tristiisch.verifplayer.utils.gui.events;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;

import fr.tristiisch.verifplayer.utils.gui.GuiTool;

public class GuiClickEvent extends Event implements Cancellable {
	
	private InventoryClickEvent inventoryClickEvent;
    private GuiTool guiTool;
    private static final HandlerList HANDLERS_LIST = new HandlerList();

    public GuiClickEvent(InventoryClickEvent inventoryClickEvent, GuiTool guiTool){
        this.inventoryClickEvent = inventoryClickEvent;
        this.guiTool = guiTool;
    }

    @Override
    public boolean isCancelled() {
        return inventoryClickEvent.isCancelled();
    }

	@Override
    public void setCancelled(boolean cancelled) {
		inventoryClickEvent.setCancelled(cancelled);
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }

    public InventoryClickEvent getInventoryClickEvent() {
        return inventoryClickEvent;
    }
    
    public GuiTool getGuiTool() {
		return guiTool;
	}
}