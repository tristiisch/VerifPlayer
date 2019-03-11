package fr.tristiisch.verifplayer.utils.gui.customevents;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerEvent;

import fr.tristiisch.verifplayer.utils.gui.GuiCreator;

public class GuiOpenEvent extends PlayerEvent implements Cancellable {

	private static final HandlerList HANDLERS_LIST = new HandlerList();

	public static HandlerList getHandlerList() {
		return HANDLERS_LIST;
	}

	private final InventoryOpenEvent inventoryOpenEvent;

	private final GuiCreator gui;

	public GuiOpenEvent(final Player player, final InventoryOpenEvent inventoryOpenEvent, final GuiCreator gui) {
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
	public void setCancelled(final boolean cancelled) {
		this.inventoryOpenEvent.setCancelled(cancelled);
	}
}
