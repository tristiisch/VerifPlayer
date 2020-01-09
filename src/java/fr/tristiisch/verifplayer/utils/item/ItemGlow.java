package fr.tristiisch.verifplayer.utils.item;

import java.lang.reflect.Field;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.enchantments.EnchantmentWrapper;
import org.bukkit.inventory.ItemStack;

public class ItemGlow extends EnchantmentWrapper {

	private static Enchantment glow;

	public static Enchantment getGlowEnchant() {
		if (glow != null) {
			return glow;
		}

		try {
			Field f = Enchantment.class.getDeclaredField("acceptingNew");
			f.setAccessible(true);
			f.set(null, true);
		} catch (Exception e) {
			e.printStackTrace();
		}

		glow = new ItemGlow(255);
		try {
			Enchantment.registerEnchantment(glow);
		} catch (IllegalArgumentException iae) {
		}
		return glow;
	}

	public static ItemEnchant getItemGlowEnchant() {
		return new ItemEnchant(getGlowEnchant());
	}

	public static boolean isGlow(ItemStack item) {
		return item.getEnchantments().containsKey(glow);
	}

	// TODO 1.13 comptatiblity
	public ItemGlow(int id) {
		super(id);
	}

	/*
	 * public ItemGlow(String id) { super(id); }
	 */

	@Override
	public boolean canEnchantItem(ItemStack item) {
		return true;
	}

	@Override
	public boolean conflictsWith(Enchantment other) {
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