package fr.tristiisch.verifplayer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import fr.tristiisch.verifplayer.object.PlayerInfo;
import fr.tristiisch.verifplayer.utils.VersionUtils;
import fr.tristiisch.verifplayer.utils.gui.api.GuiCreator;
import fr.tristiisch.verifplayer.utils.gui.api.GuiPage.GuiPageVariable;
import fr.tristiisch.verifplayer.verifgui.VerifGuiManager;
import fr.tristiisch.verifplayer.verifgui.VerifGuiPage;
import fr.tristiisch.verifplayer.verifgui.runnable.VerifGuiRunnable;

public class VerifPlayerData {

	public static final Map<UUID, PlayerInfo> playersData = new HashMap<>();
	public static final int boxPerRow = 9;
	public static final int guiSize = 63;
	public static final boolean is1_9 = VersionUtils.VersionsUtils.V1_9.isEqualOrOlder();

	public static void addNewPlayerInfo(final Player player) {
		VerifPlayerData.playersData.put(player.getUniqueId(), new PlayerInfo());
	}

	public static ChatColor getIntervalChatColor(final int i, final int min, final int max) {
		if(i == 0) {
			return ChatColor.GRAY;
		}
		if(i < min) {
			return ChatColor.GREEN;
		}
		if(i < max) {
			return ChatColor.GOLD;
		}
		return ChatColor.RED;
	}

	public static int getIntervalGlassPaneColor(final int i, final int min, final int max) {
		if(i == 0) {
			return 0;
		}
		if(i < min) {
			return 5;
		}
		if(i < max) {
			return 1;
		}
		return 14;
	}

	public static PlayerInfo getPlayerInfo(final Player player) {
		return VerifPlayerData.playersData.get(player.getUniqueId());
	}

	public static PlayerInfo getPlayerInfo(final UUID uuid) {
		return VerifPlayerData.playersData.get(uuid);
	}

	public static void openVerifGUi(final Player viewer, final Player target) {
		VerifGuiRunnable.start();
		final GuiCreator guiCreator = new GuiCreator(VerifGuiPage.HOME);
		guiCreator.getGuiPage().replaceTitleVariable(GuiPageVariable.PLAYER, target.getName());
		guiCreator.setData(target.getUniqueId().toString());
		VerifGuiManager.set(viewer, guiCreator, target);
		guiCreator.openGui(viewer);
	}

	public static void removePlayerInfo(final Player player) {
		VerifPlayerData.playersData.remove(player.getUniqueId());
	}

	public static void set(final Player player, final PlayerInfo playerInfo) {
		VerifPlayerData.playersData.put(player.getUniqueId(), playerInfo);
	}
}
