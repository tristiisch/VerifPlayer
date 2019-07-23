package fr.tristiisch.verifplayer.core.verifgui.listener;

import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import fr.tristiisch.verifplayer.VerifPlayerPlugin;
import fr.tristiisch.verifplayer.core.verifgui.VerifGuiHandler;
import fr.tristiisch.verifplayer.core.verifgui.VerifGuiPage;
import fr.tristiisch.verifplayer.gui.customevents.GuiCloseEvent;
import fr.tristiisch.verifplayer.utils.PlayerContents;
import fr.tristiisch.verifplayer.utils.config.ConfigGet;

public class VerifGuiListener implements Listener {

	@EventHandler
	public void onGuiClose(final GuiCloseEvent event) {
		final Player player = event.getPlayer();
		if(event.getGui().getGuiPage().isSamePage(VerifGuiPage.HOME)) {
			final VerifGuiHandler verifGuiHandler = VerifPlayerPlugin.getInstance().getVerifGuiHandler();
			verifGuiHandler.remove(player);
			final PlayerContents playerContents = verifGuiHandler.removePlayersChecksInventoryContents(player.getUniqueId());
			if(playerContents != null) {
				playerContents.returnHisInventory();
			}
		}
	}

	@EventHandler
	public void onPlayerQuit(final PlayerQuitEvent event) {
		final Player player = event.getPlayer();
		final VerifGuiHandler verifGuiHandler = VerifPlayerPlugin.getInstance().getVerifGuiHandler();
		final Set<UUID> playerViewers = verifGuiHandler.getViewers(player);
		if(playerViewers != null) {
			for(final UUID viewerUuid : playerViewers) {
				final Player viewer = Bukkit.getPlayer(viewerUuid);
				viewer.closeInventory();
				viewer.sendMessage(ConfigGet.MESSAGES_VERIF_PLAYERDISCONNECT.getString().replaceAll("%player%", player.getName()));
			}
		}
		verifGuiHandler.remove(player);
	}
}
