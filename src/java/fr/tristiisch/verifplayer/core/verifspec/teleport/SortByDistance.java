package fr.tristiisch.verifplayer.core.verifspec.teleport;

import java.util.Comparator;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class SortByDistance implements Comparator<Player> {

	private Location playerPosition;

	public SortByDistance(Location playerPosition) {
		this.playerPosition = playerPosition;
	}

	@Override
	public int compare(Player player1, Player player2) {
		int player1Compared = (int) Math.round(player1.getLocation().distance(playerPosition));
		int player2Compared = (int) Math.round(player2.getLocation().distance(playerPosition));
		return player2Compared - player1Compared;
	}
}
