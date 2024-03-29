package fr.tristiisch.verifplayer.utils.item;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import fr.tristiisch.verifplayer.utils.SpigotUtils;
import fr.tristiisch.verifplayer.utils.VersionUtils.ServerVersion;

public class ItemCreator {

	Material material = null;
	int size = 1;
	short dataValue = 0;
	String customName = null;
	List<String> lore = null;
	List<ItemEnchant> enchantements = null;
	List<ItemFlag> flags = null;

	public ItemCreator() {
	}

	public ItemCreator(Material material) {
		this.material = material;
	}

	public ItemCreator attributeHide() {
		this.flag(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_POTION_EFFECTS, ItemFlag.HIDE_DESTROYS);
		return this;
	}

	public ItemCreator customName(String customName) {
		this.customName = customName;
		return this;
	}

	public ItemCreator dataValue(short dataValue) {
		this.dataValue = dataValue;
		return this;
	}

	public ItemCreator enchantement(ItemEnchant enchantement) {
		return enchantements(Arrays.asList(enchantement));
	}

	public ItemCreator enchantements(List<ItemEnchant> enchantements) {
		this.enchantements = enchantements;
		return this;
	}

	public ItemCreator flag(ItemFlag... flags) {
		this.flags = Arrays.asList(flags);
		return this;
	}

	public ItemCreator flag(List<ItemFlag> flags) {
		this.flags = flags;
		return this;
	}

	public ItemStack getItemStack() {
		ItemStack itemStack = new ItemStack(material, size, dataValue);
		ItemMeta itemMeta = itemStack.getItemMeta();
		if (customName != null)
			itemMeta.setDisplayName(SpigotUtils.color(customName));
		if (lore != null)
			itemMeta.setLore(SpigotUtils.color(lore));
		if (enchantements != null)
			for (ItemEnchant enchantement : enchantements)
				enchantement.setEnchantToItemMeta(itemMeta);
		if (flags != null)
			for (ItemFlag flag : flags)
				itemMeta.addItemFlags(flag);

		itemStack.setItemMeta(itemMeta);
		return itemStack;
	}

	@SuppressWarnings("deprecation")
	public ItemStack getPlayerHead(Player player) {
		material = Material.PLAYER_HEAD;
		dataValue = 3;
		ItemStack itemStack = getItemStack();
		SkullMeta skullmeta = (SkullMeta) itemStack.getItemMeta();
		if (ServerVersion.V1_12.isEqualOrOlder())
			skullmeta.setOwningPlayer(player);
		else
			skullmeta.setOwner(player.getName());
		itemStack.setItemMeta(skullmeta);
		return itemStack;
	}

	public ItemCreator lore(List<String> lore) {
		this.lore = lore;
		return this;
	}

	public ItemCreator lore(String... lore) {
		return this.lore(Arrays.asList(lore));
	}

	public ItemCreator material(Material material) {
		this.material = material;
		return this;
	}

	public ItemCreator size(int size) {
		this.size = size;
		return this;
	}
}
