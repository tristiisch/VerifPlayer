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

	private final Map<UUID, GuiCreator> gui = new HashMap<>();

	/*
	 * public GuiHandler() { GuiHandler.INSTANCE = this; }
	 */

	public void closeAll() {
		this.gui.entrySet().forEach(entry -> {
			final UUID uuid = entry.getKey();
			final Player player = Bukkit.getPlayer(uuid);
			if (player != null) {
				player.closeInventory();
			}
		});
	}

	public Map<UUID, GuiCreator> get() {
		return this.gui;
	}

	public Map.Entry<UUID, GuiCreator> get(final GuiPage guiPage) {
		return this.gui.entrySet().stream().filter(entry -> entry.getValue().getGuiPage().isSamePage(guiPage)).findFirst().orElse(null);
	}

	public GuiCreator get(final Player player) {
		return this.get(player.getUniqueId());
	}

	public GuiCreator get(final UUID uuid) {
		return this.gui.get(uuid);
	}

	public GuiCreator remove(final Player player) {
		return this.gui.remove(player.getUniqueId());
	}

	public GuiCreator set(final Player player, final GuiCreator guiCreator) {
		return this.gui.put(player.getUniqueId(), guiCreator);
	}
}
