package fr.tristiisch.verifplayer.utils.item;

import java.lang.reflect.Field;

import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;

import fr.tristiisch.verifplayer.VerifPlayer;
import fr.tristiisch.verifplayer.utils.VersionUtils.ServerVersion;

public class ItemGlow extends Enchantment {
	
	private static Enchantment glow;

	{
		try {
			Field f = Enchantment.class.getDeclaredField("acceptingNew");
			f.setAccessible(true);
			f.set(null, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
 
		if (ServerVersion.V1_13.isEqualOrOlder()) {
			glow = new ItemGlow(new NamespacedKey(VerifPlayer.getInstance(), VerifPlayer.class.getName()));
		}
		try {
			Enchantment.registerEnchantment(glow);
		} catch (IllegalArgumentException iae) {
		}
	}

	public static ItemEnchant getItemGlowEnchant() {
		return new ItemEnchant(glow);
	}
	
	public ItemGlow(NamespacedKey id) {
		super(id);
	}

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

	@Override
	public boolean isCursed() {
		return false;
	}

	@Override
	public boolean isTreasure() {
		return false;
	}
}
