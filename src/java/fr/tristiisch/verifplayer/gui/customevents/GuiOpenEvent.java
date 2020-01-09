package fr.tristiisch.verifplayer.gui.customevents;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerEvent;

import fr.tristiisch.verifplayer.gui.objects.GuiCreator;

public class GuiOpenEvent extends PlayerEvent implements Cancellable {

	private static HandlerList HANDLERS_LIST = new HandlerList();

	public static HandlerList getHandlerList() {
		return HANDLERS_LIST;
	}

	private InventoryOpenEvent inventoryOpenEvent;

	private GuiCreator gui;

	public GuiOpenEvent(Player player, InventoryOpenEvent inventoryOpenEvent, GuiCreator gui) {
		super(player);
		this.inventoryOpenEvent = inventoryOpenEvent;
		this.gui = gui;
	}

	public GuiCreator getGuiTool() {
		return this.gui;
	}

	@Override
	public HandlerList getHandlers() {
		return HANDLERS_LIST;
	}

	public InventoryOpenEvent getInventoryOpenEvent() {
		return this.inventoryOpenEvent;
	}

	@Override
	public boolean isCancelled() {
		return this.inventoryOpenEvent.isCancelled();
	}

	@Override
	public void setCancelled(boolean cancelled) {
		this.inventoryOpenEvent.setCancelled(cancelled);
	}
}
