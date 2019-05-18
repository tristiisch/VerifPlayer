package fr.tristiisch.verifplayer.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Base64;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class PlayerContents {

	public static void clearInventory(final PlayerInventory inventory) {
		inventory.clear();
		inventory.setArmorContents(new ItemStack[inventory.getArmorContents().length]);
	}

	public static PlayerContents fromString(final String s) {
		try {
			final byte[] data = Base64.getDecoder().decode(s);
			final ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
			final PlayerContents o = (PlayerContents) ois.readObject();
			ois.close();
			return o;
		} catch(final IOException | ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

	private final UUID uuid;

	private final ItemStack[] inventoryItemContents;

	private final ItemStack[] inventoryArmorContents;

	public PlayerContents(final Player player) {
		this.inventoryItemContents = player.getInventory().getContents();
		this.inventoryArmorContents = player.getInventory().getArmorContents();
		this.uuid = player.getUniqueId();
	}

	public void clearInventory() {
		final Player player = this.getPlayer();
		//		if(player != null) {
		PlayerContents.clearInventory(player.getInventory());
		//		}
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

	public void returnHisInventory() {
		final PlayerInventory inventory = this.getPlayer().getInventory();
		PlayerContents.clearInventory(inventory);
		inventory.setContents(this.inventoryItemContents);
		inventory.setArmorContents(this.inventoryArmorContents);
	}

	@Override
	public String toString() {
		try {
			final ByteArrayOutputStream baos = new ByteArrayOutputStream();
			final ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(this);
			oos.close();
			return Base64.getEncoder().encodeToString(baos.toByteArray());
		} catch(final IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}
