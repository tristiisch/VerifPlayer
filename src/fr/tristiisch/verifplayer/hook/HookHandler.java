package fr.tristiisch.verifplayer.hook;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;

import net.luckperms.api.LuckPerms;

public class HookHandler {

	private static HookHandler INSTANCE;

	public static HookHandler getInstance() {
		return INSTANCE;
	}

	public HookHandler(Plugin plugin) {
		new VanishHook(plugin);
		new EssentialsHook(plugin);
		new GroupManagerHook(plugin);
		new LuckPermsHook(plugin);
		new PlaceholderAPIHook(plugin);
		INSTANCE = this;
	}

	public String getGroup(Player player) {
		LuckPermsHook luckPermsHook = LuckPermsHook.getInstance();
		if (luckPermsHook.isEnabled()) {
			RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
			if (provider != null) {
			    return provider.getProvider().getUserManager().getUser(player.getUniqueId()).getPrimaryGroup();
			}
		}
		
		GroupManagerHook gpHook = GroupManagerHook.getInstance();
		if (gpHook.isGMEnabled()) {
			return gpHook.getGroupManager().getWorldsHolder().getWorldPermissions(player).getGroup(player.getName());
		}

		return null;
	}
}
