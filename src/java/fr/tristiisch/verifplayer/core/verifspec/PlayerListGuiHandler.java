package fr.tristiisch.verifplayer.core.verifspec;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import fr.tristiisch.verifplayer.VerifPlayerPlugin;
import fr.tristiisch.verifplayer.core.verifgui.VerifGuiItem;
import fr.tristiisch.verifplayer.gui.GuiHandler;
import fr.tristiisch.verifplayer.gui.VerifPlayerGui;
import fr.tristiisch.verifplayer.gui.objects.GuiCreator;

public class PlayerListGuiHandler extends GuiHandler {

	public static List<Player> playerList = new ArrayList<>();
	public static List<Player> guiCPS = new ArrayList<>();

	public static void openPlayerList(Player viewer) {
		GuiCreator guiCreator = new GuiCreator(VerifPlayerGui.PLAYERS_HOME);
		VerifPlayerPlugin.getInstance().getPlayerInfoHandler().get().stream().limit(54).forEach(pi -> {
			guiCreator.addItem(VerifGuiItem.getSkull(pi.getPlayer()));
		});
		playerList.add(viewer);
		guiCreator.openGui(viewer);
	}

	public static void openPlayerListCPS(Player viewer) {
		GuiCreator guiCreator = new GuiCreator(VerifPlayerGui.PLAYERS_CPS);
		VerifPlayerPlugin.getInstance().getPlayerInfoHandler().get().stream().limit(54).forEach(pi -> {
			ItemStack itemStack = VerifGuiItem.getCps(pi.getPlayer());
			itemStack.getItemMeta().setDisplayName("§7" + pi.getRank() + pi.getName());
			guiCreator.addItem(itemStack);
		});
		guiCPS.add(viewer);
		guiCreator.openGui(viewer);
	}
}
