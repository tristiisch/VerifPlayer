package fr.tristiisch.verifplayer.hook;

import org.bukkit.plugin.Plugin;

import me.lucko.luckperms.bukkit.LPBukkitBootstrap;

public class LuckPermsHook {

	private static LuckPermsHook INSTANCE;

	public static LuckPermsHook getInstance() {
		return INSTANCE;
	}

	private LPBukkitBootstrap luckPerms;

	public LuckPermsHook(Plugin plugin) {
		this.luckPerms = (LPBukkitBootstrap) plugin.getServer().getPluginManager().getPlugin("LuckPerms");
		INSTANCE = this;
	}

	public LPBukkitBootstrap getLuckPerms() {
		return this.luckPerms;
	}

	public boolean isEnabled() {
		return this.luckPerms != null;
	}

}