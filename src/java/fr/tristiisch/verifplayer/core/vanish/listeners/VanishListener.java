package fr.tristiisch.verifplayer.core.vanish.listeners;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.potion.PotionEffectType;

import fr.tristiisch.verifplayer.VerifPlayerPlugin;
import fr.tristiisch.verifplayer.core.vanish.VanishHandler;

public class VanishListener implements Listener {

	/*
	 * @EventHandler(ignoreCancelled = true) public void onBlockCanBuildEvent(final
	 * BlockCanBuildEvent event) { if(event.isBuildable()) { return; } final
	 * Location location = event.getBlock().getLocation(); long count =
	 * Bukkit.getOnlinePlayers().stream().filter(vanished ->
	 * SpigotUtils.playerisIn(vanished, location)).count();
	 *
	 * event.getBlock();
	 *
	 * //event.setBuildable(true); }
	 */

	@EventHandler(ignoreCancelled = true)
	public void onEntityDamage(EntityDamageEvent event) {
		if (!(event.getEntity() instanceof Player) || event.getCause() != DamageCause.MAGIC) {
			return;
		}
		Player player = (Player) event.getEntity();
		if (VerifPlayerPlugin.getInstance().getVanishHandler().isVanished(player)) {
			event.setCancelled(true);
		}
	}

	@EventHandler(ignoreCancelled = true)
	public void onEntityTarget(EntityTargetEvent event) {
		if (!(event.getTarget() instanceof Player)) {
			return;
		}
		Player player = (Player) event.getTarget();
		if (VerifPlayerPlugin.getInstance().getVanishHandler().isVanished(player)) {
			event.setCancelled(true);
		}
	}

	@EventHandler(ignoreCancelled = true)
	public void onPlayerDropItem(PlayerDropItemEvent event) {
		Player player = event.getPlayer();
		if (VerifPlayerPlugin.getInstance().getVanishHandler().isVanished(player)) {
			event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onPlayerInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		if (!VerifPlayerPlugin.getInstance().getVanishHandler().isVanished(player)) {
			return;
		}

		if (event.getAction() == Action.PHYSICAL && event.getClickedBlock().getType() == Material.SOIL) {
			event.setCancelled(true);
		} else if (!player.isSneaking() && event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			Block block = event.getClickedBlock();
			Inventory inventory = null;
			BlockState blockState = block.getState();
			switch (block.getType()) {
			case TRAPPED_CHEST:
			case CHEST: {
				Chest chest = (Chest) blockState;
				inventory = chest.getInventory();
				break;
			}
			case ENDER_CHEST: {
				inventory = player.getEnderChest();
				break;
			}
			default:
				return;
			}
			event.setCancelled(true);
			player.openInventory(inventory);
		}
	}

	@SuppressWarnings("deprecation")
	@EventHandler(ignoreCancelled = true)
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		player.getActivePotionEffects().removeIf(p -> p.getType() == PotionEffectType.INVISIBILITY && p.getDuration() == 0);
		VerifPlayerPlugin.getInstance().getVanishHandler().getVanished().forEach(vanishPlayer -> player.hidePlayer(vanishPlayer));
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		VanishHandler vanishHandler = VerifPlayerPlugin.getInstance().getVanishHandler();
		if (vanishHandler.isVanished(player)) {
			event.setQuitMessage(null);
			vanishHandler.removeVanishMetadata(player);
		}
	}

	@EventHandler(ignoreCancelled = true)
	public void onPotionSplash(PotionSplashEvent event) {
		for (LivingEntity entity : event.getAffectedEntities()) {
			if (!(entity instanceof Player)) {
				continue;
			}

			Player player = (Player) entity;
			if (VerifPlayerPlugin.getInstance().getVanishHandler().isVanished(player)) {
				event.setIntensity(player, 0);
			}
		}
	}
}
