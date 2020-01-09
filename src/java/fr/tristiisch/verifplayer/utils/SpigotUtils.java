package fr.tristiisch.verifplayer.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class SpigotUtils {

	public static Location addYToLocation(Location location, float y) {
		return new Location(location.getWorld(), location.getX(), location.getY() + 1, location.getZ(), location.getYaw(), location.getPitch());
	}

	public static List<String> color(List<String> l) {
		return l.stream().map(s -> SpigotUtils.color(s)).collect(Collectors.toList());
	}

	public static String color(String s) {
		return s != null ? ChatColor.translateAlternateColorCodes('&', s) : "";
	}

	public static List<Location> getBlockAround(Location location, int raduis) {
		List<Location> locations = new ArrayList<>();
		for (int x = raduis; x >= -raduis; x--) {
			for (int y = raduis; y >= -raduis; y--) {
				for (int z = raduis; z >= -raduis; z--) {
					locations.add(location.getBlock().getRelative(x, y, z).getLocation());
				}
			}
		}
		return locations;
	}

	public static Location getFirstBlockUnderPlayer(Player player) {
		Location location = player.getLocation();
		do {
			if (location.getBlockY() == 0) {
				return null;
			}
			location = new Location(location.getWorld(), location.getBlockX(), location.getBlockY() - 1, location.getBlockZ());
		} while (!location.getBlock().getType().isOccluding());
		return location;
	}

	public static ChatColor getIntervalChatColor(int i, int min, int max) {
		if (i == 0) {
			return ChatColor.GRAY;
		}
		if (i < min) {
			return ChatColor.GREEN;
		}
		if (i < max) {
			return ChatColor.GOLD;
		}
		return ChatColor.RED;
	}

	public static short getIntervalGlassPaneColor(int i, int min, int max) {
		if (i == 0) {
			return 0;
		}
		if (i < min) {
			return 5;
		}
		if (i < max) {
			return 1;
		}
		return 14;
	}

	public static Player getNearestPlayer(Player checkNear) {
		Player nearest = null;
		for (Player p : checkNear.getWorld().getPlayers()) {
			if (nearest == null) {
				nearest = p;
			} else if (p.getLocation().distance(checkNear.getLocation()) < nearest.getLocation().distance(checkNear.getLocation())) {
				nearest = p;
			}
		}
		return nearest;
	}

	public static boolean hasEnoughPlace(Inventory inventory, ItemStack... items) {
		Inventory inventory2 = Bukkit.createInventory(null, inventory.getSize());
		inventory2.setContents(inventory.getContents());
		int amount1 = 0;
		for (ItemStack item : inventory2.getContents()) {
			if (item != null) {
				amount1 += item.getAmount();
			}
		}

		int amount2 = 0;
		for (ItemStack item : items) {
			amount2 += item.getAmount();
		}

		int amount3 = amount1 + amount2;
		inventory2.addItem(items);

		int amount4 = 0;
		for (ItemStack item : inventory2.getContents()) {
			if (item != null) {
				amount4 += item.getAmount();
			}
		}

		if (amount4 == amount3) {
			return true;
		}

		return false;
	}

	public static boolean isOnGround(Player player) {
		Location location = player.getLocation();
		location = new Location(location.getWorld(), location.getBlockX(), location.getBlockY() - 1, location.getBlockZ());
		return location.getBlock().getType() == Material.AIR;
	}

	/*
	 * public static EmeraldPlayer getPlayer(Player player) { final
	 * EmeraldPlayer emeraldPlayer = EmeraldPlayers.getPlayer(player.getUniqueId());
	 * if(emeraldPlayer != null) { return emeraldPlayer; } return
	 * MySQL.getPlayer(player.getUniqueId()); }
	 */

	public static boolean isSameLocation(Location location1, Location location2) {
		return location1.getBlockX() == location2.getBlockX() && location1.getBlockY() == location2.getBlockY() && location1.getBlockZ() == location2.getBlockZ();
	}

	public static boolean isSamePlayer(Player player, Player target) {
		return player.getUniqueId().equals(target.getUniqueId());
	}

	public static boolean playerisIn(Player player, Location location) {
		Location playerLocation = player.getLocation();
		return playerLocation.getBlockX() == location.getBlockX() && (playerLocation.getBlockY() == location.getBlockY() || playerLocation.getBlockY() + 1 == location.getBlockY()) && playerLocation.getBlockZ() == location.getBlockZ();
	}

}
