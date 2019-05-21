package fr.tristiisch.verifplayer.core.freeze;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.tristiisch.verifplayer.utils.SpigotUtils;
import fr.tristiisch.verifplayer.utils.config.ConfigGet;
import fr.tristiisch.verifplayer.utils.permission.Permission;

public class FreezeCommand implements CommandExecutor {

	@Override
	public boolean onCommand(final CommandSender sender, final Command cmd, final String arg, final String[] args) {
		if(!(sender instanceof Player)) {
			sender.sendMessage(ConfigGet.MESSAGES_CANTCONSOLE.getString());
			return true;
		}

		final Player player = (Player) sender;
		if(!Permission.MODERATOR_COMMAND_FREEZE.hasPermission(sender)) {
			player.sendMessage(ConfigGet.MESSAGES_NOPERM.getString());
			return true;
		}
		if(args.length == 1) {
			/*
			 * if(!Matcher.IsUsername(args[0])) {
			 * player.sendMessage("&2Freeze &7» &4%player% &cn'est pas un pseudo valide."
			 * .replaceAll("%player%", args[0])); }
			 */
			final Player target = Bukkit.getPlayer(args[0]);
			if(target == null) {
				player.sendMessage("&cLe joueur &4%player% &cn'est pas connecté.".replaceAll("%player%", args[0]));
			}

			if(Freeze.isFreeze(target)) {
				Freeze.unfreeze(target);
				player.sendMessage(SpigotUtils.color("&cVous avez unfreeze &4" + target.getName() + "&c."));
			} else {
				Freeze.freeze(target);
				player.sendMessage(SpigotUtils.color("&aVous avez freeze &2" + target.getName() + "&a."));
			}

		} else if(args.length >= 2) {
			final Player target = Bukkit.getPlayer(args[0]);
			if(target == null) {
				player.sendMessage("&cLe joueur &4%player% &cn'est pas connecté.".replaceAll("%player%", args[0]));
			}

			if(Freeze.isFreeze(target)) {
				Freeze.unfreeze(target);
				player.sendMessage(SpigotUtils.color("&cVous avez unfreeze &4" + target.getName() + "&c."));
			} else {
				final String reason = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
				Freeze.freeze(target, reason);
				player.sendMessage(SpigotUtils.color("&7Vous avez &cfreeze &8" + target.getName() + "&7 avec comme motif &c" + reason + "."));
			}
		} else {
			player.sendMessage(SpigotUtils.color("&cUsage &7» &c/freeze <joueur> [motif]"));
		}
		return true;
	}
}
