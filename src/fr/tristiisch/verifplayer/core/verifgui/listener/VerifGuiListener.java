package fr.tristiisch.verifplayer.core.verifgui.listener;

import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import fr.tristiisch.verifplayer.core.verifgui.VerifGuiManager;
import fr.tristiisch.verifplayer.core.verifgui.VerifGuiPage;
import fr.tristiisch.verifplayer.gui.customevents.GuiCloseEvent;
import fr.tristiisch.verifplayer.utils.PlayerContents;
import fr.tristiisch.verifplayer.utils.config.ConfigGet;

public class VerifGuiListener implements Listener {

	@EventHandler
	public void onGuiClose(final GuiCloseEvent event) {
		final Player player = event.getPlayer();
		if(event.getGui().getGuiPage().isSamePage(VerifGuiPage.HOME)) {
			VerifGuiManager.remove(player);
			final PlayerContents playerContents = VerifGuiManager.removePlayersChecksInventoryContents(player.getUniqueId());
			if(playerContents != null) {
				playerContents.returnHisInventory();
			}
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
				viewer.sendMessage(ConfigGet.MESSAGES_VERIF_PLAYERDISCONNECT.getString().replaceAll("%player%", player.getName()));
			}
		}
		VerifGuiManager.remove(player);
	}
}
