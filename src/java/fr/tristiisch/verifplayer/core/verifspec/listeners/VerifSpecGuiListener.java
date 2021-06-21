package fr.tristiisch.verifplayer.core.verifspec.listeners;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import fr.tristiisch.verifplayer.VerifPlayerPlugin;
import fr.tristiisch.verifplayer.core.verifspec.teleport.Teleporter;
import fr.tristiisch.verifplayer.gui.VerifPlayerGui;
import fr.tristiisch.verifplayer.gui.customevents.GuiClickEvent;
import fr.tristiisch.verifplayer.gui.customevents.GuiCloseEvent;
import fr.tristiisch.verifplayer.utils.VersionUtils.ServerVersion;
import fr.tristiisch.verifplayer.utils.config.ConfigGet;

public class VerifSpecGuiListener implements Listener {

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		VerifPlayerPlugin.getInstance().getPlayerListGuiHandler().guiCPS.remove(player);
		VerifPlayerPlugin.getInstance().getPlayerListGuiHandler().playerList.remove(player);
	}

	@EventHandler
	public void onGuiClose(GuiCloseEvent event) {
		Player player = event.getPlayer();
		if (event.getGui().getGuiPage().isSamePage(VerifPlayerGui.PLAYERS_CPS))
			VerifPlayerPlugin.getInstance().getPlayerListGuiHandler().guiCPS.remove(player);
		else if (event.getGui().getGuiPage().isSamePage(VerifPlayerGui.PLAYERS_HOME))
			VerifPlayerPlugin.getInstance().getPlayerListGuiHandler().playerList.remove(player);
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onGuiClick(GuiClickEvent event) {
		Player player = event.getPlayer();
		if (!event.getGui().getGuiPage().isSamePage(VerifPlayerGui.PLAYERS_CPS) && !event.getGui().getGuiPage().isSamePage(VerifPlayerGui.PLAYERS_HOME))
			return;
		ItemStack item = event.getInventoryClickEvent().getCurrentItem();
		SkullMeta skullmeta = (SkullMeta) item.getItemMeta();
		OfflinePlayer target;
		if (ServerVersion.V1_12.isEqualOrOlder())
			target = skullmeta.getOwningPlayer();
		else
			target = Bukkit.getOfflinePlayer(skullmeta.getOwner());
		if (!target.isOnline()) {
			player.sendMessage(ConfigGet.MESSAGES_VERIFSPEC_PLAYERISOFFLINE.getString().replace("%player", target.getName()));
			return;
		}
		Teleporter.tp(player, (Player) target);
	}
}
