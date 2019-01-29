package fr.tristiisch.verifplayer.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.tristiisch.verifplayer.Main;
import fr.tristiisch.verifplayer.utils.ConfigUtils;
import fr.tristiisch.verifplayer.utils.Utils;

public class VerifPlayerCommand implements CommandExecutor {

	@Override
	public boolean onCommand(final CommandSender sender, final Command cmd, final String arg, final String[] args) {

		final Player player;
		if(sender instanceof Player) {
			player = (Player) sender;
			if(!player.hasPermission("verifplayer.admin")) {
				player.sendMessage(ConfigUtils.NOPERM.getString());
				return true;
			}
		}

		if(args.length == 0) {
			sender.sendMessage(Utils.color("&cUsage &7» &c/verifplayer <reload|about>"));

		} else if(args[0].equalsIgnoreCase("reload")) {
			Main.getInstance().reloadConfig();
			sender.sendMessage(Utils.color("&2VerifCPS &7» &aThe config has been reload successfully"));

		} else if(args[0].equalsIgnoreCase("about")) {
			sender.sendMessage(Utils.color("&2VerifCPS &aV" + Main.getInstance().getDescription().getVersion() + " develop by Tristiisch."));

		} else if(Bukkit.getPlayer(args[0]) != null) {
			sender.sendMessage(Utils.color("&cUsage &7» &c/verif <player>"));

		} else {

			sender.sendMessage(Utils.color("&cUsage &7» &c/verifplayer <reload|about>"));
		}

		return true;
	}
}
