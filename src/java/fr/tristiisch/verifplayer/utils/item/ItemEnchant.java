package fr.tristiisch.verifplayer.utils.item;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemEnchant {

	private Enchantment enchantment;
	private boolean unsafe;
	private int level;

	public ItemEnchant(Enchantment enchantment) {
		this.enchantment = enchantment;
		this.level = 1;
		this.unsafe = false;
	}

	public ItemEnchant(Enchantment enchantment, int level) {
		this.enchantment = enchantment;
		this.level = level;
		this.unsafe = false;
	}

	public ItemEnchant(Enchantment enchantment, int level, boolean unsafe) {
		this.enchantment = enchantment;
		this.level = level;
		this.unsafe = unsafe;
	}

	public Enchantment getEnchantment() {
		return this.enchantment;
	}

	public int getLevel() {
		return this.level;
	}

	public boolean isUnsafe() {
		return this.unsafe;
	}

	public void setEnchantment(Enchantment enchantment) {
		this.enchantment = enchantment;
	}

	public ItemStack setEnchantToItem(ItemStack itemStack) {
		ItemMeta itemMeta = itemStack.getItemMeta();
		itemStack.setItemMeta(this.setEnchantToItemMeta(itemMeta));
		return itemStack;
	}

	public ItemMeta setEnchantToItemMeta(ItemMeta itemMeta) {
		itemMeta.addEnchant(this.getEnchantment(), this.getLevel(), this.isUnsafe());
		return itemMeta;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public void setUnsafe(boolean unsafe) {
		this.unsafe = unsafe;
	}
}
