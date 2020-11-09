package fr.tristiisch.verifplayer.core.verifspec.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import fr.tristiisch.verifplayer.core.verifspec.PlayerListGuiHandler;
import fr.tristiisch.verifplayer.gui.VerifPlayerGui;
import fr.tristiisch.verifplayer.gui.customevents.GuiCloseEvent;

public class VerifSpecGuiListener implements Listener {

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		PlayerListGuiHandler.guiCPS.remove(player);
		PlayerListGuiHandler.playerList.remove(player);
	}

	@EventHandler
	public void onGuiClose(GuiCloseEvent event) {
		Player player = event.getPlayer();
		if (event.getGui().getGuiPage().isSamePage(VerifPlayerGui.PLAYERS_CPS))
			PlayerListGuiHandler.guiCPS.remove(player);
		else if (event.getGui().getGuiPage().isSamePage(VerifPlayerGui.PLAYERS_HOME))
			PlayerListGuiHandler.playerList.remove(player);
	}
}
