package fr.tristiisch.verifplayer.utils.gui;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import fr.tristiisch.verifplayer.utils.gui.api.GuiCreator;
import fr.tristiisch.verifplayer.utils.gui.api.GuiPage;

public abstract class GuiManager {

	private static final Map<UUID, GuiCreator> gui = new HashMap<>();

	public static void closeAll() {
		GuiManager.gui.entrySet().forEach(entry -> {
			final UUID uuid = entry.getKey();
			final Player player = Bukkit.getPlayer(uuid);
			if(player != null) {
				player.closeInventory();
			}
		});
	}

	public static Map<UUID, GuiCreator> get() {
		return GuiManager.gui;
	}

	public static Map.Entry<UUID, GuiCreator> get(final GuiPage guiPage) {
		return GuiManager.gui.entrySet().stream().filter(entry -> entry.getValue().getGuiPage().isSamePage(guiPage)).findFirst().orElse(null);
	}

	public static GuiCreator get(final Player player) {
		return get(player.getUniqueId());
	}

	public static GuiCreator get(final UUID uuid) {
		return GuiManager.gui.get(uuid);
	}

	public static GuiCreator remove(final Player player) {
		return GuiManager.gui.remove(player.getUniqueId());
	}

	public static GuiCreator set(final Player player, final GuiCreator guiCreator) {
		return GuiManager.gui.put(player.getUniqueId(), guiCreator);
	}
}
