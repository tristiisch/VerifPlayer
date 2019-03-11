package fr.tristiisch.verifplayer.verifgui;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.entity.Player;

import fr.tristiisch.verifplayer.utils.gui.GuiCreator;
import fr.tristiisch.verifplayer.utils.gui.GuiManager;

public class VerifGuiManager extends GuiManager {

	private static final HashMap<UUID, Set<UUID>> playersBeingChecked;

	static {
		playersBeingChecked = new HashMap<>();
	}

	public static Map.Entry<UUID, Set<UUID>> getByViewer(final UUID uuidViewer) {
		return VerifGuiManager.playersBeingChecked.entrySet().stream().filter(pc -> pc.getValue().contains(uuidViewer)).findFirst().orElse(null);
	}

	public static HashMap<UUID, Set<UUID>> getPlayersBeingChecked() {
		return VerifGuiManager.playersBeingChecked;
	}

	public static Set<UUID> getViewers(final Player player) {
		return getViewers(player.getUniqueId());
	}

	public static Set<UUID> getViewers(final UUID uuid) {
		return VerifGuiManager.playersBeingChecked.get(uuid);
	}

	public static boolean isViewer(final Player player) {
		return VerifGuiManager.playersBeingChecked.entrySet().stream().filter(pc -> pc.getValue().contains(player.getUniqueId())).findFirst().isPresent();
	}

	public static GuiCreator remove(final Player viewer) {
		final Map.Entry<UUID, Set<UUID>> entryPlayerBeingCheck = getByViewer(viewer.getUniqueId());
		if(entryPlayerBeingCheck == null) {
			return null;
		}
		final Set<UUID> viewers = entryPlayerBeingCheck.getValue();
		viewers.remove(viewer.getUniqueId());
		if(viewers.isEmpty()) {
			VerifGuiManager.playersBeingChecked.remove(entryPlayerBeingCheck.getKey());
		}
		return GuiManager.remove(viewer);
	}

	public static GuiCreator set(final Player viewer, final GuiCreator guiCreator, final Player playerBeigCheck) {
		if(guiCreator.getGuiPage().isSamePage(VerifGuiPage.HOME)) {
			final UUID uuidPlayerBeingChecked = playerBeigCheck.getUniqueId();
			if(VerifGuiManager.playersBeingChecked.containsKey(uuidPlayerBeingChecked)) {
				VerifGuiManager.playersBeingChecked.get(uuidPlayerBeingChecked).add(viewer.getUniqueId());
			} else {
				final Set<UUID> viewersUuid = new HashSet<>();
				viewersUuid.add(viewer.getUniqueId());
				VerifGuiManager.playersBeingChecked.put(uuidPlayerBeingChecked, viewersUuid);
			}
		}
		return GuiManager.set(viewer, guiCreator);
	}
}
