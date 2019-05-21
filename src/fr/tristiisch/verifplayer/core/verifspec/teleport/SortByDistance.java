package fr.tristiisch.verifplayer.core.verifspec.teleport;

import java.util.Comparator;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class SortByDistance implements Comparator<Player> {

	private final Location spawn;

	public SortByDistance(final Location spawn) {
		this.spawn = spawn;
	}

	@Override
	public int compare(final Player player1, final Player player2) {
		return (int) Math.round(player1.getLocation().distance(this.spawn));
	}

}
