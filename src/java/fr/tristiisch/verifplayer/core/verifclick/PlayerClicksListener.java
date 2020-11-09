package fr.tristiisch.verifplayer.core.verifclick;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.BlockIterator;

import fr.tristiisch.verifplayer.VerifPlayerPlugin;
import fr.tristiisch.verifplayer.playerinfo.PlayerInfo;
import fr.tristiisch.verifplayer.utils.config.ConfigGet;

public class PlayerClicksListener implements Listener {

	@EventHandler(priority = EventPriority.MONITOR)
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
		if (!(event.getDamager() instanceof Player))
			return;
		if (!event.getCause().equals(DamageCause.ENTITY_ATTACK))
			return;
		Player attacker = (Player) event.getDamager();
		PlayerInfo playerInfo = VerifPlayerPlugin.getInstance().getPlayerInfoHandler().get(attacker);
		playerInfo.addEntityClick();
		Block blockTarget = attacker.getTargetBlock((Set<Material>) null, 6);

		if (blockTarget.getType() == Material.AIR)
			playerInfo.removeAirClick();
		if (playerInfo.getClickEntity() > ConfigGet.SETTINGS_MAXCPS.getInt())
			event.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerDropItem(PlayerDropItemEvent event) {
		Player player = event.getPlayer();
		PlayerInfo playerInfo = VerifPlayerPlugin.getInstance().getPlayerInfoHandler().get(player);
		Set<Material> list = new HashSet<>(Arrays.asList(Material.AIR, Material.WATER, Material.LAVA));
		Block block = player.getTargetBlock(list, 5);
		Block block2 = getTargetBlock(player, 5, list);
		System.out.println("block1 " + (block == null ? "null" : block.getType()) + " block2 " + (block2 == null ? "null" : block2.getType()));
		if (block2 == null)
			playerInfo.removeAirClick();
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerInteract(PlayerInteractEvent event) {
		if (event.getAction() != Action.LEFT_CLICK_AIR)
			return;
		Player player = event.getPlayer();
		PlayerInfo playerInfo = VerifPlayerPlugin.getInstance().getPlayerInfoHandler().get(player);
		playerInfo.addAirClick();
		if (playerInfo.getClickAir() > ConfigGet.SETTINGS_MAXCPS.getInt())
			event.setCancelled(true);
	}

	public final Block getTargetBlock(Player player, int range, Set<Material> list) {
		BlockIterator iter = new BlockIterator(player, range);
		Block lastBlock = iter.next();
		while (iter.hasNext()) {
			lastBlock = iter.next();
			if (!list.contains(lastBlock.getType()))
				return lastBlock;
		}
		return null;
	}
}
