package fr.tristiisch.verifplayer.core.verifspec.listeners;

import java.util.Arrays;
import java.util.HashSet;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import fr.tristiisch.verifplayer.VerifPlayerPlugin;
import fr.tristiisch.verifplayer.core.freeze.Freeze;
import fr.tristiisch.verifplayer.core.verifspec.VerifSpec;
import fr.tristiisch.verifplayer.core.verifspec.VerifSpecTool;
import fr.tristiisch.verifplayer.core.verifspec.teleport.Teleporter;
import fr.tristiisch.verifplayer.utils.VersionUtils.ServerVersion;
import fr.tristiisch.verifplayer.utils.config.ConfigGet;

public class VerifSpecToolsListener implements Listener {

	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.HIGH)
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
		if (!(event.getDamager() instanceof Player)) {
			return;
		}

		Player attacker = (Player) event.getDamager();
		if (attacker.getItemInHand() == null) {
			return;
		}

		if (!VerifSpec.isIn(attacker)) {
			return;
		}
		event.setDamage(0);

		VerifSpecTool verifSpecItem = VerifSpecTool.getTool(attacker.getItemInHand());
		if (verifSpecItem == null || verifSpecItem != VerifSpecTool.KNOCKBACK) {
			event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerDropItem(PlayerDropItemEvent event) {
		Player player = event.getPlayer();

		if (!VerifSpec.isIn(player)) {
			return;
		}

		VerifSpecTool verifSpecItem = VerifSpecTool.getTool(event.getItemDrop().getItemStack());
		if (verifSpecItem == null) {
			return;
		}

		if (verifSpecItem == VerifSpecTool.SPEED) {
			event.setCancelled(true);

			PotionEffect potionEffect = player.getPotionEffect(PotionEffectType.SPEED);
			if (potionEffect == null) {
				potionEffect = player.getPotionEffect(PotionEffectType.SLOW);
			}

			if (potionEffect != null) {
				player.sendMessage(ConfigGet.MESSAGES_VERIFSPEC_TOOLSPEEDNORMAL.getString());
				player.removePotionEffect(potionEffect.getType());
			}
		}

	}

	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerInteract(PlayerInteractEvent event) {
		if (ServerVersion.V1_9.isEqualOrOlder()) {
			EquipmentSlot equipementSlot = event.getHand();
			if (equipementSlot == null || !equipementSlot.equals(EquipmentSlot.HAND)) {
				return;
			}
		}
		Player player = event.getPlayer();

		if (!VerifSpec.isIn(player)) {
			return;
		}

		VerifSpecTool verifSpecItem = VerifSpecTool.getTool(player.getItemInHand());

		if (verifSpecItem == null) {
			return;
		}

		event.setCancelled(true);
		switch (verifSpecItem) {

		case TELEPORTER:
			/*
			 * for(Player target : Bukkit.getOnlinePlayers().stream().filter(playerOnline ->
			 * !playerOnline.getUniqueId().equals(player.getUniqueId())).collect(Utils.
			 * toShuffledList())) { if(!VerifSpec.isIn(target) && !Vanish.isVanish(target))
			 * { player.teleport(target);
			 * player.sendMessage(SpigotUtils.color("Vous avez été téléporté à " +
			 * target.getName() + "&a.")); return; } }
			 */
			Bukkit.getScheduler().runTaskAsynchronously(VerifPlayerPlugin.getInstance(), () -> {
				Teleporter.tp(player);
			});
			break;

		case SHUTTLE:
			Block block = player.getTargetBlock(new HashSet<>(Arrays.asList(Material.AIR)), 1000);
			if (block == null) {
				player.sendMessage(ConfigGet.MESSAGES_VERIFSPEC_DISTANCETOOFAR.getString());
				return;
			}
			Location playerLocation = player.getLocation();
			Location location = new Location(block.getWorld(), block.getX(), block.getY(), block.getZ(), playerLocation.getYaw(), playerLocation.getPitch());
			Block bxn = location.add(-1.0, 0.0, 0.0).getBlock();
			Block bxn2 = location.add(-0.5, 0.0, 0.0).getBlock();
			Block bxp = location.add(1.0, 0.0, 0.0).getBlock();
			Block bxp2 = location.add(0.5, 0.0, 0.0).getBlock();
			Block bzn = location.add(0.0, 0.0, -1.0).getBlock();
			Block bzn2 = location.add(0.0, 0.0, -0.5).getBlock();
			Block bzp = location.add(0.0, 0.0, 1.0).getBlock();
			Block bzp2 = location.add(0.0, 0.0, 0.5).getBlock();
			Block by = location.getBlock();
			Block by2 = location.add(0.0, 0.5, 0.0).getBlock();
			if (!by2.getType().equals(Material.AIR)) {
				for (boolean isOnLand = false; !isOnLand; isOnLand = true) {
					player.teleport(location.add(location.getDirection().multiply(-1.0)));
					if (by2.getType() != Material.AIR) {
						player.teleport(location.add(location.getDirection().multiply(-1.0)));
					}
				}
			}
			if (!by.getType().equals(Material.AIR)) {
				player.teleport(location.add(0.0, 0.5, 0.0));
			}
			if (!bxn.getType().equals(Material.AIR) || !bxn2.getType().equals(Material.AIR)) {
				player.teleport(location.add(0.5, 0.0, 0.0));
			}
			if (!bxp.getType().equals(Material.AIR) || !bxp2.getType().equals(Material.AIR)) {
				player.teleport(location.add(-0.5, 0.0, 0.0));
			}
			if (!bzn.getType().equals(Material.AIR) || !bzn2.getType().equals(Material.AIR)) {
				player.teleport(location.add(0.0, 0.0, 0.5));
			}
			if (!bzp.getType().equals(Material.AIR) || !bzp2.getType().equals(Material.AIR)) {
				player.teleport(location.add(0.0, 0.0, -0.5));
			}
			break;

		case SPEED:

			if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {

				boolean positif = true;
				int amplifier = 0;
				PotionEffect potionEffect = player.getPotionEffect(PotionEffectType.SPEED);
				if (potionEffect == null) {
					potionEffect = player.getPotionEffect(PotionEffectType.SLOW);
					if (potionEffect != null) {
						amplifier = potionEffect.getAmplifier();
						if (amplifier >= 39) {
							amplifier -= 20;
						} else if (amplifier >= 9) {
							amplifier -= 10;
						} else {
							amplifier--;
						}
						player.removePotionEffect(potionEffect.getType());
						if (amplifier >= 0) {
							positif = false;
							player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, amplifier, false, false), true);
						} else {
							player.sendMessage(ConfigGet.MESSAGES_VERIFSPEC_TOOLSPEEDNORMAL.getString());
							break;
						}
					} else {
						player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, amplifier, false, false), true);
					}

				} else {
					amplifier = potionEffect.getAmplifier();
					if (amplifier >= 39) {
						amplifier += 20;
					} else if (amplifier >= 9) {
						amplifier += 10;
					} else {
						amplifier++;
					}
					player.removePotionEffect(potionEffect.getType());
					player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, amplifier, false, false), true);
				}

				player.sendMessage(ConfigGet.MESSAGES_VERIFSPEC_TOOLSPEED.getString().replace("%speed%", (positif ? "" : "-") + String.valueOf(amplifier + 1)).replace("%color%", String.valueOf(ChatColor.GREEN)));

			} else {
				boolean positif = false;
				int amplifier = 0;
				PotionEffect potionEffect = player.getPotionEffect(PotionEffectType.SLOW);
				if (potionEffect == null) {
					potionEffect = player.getPotionEffect(PotionEffectType.SPEED);
					if (potionEffect != null) {
						amplifier = potionEffect.getAmplifier();
						if (amplifier >= 39) {
							amplifier -= 20;
						} else if (amplifier >= 19) {
							amplifier -= 10;
						} else {
							amplifier--;
						}
						player.removePotionEffect(potionEffect.getType());
						if (amplifier >= 0) {
							positif = true;
							player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, amplifier, false, false), true);
						} else {
							player.sendMessage(ConfigGet.MESSAGES_VERIFSPEC_TOOLSPEEDNORMAL.getString());
							break;
						}
					} else {
						player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, amplifier, false, false), true);
					}

				} else {
					amplifier = potionEffect.getAmplifier();
					if (amplifier >= 39) {
						amplifier += 20;
					} else if (amplifier >= 19) {
						amplifier += 10;
					} else {
						amplifier++;
					}
					player.removePotionEffect(potionEffect.getType());
					player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, amplifier, false, false), true);
				}

				player.sendMessage(ConfigGet.MESSAGES_VERIFSPEC_TOOLSPEED.getString().replace("%speed%", (positif ? "" : "-") + String.valueOf(amplifier + 1)).replace("%color%", String.valueOf(ChatColor.RED)));
			}
			break;
		default:
			break;
		}

	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlayerInteractAtEntity(PlayerInteractAtEntityEvent event) {
		if (ServerVersion.V1_9.isEqualOrOlder()) {
			EquipmentSlot equipementSlot = event.getHand();
			if (!equipementSlot.equals(EquipmentSlot.HAND)) {
				return;
			}
		}

		Player attacker = event.getPlayer();
		if (!(VerifSpec.isIn(attacker) && event.getRightClicked() instanceof Player)) {
			return;
		}
		Player target = (Player) event.getRightClicked();

		VerifSpecTool verifSpecItem = VerifSpecTool.getTool(attacker.getItemInHand());
		switch (verifSpecItem) {

		case FREEZE:
			event.setCancelled(true);
			if (Freeze.isFreeze(target)) {
				Freeze.unfreeze(target);
				attacker.sendMessage(ConfigGet.MESSAGES_FREEZE_PLAYERUNFREEZE.getString().replace("%player%", target.getName()));
			} else {
				Freeze.freeze(target);
				attacker.sendMessage(ConfigGet.MESSAGES_FREEZE_PLAYERFREEZE.getString().replace("%player%", target.getName()));
			}
			break;
		case VERIF:
			event.setCancelled(true);
			VerifPlayerPlugin.getInstance().getVerifGuiHandler().openVerifGUi(attacker, target);
			break;
		default:
			break;

		}
	}

}
