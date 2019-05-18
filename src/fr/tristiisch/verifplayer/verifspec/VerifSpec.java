package fr.tristiisch.verifplayer.verifspec;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import fr.tristiisch.verifplayer.utils.PlayerContents;
import fr.tristiisch.verifplayer.utils.Utils;
import fr.tristiisch.verifplayer.utils.config.CustomConfig.CustomConfigs;

public class VerifSpec {

	private static Set<PlayerContents> playersContents = new HashSet<>();

	public static void disable(final Player player) {
		if(player.getGameMode() != GameMode.CREATIVE) {
			player.setAllowFlight(false);
		}

		/*		if(!player.isOp()) {
					player.teleport(EmeraldSpigot.getSpawn());
				}*/

		final PlayerContents playerContents = getPlayerContents(player);
		playerContents.clearInventory();
		if(player.isOnline()) {

			final YamlConfiguration config = CustomConfigs.INVENTORY.getConfig();

			/*			final String serialiszablePlayerContents = customConfig.getConfig().getString(player.getUniqueId().toString());
			
						if(serialiszablePlayerContents == null) {
							player.sendMessage("aucune donné");
							return;
						}
			
			
						playerContents = PlayerContents.fromString(serialiszablePlayerContents);
						if(playerContents == null) {
							player.sendMessage("aucun object");
							return;
						}
						playerContents.returnHisInventory();*/
			final ItemStack[] content = (ItemStack[]) config.get(player.getUniqueId().toString());
			player.getInventory().setContents(content);

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

		playersContents.remove(playerContents);
		player.sendMessage(Utils.color("&cMode staff désactivé"));
	}

	public static void enable(final Player player) {
		final PlayerContents playerContents = new PlayerContents(player);
		playersContents.add(playerContents);

		final CustomConfigs customConfig = CustomConfigs.INVENTORY;
		customConfig.getConfig().set(player.getUniqueId().toString(), playerContents.getInventoryItemContents());
		customConfig.save();

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
		player.sendMessage(Utils.color("&aMode staff activé"));
	}

	public static Player getClosestPlayer(final Location loc) {
		return loc.getWorld().getPlayers().stream().min((o1, o2) -> {
			return Double.compare(o1.getLocation().distanceSquared(loc), o2.getLocation().distanceSquared(loc));
		}).orElse(null);
	}

	public static PlayerContents getPlayerContents(final Player player) {
		return playersContents.stream().filter(playerContents -> player.getUniqueId().equals(playerContents.getUniqueId())).findFirst().orElse(null);
	}

	public static boolean isIn(final Player player) {
		return playersContents.stream().filter(playerContents -> player.getUniqueId().equals(playerContents.getUniqueId())).findFirst().isPresent();
	}

	protected static void setVerifSpecItems(final Player player) {
		final PlayerInventory playerInventory = player.getInventory();
		for(final VerifSpecItem verifSpecItem : VerifSpecItem.values()) {
			if(verifSpecItem.isEnable()) {
				playerInventory.setItem(verifSpecItem.getSlot(), verifSpecItem.getItemStack());
			}
		}
	}

	/*	public static boolean remove(final Player player) {
			final PlayerContents playerContents = getPlayerContents(player);
			if(playerContents != null) {
				return playersContents.remove(playerContents);
			}
			return false;
		}*/
}
