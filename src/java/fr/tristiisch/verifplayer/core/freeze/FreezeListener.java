package fr.tristiisch.verifplayer.core.freeze;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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
		if (!(event.getEntity() instanceof Player))
			return;
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
			if (Freeze.isFreeze(attacker))
				event.setCancelled(true);
		}
	}

	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent event) {
		Map<UUID, List<Player>> playersFreeze = Freeze.players;
		if (playersFreeze.isEmpty())
			return;
		Player player = event.getPlayer();
		boolean isFrozen = playersFreeze.containsKey(player.getUniqueId());
		if (isFrozen) {
			event.getRecipients().removeIf(p -> !p.getUniqueId().equals(player.getUniqueId()) && (!playersFreeze.get(player.getUniqueId()).contains(p) || !Permission.MODERATOR_SEEFREEZEMSG.hasPermission(p)));
			event.setFormat(SpigotUtils.color("&aFreeze &7» %s &r") + "%s");
		} else if (Permission.MODERATOR_SEEFREEZEMSG.hasPermission(player)) {
			Entry<UUID, List<Player>> entry = playersFreeze.entrySet().stream().filter(e -> e.getValue().contains(player)).findFirst().orElse(null);
			event.getRecipients().removeIf(p -> !p.getUniqueId().equals(entry.getKey()) && (!entry.getValue().contains(p) || !Permission.MODERATOR_SEEFREEZEMSG.hasPermission(p)));
			event.setFormat(SpigotUtils.color("&4Freeze &7» %s &r") + "%s");
		}
		//		if (!isFrozen || !Permission.MODERATOR_SEEFREEZEMSG.hasPermission(player)) {
		//			event.getRecipients().removeIf(p -> p.getUniqueId() != player.getUniqueId() && playersFreeze.contains(p.getUniqueId()));
		//			return;
		//		}
	}

	@EventHandler(ignoreCancelled = true)
	public void onPlayerDropItem(PlayerDropItemEvent event) {
		Player player = event.getPlayer();
		if (Freeze.isFreeze(player))
			event.setCancelled(true);
	}

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		if (!Freeze.isFreeze(player))
			return;
		Location location = event.getFrom();
		Location locationTo = event.getTo();
		if (location.getX() != locationTo.getX() || location.getZ() != locationTo.getZ() || location.getY() != locationTo.getY())
			player.teleport(location);
	}

	@EventHandler
	public void onPlayerQuitEvent(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		if (!Freeze.isFreeze(player))
			return;
		Freeze.unfreeze(player);
		Permission.MODERATOR_RECEIVEFREEZEDISCONNECTED.sendMessageToOnlinePlayers(ConfigGet.MESSAGES_FREEZE_PLAYERDISCONNECTWHILEFREEZE.getString().replace("%player%", player.getName()));
	}

	@EventHandler(ignoreCancelled = true)
	public void onPlayerToggleSneak(PlayerToggleSneakEvent event) {
		Player player = event.getPlayer();
		if (Freeze.isFreeze(player))
			event.setCancelled(true);
	}
}
