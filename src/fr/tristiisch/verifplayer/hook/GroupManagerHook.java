package fr.tristiisch.verifplayer.hook;

import org.anjocaido.groupmanager.GroupManager;
import org.bukkit.plugin.Plugin;

public class GroupManagerHook {

	private static GroupManagerHook INSTANCE;

	public static GroupManagerHook getInstance() {
		return INSTANCE;
	}

	private final GroupManager groupManager;

	public GroupManagerHook(final Plugin plugin) {
		this.groupManager = (GroupManager) plugin.getServer().getPluginManager().getPlugin("GroupManager");

		INSTANCE = this;
	}

	public GroupManager getGroupManager() {
		return this.groupManager;
	}

	public boolean isGMEnabled() {
		return this.groupManager != null;
	}
}
