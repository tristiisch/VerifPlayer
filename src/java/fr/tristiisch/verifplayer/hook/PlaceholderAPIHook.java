package fr.tristiisch.verifplayer.hook;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.PlaceholderAPIPlugin;

public class PlaceholderAPIHook {

	private static PlaceholderAPIHook INSTANCE;

	public static PlaceholderAPIHook getInstance() {
		return INSTANCE;
	}

	private PlaceholderAPIPlugin placeholderApi;

	public PlaceholderAPIHook(Plugin plugin) {
		this.placeholderApi = (PlaceholderAPIPlugin) plugin.getServer().getPluginManager().getPlugin("PlaceholderAPI");
		INSTANCE = this;
	}

	public PlaceholderAPIPlugin getPlaceholderAPI() {
		return this.placeholderApi;
	}

	public boolean isEnabled() {
		return this.placeholderApi != null;
	}

	public void setPlaceholders(Player player, String s) {
		if (this.isEnabled()) {
			PlaceholderAPI.setPlaceholders(player, s);
		}
	}

}