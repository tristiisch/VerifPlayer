package fr.tristiisch.verifplayer.core.freeze;

import java.util.List;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import fr.tristiisch.verifplayer.utils.SpigotUtils;
import fr.tristiisch.verifplayer.utils.config.ConfigGet;
import fr.tristiisch.verifplayer.utils.permission.Permission;

public class FreezeListener implements Listener {

	@EventHandler(ignoreCancelled = true)
	public void onEntityDamage(EntityDamageByEntityEvent event) {
		if (!(event.getEntity() instanceof Player)) {
			return;
		}
		Player victim = (Player) event.getEntity();
		if (Freeze.isFreeze(victim)) {

			if (event.getDamager() instanceof Player) {
				Player attacker = (Player) event.getDamager();
				attacker.sendMessage(ConfigGet.MESSAGES_FREEZE_PLAYERISFREEZE.getString().replace("%player%", victim.getName()));
			}
			event.setCancelled(true);
		}
		if (event.getDamager() instanceof Player) {

			Player attacker = (Player) event.getDamager();
			if (Freeze.isFreeze(attacker)) {
				event.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent event) {
		List<UUID> playersFreeze = Freeze.players;
		if (playersFreeze.isEmpty()) {
			return;
		}
		Player player = event.getPlayer();
		if (!Freeze.isFreeze(player) && !Permission.MODERATOR_SEEFREEZEMSG.hasPermission(player)) {
			event.getRecipients().removeIf(p -> p.getUniqueId() != player.getUniqueId() && playersFreeze.contains(p.getUniqueId()));
			return;
		}
		event.setFormat(SpigotUtils.color("&2Freeze &7» %s &r") + "%s");
		event.getRecipients().removeIf(p -> p.getUniqueId() != player.getUniqueId() && !Permission.MODERATOR_SEEFREEZEMSG.hasPermission(p));
	}

	@EventHandler(ignoreCancelled = true)
	public void onPlayerDropItem(PlayerDropItemEvent event) {
		Player player = event.getPlayer();
		if (Freeze.isFreeze(player)) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		if (!Freeze.isFreeze(player)) {
			return;
		}
		Location location = event.getFrom();
		Location locationTo = event.getTo();
		if (location.getX() != locationTo.getX() || location.getZ() != locationTo.getZ() || location.getY() != locationTo.getY()) {
			player.teleport(location);
		}
	}

	@EventHandler
	public void onPlayerQuitEvent(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		if (!Freeze.isFreeze(player)) {
			return;
		}
		Freeze.unfreeze(player);

		Permission.MODERATOR_RECEIVEFREEZEDISCONNECTED.sendMessageToOnlinePlayers(ConfigGet.MESSAGES_FREEZE_PLAYERDISCONNECTWHILEFREEZE.getString().replace("%player%", player.getName()));
	}

	@EventHandler(ignoreCancelled = true)
	public void onPlayerToggleSneak(PlayerToggleSneakEvent event) {
		Player player = event.getPlayer();
		if (Freeze.isFreeze(player)) {
			event.setCancelled(true);
		}
	}
}
