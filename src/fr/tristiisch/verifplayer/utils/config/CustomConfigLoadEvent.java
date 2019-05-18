package fr.tristiisch.verifplayer.utils.config;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import fr.tristiisch.verifplayer.utils.config.CustomConfig.CustomConfigs;

public class CustomConfigLoadEvent extends Event {

	public static final HandlerList handlers = new HandlerList();

	public static HandlerList getHandlerList() {
		return handlers;
	}

	CustomConfigs customConfig;

	public CustomConfigLoadEvent(final CustomConfigs customConfig) {
		this.customConfig = customConfig;
	}

	public CustomConfigs getCustomConfig() {
		return this.customConfig;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

}
