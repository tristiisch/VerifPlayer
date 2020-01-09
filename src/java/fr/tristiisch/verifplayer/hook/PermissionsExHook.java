package fr.tristiisch.verifplayer.hook;

import org.bukkit.plugin.Plugin;

import ru.tehkode.permissions.bukkit.PermissionsEx;

public class PermissionsExHook {

	private static PermissionsExHook INSTANCE;

	public static PermissionsExHook getInstance() {
		return INSTANCE;
	}

	private PermissionsEx pex;

	public PermissionsExHook(Plugin plugin) {
		this.pex = (PermissionsEx) plugin.getServer().getPluginManager().getPlugin("PermissionsEx");
		INSTANCE = this;
	}

	public PermissionsEx getPex() {
		return this.pex;
	}

	public boolean isEnabled() {
		return this.pex != null;
	}

}
