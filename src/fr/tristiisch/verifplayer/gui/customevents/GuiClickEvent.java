package fr.tristiisch.verifplayer.gui.customevents;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerEvent;

import fr.tristiisch.verifplayer.gui.api.GuiCreator;

public class GuiClickEvent extends PlayerEvent implements Cancellable {

	public static final HandlerList handlers;
	static {
		handlers = new HandlerList();
	}

	public static HandlerList getHandlerList() {
		return GuiClickEvent.handlers;
	}

	private final GuiCreator gui;

	private final InventoryClickEvent inventoryClickEvent;

	public GuiClickEvent(final Player player, final GuiCreator gui, final InventoryClickEvent event) {
		super(player);
		this.gui = gui;
		this.inventoryClickEvent = event;
	}

	public GuiCreator getGui() {
		return this.gui;
	}

	@Override
	public HandlerList getHandlers() {
		return GuiClickEvent.handlers;
	}

	public InventoryClickEvent getInventoryClickEvent() {
		return this.inventoryClickEvent;
	}

	@Override
	public boolean isCancelled() {
		return this.inventoryClickEvent.isCancelled();
	}

	@Override
	public void setCancelled(final boolean cancel) {
		this.inventoryClickEvent.setCancelled(cancel);
	}
}
