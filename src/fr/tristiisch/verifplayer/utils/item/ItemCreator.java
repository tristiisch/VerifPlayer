package fr.tristiisch.verifplayer.utils.item;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import fr.tristiisch.verifplayer.utils.Utils;
import fr.tristiisch.verifplayer.utils.VersionUtils.Versions;

public class ItemCreator {

	Material material = null;
	int size = 1;
	short dataValue = 0;
	String customName = null;
	List<String> lore = null;
	List<ItemEnchant> enchantements = null;

	public ItemCreator() {
	}

	public ItemCreator(final Material material) {
		this.material = material;
	}

	public ItemCreator customName(final String customName) {
		this.customName = customName;
		return this;
	}

	public ItemCreator dataValue(final short dataValue) {
		this.dataValue = dataValue;
		return this;
	}

	public ItemCreator enchantement(final ItemEnchant enchantement) {
		return this.enchantements(Arrays.asList(enchantement));
	}

	public ItemCreator enchantements(final List<ItemEnchant> enchantements) {
		this.enchantements = enchantements;
		return this;
	}

	public ItemStack getItemStack() {
		final ItemStack itemStack = new ItemStack(this.material, this.size, this.dataValue);
		final ItemMeta itemMeta = itemStack.getItemMeta();
		if(this.customName != null) {
			itemMeta.setDisplayName(Utils.color(this.customName));
		}

		if(this.lore != null) {
			itemMeta.setLore(Utils.color(this.lore));
		}

		if(this.enchantements != null) {
			for(final ItemEnchant enchantement : this.enchantements) {
				enchantement.setEnchantToItemMeta(itemMeta);
			}
		}

		itemStack.setItemMeta(itemMeta);
		return itemStack;
	}

	@SuppressWarnings("deprecation")
	public ItemStack getPlayerHead(final Player player) {
		this.material = Material.SKULL;
		final ItemStack itemStack = this.getItemStack();
		final SkullMeta skullmeta = (SkullMeta) itemStack.getItemMeta();
		if(Versions.V1_9.isEqualOrOlder()) {
			skullmeta.setOwningPlayer(player);
		} else {
			skullmeta.setOwner(player.getName());
		}
		itemStack.setItemMeta(skullmeta);
		return itemStack;
	}

	public ItemCreator lore(final List<String> lore) {
		this.lore = lore;
		return this;
	}

	public ItemCreator lore(final String... lore) {
		return this.lore(Arrays.asList(lore));
	}

	public ItemCreator material(final Material material) {
		this.material = material;
		return this;
	}

	public ItemCreator size(final int size) {
		this.size = size;
		return this;
	}
}
