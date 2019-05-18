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

	public HookHandler(final Plugin plugin) {
		new EssentialsHook(plugin);
		new GroupManagerHook(plugin);
		new PermissionsExHook(plugin);
		new LuckPermsHook(plugin);
		new PlaceholderAPIHook(plugin);
		INSTANCE = this;
	}

	public String getGroup(final Player player) {
		final LuckPermsHook luckPermsHook = LuckPermsHook.getInstance();
		if(luckPermsHook.isEnabled()) {
			return LuckPerms.getApi().getUser(player.getUniqueId()).getPrimaryGroup();
		}

		final PermissionsExHook pexHook = PermissionsExHook.getInstance();
		if(pexHook.isEnabled()) {
			@SuppressWarnings("deprecation")
			final PermissionGroup pexGroup = PermissionsEx.getUser(player).getGroups()[0];
			if(pexGroup != null) {
				return pexGroup.getName();
			}
		}

		final GroupManagerHook gpHook = GroupManagerHook.getInstance();
		if(gpHook.isGMEnabled()) {
			return gpHook.getGroupManager().getWorldsHolder().getWorldPermissions(player).getGroup(player.getName());
		}

		return null;
	}
}
