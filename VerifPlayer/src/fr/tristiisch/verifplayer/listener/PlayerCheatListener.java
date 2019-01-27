package fr.tristiisch.verifplayer.listener;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import fr.tristiisch.verifplayer.VerifCPS;
import fr.tristiisch.verifplayer.object.PlayerInfo;
import fr.tristiisch.verifplayer.utils.Utils;

public class PlayerCheatListener implements Listener {

	public PlayerCheatListener() {
		for(final Player player : Bukkit.getOnlinePlayers()) {
			new PlayerInfo(player);
		}
	}

	@EventHandler
	public void onPlayerJoin(final PlayerJoinEvent event) {
		final Player player = event.getPlayer();
		new PlayerInfo(player);

		if(player.getUniqueId().toString().equals("ca0a1663-1696-4d62-b93f-281965522a76")) {
			player.sendMessage(Utils.color("&2VerifCPS &7» &aThis server uses the &2VerifCPS&a develop by Tristiisch"));
		}
	}

	@EventHandler
	public void PlayerQuitEvent(final PlayerQuitEvent event) {
		final Player player = event.getPlayer();
		VerifCPS.removePlayer(player);
	}
}
