package fr.tristiisch.verifplayer.listener;

import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import fr.tristiisch.verifplayer.VerifPlayerData;
import fr.tristiisch.verifplayer.utils.ConfigUtils;
import fr.tristiisch.verifplayer.utils.Utils;
import fr.tristiisch.verifplayer.verifgui.*;

public class VerifPlayerListener implements Listener {

	public VerifPlayerListener() {
		for(final Player player : Bukkit.getOnlinePlayers()) {
			VerifPlayerData.addNewPlayerInfo(player);
		}
	}

	@EventHandler
	public void onPlayerJoin(final PlayerJoinEvent event) {
		final Player player = event.getPlayer();
		VerifPlayerData.addNewPlayerInfo(player);
		if(player.getUniqueId().toString().equals("ca0a1663-1696-4d62-b93f-281965522a76")) {
			player.sendMessage(Utils.color("&2VerifPlayer &7» &aThis server uses &2VerifCPS&a develop by Tristiisch"));
		}
	}

	@EventHandler
	public void onPlayerQuit(final PlayerQuitEvent event) {
		final Player player = event.getPlayer();
		final Set<UUID> playerViewers = VerifGuiManager.getViewers(player);
		if(playerViewers != null) {
			for(final UUID viewerUuid : playerViewers) {
				final Player viewer = Bukkit.getPlayer(viewerUuid);
				viewer.closeInventory();
				viewer.sendMessage(ConfigUtils.VERIF_PLAYERDISCONNECT.getString().replaceAll("%player%", player.getName()));
			}
		}
		VerifPlayerData.removePlayerInfo(player);
	}
}
