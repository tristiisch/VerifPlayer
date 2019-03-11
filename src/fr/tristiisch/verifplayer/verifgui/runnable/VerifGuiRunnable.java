package fr.tristiisch.verifplayer.verifgui.runnable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

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
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;

import fr.tristiisch.verifplayer.VerifPlayerData;
import fr.tristiisch.verifplayer.object.PlayerInfo;
import fr.tristiisch.verifplayer.utils.ConfigUtils;
import fr.tristiisch.verifplayer.utils.ItemTools;
import fr.tristiisch.verifplayer.utils.Reflection;
import fr.tristiisch.verifplayer.utils.TPS;
import fr.tristiisch.verifplayer.utils.Utils;
import fr.tristiisch.verifplayer.utils.gui.GuiCreator;
import fr.tristiisch.verifplayer.utils.gui.GuiManager;
import fr.tristiisch.verifplayer.verifclick.FastClickRunnable;
import fr.tristiisch.verifplayer.verifgui.VerifGuiManager;
import fr.tristiisch.verifplayer.verifgui.VerifGuiPage;

public class VerifGuiRunnable extends BukkitRunnable {

	@SuppressWarnings("deprecation")
	@Override
	public void run() {
		final double[] tps = TPS.getTPSArray();
		for(final Map.Entry<UUID, Set<UUID>> entry : VerifGuiManager.getPlayersBeingChecked().entrySet()) {
			final UUID uuid = entry.getKey();
			final Set<UUID> viewers = entry.getValue();
			final PlayerInfo playerInfo = VerifPlayerData.get(uuid);
			final Player player = Bukkit.getPlayer(uuid);
			if(player == null) {
				return;
			}
			final HashMap<Integer, ItemStack> items = new HashMap<>();
			final List<String> lore = new ArrayList<>();
			int slot = VerifPlayerData.slotTools;
			ItemStack item = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
			final SkullMeta skullmeta = (SkullMeta) item.getItemMeta();
			if(VerifPlayerData.is1_9) {
				skullmeta.setOwningPlayer(player);
			} else {
				skullmeta.setOwner(player.getName());
			}
			skullmeta.setDisplayName("§6" + player.getName());
			final Map<String, String> replace = new HashMap<>();
			if(player.isDead()) {
				replace.put("%heal%", Utils.color("&c\u2716 Dead"));
			} else {
				final StringBuilder heal = new StringBuilder();
				heal.append(String.valueOf(Math.round(player.getHealth()) / 2.0f).replace("\\.?0*$", ""));
				heal.append(" ");
				heal.append(Utils.color("&c\u2764"));
				replace.put("%heal%", heal.toString());
			}
			replace.put("%food%", String.valueOf(Math.round(player.getFoodLevel()) / 2.0f).replace("\\.?0*$", ""));
			replace.put("%xp_level%", String.valueOf(player.getLevel()));
			replace.put("%first_played%", Utils.timestampToDate(player.getFirstPlayed() / 1000L));
			replace.put("%last_played%", Utils.timestampToDateAndHour(player.getLastPlayed() / 1000L));
			if(player.isInsideVehicle()) {
				final Entity entity = player.getVehicle();
				replace.put("%vehicule%", entity.getName());
			} else {
				replace.put("%vehicule%", Utils.color("&c\u2716"));
			}
			final InventoryView openInventory = player.getOpenInventory();
			if(openInventory != null && openInventory.getType() != InventoryType.CRAFTING && openInventory.getType() != InventoryType.CREATIVE) {
				replace.put("%gui%", openInventory.getType().name());
			} else {
				replace.put("%gui%", Utils.color("&c\u2716"));
			}
			final List<String> infos = ConfigUtils.VERIFGUI_INFO.getStringList();
			Utils.replaceAll(infos, replace);
			skullmeta.setLore(infos);
			item.setItemMeta(skullmeta);
			items.put(slot++, item);
			final Collection<PotionEffect> potionEffects = player.getActivePotionEffects();
			int effectSize = potionEffects.size();
			lore.add("");
			String name = ConfigUtils.VERIFGUI_EFFECT.getString();
			Material material;
			if(effectSize == 0) {
				lore.add(ConfigUtils.VERIFGUI_NOEFFECT.getString());
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
						sb.append(ConfigUtils.VERIFGUI_NOPARTICLE.getString());
					}
					sb.append(": &e");
					sb.append(Utils.secondsToCalendar(potionEffect.getDuration() / 20));
					lore.add(Utils.color(sb.toString()));
				}
			}
			item = ItemTools.create(material, effectSize, name, lore);
			lore.clear();
			items.put(slot++, item);
			Integer currentClick = playerInfo.getCurrentClicks();
			int glassPaneColor = VerifPlayerData.getIntervalGlassPaneColor(currentClick, 10, 18);
			lore.add("");
			String nbAlert = ConfigUtils.VERIFGUI_NBALERT.getString();
			nbAlert = nbAlert.replaceAll("%s%", Utils.withOrWithoutS(playerInfo.getNbAlerts()));
			nbAlert = nbAlert.replaceAll("%alert_nb%", String.valueOf(playerInfo.getNbAlerts()));
			lore.add(nbAlert);
			String cpsMax = ConfigUtils.VERIFGUI_CPSMAX.getString();
			cpsMax = cpsMax.replaceAll("%cps_max%", String.valueOf(playerInfo.getMaxCPS()));
			lore.add(cpsMax);
			lore.add("");
			for(int i = playerInfo.getClicksAir().size() - 1; i >= 0; --i) {
				final int clickAir = playerInfo.getClicksAir().get(i);
				final int clickEntity = playerInfo.getClicksEntity().get(i);
				final int clickGlobal = clickAir + clickEntity;
				final ChatColor chatColor = VerifPlayerData.getIntervalChatColor(clickGlobal, 10, 18);
				String string = ConfigUtils.VERIFGUI_CPSFORMAT.getString();
				string = string.replace("%color%", chatColor.toString());
				string = string.replace("%cpsAir%", String.valueOf(clickAir));
				string = string.replace("%cpsEntity%", String.valueOf(clickEntity));
				string = string.replace("%cpsGlobal%", String.valueOf(clickGlobal));
				lore.add(string);
			}
			do {
				lore.add("");
			} while(FastClickRunnable.sizeHistoryCps + 4 >= lore.size());
			if(currentClick == 0) {
				currentClick = 1;
			}
			final ChatColor[] colors = ChatColor.values();
			final ChatColor ramdomColor = ChatColor.values()[new Random().nextInt(colors.length)];
			item = ItemTools.create(Material.STAINED_GLASS_PANE, currentClick, (byte) glassPaneColor, ramdomColor + ConfigUtils.VERIFGUI_CPSITEMNAME.getString(), lore);
			lore.clear();
			items.put(slot++, item);
			int ping = Reflection.getPing(player);
			glassPaneColor = VerifPlayerData.getIntervalGlassPaneColor(ping, 100, 200);
			ChatColor chatColor2 = VerifPlayerData.getIntervalChatColor(ping, 100, 200);
			lore.add("");
			lore.add(ConfigUtils.VERIFGUI_PINGFORMAT.getString().replaceAll("%color%", chatColor2.toString()).replaceAll("%ping%", String.valueOf(ping)));
			if(ping == 0) {
				ping = 1;
			}
			lore.add("");
			lore.add(ConfigUtils.VERIFGUI_TPSITEMNAME.getString());
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
				String tpsFormat = ConfigUtils.VERIFGUI_TPSFORMAT.getString();
				int time = 0;
				switch(j) {
				case 0: {
					time = 1;
					break;
				}
				case 1: {
					time = 5;
					break;
				}
				case 2: {
					time = 15;
					break;
				}
				default: {
					time = -1;
					break;
				}
				}
				tpsFormat = tpsFormat.replace("%time%", String.valueOf(time));
				tpsFormat = tpsFormat.replace("%color%", chatColor2.toString());
				tpsFormat = tpsFormat.replace("%tps%", String.valueOf(tpsDouble));
				lore.add(tpsFormat);
			}
			item = ItemTools.create(Material.STAINED_GLASS_PANE, ping, (byte) glassPaneColor, ConfigUtils.VERIFGUI_PINGITEMNAME.getString(), lore);
			lore.clear();
			items.put(slot++, item);
			final PlayerInventory targetInventory = player.getInventory();
			slot = VerifPlayerData.slotArmor;
			final ItemStack[] armors = targetInventory.getArmorContents();
			ArrayUtils.reverse(armors);
			ItemStack[] array;
			for(int length = (array = armors).length, k = 0; k < length; ++k) {
				final ItemStack armorItem = array[k];
				items.put(slot++, armorItem);
			}
			int slotInv = VerifPlayerData.slotInv;
			int slotHotbar = VerifPlayerData.slotHotBar;
			ItemStack[] contents;
			for(int length2 = (contents = targetInventory.getContents()).length, l = 0; l < length2; ++l) {
				final ItemStack itemStack = contents[l];
				if(slotHotbar < VerifPlayerData.slotHotBar + 9) {
					items.put(slotHotbar++, itemStack);
				} else if(slotInv < VerifPlayerData.slotHotBar) {
					items.put(slotInv++, itemStack);
				}
			}
			if(VerifPlayerData.is1_9) {
				items.put(slot, targetInventory.getItemInOffHand());
			}
			slot = VerifPlayerData.slotHolding;
			final int heldSlot = targetInventory.getHeldItemSlot() + slot;
			for(int i2 = slot; slot + 9 > i2; ++i2) {
				items.put(i2, ItemTools.create(Material.AIR));
			}
			items.put(heldSlot, ItemTools.create(Material.GHAST_TEAR, 1, "&ePlayer Holding"));
			for(final UUID viewersUuid : viewers) {
				final Player viewer = Bukkit.getPlayer(viewersUuid);
				final GuiCreator gui = GuiManager.get(viewer);
				if(gui == null || !gui.getGuiPage().isSamePage(VerifGuiPage.HOME)) {
					System.out.println("BUG: VerifGuiManager.remove(viewer)");
					VerifGuiManager.remove(viewer);
				}
				final InventoryView inventory = viewer.getOpenInventory();
				for(final Map.Entry<Integer, ItemStack> entryItems : items.entrySet()) {
					final ItemStack entryItem = entryItems.getValue();
					final Integer entrySlot = entryItems.getKey();
					final ItemStack actuelItem = inventory.getItem(entrySlot);
					if(entryItem != null && !entryItem.isSimilar(actuelItem)) {
						inventory.setItem(entrySlot, entryItem);
					}
				}
			}
		}
	}
}
