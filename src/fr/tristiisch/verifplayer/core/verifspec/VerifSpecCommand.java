package fr.tristiisch.verifplayer.core.verifspec;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.tristiisch.verifplayer.utils.SpigotUtils;
import fr.tristiisch.verifplayer.utils.config.ConfigGet;
import fr.tristiisch.verifplayer.utils.permission.Permission;

public class VerifSpecCommand implements CommandExecutor {

	@Override
	public boolean onCommand(final CommandSender sender, final Command cmd, final String arg, final String[] args) {
		if(!(sender instanceof Player)) {
			sender.sendMessage(ConfigGet.MESSAGES_CANTCONSOLE.getString());
			return true;
		}
		final Player player = (Player) sender;
		if(!Permission.MODERATOR_COMMAND_VERIFSPEC.hasPermission(sender)) {
			player.sendMessage(ConfigGet.MESSAGES_NOPERM.getString());
			return true;
		}

		if(VerifSpec.isIn(player)) {
			VerifSpec.disable(player);
			return true;
		}

		if(!player.isOnGround()) {
			player.sendMessage(SpigotUtils.color("&2EmeraldMC &7» &cVous devez être au sol pour utiliser cette commande."));
			return true;
		}

		VerifSpec.enable(player);
		return true;
	}

}
