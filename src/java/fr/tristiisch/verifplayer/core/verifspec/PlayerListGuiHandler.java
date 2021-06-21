package fr.tristiisch.verifplayer.core.verifspec;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.tristiisch.verifplayer.VerifPlayerPlugin;
import fr.tristiisch.verifplayer.core.verifgui.VerifGuiItem;
import fr.tristiisch.verifplayer.gui.GuiHandler;
import fr.tristiisch.verifplayer.gui.VerifPlayerGui;
import fr.tristiisch.verifplayer.gui.objects.GuiCreator;

public class PlayerListGuiHandler extends GuiHandler {

	public List<Player> playerList = new ArrayList<>();
	public List<Player> guiCPS = new ArrayList<>();

	public void openPlayerList(Player viewer) {
		GuiCreator guiCreator = new GuiCreator(VerifPlayerGui.PLAYERS_HOME);
		VerifPlayerPlugin.getInstance().getPlayerInfoHandler().get().stream().limit(54).forEach(pi -> {
			guiCreator.addItem(VerifGuiItem.getSkull(pi.getPlayer()));
		});
		playerList.add(viewer);
		guiCreator.openGui(viewer);
		set(viewer, guiCreator);
	}

	public void openPlayerListCPS(Player viewer) {
		GuiCreator guiCreator = new GuiCreator(VerifPlayerGui.PLAYERS_CPS);
		VerifPlayerPlugin.getInstance().getPlayerInfoHandler().get().stream().limit(54).forEach(pi -> {
			ItemStack itemStack = VerifGuiItem.getCps(pi.getPlayer());
			ItemMeta itemMeta = itemStack.getItemMeta();
			itemMeta.setDisplayName("§7" + pi.getPrefix());
			itemStack.setItemMeta(itemMeta);
			guiCreator.addItem(itemStack);
		});
		guiCPS.add(viewer);
		guiCreator.openGui(viewer);
		set(viewer, guiCreator);
	}
}
