package fr.tristiisch.verifplayer.core.freeze;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.tristiisch.verifplayer.utils.config.ConfigGet;
import fr.tristiisch.verifplayer.utils.permission.Permission;

public class FreezeCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String arg, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(ConfigGet.MESSAGES_CANTCONSOLE.getString());
			return true;
		}

		Player player = (Player) sender;
		if (!Permission.MODERATOR_COMMAND_FREEZE.hasPermission(sender)) {
			player.sendMessage(ConfigGet.MESSAGES_NOPERM.getString());
			return true;
		}
		if (args.length == 1) {
			/*
			 * if(!Matcher.IsUsername(args[0])) {
			 * player.sendMessage("&2Freeze &7» &4%player% &cn'est pas un pseudo valide."
			 * .replaceAll("%player%", args[0])); }
			 */
			Player target = Bukkit.getPlayer(args[0]);
			if (target == null) {
				player.sendMessage(ConfigGet.MESSAGES_FREEZE_PLAYERNOTCONNECTED.getString().replaceAll("%player%", args[0]));
				return true;
			}

			if (Freeze.isFreeze(target)) {
				Freeze.unfreeze(target);
				player.sendMessage(ConfigGet.MESSAGES_FREEZE_PLAYERUNFREEZE.getString().replace("%player%", target.getName()));
			} else {
				Freeze.freeze(target);
				player.sendMessage(ConfigGet.MESSAGES_FREEZE_PLAYERFREEZE.getString().replace("%player%", target.getName()));
			}

		} else if (args.length >= 2) {
			Player target = Bukkit.getPlayer(args[0]);
			if (target == null) {
				player.sendMessage(ConfigGet.MESSAGES_FREEZE_PLAYERNOTCONNECTED.getString().replaceAll("%player%", args[0]));
				return true;
			}

			if (Freeze.isFreeze(target)) {
				Freeze.unfreeze(target);
				player.sendMessage(ConfigGet.MESSAGES_FREEZE_PLAYERUNFREEZE.getString().replace("%player%", target.getName()));
			} else {
				String reason = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
				Freeze.freeze(target, reason);
				player.sendMessage(ConfigGet.MESSAGES_FREEZE_PLAYERFREEZEREASON.getString().replace("%player%", target.getName()).replace("%reason%", reason));
			}
		} else {
			player.sendMessage(ConfigGet.MESSAGES_FREEZE_USAGE.getString());
		}
		return true;
	}
}
