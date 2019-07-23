package fr.tristiisch.verifplayer.core.verifspec.listeners;

import java.util.Arrays;
import java.util.HashSet;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EquipmentSlot;

import fr.tristiisch.verifplayer.VerifPlayerPlugin;
import fr.tristiisch.verifplayer.core.freeze.Freeze;
import fr.tristiisch.verifplayer.core.verifspec.VerifSpec;
import fr.tristiisch.verifplayer.core.verifspec.VerifSpecTool;
import fr.tristiisch.verifplayer.core.verifspec.teleport.Teleporter;
import fr.tristiisch.verifplayer.utils.PlayerContents;
import fr.tristiisch.verifplayer.utils.VersionUtils.ServerVersion;
import fr.tristiisch.verifplayer.utils.config.ConfigGet;

public class VerifSpecToolsListener implements Listener {

	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.HIGH)
	public void onEntityDamageByEntity(final EntityDamageByEntityEvent event) {
		if(!(event.getDamager() instanceof Player)) {
			return;
		}

		if(!(event.getEntity() instanceof Player)) {
			return;
		}

		final Player attacker = (Player) event.getDamager();
		/*final Player target = (Player) event.getEntity();*/
		if(attacker.getItemInHand() == null) {
			return;
		}

		if(!VerifSpec.isIn(attacker)) {
			return;
		}

		final VerifSpecTool verifSpecItem = VerifSpecTool.getTool(attacker.getItemInHand());

		switch(verifSpecItem) {
		case FREEZE:
			/*			if(Freeze.isFreeze(target)) {
							Freeze.unfreeze(target);
							attacker.sendMessage(ConfigGet.MESSAGES_FREEZE_PLAYERUNFREEZE.getString().replace("%player%", target.getName()));
						} else {
							Freeze.freeze(target);
							attacker.sendMessage(ConfigGet.MESSAGES_FREEZE_PLAYERFREEZE.getString().replace("%player%", target.getName()));
						}*/
			event.setCancelled(true);
			break;
		case VERIF:
			/*VerifPlayerPlugin.getInstance().getVerifGuiHandler().openVerifGUi(attacker, target);*/
			event.setCancelled(true);
			break;
		case KNOCKBACK:
			break;
		default:
			event.setCancelled(true);
			break;

		}
	}

	@SuppressWarnings({ "deprecation" })
	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerInteract(final PlayerInteractEvent event) {
		if(ServerVersion.V1_9.isEqualOrOlder()) {
			final EquipmentSlot equipementSlot = event.getHand();
			if(!equipementSlot.equals(EquipmentSlot.HAND)) {
				return;
			}
		}
		final Player player = event.getPlayer();
		if(player.getItemInHand() == null) {
			return;
		}

		if(!VerifSpec.isIn(player)) {
			return;
		}

		final VerifSpecTool verifSpecItem = VerifSpecTool.getTool(player.getItemInHand());

		if(verifSpecItem == null) {
			return;
		}

		event.setCancelled(true);
		switch(verifSpecItem) {

		case TELEPORTER:
			/*	for(final Player target : Bukkit.getOnlinePlayers().stream().filter(playerOnline -> !playerOnline.getUniqueId().equals(player.getUniqueId())).collect(Utils.toShuffledList())) {
				if(!VerifSpec.isIn(target) && !Vanish.isVanish(target)) {
					player.teleport(target);
					player.sendMessage(SpigotUtils.color("Vous avez été téléporté à " + target.getName() + "&a."));
					return;
				}
			}
			*/
			Bukkit.getScheduler().runTaskAsynchronously(VerifPlayerPlugin.getInstance(), () -> {
				Teleporter.tp(player);
			});
			break;

		case SHUTTLE:
			final Block block = player.getTargetBlock(new HashSet<>(Arrays.asList(Material.AIR)), 1000);
			if(block == null) {
				player.sendMessage(ConfigGet.MESSAGES_VERIFSPEC_DISTANCETOOFAR.getString());
				return;
			}
			final Location playerLocation = player.getLocation();
			final Location location = new Location(block.getWorld(), block.getX(), block.getY(), block.getZ(), playerLocation.getYaw(), playerLocation.getPitch());
			final Block bxn = location.add(-1.0, 0.0, 0.0).getBlock();
			final Block bxn2 = location.add(-0.5, 0.0, 0.0).getBlock();
			final Block bxp = location.add(1.0, 0.0, 0.0).getBlock();
			final Block bxp2 = location.add(0.5, 0.0, 0.0).getBlock();
			final Block bzn = location.add(0.0, 0.0, -1.0).getBlock();
			final Block bzn2 = location.add(0.0, 0.0, -0.5).getBlock();
			final Block bzp = location.add(0.0, 0.0, 1.0).getBlock();
			final Block bzp2 = location.add(0.0, 0.0, 0.5).getBlock();
			final Block by = location.getBlock();
			final Block by2 = location.add(0.0, 0.5, 0.0).getBlock();
			if(!by2.getType().equals(Material.AIR)) {
				for(boolean isOnLand = false; !isOnLand; isOnLand = true) {
					player.teleport(location.add(location.getDirection().multiply(-1.0)));
					if(by2.getType() != Material.AIR) {
						player.teleport(location.add(location.getDirection().multiply(-1.0)));
					}
				}
			}
			if(!by.getType().equals(Material.AIR)) {
				player.teleport(location.add(0.0, 0.5, 0.0));
			}
			if(!bxn.getType().equals(Material.AIR) || !bxn2.getType().equals(Material.AIR)) {
				player.teleport(location.add(0.5, 0.0, 0.0));
			}
			if(!bxp.getType().equals(Material.AIR) || !bxp2.getType().equals(Material.AIR)) {
				player.teleport(location.add(-0.5, 0.0, 0.0));
			}
			if(!bzn.getType().equals(Material.AIR) || !bzn2.getType().equals(Material.AIR)) {
				player.teleport(location.add(0.0, 0.0, 0.5));
			}
			if(!bzp.getType().equals(Material.AIR) || !bzp2.getType().equals(Material.AIR)) {
				player.teleport(location.add(0.0, 0.0, -0.5));
			}
			break;

		default:
			break;
		}

	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlayerInteractAtEntity(final PlayerInteractAtEntityEvent event) {
		if(ServerVersion.V1_9.isEqualOrOlder()) {
			final EquipmentSlot equipementSlot = event.getHand();
			if(!equipementSlot.equals(EquipmentSlot.HAND)) {
				return;
			}
		}

		final Player attacker = event.getPlayer();
		if(!VerifSpec.isIn(attacker) && !(event.getRightClicked() instanceof Player)) {
			return;
		}
		final Player target = (Player) event.getRightClicked();

		final VerifSpecTool verifSpecItem = VerifSpecTool.getTool(attacker.getItemInHand());
		switch(verifSpecItem) {

		case FREEZE:
			event.setCancelled(true);
			if(Freeze.isFreeze(target)) {
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

	@EventHandler
	public void onPlayerQuit(final PlayerQuitEvent event) {
		final Player player = event.getPlayer();
		if(VerifSpec.isIn(player)) {
			VerifSpec.disable(player);
		}
	}

	@EventHandler
	public void PlayerJoinEvent(final PlayerJoinEvent event) {
		final Player player = event.getPlayer();
		final PlayerContents playerContents = PlayerContents.fromDisk(player);
		if(!playerContents.hasData()) {
			return;
		}

		playerContents.returnHisInventory();
	}
}
