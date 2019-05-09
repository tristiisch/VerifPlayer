package fr.tristiisch.verifplayer.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.tristiisch.verifplayer.Main;
import fr.tristiisch.verifplayer.utils.Utils;
import fr.tristiisch.verifplayer.utils.config.ConfigUtils;
import fr.tristiisch.verifplayer.utils.permission.Permission;

public class VerifPlayerCommand implements CommandExecutor {

	@Override
	public boolean onCommand(final CommandSender sender, final Command cmd, final String arg, final String[] args) {
		final Player player;
		if(sender instanceof Player) {
			player = (Player) sender;
			if(!Permission.ADMIN.hasPermission(sender)) {
				if(args.length == 0 && args[0].equalsIgnoreCase("about")) {
					sender.sendMessage(Utils.color("&2VerifPlayer &aV" + Main.getInstance().getDescription().getVersion() + " develop by Tristiisch."));
					return true;
				}
				player.sendMessage(ConfigUtils.MESSAGES_NOPERM.getString());
				return true;
			}
		}

		if(args.length == 0) {
			sender.sendMessage(Utils.color("&cUsage &7» &c/verifplayer <reload|about>"));

		} else if(args[0].equalsIgnoreCase("reload")) {
			Main.getInstance().reloadConfig();
			sender.sendMessage(Utils.color("&2VerifPlayer &7» &aThe config has been reload successfully"));

		} else if(Bukkit.getPlayer(args[0]) != null) {
			sender.sendMessage(Utils.color("&c&nWrong command&c: &cUsage &7» &c/verif <player>"));

		} else {

			sender.sendMessage(Utils.color("&cUsage &7» &c/verifplayer <reload>"));
		}

		return true;
	}
}
