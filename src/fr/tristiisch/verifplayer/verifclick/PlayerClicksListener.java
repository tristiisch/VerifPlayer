package fr.tristiisch.verifplayer.verifclick;

import java.util.Set;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import fr.tristiisch.verifplayer.VerifPlayerData;
import fr.tristiisch.verifplayer.object.PlayerInfo;
import fr.tristiisch.verifplayer.utils.VersionUtils.Versions;
import fr.tristiisch.verifplayer.utils.config.ConfigUtils;

public class PlayerClicksListener implements Listener {

	@EventHandler
	public void onEntityDamageByEntity(final EntityDamageByEntityEvent event) {
		if(!(event.getDamager() instanceof Player)) {
			return;
		}
		final Player player = (Player) event.getDamager();
		final PlayerInfo playerInfo = VerifPlayerData.getPlayerInfo(player);
		playerInfo.addClickEntity();
		Block blockTarget;
		if(Versions.V1_8.isEqualOrOlder()) {
			blockTarget = player.getTargetBlock((Set<Material>) null, 6);
		} else {
			blockTarget = player.getTargetBlock(null, 6);
		}

		if(blockTarget.getType() == Material.AIR) {
			playerInfo.removeClickAir();
		}
		if(playerInfo.getClickEntity() > ConfigUtils.SETTINGS_SIZEHISTORYCPS.getInt()) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onPlayerInteract(final PlayerInteractEvent event) {
		if(event.getAction() != Action.LEFT_CLICK_AIR) {
			return;
		}
		final Player player = event.getPlayer();
		final PlayerInfo playerInfo = VerifPlayerData.getPlayerInfo(player);
		playerInfo.addClickAir();
		if(playerInfo.getClickAir() > ConfigUtils.SETTINGS_SIZEHISTORYCPS.getInt()) {
			event.setCancelled(true);
		}

	}
}
