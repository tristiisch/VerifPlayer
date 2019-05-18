package fr.tristiisch.verifplayer.utils;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class SpigotUtils {

	public static Location addYToLocation(final Location location, final float y) {
		return new Location(location.getWorld(), location.getX(), location.getY() + 1, location.getZ(), location.getYaw(), location.getPitch());
	}

	public static List<Location> getBlockAround(final Location location, final int raduis) {
		final List<Location> locations = new ArrayList<>();
		for(int x = raduis; x >= -raduis; x--) {
			for(int y = raduis; y >= -raduis; y--) {
				for(int z = raduis; z >= -raduis; z--) {
					locations.add(location.getBlock().getRelative(x, y, z).getLocation());
				}
			}
		}
		return locations;
	}

	public static Location getFirstBlockUnderPlayer(final Player player) {
		Location location = player.getLocation();
		do {
			if(location.getBlockY() == 0) {
				return null;
			}
			location = new Location(location.getWorld(), location.getBlockX(), location.getBlockY() - 1, location.getBlockZ());
		} while(location.getBlock().getType() == Material.AIR);
		return location;
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

	public static short getIntervalGlassPaneColor(final int i, final int min, final int max) {
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

	public static boolean hasEnoughPlace(final Inventory inventory, final ItemStack... items) {
		final Inventory inventory2 = Bukkit.createInventory(null, inventory.getSize());
		inventory2.setContents(inventory.getContents());
		int amount1 = 0;
		for(final ItemStack item : inventory2.getContents()) {
			if(item != null) {
				amount1 += item.getAmount();
			}
		}

		int amount2 = 0;
		for(final ItemStack item : items) {
			amount2 += item.getAmount();
		}

		final int amount3 = amount1 + amount2;
		inventory2.addItem(items);

		int amount4 = 0;
		for(final ItemStack item : inventory2.getContents()) {
			if(item != null) {
				amount4 += item.getAmount();
			}
		}

		if(amount4 == amount3) {
			return true;
		}

		return false;
	}

	/*
	 * public static EmeraldPlayer getPlayer(final Player player) { final
	 * EmeraldPlayer emeraldPlayer = EmeraldPlayers.getPlayer(player.getUniqueId());
	 * if(emeraldPlayer != null) { return emeraldPlayer; } return
	 * MySQL.getPlayer(player.getUniqueId()); }
	 */

	public static boolean isOnGround(final Player player) {
		Location location = player.getLocation();
		location = new Location(location.getWorld(), location.getBlockX(), location.getBlockY() - 1, location.getBlockZ());
		return location.getBlock().getType() == Material.AIR;
	}

	public static boolean isSamePlayer(final Player player, final Player target) {
		return player.getUniqueId().equals(target.getUniqueId());
	}

}
