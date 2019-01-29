package fr.tristiisch.verifplayer.utils;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemTools {

	public static ItemStack create(final Material mat) {
		return create(mat, 1, (short) 0, null, (List<String>) null);
	}

	public static ItemStack create(final Material mat, final int size) {
		return create(mat, size, (short) 0, null, (List<String>) null);
	}

	public static ItemStack create(final Material mat, final int size, final short dataValue) {
		return create(mat, size, dataValue, null, (List<String>) null);
	}

	public static ItemStack create(final Material mat, final int size, final short dataValue, final List<String> lore) {
		return create(mat, size, dataValue, null, lore);
	}

	public static ItemStack create(final Material mat, final int size, final short dataValue, final String name) {
		return create(mat, size, dataValue, name, (List<String>) null);
	}

	public static ItemStack create(final Material mat, final int size, final short dataValue, final String name, final List<String> lore) {
		final ItemStack itemStack = new ItemStack(mat, size, dataValue);
		final ItemMeta itemMeta = itemStack.getItemMeta();
		if(name != null) {
			itemMeta.setDisplayName(Utils.color(name));
		}

		if(lore != null) {
			itemMeta.setLore(Utils.color(lore));
		}

		itemStack.setItemMeta(itemMeta);

		return itemStack;
	}

	public static ItemStack create(final Material mat, final int size, final short dataValue, final String name, final String... lore) {
		return create(mat, size, dataValue, name, Arrays.asList(lore));
	}

	public static ItemStack create(final Material mat, final int size, final String name) {
		return create(mat, size, (short) 0, name, (List<String>) null);
	}

	public static ItemStack create(final Material mat, final int size, final String name, final List<String> lore) {
		return create(mat, size, (short) 0, name, lore);
	}

	public static ItemStack create(final Material mat, final int size, final String name, final String... lore) {
		return create(mat, size, (short) 0, name, Arrays.asList(lore));
	}

	public static ItemStack create(final Material mat, final short dataValue) {
		return create(mat, 1, dataValue, null, (List<String>) null);
	}
}
