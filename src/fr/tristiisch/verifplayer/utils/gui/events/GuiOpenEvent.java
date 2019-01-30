package fr.tristiisch.verifplayer.utils.gui.events;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryOpenEvent;

import fr.tristiisch.verifplayer.utils.gui.GuiTool;

public class GuiOpenEvent extends Event implements Cancellable {
	
	private InventoryOpenEvent inventoryOpenEvent;
    private GuiTool guiTool;
    private static final HandlerList HANDLERS_LIST = new HandlerList();

    public GuiOpenEvent(InventoryOpenEvent inventoryOpenEvent, GuiTool guiTool){
        this.inventoryOpenEvent = inventoryOpenEvent;
        this.guiTool = guiTool;
    }

    @Override
    public boolean isCancelled() {
        return inventoryOpenEvent.isCancelled();
    }

	@Override
    public void setCancelled(boolean cancelled) {
		inventoryOpenEvent.setCancelled(cancelled);
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }

    public InventoryOpenEvent getInventoryOpenEvent() {
        return inventoryOpenEvent;
    }
    
    public GuiTool getGuiTool() {
		return guiTool;
	}
}
