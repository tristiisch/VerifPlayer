package fr.tristiisch.verifplayer.core.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.tristiisch.verifplayer.core.verifgui.VerifGuiManager;
import fr.tristiisch.verifplayer.utils.config.ConfigGet;
import fr.tristiisch.verifplayer.utils.permission.Permission;

public class VerifCommand implements CommandExecutor {

	@Override
	public boolean onCommand(final CommandSender sender, final Command cmd, final String arg, final String[] args) {
		if(!(sender instanceof Player)) {
			sender.sendMessage(ConfigGet.MESSAGES_CANTCONSOLE.getString());
			return true;
		}
		final Player player = (Player) sender;
		if(!Permission.MODERATOR_COMMAND_VERIF.hasPermission(sender)) {
			player.sendMessage(ConfigGet.MESSAGES_NOPERM.getString());
			return true;
		}

		if(args.length == 0) {
			player.sendMessage(ConfigGet.MESSAGES_VERIF_USAGE.getString());
			return true;
		}

		final Player target = Bukkit.getPlayer(args[0]);
		if(target == null) {
			player.sendMessage(ConfigGet.MESSAGES_VERIF_ISNOTCONNECTED.getString().replaceAll("%player%", args[0]));
			return true;
		}

		if(Permission.ADMIN.hasPermission(target) && !Permission.ADMIN.hasPermission(player)) {
			player.sendMessage(ConfigGet.MESSAGES_VERIF_CANTVERIFADMIN.getString().replaceAll("%player%", args[0]));
			return true;
		}

		if(target.getUniqueId().equals(player.getUniqueId()) && !Permission.ADMIN.hasPermission(sender)) {
			player.sendMessage(ConfigGet.MESSAGES_VERIF_CANTVERIFYOURSELF.getString().replaceAll("%player%", args[0]));
			return true;
		}

		VerifGuiManager.openVerifGUi(player, target);
		return true;
	}
}