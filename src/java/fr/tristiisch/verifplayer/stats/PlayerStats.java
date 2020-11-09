package fr.tristiisch.verifplayer.stats;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

public class PlayerStats {

	private Player player;
	private List<PlayerAction> lastActions = new ArrayList<>();

	public PlayerStats(Player player) {
		this.player = player;
	}

	public List<PlayerAction> getLastActions() {
		return lastActions;
	}

	public PlayerAction getAction(PlayerAction playerAction) {
		return lastActions.stream().filter(pa -> pa.getName().equals(playerAction.getName())).findFirst().orElse(null);
	}

	public PlayerAction getLastAction() {
		return lastActions.stream().sorted((o1, o2) -> Math.toIntExact(o2.getLastUpdate() - o1.getLastUpdate())).findFirst().orElse(null);
	}

	public void addLastAction(PlayerAction playerAction) {
		PlayerAction oldAction = getAction(playerAction);
		if (oldAction == null)
			lastActions.add(playerAction);
		else
			oldAction.updatePlayer(player, playerAction);
		if (lastActions.size() > 10)
			lastActions.remove(getLastAction());
	}
}
