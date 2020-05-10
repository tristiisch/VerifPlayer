package fr.tristiisch.verifplayer.hook;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import me.lucko.luckperms.LuckPerms;
import ru.tehkode.permissions.PermissionGroup;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class HookHandler {

	private static HookHandler INSTANCE;

	public static HookHandler getInstance() {
		return INSTANCE;
	}

	public HookHandler(Plugin plugin) {
		new VanishHook(plugin);
		new EssentialsHook(plugin);
		new GroupManagerHook(plugin);
		new PermissionsExHook(plugin);
		new LuckPermsHook(plugin);
		new PlaceholderAPIHook(plugin);
		INSTANCE = this;
	}

	public String getGroup(Player player) {
		LuckPermsHook luckPermsHook = LuckPermsHook.getInstance();
		if (luckPermsHook.isEnabled()) {
			return LuckPerms.getApi().getUser(player.getUniqueId()).getPrimaryGroup();
		}

		PermissionsExHook pexHook = PermissionsExHook.getInstance();
		if (pexHook.isEnabled()) {
			@SuppressWarnings("deprecation")
			PermissionGroup pexGroup = PermissionsEx.getUser(player).getGroups()[0];
			if (pexGroup != null) {
				return pexGroup.getName();
			}
		}

		GroupManagerHook gpHook = GroupManagerHook.getInstance();
		if (gpHook.isGMEnabled()) {
			return gpHook.getGroupManager().getWorldsHolder().getWorldPermissions(player).getGroup(player.getName());
		}

		return null;
	}
}
