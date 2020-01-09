package fr.tristiisch.verifplayer.core.verifspec.teleport;

import java.util.Comparator;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class SortByDistance implements Comparator<Player> {

	private Location spawn;

	public SortByDistance(Location spawn) {
		this.spawn = spawn;
	}

	@Override
	public int compare(Player player1, Player player2) {
		return (int) Math.round(player1.getLocation().distance(this.spawn));
	}
}
