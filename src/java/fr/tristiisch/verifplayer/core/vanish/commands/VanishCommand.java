package fr.tristiisch.verifplayer.core.vanish.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.tristiisch.verifplayer.VerifPlayerPlugin;
import fr.tristiisch.verifplayer.core.vanish.VanishHandler;
import fr.tristiisch.verifplayer.utils.config.ConfigGet;
import fr.tristiisch.verifplayer.utils.permission.Permission;

public class VanishCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(ConfigGet.MESSAGES_CANTCONSOLE.getString());
			return true;
		}
		Player player = (Player) sender;
		if (!Permission.MODERATOR_COMMAND_VERIFVANISH.hasPermission(sender)) {
			player.sendMessage(ConfigGet.MESSAGES_NOPERM.getString());
			return true;
		}

		Player target;
		if (args.length == 1) {
			target = Bukkit.getPlayer(args[0]);
		} else {
			target = player;
		}
		VanishHandler vanishHandler = VerifPlayerPlugin.getInstance().getVanishHandler();
		if (vanishHandler.isVanished(target)) {
			vanishHandler.disable(target, true);
		} else {
			vanishHandler.enable(target, true);
		}
		return true;
	}
}