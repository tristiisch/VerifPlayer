package fr.tristiisch.verifplayer.utils.config;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import fr.tristiisch.verifplayer.utils.config.CustomConfigs.CustomConfig;

public class CustomConfigLoadEvent extends Event {

	public static HandlerList handlers = new HandlerList();

	public static HandlerList getHandlerList() {
		return handlers;
	}

	CustomConfig customConfig;

	public CustomConfigLoadEvent(CustomConfig customConfig) {
		this.customConfig = customConfig;
	}

	public CustomConfig getCustomConfig() {
		return this.customConfig;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

}
