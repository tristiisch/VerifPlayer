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
	// FAST(6, new ItemCreator(Material.LEATHER_BOOTS)),
	// JUMP(7, new ItemCreator(Material.FIREWORK)),
	QUIT(35, new ItemCreator(Material.BARRIER)),;

	static {
		/*
		 * FAST.setItemStack(NBTEditor.setItemTag(FAST.getItemStack(),
		 * "generic.movementSpeed", "AttributeModifiers", null, "AttributeName"));
		 * FAST.setItemStack(NBTEditor.setItemTag(FAST.getItemStack(),
		 * "generic.movementSpeed", "AttributeModifiers", 0, "Name"));
		 * FAST.setItemStack(NBTEditor.setItemTag(FAST.getItemStack(), "mainhand",
		 * "AttributeModifiers", 0, "Slot"));
		 * FAST.setItemStack(NBTEditor.setItemTag(FAST.getItemStack(), 0.75,
		 * "AttributeModifiers", 0, "Amount"));
		 * FAST.setItemStack(NBTEditor.setItemTag(FAST.getItemStack(), 1,
		 * "AttributeModifiers", 0, "Operation"));
		 * FAST.setItemStack(NBTEditor.setItemTag(FAST.getItemStack(), 894654L,
		 * "AttributeModifiers", 0, "UUIDLeast"));
		 * FAST.setItemStack(NBTEditor.setItemTag(FAST.getItemStack(), 2872L,
		 * "AttributeModifiers", 0, "UUIDMost"));
		 *
		 * SLOW.setItemStack(NBTEditor.setItemTag(SLOW.getItemStack(),
		 * "generic.movementSpeed", "AttributeModifiers", null, "AttributeName"));
		 * SLOW.setItemStack(NBTEditor.setItemTag(SLOW.getItemStack(),
		 * "generic.movementSpeed", "AttributeModifiers", 0, "Name"));
		 * SLOW.setItemStack(NBTEditor.setItemTag(SLOW.getItemStack(), "mainhand",
		 * "AttributeModifiers", 0, "Slot"));
		 * SLOW.setItemStack(NBTEditor.setItemTag(SLOW.getItemStack(), -0.25,
		 * "AttributeModifiers", 0, "Amount"));
		 * SLOW.setItemStack(NBTEditor.setItemTag(SLOW.getItemStack(), 1,
		 * "AttributeModifiers", 0, "Operation"));
		 * SLOW.setItemStack(NBTEditor.setItemTag(SLOW.getItemStack(), 894654L,
		 * "AttributeModifiers", 0, "UUIDLeast"));
		 * SLOW.setItemStack(NBTEditor.setItemTag(SLOW.getItemStack(), 2872L,
		 * "AttributeModifiers", 0, "UUIDMost"));
		 */
	}

	public static VerifSpecTool getTool(final ItemStack itemStack) {
		return Arrays.stream(VerifSpecTool.values()).filter(verifSpecItem -> verifSpecItem.getItemStack().isSimilar(itemStack)).findFirst().orElse(null);
	}

	public static VerifSpecTool getTool(final String name) {
		return Arrays.stream(VerifSpecTool.values()).filter(verifSpecItem -> verifSpecItem.getName().equals(name)).findFirst().orElse(null);
	}

	private int slot;
	private boolean enable = true;
	private ItemCreator itemCreator;

	VerifSpecTool(final int slot, final ItemCreator itemCreator) {
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

	public void setEnable(final boolean enable) {
		this.enable = enable;
	}

	public void setItemCreator(final ItemCreator itemCreator) {
		this.itemCreator = itemCreator;
	}

	public void setSlot(final int slot) {
		this.slot = slot;
	}
}
