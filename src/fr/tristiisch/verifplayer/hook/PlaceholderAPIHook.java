package fr.tristiisch.verifplayer.hook;

import org.bukkit.plugin.Plugin;

import me.clip.placeholderapi.PlaceholderAPI;

public class PlaceholderAPIHook {

	private static PlaceholderAPIHook INSTANCE;

	public static PlaceholderAPIHook getInstance() {
		return INSTANCE;
	}

	private final PlaceholderAPI placeholderApi;

	public PlaceholderAPIHook(final Plugin plugin) {
		this.placeholderApi = (PlaceholderAPI) plugin.getServer().getPluginManager().getPlugin("PlaceholderAPI");
		INSTANCE = this;
	}

	public PlaceholderAPI getPlaceholderAPI() {
		return this.placeholderApi;
	}

	public boolean isEnabled() {
		return this.placeholderApi != null;
	}

}