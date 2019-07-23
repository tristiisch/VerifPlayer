package fr.tristiisch.verifplayer.gui.customevents;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerEvent;

import fr.tristiisch.verifplayer.gui.objects.GuiCreator;

public class GuiCloseEvent extends PlayerEvent {

	public static final HandlerList handlers;
	static {
		handlers = new HandlerList();
	}

	public static HandlerList getHandlerList() {
		return GuiCloseEvent.handlers;
	}

	private final GuiCreator gui;

	private final InventoryCloseEvent inventoryOpenEvent;

	public GuiCloseEvent(final Player player, final GuiCreator gui, final InventoryCloseEvent event) {
		super(player);
		this.gui = gui;
		this.inventoryOpenEvent = event;
	}

	public GuiCreator getGui() {
		return this.gui;
	}

	@Override
	public HandlerList getHandlers() {
		return GuiCloseEvent.handlers;
	}

	public InventoryCloseEvent getInventoryCloseEvent() {
		return this.inventoryOpenEvent;
	}
}
