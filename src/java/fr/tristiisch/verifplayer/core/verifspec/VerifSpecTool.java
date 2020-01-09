package fr.tristiisch.verifplayer.core.verifspec;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import fr.tristiisch.verifplayer.utils.item.ItemCreator;
import fr.tristiisch.verifplayer.utils.item.ItemEnchant;

public enum VerifSpecTool {

	TELEPORTER(0, new ItemCreator(Material.COMPASS)),
	FREEZE(1, new ItemCreator(Material.BLAZE_ROD)),
	KNOCKBACK(2, new ItemCreator(Material.STICK).enchantement(new ItemEnchant(Enchantment.KNOCKBACK, 2))),
	VERIF(3, new ItemCreator(Material.CHEST)),
	SHUTTLE(4, new ItemCreator(Material.MINECART)),
	SPEED(5, new ItemCreator(Material.FIREWORK_CHARGE)),
	QUIT(35, new ItemCreator(Material.BARRIER)),;

	public static VerifSpecTool getTool(ItemStack itemStack) {
		return Arrays.stream(VerifSpecTool.values()).filter(verifSpecItem -> verifSpecItem.getItemStack().isSimilar(itemStack)).findFirst().orElse(null);
	}

	public static VerifSpecTool getTool(String name) {
		return Arrays.stream(VerifSpecTool.values()).filter(verifSpecItem -> verifSpecItem.getName().equals(name)).findFirst().orElse(null);
	}

	private int slot;
	private boolean enable = true;
	private ItemCreator itemCreator;

	VerifSpecTool(int slot, ItemCreator itemCreator) {
		this.slot = slot;
		this.itemCreator = itemCreator;
	}

	public ItemCreator getItemCreator() {
		return this.itemCreator;
	}

	public ItemStack getItemStack() {
		return this.itemCreator.getItemStack();
	}

	public String getName() {
		return this.toString().toLowerCase();
	}

	public int getSlot() {
		return this.slot;
	}

	public boolean isEnable() {
		return this.enable;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	public void setItemCreator(ItemCreator itemCreator) {
		this.itemCreator = itemCreator;
	}

	public void setSlot(int slot) {
		this.slot = slot;
	}
}
