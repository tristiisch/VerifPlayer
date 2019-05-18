package fr.tristiisch.verifplayer.verifspec.listeners;

import java.util.Arrays;
import java.util.HashSet;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import fr.tristiisch.verifplayer.freeze.Freeze;
import fr.tristiisch.verifplayer.utils.SpigotUtils;
import fr.tristiisch.verifplayer.utils.Utils;
import fr.tristiisch.verifplayer.vanish.Vanish;
import fr.tristiisch.verifplayer.verifgui.VerifGuiManager;
import fr.tristiisch.verifplayer.verifspec.VerifSpec;
import fr.tristiisch.verifplayer.verifspec.VerifSpecItem;

public class VerifSpecListener implements Listener {

	@SuppressWarnings("deprecation")
	private static void targetPlayer(final Player attacker, final Player target) {
		final VerifSpecItem verifSpecItem = VerifSpecItem.getItem(attacker.getItemInHand());
		switch(verifSpecItem) {
		case FREEZE:
			if(Freeze.isFreeze(target)) {
				Freeze.unfreeze(target);
				attacker.sendMessage(Utils.color("&cVous avez unfreeze &4" + target.getName() + "&c."));
			} else {
				Freeze.freeze(target);
				attacker.sendMessage(Utils.color("&aVous avez freeze &2" + target.getName() + "&a."));
			}
			break;
		case VERIF:
			VerifGuiManager.openVerifGUi(attacker, target);
			break;
		default:
			break;

		}
	}

	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.HIGH)
	public void onEntityDamageByEntity(final EntityDamageByEntityEvent event) {
		if(!(event.getDamager() instanceof Player)) {
			return;
		}

		final Player attacker = (Player) event.getDamager();
		if(attacker.getItemInHand() == null) {
			return;
		}

		if(!VerifSpec.isIn(attacker)) {
			return;
		}
		final Entity victim = event.getEntity();

		if(attacker.getItemInHand().isSimilar(VerifSpecItem.KNOCKBACK.getItemStack())) {
			event.setCancelled(false);
			return;
		}

		event.setCancelled(true);

		if(victim instanceof Player) {
			final Player target = (Player) victim;
			VerifSpecListener.targetPlayer(attacker, target);
		}

	}

	@SuppressWarnings({ "deprecation" })
	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerInteract(final PlayerInteractEvent event) {
		final Player player = event.getPlayer();
		if(player.getItemInHand() == null) {
			return;
		}

		if(!VerifSpec.isIn(player)) {
			return;
		}

		final VerifSpecItem verifSpecItem = VerifSpecItem.getItem(player.getItemInHand());

		switch(verifSpecItem) {

		default:
			event.setCancelled(true);

		case TELEPORTER:
			for(final Player target : Bukkit.getOnlinePlayers().stream().collect(Utils.toShuffledList())) {
				if(!SpigotUtils.isSamePlayer(player, target) || !VerifSpec.isIn(target) && !Vanish.isVanish(target)) {
					player.teleport(target);
					player.sendMessage(Utils.color("Vous avez été téléporté à " + target.getName() + "&a."));
					return;
				}
			}
			player.sendMessage(Utils.color("Il n'y a aucun joueurs."));
			break;

		case SHUTTLE:
			final Block block = player.getTargetBlock(new HashSet<>(Arrays.asList(Material.AIR)), 1000);
			if(block == null) {
				player.sendMessage(Utils.color("La distance est trop loin."));
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

		case FREEZE:
			player.sendMessage(Utils.color("Il n'y a pas de joueur target."));
			break;

		case KNOCKBACK:
			event.setCancelled(false);
			player.sendMessage(Utils.color("Il n'y a pas de joueur target."));
			break;
		}
	}

	/*	@EventHandler
		public void PlayerJoinEvent(final PlayerJoinEvent event) {


			 * final FileConfiguration config =
			 * CustomConfigUtils.getConfig(EmeraldSpigot.getInstance(), "data");
			 * config.getString(player.getUniqueId().toString() +
			 * ".inventaire");(player.getUniqueId().toString() + ".inventaire", datas);

		}*/

	@EventHandler
	public void onPlayerInteractAtEntity(final PlayerInteractAtEntityEvent event) {
		final Player player = event.getPlayer();
		if(VerifSpec.isIn(player)) {
			event.setCancelled(true);
			if(event.getRightClicked() instanceof Player) {
				final Player target = (Player) event.getRightClicked();
				VerifSpecListener.targetPlayer(player, target);
			}
		}
	}

	@EventHandler
	public void onPlayerQuit(final PlayerQuitEvent event) {
		final Player player = event.getPlayer();
		if(VerifSpec.isIn(player)) {
			VerifSpec.disable(player);
		}
	}
}
