package fr.tristiisch.verifplayer.gui;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import fr.tristiisch.verifplayer.gui.objects.GuiCreator;
import fr.tristiisch.verifplayer.gui.objects.GuiPage;

public abstract class GuiHandler {

	/*
	 * protected static GuiHandler INSTANCE;
	 * 
	 * public static GuiHandler getInstance() { return INSTANCE; }
	 */

	private Map<UUID, GuiCreator> gui = new HashMap<>();

	/*
	 * public GuiHandler() { GuiHandler.INSTANCE = this; }
	 */

	public void closeAll() {
		this.gui.entrySet().forEach(entry -> {
			UUID uuid = entry.getKey();
			Player player = Bukkit.getPlayer(uuid);
			if (player != null) {
				player.closeInventory();
			}
		});
	}

	public Map<UUID, GuiCreator> get() {
		return this.gui;
	}

	public Map.Entry<UUID, GuiCreator> get(GuiPage guiPage) {
		return this.gui.entrySet().stream().filter(entry -> entry.getValue().getGuiPage().isSamePage(guiPage)).findFirst().orElse(null);
	}

	public GuiCreator get(Player player) {
		return this.get(player.getUniqueId());
	}

	public GuiCreator get(UUID uuid) {
		return this.gui.get(uuid);
	}

	public GuiCreator remove(Player player) {
		return this.gui.remove(player.getUniqueId());
	}

	public GuiCreator set(Player player, GuiCreator guiCreator) {
		return this.gui.put(player.getUniqueId(), guiCreator);
	}
}
