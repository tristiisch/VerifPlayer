package fr.tristiisch.verifplayer.core.verifspec;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;

import fr.tristiisch.verifplayer.utils.PlayerContents;
import fr.tristiisch.verifplayer.utils.SpigotUtils;

public class VerifSpec {

	private static Set<UUID> players = new HashSet<>();

	public static void disable(final Player player) {
		if(player.getGameMode() != GameMode.CREATIVE) {
			player.setAllowFlight(false);
		}

		/*		if(!player.isOp()) {
					player.teleport(EmeraldSpigot.getSpawn());
				}*/

		final PlayerContents playerContents = PlayerContents.fromDisk(player);
		if(player.isOnline()) {
			playerContents.returnHisInventory();
		} else {
			/*
			 * final FileConfiguration config =
			 * CustomConfigUtils.getConfig(EmeraldSpigot.getInstance(), "data");
			 * config.set(player.getUniqueId().toString() + ".armure",
			 * InventoryToBase64.toBase64(datas));
			 * config.set(player.getUniqueId().toString() + ".inventaire",
			 * InventoryToBase64.toBase64(datas.getInventoryItemContents()));
			 */

		}

		//Vanish.disable(player, false);

		remove(player);
		player.sendMessage(SpigotUtils.color("&cMode staff désactivé"));
	}

	public static void enable(final Player player) {
		final PlayerContents playerContents = new PlayerContents(player);
		players.add(player.getUniqueId());

		playerContents.saveToDisk();
		playerContents.clearInventory();

		//Vanish.enable(player, false);
		setVerifSpecItems(player);

		/*		player.getInventory().setItem(0, VerifSpecItems.TELEPORTER.getItemStack());
				player.getInventory().setItem(1, VerifSpecItems.FREEZE.getItemStack());
				player.getInventory().setItem(2, VerifSpecItems.VERIF.getItemStack());
				player.getInventory().setItem(3, VerifSpecItems.SHUTTLE.getItemStack());
				player.getInventory().setItem(4, VerifSpecItems.KNOCKBACK.getItemStack());

				player.getInventory().setItem(7, VerifSpecItems.SLOW.getItemStack());
				player.getInventory().setItem(8, VerifSpecItems.FAST.getItemStack());*/
		//player.setCompassTarget(EmeraldSpigot.getSpawn());

		player.setAllowFlight(true);
		player.sendMessage(SpigotUtils.color("&aMode staff activé"));
	}

	public static Player getClosestPlayer(final Location loc) {
		return loc.getWorld().getPlayers().stream().min((o1, o2) -> {
			return Double.compare(o1.getLocation().distanceSquared(loc), o2.getLocation().distanceSquared(loc));
		}).orElse(null);
	}

	public static boolean isIn(final Player player) {
		return players.stream().filter(uuid -> player.getUniqueId().equals(uuid)).findFirst().isPresent();
	}

	public static boolean remove(final Player player) {
		return players.remove(player.getUniqueId());
	}

	protected static void setVerifSpecItems(final Player player) {
		final PlayerInventory playerInventory = player.getInventory();
		for(final VerifSpecTool verifSpecItem : VerifSpecTool.values()) {
			if(verifSpecItem.isEnable()) {
				playerInventory.setItem(verifSpecItem.getSlot(), verifSpecItem.getItemStack());
			}
		}
	}
}
