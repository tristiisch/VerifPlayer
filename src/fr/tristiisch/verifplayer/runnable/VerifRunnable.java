package fr.tristiisch.verifplayer.runnable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang.ArrayUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;

import fr.tristiisch.verifplayer.VerifPlayer;
import fr.tristiisch.verifplayer.object.PlayerInfo;
import fr.tristiisch.verifplayer.utils.ConfigUtils;
import fr.tristiisch.verifplayer.utils.ItemTools;
import fr.tristiisch.verifplayer.utils.Reflection;
import fr.tristiisch.verifplayer.utils.TPS;
import fr.tristiisch.verifplayer.utils.Utils;
import fr.tristiisch.verifplayer.utils.gui.GuiName;
import fr.tristiisch.verifplayer.utils.gui.GuiTool;
import fr.tristiisch.verifplayer.utils.gui.GuiTools;

public class VerifRunnable extends BukkitRunnable {

	@SuppressWarnings("deprecation")
	@Override
	public void run() {
		final double[] tps = TPS.getTPSArray();
		//for(final PlayerInfo target : VerifCPS.verifier.values()) {
		for(final Entry<UUID, Set<UUID>> entry : VerifPlayer.viewers.entrySet()) {
			final UUID uuid = entry.getKey();
			final PlayerInfo playerInfo = VerifPlayer.get(uuid);
			final Player player = Bukkit.getPlayer(playerInfo.getUniqueId());
			if(player == null) {
				return;
			}
			if(!player.isOnline()) {
				VerifPlayer.removePlayer(player);
				return;
			}
			final HashMap<Integer, ItemStack> items = new HashMap<>();
			ItemStack item;
			final List<String> lore = new ArrayList<>();
			int slot = VerifPlayer.slotTools;

			// Tête de target
			item = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
			final SkullMeta skullmeta = (SkullMeta) item.getItemMeta();
			if(VerifPlayer.is1_9) {
				skullmeta.setOwningPlayer(player);
			} else {
				skullmeta.setOwner(player.getName());
			}
			skullmeta.setDisplayName("§6" + player.getName());

			final Map<String, String> replace = new HashMap<>();

			if(player.isDead()) {
				replace.put("%heal%", Utils.color("&c✖ Dead"));
			} else {
				final StringBuilder heal = new StringBuilder();
				heal.append(String.valueOf((float) Math.round(player.getHealth()) / 2).replace("\\.?0*$", ""));
				heal.append(" ");
				heal.append(Utils.color("&c❤"));

				replace.put("%heal%", heal.toString());
			}
			replace.put("%food%", String.valueOf((float) Math.round(player.getFoodLevel()) / 2).replace("\\.?0*$", ""));
			replace.put("%xp_level%", String.valueOf(player.getLevel()));
			replace.put("%first_played%", Utils.timestampToDate(player.getFirstPlayed() / 1000L));
			replace.put("%last_played%", Utils.timestampToDateAndHour(player.getLastPlayed() / 1000L));

			if(player.isInsideVehicle()) {
				final Entity entity = player.getVehicle();
				replace.put("%vehicule%", entity.getName());
			} else {
				replace.put("%vehicule%", Utils.color("&c✖"));
			}
			final InventoryView openInventory = player.getOpenInventory();

			if(openInventory != null && openInventory.getType() != InventoryType.CRAFTING && openInventory.getType() != InventoryType.CREATIVE) {
				// TODO: Faire la différence entre Chest & Gui
				replace.put("%gui%", openInventory.getType().name());
			} else {
				replace.put("%gui%", Utils.color("&c✖"));
			}

			final List<String> infos = ConfigUtils.VERIFGUI_INFO.getStringList();
			Utils.replaceAll(infos, replace);

			skullmeta.setLore(infos);
			item.setItemMeta(skullmeta);

			items.put(slot++, item);

			// Effects
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
				name += "s";
				material = Material.POTION;
				for(final PotionEffect potionEffect : potionEffects) {
					// TODO: Effects en français
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

			// CPS
			Integer currentClick = playerInfo.getCurrentClicks();
			int glassPaneColor = VerifPlayer.getIntervalGlassPaneColor(currentClick, 10, 18);
			lore.add("");

			String nbAlert = ConfigUtils.VERIFGUI_NBALERT.getString();
			nbAlert = nbAlert.replaceAll("%s%", Utils.withOrWithoutS(playerInfo.getNbAlerts()));
			nbAlert = nbAlert.replaceAll("%alert_nb%", String.valueOf(playerInfo.getNbAlerts()));
			lore.add(nbAlert);

			String cpsMax = ConfigUtils.VERIFGUI_CPSMAX.getString();
			cpsMax = cpsMax.replaceAll("%cps_max%", String.valueOf(playerInfo.getMaxCPS()));
			lore.add(cpsMax);

			lore.add("");

			for(int i = playerInfo.getClicksAir().size() - 1; i >= 0; i--) {
				final int clickAir = playerInfo.getClicksAir().get(i);
				final int clickEntity = playerInfo.getClicksEntity().get(i);
				final int clickGlobal = clickAir + clickEntity;

				final ChatColor chatColor = VerifPlayer.getIntervalChatColor(clickGlobal, 10, 18);
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

			item = ItemTools.create(Material.STAINED_GLASS_PANE, currentClick, (byte) glassPaneColor, ConfigUtils.VERIFGUI_CPSITEMNAME.getString(), lore);
			lore.clear();

			items.put(slot++, item);

			// PING
			int ping = Reflection.getPing(player);

			glassPaneColor = VerifPlayer.getIntervalGlassPaneColor(ping, 100, 200);
			ChatColor chatColor = VerifPlayer.getIntervalChatColor(ping, 100, 200);

			lore.add("");
			lore.add(ConfigUtils.VERIFGUI_PINGFORMAT.getString().replaceAll("%color%", chatColor.toString()).replaceAll("%ping%", String.valueOf(ping)));
			if(ping == 0) {
				ping = 1;
			}
			lore.add("");
			lore.add(ConfigUtils.VERIFGUI_TPSITEMNAME.getString());
			lore.add("");

			for(int i = 0; i < tps.length; i++) {
				final double tpsDouble = tps[i];
				final int tpsInt = (int) Math.round(tpsDouble);
				if(tpsInt >= 18) {
					chatColor = ChatColor.GREEN;
				} else if(tpsInt >= 16) {
					chatColor = ChatColor.GOLD;
				} else {
					chatColor = ChatColor.RED;
				}

				String tpsFormat = ConfigUtils.VERIFGUI_TPSFORMAT.getString();
				final int time;
				switch(i) {
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
				tpsFormat = tpsFormat.replace("%color%", chatColor.toString());
				tpsFormat = tpsFormat.replace("%tps%", String.valueOf(tpsDouble));
				lore.add(tpsFormat);
			}

			item = ItemTools.create(Material.STAINED_GLASS_PANE, ping, (byte) glassPaneColor, ConfigUtils.VERIFGUI_PINGITEMNAME.getString(), lore);
			lore.clear();

			items.put(slot++, item);

			// TPS

			/*lore.add("");

			int tpsIntNow = 1;
			for(int i = 0; i < tps.length; i++) {
				final double tpsDouble = tps[i];
				final int tpsInt = (int) Math.round(tpsDouble);
				if(tpsInt >= 18) {
					chatColor = ChatColor.GREEN;
					glassPaneColor = 5;
				} else if(tpsInt >= 16) {
					chatColor = ChatColor.GOLD;
					glassPaneColor = 1;
				} else {
					chatColor = ChatColor.RED;
					glassPaneColor = 14;
				}

				String tpsFormat = ConfigUtils.VERIFGUI_TPSFORMAT.getString();
				final int time;
				switch(i) {
				case 0:
					time = 1;
					if(tpsInt == 0) {
						tpsIntNow = 1;
					} else {
						tpsIntNow = tpsInt;
					}
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
				tpsFormat = tpsFormat.replace("%color%", chatColor.toString());
				tpsFormat = tpsFormat.replace("%tps%", String.valueOf(tpsDouble));
				lore.add(tpsFormat);
			}
			item = ItemTools.create(Material.STAINED_GLASS_PANE, tpsIntNow, (byte) glassPaneColor, ConfigUtils.VERIFGUI_TPSITEMNAME.getString(), lore);

			lore.clear();
			items.put(slot++, item);*/

			final PlayerInventory targetInventory = player.getInventory();

			// Armor
			slot = VerifPlayer.slotArmor;
			final ItemStack[] armors = targetInventory.getArmorContents();
			ArrayUtils.reverse(armors);

			for(final ItemStack armorItem : armors) {
				items.put(slot++, armorItem);
			}

			// Inventaire
			int slotInv = VerifPlayer.slotInv;
			int slotHotbar = VerifPlayer.slotHotBar;

			for(final ItemStack itemStack : targetInventory.getContents()) {
				if(slotHotbar < VerifPlayer.slotHotBar + 9) {
					items.put(slotHotbar++, itemStack);
				} else if(slotInv < VerifPlayer.slotHotBar) {
					items.put(slotInv++, itemStack);
				}

			}

			if(VerifPlayer.is1_9) {
				items.put(slot, targetInventory.getItemInOffHand());
			}

			// Holding
			slot = VerifPlayer.slotHolding;
			final int heldSlot = targetInventory.getHeldItemSlot() + slot;
			for(int i1 = slot; slot + 9 > i1; i1++) {
				items.put(i1, ItemTools.create(Material.AIR));
			}
			items.put(heldSlot, ItemTools.create(Material.GHAST_TEAR, 1, "&ePlayer Holding"));

			final Set<UUID> viewers = entry.getValue();
			for(final UUID viewersUuid : viewers) {
				final Player viewer = Bukkit.getPlayer(viewersUuid);
				//final Inventory topInventory = viewer.getOpenInventory().getTopInventory();
				GuiTool guiTool = GuiTools.get(viewersUuid);
				
				if(guiTool.getGuiName() != GuiName.VERIF_PLAYER) {
					VerifPlayer.removeViewer(viewersUuid);
					return;
				}
				
				final InventoryView inventory = viewer.getOpenInventory();
				for(final Entry<Integer, ItemStack> entryItem : items.entrySet()) {
					if(entryItem.getValue() == null || !entryItem.getValue().equals(inventory.getItem(entryItem.getKey()))) {
						inventory.setItem(entryItem.getKey(), entryItem.getValue());
					}
				}
				
			}
		}
	}
}
