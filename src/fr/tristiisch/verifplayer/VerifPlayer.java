package fr.tristiisch.verifplayer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import fr.tristiisch.verifplayer.object.PlayerInfo;
import fr.tristiisch.verifplayer.utils.ConfigUtils;
import fr.tristiisch.verifplayer.utils.VersionUtils.VersionsUtils;
import fr.tristiisch.verifplayer.utils.gui.GuiName;
import fr.tristiisch.verifplayer.utils.gui.GuiTool;

public class VerifPlayer {

	public static Set<PlayerInfo> playersData = new HashSet<>();
	// TODO remove players from viewer
	public static Map<UUID, Set<UUID>> viewers = new HashMap<>();
	final public static int line = 9;
	public static int guiSize = 7 * line;
	public static int slotArmor = 4;
	public static int slotInv = 9;
	public static int slotHotBar = slotInv + 3 * line;
	public static int slotHolding = slotHotBar + line;
	public static int slotTools = 0;
	public static boolean is1_9 = VersionsUtils.V1_9.isEqualOrOlder();

	public static void addNewPlayer(final Player player) {
		playersData.add(new PlayerInfo(player));
	}

	public static PlayerInfo get(final Player player) {
		return get(player.getUniqueId());
	}

	public static PlayerInfo get(final UUID uuid) {
		return playersData.stream().filter(pc -> pc.getUniqueId().equals(uuid)).findFirst().orElse(null);
	}

	public static Entry<UUID, Set<UUID>> getFromViewer(final UUID uuidViewer) {
		return viewers.entrySet().stream().filter(pc -> pc.getValue().contains(uuidViewer)).findFirst().orElse(null);
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
			return 0;
		} else if(i < min) {
			return 5;
		} else if(i < max) {
			return 1;
		} else {
			return 14;
		}
	}

	public static Set<UUID> getViewers(final Player player) {
		return getViewers(player.getUniqueId());
	}

	public static Set<UUID> getViewers(final UUID uuid) {
		return viewers.get(uuid);
	}

	public static void openVerifGUi(final Player viewer, final Player target) {
		GuiTool guiTool = new GuiTool(GuiName.VERIF_PLAYER, ConfigUtils.VERIFGUI_NAME.getString() + target.getName(), VerifPlayer.guiSize);
		
		/*
		 * final Inventory inventory = Bukkit.createInventory(null, VerifPlayer.guiSize,
		 * ConfigUtils.VERIFGUI_NAME.getString() + target.getName());
		 */
		final UUID targetUuid = target.getUniqueId();
		if(viewers.containsKey(targetUuid)) {
			viewers.get(targetUuid).add(viewer.getUniqueId());
		} else {
			final Set<UUID> viewersUuid = new HashSet<>();
			viewersUuid.add(viewer.getUniqueId());
			viewers.put(targetUuid, viewersUuid);
		}

		/* viewer.openInventory(inventory); */
		guiTool.open(viewer);
	}

	public static void removePlayer(final Player player) {
		final PlayerInfo playerInfo = get(player);
		if(playerInfo != null) {
			playersData.remove(playerInfo);
		}

		final Set<UUID> playerViewers = getViewers(player);
		if(playerViewers != null) {
			for(final UUID viewerUuid : playerViewers) {
				final Player viewer = Bukkit.getPlayer(viewerUuid);
				viewer.closeInventory();
				viewer.sendMessage(ConfigUtils.VERIF_PLAYERDISCONNECT.getString().replaceAll("%player%", player.getName()));
			}
		}
		viewers.remove(player.getUniqueId());
	}

	public static void removeViewer(final UUID uuidViewer) {
		getFromViewer(uuidViewer).getValue().remove(uuidViewer);
	}

}
