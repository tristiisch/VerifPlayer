package fr.tristiisch.verifplayer.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.tristiisch.verifplayer.VerifPlayer;
import fr.tristiisch.verifplayer.utils.ConfigUtils;

public class VerifCommand implements CommandExecutor {

	@Override
	public boolean onCommand(final CommandSender sender, final Command cmd, final String arg, final String[] args) {
		if(!(sender instanceof Player)) {
			sender.sendMessage(ConfigUtils.CANTCONSOLE.getString());
			return true;
		}
		final Player player = (Player) sender;
		if(!player.hasPermission("verifplayer.mod")) {
			player.sendMessage(ConfigUtils.NOPERM.getString());
			return true;
		}

		if(args.length == 0) {
			player.sendMessage(ConfigUtils.VERIF_USAGE.getString());
			return true;
		}

		final Player target = Bukkit.getPlayer(args[0]);
		if(target == null) {
			player.sendMessage(ConfigUtils.VERIF_ISNOTCONNECTED.getString().replaceAll("%player%", args[0]));
			return true;
		}
		VerifPlayer.openVerifGUi(player, target);
		return true;
	}
}
