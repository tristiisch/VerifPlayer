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

	public ItemCreator flag(final ItemFlag... flags) {
		this.flags = Arrays.asList(flags);
		return this;
	}

	public ItemCreator flag(final List<ItemFlag> flags) {
		this.flags = flags;
		return this;
	}

	public ItemStack getItemStack() {
		final ItemStack itemStack = new ItemStack(this.material, this.size, this.dataValue);
		final ItemMeta itemMeta = itemStack.getItemMeta();
		if(this.customName != null) {
			itemMeta.setDisplayName(SpigotUtils.color(this.customName));
		}

		if(this.lore != null) {
			itemMeta.setLore(SpigotUtils.color(this.lore));
		}

		if(this.enchantements != null) {
			for(final ItemEnchant enchantement : this.enchantements) {
				enchantement.setEnchantToItemMeta(itemMeta);
			}
		}

		if(this.flags != null) {
			for(final ItemFlag flag : this.flags) {
				itemMeta.addItemFlags(flag);
			}
		}

		itemStack.setItemMeta(itemMeta);
		return itemStack;
	}

	@SuppressWarnings("deprecation")
	public ItemStack getPlayerHead(final Player player) {
		this.material = Material.SKULL_ITEM;
		this.dataValue = 3;
		final ItemStack itemStack = this.getItemStack();
		final SkullMeta skullmeta = (SkullMeta) itemStack.getItemMeta();
		if(ServerVersion.V1_12.isEqualOrOlder()) {
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
