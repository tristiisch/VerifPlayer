package fr.tristiisch.verifplayer.utils.item;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.enchantments.EnchantmentWrapper;
import org.bukkit.inventory.ItemStack;

public class ItemGlow1_13 extends EnchantmentWrapper {

	// TODO 1.13 comptatiblity
	public ItemGlow1_13(String id) {
		super(id);
	}
	
	/*
	 * public ItemGlow(final String id) { super(id); }
	 */

	@Override
	public boolean canEnchantItem(final ItemStack item) {
		return true;
	}

	@Override
	public boolean conflictsWith(final Enchantment other) {
		return false;
	}

	@Override
	public EnchantmentTarget getItemTarget() {
		return null;
	}

	@Override
	public int getMaxLevel() {
		return 10;
	}

	@Override
	public String getName() {
		return "Glow";
	}

	@Override
	public int getStartLevel() {
		return 1;
	}
}