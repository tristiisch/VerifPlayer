package fr.tristiisch.verifplayer;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import fr.tristiisch.verifplayer.object.PlayerInfo;
import fr.tristiisch.verifplayer.utils.ConfigUtils;

public class VerifCPS {

	public static Map<PlayerInfo, Set<UUID>> playersData = new HashMap<>();
	public static int guiSize = 6 * 9;
	public static int slotArmor = 5;
	public static int slotHotBar = 36;
	public static int slotInv = 9;

	public static Entry<PlayerInfo, Set<UUID>> get(final UUID uuid) {
		return playersData.entrySet().stream().filter(entry -> entry.getKey().getUniqueId().equals(uuid)).findFirst().orElse(null);
	}

	public static PlayerInfo getByPlayer(final Player player) {
		return getByUUID(player.getUniqueId());
	}

	public static PlayerInfo getByUUID(final UUID uuid) {
		return playersData.keySet().stream().filter(pc -> pc.getUniqueId().equals(uuid)).findFirst().orElse(null);
	}

	public static Entry<PlayerInfo, Set<UUID>> getFromViewer(final UUID uuidViewer) {
		return playersData.entrySet().stream().filter(entry -> entry.getValue().contains(uuidViewer)).findFirst().orElse(null);
	}

	public static ChatColor getIntervalChatColor(final int i, final int min, final int max) {
		if(i == 0) {
			return ChatColor.GRAY;
		} else if(i < min) {
			return ChatColor.GREEN;
		} else if(i < max) {
			return ChatColor.GOLD;
		} else {
			return ChatColor.RED;
		}
	}

	public static int getIntervalGlassPaneColor(final int i, final int min, final int max) {
		if(i == 0) {
			return 8;
		} else if(i < min) {
			return 5;
		} else if(i < max) {
			return 1;
		} else {
			return 14;
		}
	}

	public static void openVerifGUi(final Player player, final Player target) {
		final Inventory i = Bukkit.createInventory(null, VerifCPS.guiSize, ConfigUtils.VERIFGUI_NAME.getString() + target.getName());
		final Entry<PlayerInfo, Set<UUID>> targetInfo = get(target.getUniqueId());
		targetInfo.getValue().add(player.getUniqueId());
		player.openInventory(i);
	}

	public static void removePlayer(final Player player) {
		final Entry<PlayerInfo, Set<UUID>> entry = get(player.getUniqueId());
		if(entry == null) {
			return;
		}
		for(final UUID viewerUuid : entry.getValue()) {
			final Player viewer = Bukkit.getPlayer(viewerUuid);
			viewer.closeInventory();
			viewer.sendMessage(ConfigUtils.VERIF_PLAYERDISCONNECT.getString().replaceAll("%player%", player.getName()));
		}
		playersData.remove(entry.getKey());
	}

	public static void removeViewer(final UUID uuidViewer) {
		getFromViewer(uuidViewer).getValue().remove(uuidViewer);
	}

}
