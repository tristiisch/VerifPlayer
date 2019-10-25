package fr.tristiisch.verifplayer.core.verifspec;

import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.tristiisch.verifplayer.utils.config.ConfigGet;
import fr.tristiisch.verifplayer.utils.permission.Permission;

public class VerifSpecCommand implements CommandExecutor {

	@Override
	public boolean onCommand(final CommandSender sender, final Command cmd, final String arg, final String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(ConfigGet.MESSAGES_CANTCONSOLE.getString());
			return true;
		}
		final Player player = (Player) sender;
		if (!Permission.MODERATOR_COMMAND_VERIFSPEC.hasPermission(sender)) {
			player.sendMessage(ConfigGet.MESSAGES_NOPERM.getString());
			return true;
		}

		if (VerifSpec.isIn(player)) {
			VerifSpec.disable(player);
			return true;
		}

		final GameMode gamemode = player.getGameMode();
		if ((gamemode.equals(GameMode.SURVIVAL) || gamemode.equals(GameMode.ADVENTURE)) && !player.isOnGround()) {
			player.sendMessage(ConfigGet.MESSAGES_VERIFSPEC_SHOULDBEONGROUND.getString());
			return true;
		}

		VerifSpec.enable(player);
		return true;
	}

}
