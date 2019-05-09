package fr.tristiisch.verifplayer.verifgui.listener;

import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import fr.tristiisch.verifplayer.gui.customevents.GuiCloseEvent;
import fr.tristiisch.verifplayer.utils.config.ConfigUtils;
import fr.tristiisch.verifplayer.verifgui.VerifGuiManager;
import fr.tristiisch.verifplayer.verifgui.VerifGuiPage;

public class VerifGuiListener implements Listener {

	@EventHandler
	public void onGuiClose(final GuiCloseEvent event) {
		final Player player = event.getPlayer();
		if(event.getGui().getGuiPage().isSamePage(VerifGuiPage.HOME)) {
			VerifGuiManager.remove(player);
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
				viewer.sendMessage(ConfigUtils.MESSAGES_VERIF_PLAYERDISCONNECT.getString().replaceAll("%player%", player.getName()));
			}
		}
		VerifGuiManager.remove(player);
	}
}
