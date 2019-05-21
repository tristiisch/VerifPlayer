package fr.tristiisch.verifplayer.core.verifspec.listeners;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

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
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.google.common.base.Predicate;

import fr.tristiisch.verifplayer.Main;
import fr.tristiisch.verifplayer.core.freeze.Freeze;
import fr.tristiisch.verifplayer.core.vanish.Vanish;
import fr.tristiisch.verifplayer.core.verifgui.VerifGuiManager;
import fr.tristiisch.verifplayer.core.verifspec.VerifSpec;
import fr.tristiisch.verifplayer.core.verifspec.VerifSpecTool;
import fr.tristiisch.verifplayer.core.verifspec.teleport.SortByDistance;
import fr.tristiisch.verifplayer.playerinfo.PlayerInfo;
import fr.tristiisch.verifplayer.playerinfo.PlayersInfos;
import fr.tristiisch.verifplayer.utils.PlayerContents;
import fr.tristiisch.verifplayer.utils.SpigotUtils;

public class VerifSpecToolsListener implements Listener {

	@SuppressWarnings("deprecation")
	private static void targetPlayer(final Player attacker, final Player target) {
		final VerifSpecTool verifSpecItem = VerifSpecTool.getTool(attacker.getItemInHand());
		switch(verifSpecItem) {
		case FREEZE:
			if(Freeze.isFreeze(target)) {
				Freeze.unfreeze(target);
				attacker.sendMessage(SpigotUtils.color("&cVous avez unfreeze &4" + target.getName() + "&c."));
			} else {
				Freeze.freeze(target);
				attacker.sendMessage(SpigotUtils.color("&aVous avez freeze &2" + target.getName() + "&a."));
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

		if(attacker.getItemInHand().isSimilar(VerifSpecTool.KNOCKBACK.getItemStack())) {
			event.setCancelled(false);
			return;
		}

		event.setCancelled(true);

		if(victim instanceof Player) {
			final Player target = (Player) victim;
			VerifSpecToolsListener.targetPlayer(attacker, target);
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

		final VerifSpecTool verifSpecItem = VerifSpecTool.getTool(player.getItemInHand());

		switch(verifSpecItem) {

		default:
			event.setCancelled(true);

		case TELEPORTER:
			/*	for(final Player target : Bukkit.getOnlinePlayers().stream().filter(playerOnline -> !playerOnline.getUniqueId().equals(player.getUniqueId())).collect(Utils.toShuffledList())) {
				if(!VerifSpec.isIn(target) && !Vanish.isVanish(target)) {
					player.teleport(target);
					player.sendMessage(SpigotUtils.color("Vous avez été téléporté à " + target.getName() + "&a."));
					return;
				}
			}
			*/
			Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {

				final PlayerInfo playerInfos = PlayersInfos.getPlayerInfo(player);
				final List<Player> alreadyTeleportedPlayers = playerInfos.getAlreadyTeleportedPlayers();

				final Predicate<Player> predicate = playerOnline -> alreadyTeleportedPlayers
						.contains(playerOnline) && !playerOnline.getUniqueId().equals(player.getUniqueId()) && !VerifSpec.isIn(playerOnline) && !Vanish.isVanish(playerOnline);

				final Location spawn = Bukkit.getWorlds().get(0).getSpawnLocation();

				final Player target = Bukkit.getOnlinePlayers().stream().filter(predicate).sorted(new SortByDistance(spawn)).findFirst().orElse(null);

				if(target == null) {
					player.sendMessage(SpigotUtils.color("Il n'y a aucun joueurs."));
				} else {
					player.teleport(target);
					player.sendMessage(SpigotUtils.color("Vous avez été téléporté à " + target.getName() + "&a."));
					alreadyTeleportedPlayers.add(target);
					playerInfos.setAlreadyTeleportedPlayers(alreadyTeleportedPlayers);
				}
			});
			break;

		case SHUTTLE:
			final Block block = player.getTargetBlock(new HashSet<>(Arrays.asList(Material.AIR)), 1000);
			if(block == null) {
				player.sendMessage(SpigotUtils.color("La distance est trop loin."));
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
			player.sendMessage(SpigotUtils.color("Il n'y a pas de joueur target."));
			break;

		case KNOCKBACK:
			event.setCancelled(false);
			player.sendMessage(SpigotUtils.color("Il n'y a pas de joueur target."));
			break;
		}
	}

	@EventHandler
	public void onPlayerInteractAtEntity(final PlayerInteractAtEntityEvent event) {
		final Player player = event.getPlayer();
		if(VerifSpec.isIn(player)) {
			event.setCancelled(true);
			if(event.getRightClicked() instanceof Player) {
				final Player target = (Player) event.getRightClicked();
				VerifSpecToolsListener.targetPlayer(player, target);
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