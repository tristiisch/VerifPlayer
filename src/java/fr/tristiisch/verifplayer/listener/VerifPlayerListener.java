package fr.tristiisch.verifplayer.listener;

import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.PluginDescriptionFile;

import fr.tristiisch.verifplayer.VerifPlayerPlugin;
import fr.tristiisch.verifplayer.utils.SpigotUtils;
import fr.tristiisch.verifplayer.utils.config.ConfigGet;
import fr.tristiisch.verifplayer.utils.permission.Permission;

public class VerifPlayerListener implements Listener {

	public VerifPlayerListener() {
		for (Player player : Bukkit.getOnlinePlayers()) {
			VerifPlayerPlugin.getInstance().getPlayerInfoHandler().addNew(player);
		}
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		VerifPlayerPlugin.getInstance().getPlayerInfoHandler().addNew(player);

		if (Permission.isAuthor(player)) {
			PluginDescriptionFile description = VerifPlayerPlugin.getInstance().getDescription();
			player.sendMessage(SpigotUtils.color("&2VerifPlayer &7» &aThis server uses &2" + description.getName() + "&a &2" + description.getVersion() + "&a develop by " + String.join(", ", description.getAuthors())));
		}
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		Set<UUID> playerViewers = VerifPlayerPlugin.getInstance().getVerifGuiHandler().getViewers(player);
		if (playerViewers != null) {
			for (UUID viewerUuid : playerViewers) {
				Player viewer = Bukkit.getPlayer(viewerUuid);
				viewer.closeInventory();
				viewer.sendMessage(ConfigGet.MESSAGES_VERIF_PLAYERDISCONNECT.getString().replaceAll("%player%", player.getName()));
			}
		}
		VerifPlayerPlugin.getInstance().getPlayerInfoHandler().remove(player);
	}
}
