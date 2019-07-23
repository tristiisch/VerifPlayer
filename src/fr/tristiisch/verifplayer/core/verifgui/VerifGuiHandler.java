package fr.tristiisch.verifplayer.core.verifgui;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.entity.Player;

import fr.tristiisch.verifplayer.VerifPlayerPlugin;
import fr.tristiisch.verifplayer.gui.GuiHandler;
import fr.tristiisch.verifplayer.gui.objects.GuiCreator;
import fr.tristiisch.verifplayer.gui.objects.GuiPage;
import fr.tristiisch.verifplayer.gui.objects.GuiPageVariable;
import fr.tristiisch.verifplayer.utils.PlayerContents;

public class VerifGuiHandler extends GuiHandler {
	/*
		public static VerifGuiHandler getInstance() {
			return (VerifGuiHandler) GuiHandler.INSTANCE;
		}*/

	private final HashMap<UUID, Set<UUID>> playersBeingChecked = new HashMap<>();
	private final HashMap<UUID, PlayerContents> playersChecksInventoryContents = new HashMap<>();

	public Map.Entry<UUID, Set<UUID>> getByViewer(final UUID uuidViewer) {
		return this.playersBeingChecked.entrySet().stream().filter(pc -> pc.getValue().contains(uuidViewer)).findFirst().orElse(null);
	}

	public HashMap<UUID, Set<UUID>> getPlayersBeingChecked() {
		return this.playersBeingChecked;
	}

	public PlayerContents getPlayersChecksInventoryContents(final UUID uuid) {
		return this.playersChecksInventoryContents.get(uuid);
	}

	public Set<UUID> getViewers(final Player player) {
		return this.getViewers(player.getUniqueId());
	}

	public Set<UUID> getViewers(final UUID uuid) {
		return this.playersBeingChecked.get(uuid);
	}

	public boolean isViewer(final Player player) {
		return this.playersBeingChecked.entrySet().stream().filter(pc -> pc.getValue().contains(player.getUniqueId())).findFirst().isPresent();
	}

	public void openVerifGUi(final Player viewer, final Player target) {
		final GuiCreator guiCreator = new GuiCreator(VerifGuiPage.HOME);

		final GuiPage guiPage = guiCreator.getGuiPage();

		guiPage.setVariable(GuiPageVariable.PLAYER, VerifPlayerPlugin.getInstance().getPlayerInfoHandler().get(target));

		guiCreator.setItems(VerifGuiItem.getAllItems(target));
		this.set(viewer, guiCreator, target);

		//				final PlayerContents playerContents = new PlayerContents(viewer);
		//				playersChecksInventoryContents.put(viewer.getUniqueId(), playerContents);
		//				playerContents.clearInventory(viewer);

		guiCreator.openGui(viewer);
	}

	@Override
	public GuiCreator remove(final Player viewer) {
		final Map.Entry<UUID, Set<UUID>> entryPlayerBeingCheck = this.getByViewer(viewer.getUniqueId());
		if(entryPlayerBeingCheck == null) {
			return null;
		}
		final Set<UUID> viewers = entryPlayerBeingCheck.getValue();
		viewers.remove(viewer.getUniqueId());
		if(viewers.isEmpty()) {
			this.playersBeingChecked.remove(entryPlayerBeingCheck.getKey());
		}
		return super.remove(viewer);
	}

	public PlayerContents removePlayersChecksInventoryContents(final UUID uuid) {
		return this.playersChecksInventoryContents.remove(uuid);
	}

	public GuiCreator set(final Player viewer, final GuiCreator guiCreator, final Player playerBeigCheck) {
		if(guiCreator.getGuiPage().isSamePage(VerifGuiPage.HOME)) {
			final UUID uuidPlayerBeingChecked = playerBeigCheck.getUniqueId();
			if(this.playersBeingChecked.containsKey(uuidPlayerBeingChecked)) {
				this.playersBeingChecked.get(uuidPlayerBeingChecked).add(viewer.getUniqueId());
			} else {
				final Set<UUID> viewersUuid = new HashSet<>();
				viewersUuid.add(viewer.getUniqueId());
				this.playersBeingChecked.put(uuidPlayerBeingChecked, viewersUuid);
			}
		}
		return super.set(viewer, guiCreator);
	}

}
