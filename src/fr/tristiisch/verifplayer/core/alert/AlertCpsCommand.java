package fr.tristiisch.verifplayer.core.alert;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.tristiisch.verifplayer.utils.config.ConfigGet;
import fr.tristiisch.verifplayer.utils.permission.Permission;

public class AlertCpsCommand implements CommandExecutor {

	@Override
	public boolean onCommand(final CommandSender sender, final Command cmd, final String arg, final String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(ConfigGet.MESSAGES_CANTCONSOLE.getString());
			return true;
		}

		final Player player = (Player) sender;
		if (!Permission.MODERATOR_COMMAND_ALERTCPS.hasPermission(sender)) {
			sender.sendMessage(ConfigGet.MESSAGES_NOPERM.getString());
			return true;
		}

		if (args.length == 0) {
			if (Alert.alertsEnabled(player)) {
				player.sendMessage(ConfigGet.MESSAGES_ALERTCPS_DISABLED.getString());
				Alert.disableAlerts(player);
			} else {
				player.sendMessage(ConfigGet.MESSAGES_ALERTCPS_ENABLED.getString());
				Alert.enableAlerts(player);
			}
		} else {
			String arg1 = args[0].toLowerCase();
			if ("off".equals(arg1)) {
				if (Alert.disableAlerts(player)) {
					player.sendMessage(ConfigGet.MESSAGES_ALERTCPS_DISABLED.getString());
				} else {
					player.sendMessage(ConfigGet.MESSAGES_ALERTCPS_ALREADYDISABLED.getString());
				}
			} else if ("on".equals(arg1)) {
				if (Alert.enableAlerts(player)) {
					player.sendMessage(ConfigGet.MESSAGES_ALERTCPS_ENABLED.getString());
				} else {
					player.sendMessage(ConfigGet.MESSAGES_ALERTCPS_ALREADYENABLED.getString());
				}
			} else {
				player.sendMessage(ConfigGet.MESSAGES_ALERTCPS_USAGE.getString());
			}
		}
		return true;
	}

}
