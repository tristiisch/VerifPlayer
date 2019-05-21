package fr.tristiisch.verifplayer.core.freeze;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import fr.tristiisch.verifplayer.utils.SpigotUtils;
import fr.tristiisch.verifplayer.utils.permission.Permission;

public class FreezeListener implements Listener {

	@EventHandler
	public void EntityDamageEvent(final EntityDamageByEntityEvent event) {
		if(event.isCancelled()) {
			return;
		}
		if(!(event.getEntity() instanceof Player)) {
			return;
		}
		final Player victim = (Player) event.getEntity();
		if(Freeze.isFreeze(victim)) {

			if(event.getDamager() instanceof Player) {
				event.getDamager().sendMessage(SpigotUtils.color("&cLe joueur &4" + victim.getName() + "&c est freeze."));
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
		if(Freeze.isFreeze(player)) {
			// TODO 1.8/1.7 comptability
			player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 0);
			player.teleport(event.getFrom());
		}
	}

	@EventHandler
	public void PlayerQuitEvent(final PlayerQuitEvent event) {
		final Player player = event.getPlayer();
		if(Freeze.isFreeze(player)) {
			Freeze.unfreeze(player);
			for(final Player staff : Bukkit.getOnlinePlayers()) {
				if(Permission.MODERATOR_RECEIVEFREEZEDISCONNECTED.hasPermission(staff)) {
					staff.sendMessage(SpigotUtils.color("&bLe joueur &3" + player.getName() + "&b s'est déconnecté en étant freeze."));
				}
			}
		}
	}
}
