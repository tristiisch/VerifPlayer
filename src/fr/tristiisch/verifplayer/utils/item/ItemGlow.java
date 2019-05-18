package fr.tristiisch.verifplayer.utils.item;

import java.lang.reflect.Field;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.enchantments.EnchantmentWrapper;
import org.bukkit.inventory.ItemStack;

public class ItemGlow extends EnchantmentWrapper {

	private static Enchantment glow;

	public ItemGlow(final int id) {
		super(id);
	}

	public static ItemEnchant getItemGlowEnchant() {
		return new ItemEnchant(getGlowEnchant());
	}

	public static Enchantment getGlowEnchant() {
		if(glow != null) {
			return glow;
		}

		try {
			final Field f = Enchantment.class.getDeclaredField("acceptingNew");
			f.setAccessible(true);
			f.set(null, true);
		} catch(final Exception e) {
			e.printStackTrace();
		}

		glow = new ItemGlow(255);
		try {
			Enchantment.registerEnchantment(glow);
		} catch(final IllegalArgumentException iae) {
		}
		return glow;
	}

	public static boolean isGlow(final ItemStack item) {
		return item.getEnchantments().containsKey(glow);
	}

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