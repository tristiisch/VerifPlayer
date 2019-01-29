package fr.tristiisch.verifplayer.listener;

import java.util.Set;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import fr.tristiisch.verifplayer.VerifPlayer;
import fr.tristiisch.verifplayer.object.PlayerInfo;
import fr.tristiisch.verifplayer.utils.ConfigUtils;

public class FastClickListener implements Listener {

	@EventHandler
	public void onInventoryClick(final InventoryClickEvent event) {
		if(event.getInventory().getTitle().startsWith(ConfigUtils.VERIFGUI_NAME.getString())) {
			if(event.getClickedInventory() == null) {
				event.getWhoClicked().closeInventory();
				return;
			}
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onPlayerInteract(final EntityDamageByEntityEvent event) {
		if(!(event.getDamager() instanceof Player)) {
			return;
		}
		final Player player = (Player) event.getDamager();
		final PlayerInfo pc = VerifPlayer.get(player);
		pc.addClickEntity();
		final Block blockTarget = player.getTargetBlock((Set<Material>) null, 6);
		if(blockTarget.getType() == Material.AIR) {
			pc.removeClickAir();
		}
		if(pc.getClickEntity() >= 18) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onPlayerInteract(final PlayerInteractEvent event) {
		if(event.getAction() == Action.LEFT_CLICK_AIR) {
			final Player player = event.getPlayer();
			final PlayerInfo pc = VerifPlayer.get(player);
			/*			if(player.getTargetBlock((Set<Material>) null, 100).getLocation().distance(player.getLocation()) < 6.0 && pc.lastBlockInteraction > System.currentTimeMillis() && pc.click >= 10) {
							event.setCancelled(true);
						}*/

			pc.addClickAir();
			if(pc.getClickAir() >= 18) {
				event.setCancelled(true);
			}
		} /*else if(event.getAction() == Action.LEFT_CLICK_BLOCK) {
			final Player player = event.getPlayer();
			final PlayerInfo pc = VerifCPS.getByPlayer(player);
			pc.lastBlockInteraction = System.currentTimeMillis() + 5000L;
			}*/
	}

	@EventHandler
	public void onPlayerQuit(final PlayerQuitEvent e) {
		VerifPlayer.removePlayer(e.getPlayer());
	}

}
