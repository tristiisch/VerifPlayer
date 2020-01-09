package fr.tristiisch.verifplayer.hook;

import org.bukkit.plugin.Plugin;

import com.earth2me.essentials.Essentials;

public class EssentialsHook {

	private static EssentialsHook INSTANCE;

	public static EssentialsHook getInstance() {
		return INSTANCE;
	}

	private Essentials essentiels;

	public EssentialsHook(Plugin plugin) {
		this.essentiels = (Essentials) plugin.getServer().getPluginManager().getPlugin("Essentials");
		INSTANCE = this;
	}

	public Essentials getEssentials() {
		return this.essentiels;
	}

	public boolean isEssEnabled() {
		return this.essentiels != null;
	}

}
