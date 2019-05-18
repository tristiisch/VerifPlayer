package fr.tristiisch.verifplayer.vanish;

import org.bukkit.Material;
import org.bukkit.block.Beacon;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.BrewingStand;
import org.bukkit.block.Chest;
import org.bukkit.block.Dispenser;
import org.bukkit.block.Dropper;
import org.bukkit.block.Furnace;
import org.bukkit.block.Hopper;
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

public class VanishListener implements Listener {

	@EventHandler(ignoreCancelled = true)
	public void onEntityDamage(final EntityDamageEvent event) {
		if(!(event.getEntity() instanceof Player) || event.getCause() != DamageCause.MAGIC) {
			return;
		}
		final Player player = (Player) event.getEntity();
		if(Vanish.isVanish(player)) {
			event.setCancelled(true);
		}
	}

	@EventHandler(ignoreCancelled = true)
	public void onEntityTarget(final EntityTargetEvent event) {
		if(!(event.getTarget() instanceof Player)) {
			return;
		}
		final Player player = (Player) event.getTarget();
		if(Vanish.isVanish(player)) {
			event.setCancelled(true);
		}
	}

	@EventHandler(ignoreCancelled = true)
	public void onPlayerDropItem(final PlayerDropItemEvent event) {
		final Player player = event.getPlayer();
		if(Vanish.isVanish(player)) {
			event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onPlayerInteract(final PlayerInteractEvent event) {
		if(event.isCancelled()) {
			return;
		}
		final Player player = event.getPlayer();
		if(!Vanish.isVanish(player)) {
			return;
		}
		if(!player.isSneaking() && event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			final Block block = event.getClickedBlock();
			Inventory inventory = null;
			final BlockState blockState = block.getState();
			switch(block.getType()) {
			case TRAPPED_CHEST:
			case CHEST: {
				final Chest chest = (Chest) blockState;
				inventory = chest.getInventory();
				break;
			}
			case ENDER_CHEST: {
				inventory = player.getEnderChest();
				break;
			}
			case DISPENSER: {
				inventory = ((Dispenser) blockState).getInventory();
				break;
			}
			case HOPPER: {
				inventory = ((Hopper) blockState).getInventory();
				break;
			}
			case DROPPER: {
				inventory = ((Dropper) blockState).getInventory();
				break;
			}
			case FURNACE: {
				inventory = ((Furnace) blockState).getInventory();
				break;
			}
			case BREWING_STAND: {
				inventory = ((BrewingStand) blockState).getInventory();
				break;
			}
			case BEACON: {
				inventory = ((Beacon) blockState).getInventory();
				break;
			}
			default:
				break;
			}
			if(inventory != null) {
				event.setCancelled(true);
				player.openInventory(inventory);
				return;
			}
		}
		if(event.getAction() == Action.PHYSICAL && event.getClickedBlock().getType() == Material.SOIL) {
			event.setCancelled(true);
		}
	}

	@SuppressWarnings("deprecation")
	@EventHandler(ignoreCancelled = true)
	public void onPlayerJoin(final PlayerJoinEvent event) {
		final Player player = event.getPlayer();
		player.getActivePotionEffects().removeIf(p -> p.getType() == PotionEffectType.INVISIBILITY && p.getDuration() == 0);
		for(final Player vanish : Vanish.getVanish()) {
			player.hidePlayer(vanish);
		}
	}

	@EventHandler
	public void onPlayerQuit(final PlayerQuitEvent event) {
		final Player player = event.getPlayer();
		if(Vanish.isVanish(player)) {
			event.setQuitMessage(null);
			player.removePotionEffect(PotionEffectType.INVISIBILITY);
			Vanish.removeVanish(player);
		}
	}

	@EventHandler(ignoreCancelled = true)
	public void onPotionSplash(final PotionSplashEvent event) {
		for(final LivingEntity entity : event.getAffectedEntities()) {
			if(!(entity instanceof Player)) {
				continue;
			}

			final Player player = (Player) entity;
			if(Vanish.isVanish(player)) {
				event.setIntensity(player, 0);
			}
		}
	}
}
