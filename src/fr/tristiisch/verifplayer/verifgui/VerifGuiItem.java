package fr.tristiisch.verifplayer.verifgui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.ArrayUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;

import fr.tristiisch.verifplayer.gui.GuiManager;
import fr.tristiisch.verifplayer.gui.api.GuiCreator;
import fr.tristiisch.verifplayer.gui.api.GuiPage;
import fr.tristiisch.verifplayer.hook.HookHandler;
import fr.tristiisch.verifplayer.hook.PlaceholderAPIHook;
import fr.tristiisch.verifplayer.playerinfo.PlayerInfo;
import fr.tristiisch.verifplayer.playerinfo.PlayersInfos;
import fr.tristiisch.verifplayer.utils.Reflection;
import fr.tristiisch.verifplayer.utils.SpigotUtils;
import fr.tristiisch.verifplayer.utils.TPS;
import fr.tristiisch.verifplayer.utils.Utils;
import fr.tristiisch.verifplayer.utils.VersionUtils.Versions;
import fr.tristiisch.verifplayer.utils.config.ConfigGet;
import fr.tristiisch.verifplayer.utils.item.ItemCreator;
import me.clip.placeholderapi.PlaceholderAPI;

public class VerifGuiItem {

	public enum VerifGuiSlot {
		SKULL(0),
		EFFECTS(1),
		CPS(2),
		PING_AND_TPS(3),
		ARMOR(4),
		OFFHAND(8),
		INVENTORY(9),
		HOTBAR(4 * 9),
		HOLDING(5 * 9);

		private final int slot;

		private VerifGuiSlot(final int slot) {
			this.slot = slot;
		}

		public int getSlot() {
			return this.slot;
		}

	}

	public static Map<Integer, ItemStack> getAllItems(final Player player) {
		final Map<Integer, ItemStack> items = new HashMap<>();

		// Head
		items.put(VerifGuiSlot.SKULL.getSlot(), VerifGuiItem.getSkull(player));

		// Effects
		items.put(VerifGuiSlot.EFFECTS.getSlot(), VerifGuiItem.getEffects(player));

		// CPS
		items.put(VerifGuiSlot.CPS.getSlot(), VerifGuiItem.getCps(player));

		// Ping + Tps
		items.put(VerifGuiSlot.PING_AND_TPS.getSlot(), VerifGuiItem.getPingAndTps(player));

		// Inv + armor + Holding slot
		items.putAll(VerifGuiItem.getInventory(player.getInventory()));

		return items;
	}

	public static ItemStack getCps(final Player player) {
		final List<String> lore = new ArrayList<>();
		final PlayerInfo playerInfo = PlayersInfos.getPlayerInfo(player);

		Integer currentClick = playerInfo.getCurrentClicks();
		final short glassPaneColor = SpigotUtils.getIntervalGlassPaneColor(currentClick, 10, 18);
		lore.add("");

		String nbAlert;
		if(playerInfo.getNbAlerts() > 1) {
			nbAlert = ConfigGet.MESSAGES_VERIFGUI_NBALERTS.getString();
		} else {
			nbAlert = ConfigGet.MESSAGES_VERIFGUI_NBALERT.getString();
		}

		nbAlert = nbAlert.replaceAll("%alert_nb%", String.valueOf(playerInfo.getNbAlerts()));
		lore.add(nbAlert);
		String cpsMax = ConfigGet.MESSAGES_VERIFGUI_CPSMAX.getString();
		cpsMax = cpsMax.replaceAll("%cps_max%", String.valueOf(playerInfo.getMaxCPS()));
		lore.add(cpsMax);
		lore.add("");
		for(int i = playerInfo.getClicksAir().size() - 1; i >= 0; --i) {
			final int clickAir = playerInfo.getClicksAir().get(i);
			final int clickEntity = playerInfo.getClicksEntity().get(i);
			final int clickGlobal = clickAir + clickEntity;
			final ChatColor chatColor = SpigotUtils.getIntervalChatColor(clickGlobal, 10, 18);
			String string = ConfigGet.MESSAGES_VERIFGUI_CPSFORMAT.getString();
			string = string.replace("%color%", chatColor.toString());
			string = string.replace("%cpsAir%", String.valueOf(clickAir));
			string = string.replace("%cpsEntity%", String.valueOf(clickEntity));
			string = string.replace("%cpsGlobal%", String.valueOf(clickGlobal));
			lore.add(string);
		}
		do {
			lore.add("");
		} while(ConfigGet.SETTINGS_SIZEHISTORYCPS.getInt() + 4 > lore.size());

		final Map<Long, Integer> alertHistory = playerInfo.getAlertHistory();
		if(!alertHistory.isEmpty()) {

			for(final Entry<Long, Integer> entry2 : alertHistory.entrySet()) {
				final long time = entry2.getKey();
				final int click = entry2.getValue();
				;
				String string = ConfigGet.MESSAGES_VERIFGUI_CPSALERTFORMAT.getString();

				string = string.replace("%cps%", String.valueOf(click));
				string = string.replace("%time%", Utils.timestampToDuration(time));

			}
			lore.add("");
		}

		if(currentClick == 0) {
			currentClick = 1;
		}
		final ChatColor[] colors = ChatColor.values();
		final ChatColor ramdomColor = ChatColor.values()[new Random().nextInt(colors.length)];
		return new ItemCreator(Material.STAINED_GLASS_PANE).size(currentClick).dataValue(glassPaneColor).customName(ramdomColor + ConfigGet.MESSAGES_VERIFGUI_CPSITEMNAME.getString()).lore(lore)
				.getItemStack();
	}

	public static ItemStack getEffects(final Player player) {
		final List<String> lore = new ArrayList<>();
		final Collection<PotionEffect> potionEffects = player.getActivePotionEffects();
		int effectSize = potionEffects.size();
		lore.add("");
		String name = ConfigGet.MESSAGES_VERIFGUI_EFFECT.getString();
		Material material;
		if(effectSize == 0) {
			lore.add(ConfigGet.MESSAGES_VERIFGUI_NOEFFECT.getString());
			material = Material.GLASS_BOTTLE;
			effectSize = 1;
		} else {
			name = String.valueOf(name) + "s";
			material = Material.POTION;
			for(final PotionEffect potionEffect : potionEffects) {
				final StringBuilder sb = new StringBuilder();
				sb.append("&7");
				sb.append(potionEffect.getType().getName());
				sb.append(" ");
				sb.append(potionEffect.getAmplifier() + 1);
				if(!potionEffect.hasParticles()) {
					sb.append(ConfigGet.MESSAGES_VERIFGUI_NOPARTICLE.getString());
				}
				sb.append(": &e");
				sb.append(Utils.secondsToCalendar(potionEffect.getDuration() / 20));
				lore.add(Utils.color(sb.toString()));
			}
		}
		return new ItemCreator(material).size(effectSize).customName(name).lore(lore).getItemStack();
	}

	public static ConcurrentHashMap<Integer, ItemStack> getInventory(final PlayerInventory playerInventory) {
		final ConcurrentHashMap<Integer, ItemStack> items = new ConcurrentHashMap<>();

		final ItemStack itemStackAir = new ItemCreator(Material.AIR).getItemStack();

		// Armor
		int slot = VerifGuiSlot.ARMOR.getSlot();
		final ItemStack[] armors = playerInventory.getArmorContents();
		ArrayUtils.reverse(armors);
		ItemStack[] array;
		for(int length = (array = armors).length, k = 0; k < length; ++k) {
			ItemStack armorItem = array[k];
			if(armorItem == null) {
				armorItem = itemStackAir;
			}
			items.put(slot++, armorItem);
		}

		// OffHand if 1.9+
		if(Versions.V1_9.isEqualOrOlder()) {
			ItemStack itemInOffHand = playerInventory.getItemInOffHand();
			//if(itemInOffHand != null) {
			if(itemInOffHand == null) {
				itemInOffHand = itemStackAir;
			}
			items.put(VerifGuiSlot.OFFHAND.getSlot(), itemInOffHand);
			//}
		}

		// Inv
		int slotInv = VerifGuiSlot.INVENTORY.getSlot();
		int slotHotbar = VerifGuiSlot.HOTBAR.getSlot();
		ItemStack[] contents;
		for(int length2 = (contents = playerInventory.getContents()).length, l = 0; l < length2; ++l) {
			ItemStack itemStack = contents[l];
			int slot1 = -1;
			if(slotHotbar < VerifGuiSlot.HOTBAR.getSlot() + 9) {
				slot1 = slotHotbar++;
			} else if(slotInv < VerifGuiSlot.HOTBAR.getSlot()) {
				slot1 = slotInv++;
			} else {
				continue;
			}

			if(itemStack == null) {
				itemStack = itemStackAir;
			}
			items.put(slot1, itemStack);
		}

		// Holding slot
		slot = VerifGuiSlot.HOLDING.getSlot();
		final int heldSlot = playerInventory.getHeldItemSlot() + slot;
		for(int i2 = slot; slot + 9 > i2; ++i2) {
			items.put(i2, itemStackAir);
		}
		items.put(heldSlot, new ItemCreator(Material.GHAST_TEAR).customName("&ePlayer Holding").getItemStack());

		return items;
	}

	public static ItemStack getPingAndTps(final Player player) {
		final double[] tps = TPS.getTPSArray();
		final List<String> lore = new ArrayList<>();
		int ping = Reflection.getPing(player);
		final short glassPaneColor = SpigotUtils.getIntervalGlassPaneColor(ping, 100, 200);
		ChatColor chatColor2 = SpigotUtils.getIntervalChatColor(ping, 100, 200);
		lore.add("");
		lore.add(ConfigGet.MESSAGES_VERIFGUI_PINGFORMAT.getString().replaceAll("%color%", chatColor2.toString()).replaceAll("%ping%", String.valueOf(ping)));
		if(ping == 0) {
			ping = 1;
		}
		lore.add("");
		lore.add(ConfigGet.MESSAGES_VERIFGUI_TPSITEMNAME.getString());
		lore.add("");
		for(int j = 0; j < tps.length; ++j) {
			final double tpsDouble = tps[j];
			final int tpsInt = (int) Math.round(tpsDouble);
			if(tpsInt >= 18) {
				chatColor2 = ChatColor.GREEN;
			} else if(tpsInt >= 16) {
				chatColor2 = ChatColor.GOLD;
			} else {
				chatColor2 = ChatColor.RED;
			}
			String tpsFormat = ConfigGet.MESSAGES_VERIFGUI_TPSFORMAT.getString();
			int time = 0;
			switch(j) {
			case 0:
				time = 1;
				break;

			case 1:
				time = 5;
				break;

			case 2:
				time = 15;
				break;

			default:
				time = -1;
				break;
			}
			tpsFormat = tpsFormat.replace("%time%", String.valueOf(time));
			tpsFormat = tpsFormat.replace("%color%", chatColor2.toString());
			tpsFormat = tpsFormat.replace("%tps%", String.valueOf(tpsDouble));
			lore.add(tpsFormat);
		}
		return new ItemCreator(Material.STAINED_GLASS_PANE).size(ping).dataValue(glassPaneColor).customName(ConfigGet.MESSAGES_VERIFGUI_PINGITEMNAME.getString()).lore(lore).getItemStack();
	}

	public static ItemStack getSkull(final Player player) {
		final ItemCreator itemCreator = new ItemCreator().customName("§6" + player.getName());

		final ConcurrentHashMap<String, String> replace = new ConcurrentHashMap<>();

		if(player.isDead()) {
			replace.put("%heal%", Utils.color("&4Dead"));
		} else {
			final StringBuilder heal = new StringBuilder();
			heal.append(String.valueOf(Math.round(player.getHealth()) / 2.0f).replaceFirst("\\.0*$", ""));
			heal.append(" ");
			replace.put("%heal%", heal.toString());
		}

		final String rank = HookHandler.getInstance().getGroup(player);
		if(rank == null) {
			replace.put("%rank%", "");
		} else {
			replace.put("%rank%", rank);
		}

		replace.put("%food%", String.valueOf(Math.round(player.getFoodLevel()) / 2.0f).replaceFirst("\\.0*$", ""));

		replace.put("%xp_level%", String.valueOf(player.getLevel()));

		replace.put("%first_played%", Utils.timestampToDate(player.getFirstPlayed() / 1000L));

		replace.put("%last_played%", Utils.timestampToDateAndHour(player.getLastPlayed() / 1000L));

		if(player.isInsideVehicle()) {
			final Entity entity = player.getVehicle();
			if(Versions.V1_8.isEqualOrOlder()) {
				replace.put("%vehicule%", entity.getName());
			} else {
				replace.put("%vehicule%", entity.toString().toLowerCase());
			}

		} else {
			replace.put("%vehicule%", Utils.color("&c\u2716"));
		}

		final String guiName;
		final GuiCreator gui = GuiManager.get(player);
		if(gui != null) {
			final GuiPage guiPage = gui.getGuiPage();
			if(guiPage instanceof VerifGuiPage) {
				final VerifGuiPage verifGuiPage = (VerifGuiPage) guiPage;

				final UUID uuidPlayerBeingCheckByPlayerCheck = VerifGuiManager.getByViewer(player.getUniqueId()).getKey();
				final Player playerBeingCheckByPlayerCheck = Bukkit.getPlayer(uuidPlayerBeingCheckByPlayerCheck);
				guiName = verifGuiPage.getDescription(playerBeingCheckByPlayerCheck);
			} else {
				guiName = guiPage.getDescription();
			}
		} else {
			final InventoryView openInventory = player.getOpenInventory();
			if(openInventory != null && openInventory.getType() != InventoryType.CRAFTING && openInventory.getType() != InventoryType.CREATIVE) {
				guiName = openInventory.getType().name().toLowerCase();
			} else {
				guiName = Utils.color("&c\u2716");
			}
		}
		replace.put("%gui%", guiName);

		List<String> infos = ConfigGet.MESSAGES_VERIFGUI_INFO.getStringList();

		for(int i = 0; infos.size() > i; i++) {
			String string = infos.get(i);

			for(final Entry<String, String> entry : replace.entrySet()) {
				final String toBeReplaced = entry.getKey();
				final String value = entry.getValue();

				if(string.contains(toBeReplaced)) {
					if(value.isEmpty()) {
						infos.remove(i--);
						replace.remove(toBeReplaced);
						break;
					}
					string = string.replaceAll(toBeReplaced, entry.getValue());
				}

				infos.set(i, string);
			}

		}

		if(PlaceholderAPIHook.getInstance().isEnabled()) {
			infos = PlaceholderAPI.setPlaceholders(player, infos);
		}
		itemCreator.lore(infos);
		return itemCreator.getPlayerHead(player);
	}
}
