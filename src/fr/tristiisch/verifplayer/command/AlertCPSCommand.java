package fr.tristiisch.verifplayer.command;

import java.util.HashMap;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.tristiisch.verifplayer.utils.config.ConfigUtils;
import fr.tristiisch.verifplayer.utils.permission.Permission;

public class AlertCPSCommand implements CommandExecutor {

	private final HashMap<Player, Boolean> alert = new HashMap<>();

	public boolean canAlert(final Player player) {
		return this.alert.containsKey(player) ? this.alert.get(player) : true;
	}

	@Override
	public boolean onCommand(final CommandSender sender, final Command cmd, final String arg, final String[] args) {
		if(!(sender instanceof Player)) {
			sender.sendMessage(ConfigUtils.MESSAGES_CANTCONSOLE.getString());
			return true;
		}

		final Player player = (Player) sender;
		if(!Permission.MODERATOR_COMMAND_ALERTCPS.hasPermission(sender)) {
			sender.sendMessage(ConfigUtils.MESSAGES_NOPERM.getString());
			return true;
		}

		if(args.length == 0) {
			if(this.canAlert(player)) {
				player.sendMessage(ConfigUtils.MESSAGES_ALERTCPS_DISABLED.getString());
				this.setAlert(player, false);
			} else {
				player.sendMessage(ConfigUtils.MESSAGES_ALERTCPS_ENABLED.getString());
				this.setAlert(player, true);
			}
		} else {
			switch(args[0].toLowerCase()) {
			case "off":
				if(!this.canAlert(player)) {
					player.sendMessage(ConfigUtils.MESSAGES_ALERTCPS_ALREADY_DISABLED.getString());
				} else {
					player.sendMessage(ConfigUtils.MESSAGES_ALERTCPS_DISABLED.getString());
					this.setAlert(player, false);
				}
				break;
			case "on":
				if(this.canAlert(player)) {
					player.sendMessage(ConfigUtils.MESSAGES_ALERTCPS_ALREADY_ENABLED.getString());
				} else {
					player.sendMessage(ConfigUtils.MESSAGES_ALERTCPS_ENABLED.getString());
					this.setAlert(player, true);
				}
				break;
			default:
				player.sendMessage(ConfigUtils.MESSAGES_ALERTCPS_USAGE.getString());
			}
		}
		return true;
	}

	public void setAlert(final Player player, final boolean b) {
		this.alert.put(player, b);
	}
}
