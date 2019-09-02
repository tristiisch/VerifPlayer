package fr.tristiisch.verifplayer.core.freeze;

import java.util.List;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
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
	public void onEntityDamage(final EntityDamageByEntityEvent event) {
		if (!(event.getEntity() instanceof Player)) {
			return;
		}
		final Player victim = (Player) event.getEntity();
		if (Freeze.isFreeze(victim)) {

			if (event.getDamager() instanceof Player) {
				final Player attacker = (Player) event.getDamager();
				attacker.sendMessage(ConfigGet.MESSAGES_FREEZE_PLAYERISFREEZE.getString().replace("%player%", victim.getName()));
			}
			event.setCancelled(true);
		}
		if (event.getDamager() instanceof Player) {

			final Player attacker = (Player) event.getDamager();
			if (Freeze.isFreeze(attacker)) {
				event.setCancelled(true);
			}
		}
	}

	// TODO 1.9 compa + other event (like tchat)

	@EventHandler
	public void onPlayerChat(final AsyncPlayerChatEvent event) {
		List<UUID> playersFreeze = Freeze.players;
		if (playersFreeze.isEmpty()) {
			return;
		}

		Player player = event.getPlayer();
		if (!Freeze.isFreeze(player)) {
			event.getRecipients().removeIf(p -> playersFreeze.contains(p.getUniqueId()));
			return;
		}

		event.setFormat(SpigotUtils.color("&2Freeze &7» %s &r%s"));
		event.getRecipients().removeIf(p -> !Permission.MODERATOR_COMMAND_FREEZE.hasPermission(p));
	}

	@EventHandler(ignoreCancelled = true)
	public void onEntityPickupItem(final EntityPickupItemEvent event) {
		if (event.getEntity() instanceof Player) {
			final Player player = (Player) event.getEntity();
			if (Freeze.isFreeze(player)) {
				event.setCancelled(true);
			}
		}
	}

	@EventHandler(ignoreCancelled = true)
	public void onPlayerDropItem(final PlayerDropItemEvent event) {
		final Player player = event.getPlayer();
		if (Freeze.isFreeze(player)) {
			event.setCancelled(true);
		}
	}

	@EventHandler(ignoreCancelled = true)
	public void onPlayerToggleSneak(final PlayerToggleSneakEvent event) {
		final Player player = event.getPlayer();
		if (Freeze.isFreeze(player)) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onPlayerMoveEvent(final PlayerMoveEvent event) {
		if (event.isCancelled()) {
			return;
		}
		final Player player = event.getPlayer();
		if (!Freeze.isFreeze(player)) {
			return;
		}
		// TODO 1.8/1.7 comptability
		// player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 0);
		player.teleport(event.getFrom());
	}

	@EventHandler
	public void onPlayerQuitEvent(final PlayerQuitEvent event) {
		final Player player = event.getPlayer();
		if (!Freeze.isFreeze(player)) {
			return;
		}
		Freeze.unfreeze(player);

		Permission.MODERATOR_RECEIVEFREEZEDISCONNECTED.sendMessageToOnlinePlayers(ConfigGet.MESSAGES_FREEZE_PLAYERDISCONNECTWHILEFREEZE.getString().replace("%player%", player.getName()));
	}
}
