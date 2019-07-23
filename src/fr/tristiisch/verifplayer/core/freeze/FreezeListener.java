package fr.tristiisch.verifplayer.core.freeze;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import fr.tristiisch.verifplayer.utils.config.ConfigGet;
import fr.tristiisch.verifplayer.utils.permission.Permission;

public class FreezeListener implements Listener {

	@EventHandler(ignoreCancelled = true)
	public void EntityDamageEvent(final EntityDamageByEntityEvent event) {
		if(!(event.getEntity() instanceof Player)) {
			return;
		}
		final Player victim = (Player) event.getEntity();
		if(Freeze.isFreeze(victim)) {

			if(event.getDamager() instanceof Player) {
				final Player attacker = (Player) event.getDamager();
				attacker.sendMessage(ConfigGet.MESSAGES_FREEZE_PLAYERISFREEZE.getString().replace("%player%", victim.getName()));
			}
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void PlayerMoveEvent(final PlayerMoveEvent event) {
		if(event.isCancelled()) {
			return;
		}
		final Player player = event.getPlayer();
		if(!Freeze.isFreeze(player)) {
			return;
		}
		// TODO 1.8/1.7 comptability
		// player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 0);
		player.teleport(event.getFrom());
	}

	@EventHandler
	public void PlayerQuitEvent(final PlayerQuitEvent event) {
		final Player player = event.getPlayer();
		if(Freeze.isFreeze(player)) {
			return;
		}
		Freeze.unfreeze(player);

		Permission.MODERATOR_RECEIVEFREEZEDISCONNECTED
				.sendMessageToOnlinePlayers(ConfigGet.MESSAGES_FREEZE_PLAYERDISCONNECTWHILEFREEZE.getString().replace("%player%", player.getName()));
	}
}
