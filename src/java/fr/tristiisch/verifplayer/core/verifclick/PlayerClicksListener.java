package fr.tristiisch.verifplayer.core.verifclick;

import java.util.Set;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import fr.tristiisch.verifplayer.VerifPlayerPlugin;
import fr.tristiisch.verifplayer.playerinfo.PlayerInfo;
import fr.tristiisch.verifplayer.utils.config.ConfigGet;

public class PlayerClicksListener implements Listener {

	@EventHandler(priority = EventPriority.MONITOR)
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
		if (!(event.getDamager() instanceof Player)) {
			return;
		}
		Player attacker = (Player) event.getDamager();
		PlayerInfo playerInfo = VerifPlayerPlugin.getInstance().getPlayerInfoHandler().get(attacker);
		playerInfo.addEntityClick();
		Block blockTarget = attacker.getTargetBlock((Set<Material>) null, 6);

		if (blockTarget.getType() == Material.AIR) {
			playerInfo.removeAirClick();
		}
		if (playerInfo.getClickEntity() > ConfigGet.SETTINGS_MAXCPS.getInt()) {
			event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerInteract(PlayerInteractEvent event) {
		if (event.getAction() != Action.LEFT_CLICK_AIR) {
			return;
		}
		Player player = event.getPlayer();
		PlayerInfo playerInfo = VerifPlayerPlugin.getInstance().getPlayerInfoHandler().get(player);
		playerInfo.addAirClick();
		if (playerInfo.getClickAir() > ConfigGet.SETTINGS_MAXCPS.getInt()) {
			event.setCancelled(true);
		}
	}
}
