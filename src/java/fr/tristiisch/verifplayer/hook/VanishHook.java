package fr.tristiisch.verifplayer.hook;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.kitteh.vanish.VanishManager;
import org.kitteh.vanish.VanishPlugin;

import de.myzelyam.api.vanish.VanishAPI;

public class VanishHook {

	// SuperVanish & PremiumVanish & VanishNoPacket
	private static VanishHook INSTANCE;

	public static VanishHook getInstance() {
		return INSTANCE;
	}

	private boolean enabledSuperVanish;
	private boolean enabledVanishNoPacket = false;
	private VanishManager vanishNoPacketManager;

	public VanishHook(Plugin plugin) {
		INSTANCE = this;
		PluginManager pluginManager = plugin.getServer().getPluginManager();
		this.enabledSuperVanish = pluginManager.isPluginEnabled("SuperVanish") || pluginManager.isPluginEnabled("PremiumVanish");

		VanishPlugin vanishNoPacket = (VanishPlugin) pluginManager.getPlugin("VanishNoPacket");
		if (vanishNoPacket != null) {
			this.enabledVanishNoPacket = true;
			this.vanishNoPacketManager = vanishNoPacket.getManager();
		}
	}

	public void hidePlayer(Player player) {
		if (this.enabledSuperVanish) {
			VanishAPI.hidePlayer(player);
		} else if (this.enabledVanishNoPacket) {
			this.vanishNoPacketManager.reveal(player, true, true);
		}
	}

	public boolean isEnabled() {
		return this.enabledSuperVanish || this.enabledVanishNoPacket;
	}

	public void showPlayer(Player player) {
		if (this.enabledSuperVanish) {
			VanishAPI.showPlayer(player);
		} else if (this.enabledVanishNoPacket) {
			this.vanishNoPacketManager.vanish(player, true, true);
		}
	}
}