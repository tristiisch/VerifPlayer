package fr.tristiisch.verifplayer.core.verifgui;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.entity.Player;

import fr.tristiisch.verifplayer.VerifPlayerPlugin;
import fr.tristiisch.verifplayer.gui.GuiHandler;
import fr.tristiisch.verifplayer.gui.VerifPlayerGui;
import fr.tristiisch.verifplayer.gui.objects.GuiCreator;
import fr.tristiisch.verifplayer.gui.objects.GuiPage;
import fr.tristiisch.verifplayer.gui.objects.GuiPageVariable;
import fr.tristiisch.verifplayer.utils.PlayerContents;

public class VerifGuiHandler extends GuiHandler {
	/*
	 * public static PlayerListGuiHandler getInstance() { return (PlayerListGuiHandler)
	 * GuiHandler.INSTANCE; }
	 */

	private HashMap<UUID, Set<UUID>> playersBeingChecked = new HashMap<>();
	private HashMap<UUID, PlayerContents> playersChecksInventoryContents = new HashMap<>();

	public Map.Entry<UUID, Set<UUID>> getByViewer(UUID uuidViewer) {
		return playersBeingChecked.entrySet().stream().filter(pc -> pc.getValue().contains(uuidViewer)).findFirst().orElse(null);
	}

	public HashMap<UUID, Set<UUID>> getPlayersBeingChecked() {
		return playersBeingChecked;
	}

	public PlayerContents getPlayersChecksInventoryContents(UUID uuid) {
		return playersChecksInventoryContents.get(uuid);
	}

	public Set<UUID> getViewers(Player player) {
		return this.getViewers(player.getUniqueId());
	}

	public Set<UUID> getViewers(UUID uuid) {
		return playersBeingChecked.get(uuid);
	}

	public boolean isViewer(Player player) {
		return playersBeingChecked.entrySet().stream().filter(pc -> pc.getValue().contains(player.getUniqueId())).findFirst().isPresent();
	}

	public void openVerifGUi(Player viewer, Player target) {
		GuiCreator guiCreator = new GuiCreator(VerifPlayerGui.VERIFGUI_HOME);

		GuiPage guiPage = guiCreator.getGuiPage();

		guiPage.setVariable(GuiPageVariable.PLAYER, VerifPlayerPlugin.getInstance().getPlayerInfoHandler().get(target));

		guiCreator.setItems(VerifGuiItem.getAllItems(target));
		this.set(viewer, guiCreator, target);

		// PlayerContents playerContents = new PlayerContents(viewer);
		// playersChecksInventoryContents.put(viewer.getUniqueId(), playerContents);
		// playerContents.clearInventory(viewer);

		guiCreator.openGui(viewer);
	}

	@Override
	public GuiCreator remove(Player viewer) {
		Map.Entry<UUID, Set<UUID>> entryPlayerBeingCheck = getByViewer(viewer.getUniqueId());
		if (entryPlayerBeingCheck == null)
			return null;
		Set<UUID> viewers = entryPlayerBeingCheck.getValue();
		viewers.remove(viewer.getUniqueId());
		if (viewers.isEmpty())
			playersBeingChecked.remove(entryPlayerBeingCheck.getKey());
		return super.remove(viewer);
	}

	public PlayerContents removePlayersChecksInventoryContents(UUID uuid) {
		return playersChecksInventoryContents.remove(uuid);
	}

	public GuiCreator set(Player viewer, GuiCreator guiCreator, Player playerBeigCheck) {
		if (guiCreator.getGuiPage().isSamePage(VerifPlayerGui.VERIFGUI_HOME)) {
			UUID uuidPlayerBeingChecked = playerBeigCheck.getUniqueId();
			if (playersBeingChecked.containsKey(uuidPlayerBeingChecked))
				playersBeingChecked.get(uuidPlayerBeingChecked).add(viewer.getUniqueId());
			else {
				Set<UUID> viewersUuid = new HashSet<>();
				viewersUuid.add(viewer.getUniqueId());
				playersBeingChecked.put(uuidPlayerBeingChecked, viewersUuid);
			}
		}
		return super.set(viewer, guiCreator);
	}

}
