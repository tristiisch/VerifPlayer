package fr.tristiisch.verifplayer.utils;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import fr.tristiisch.verifplayer.VerifPlayerPlugin;
import fr.tristiisch.verifplayer.utils.VersionUtils.ServerVersion;
import fr.tristiisch.verifplayer.utils.config.CustomConfigs.CustomConfig;

public class PlayerContents {

	public static void clearInventory(PlayerInventory inventory) {
		inventory.clear();
		inventory.setArmorContents(new ItemStack[inventory.getArmorContents().length]);
	}

	public static PlayerContents fromDisk(Player player) {
		String uuid = player.getUniqueId().toString();
		CustomConfig customConfig = CustomConfig.INVENTORY;
		YamlConfiguration config = customConfig.getConfig();

		Object itemContents = config.get(uuid.toString() + ".contents");
		ItemStack[] inventoryItemContents = null;
		if (itemContents != null) {
			if (itemContents instanceof ItemStack[]) {
				inventoryItemContents = (ItemStack[]) itemContents;
			} else {
				VerifPlayerPlugin.getInstance().sendMessage("An error as occured with " + player.getName() + "'s inventory: his stuff is lost ...");
				return new PlayerContents(player.getUniqueId(), null, null);
			}
		}

		Object armorContents = config.get(uuid.toString() + ".armor");
		ItemStack[] inventoryArmorContents = null;
		if (armorContents != null) {
			if (armorContents instanceof ItemStack[]) {
				inventoryArmorContents = (ItemStack[]) armorContents;
			}
		}

		if (inventoryItemContents != null || inventoryArmorContents != null) {
			config.set(uuid.toString() + ".contents", null);
			config.set(uuid.toString() + ".armor", null);
			customConfig.save();
		}

		return new PlayerContents(player.getUniqueId(), inventoryItemContents, inventoryArmorContents);
	}

	private UUID uuid;
	private ItemStack[] inventoryItemContents;
	private ItemStack[] inventoryArmorContents = null;

	public PlayerContents(Player player) {
		this.uuid = player.getUniqueId();
		this.inventoryItemContents = player.getInventory().getContents();

		if (ServerVersion.V1_9.isYounger()) {
			this.inventoryArmorContents = player.getInventory().getArmorContents();
		}
	}

	private PlayerContents(UUID uuid, ItemStack[] inventoryItemContents, ItemStack[] inventoryArmorContents) {
		this.uuid = uuid;
		this.inventoryItemContents = inventoryItemContents;
		if (ServerVersion.V1_9.isYounger()) {
			this.inventoryArmorContents = inventoryArmorContents;
		}
	}

	public void clearInventory() {
		Player player = this.getPlayer();
		PlayerContents.clearInventory(player.getInventory());
	}

	public ItemStack[] getInventoryArmorContents() {
		return this.inventoryArmorContents;
	}

	public ItemStack[] getInventoryItemContents() {
		return this.inventoryItemContents;
	}

	private Player getPlayer() {
		return Bukkit.getPlayer(this.uuid);
	}

	public UUID getUniqueId() {
		return this.uuid;
	}

	public boolean hasData() {
		return this.inventoryItemContents != null && this.inventoryArmorContents != null;
	}

	public void returnHisInventory() {
		PlayerInventory inventory = this.getPlayer().getInventory();
		clearInventory(inventory);
		if (this.inventoryItemContents != null) {
			inventory.setContents(this.inventoryItemContents);
		}
		if (this.inventoryArmorContents != null) {
			inventory.setArmorContents(this.inventoryArmorContents);
		}
	}

	public void saveToDisk() {
		CustomConfig customConfig = CustomConfig.INVENTORY;
		YamlConfiguration config = customConfig.getConfig();
		config.set(this.uuid.toString() + ".contents", this.inventoryItemContents);
		if (this.inventoryArmorContents != null) {
			config.set(this.uuid.toString() + ".armor", this.inventoryArmorContents);
		}
		customConfig.save();
	}
}