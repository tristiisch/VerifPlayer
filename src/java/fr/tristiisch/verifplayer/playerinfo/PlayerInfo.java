package fr.tristiisch.verifplayer.playerinfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PlayerInfo extends PlayerInfoHook {

	private int clickAir = 0;
	private int clickEntity = 0;
	private int maxCPS = 0;
	private int alert = 0;
	private long lastAlert = 0L;

	/**
	 * private boolean freezed; private boolean alertEnabled; private boolean
	 * verifSpecEnabled;
	 **/

	private List<Integer> clicksAir = new ArrayList<>();
	private List<Integer> clicksEntity = new ArrayList<>();
	private Map<Long, Integer> alertHistory = new HashMap<>();
	private List<UUID> alreadyTeleportedPlayers = new ArrayList<>();

	public PlayerInfo(Player player) {
		super(player);
	}

	public void addAirClick() {
		++clickAir;
	}

	public void addAlert() {
		++alert;
	}

	public void addAlertHistory(long time, int value) {
		alertHistory.put(time, value);
	}

	public void addEntityClick() {
		++clickEntity;
	}

	public void clearAlreadyTeleportedPlayers() {
		alreadyTeleportedPlayers.clear();
	}

	public List<Integer> getAirClicks() {
		return clicksAir;
	}

	public Map<Long, Integer> getAlertHistory() {
		return alertHistory;
	}

	public int getNumberAlert() {
		return alert;
	}

	public List<Player> getAlreadyTeleportedPlayers() {
		return alreadyTeleportedPlayers.stream().map(uuid -> Bukkit.getPlayer(uuid)).filter(player -> player != null).collect(Collectors.toList());
	}

	public int getClickAir() {
		return clickAir;
	}

	public int getClickEntity() {
		return clickEntity;
	}

	public int getCurrentClicks() {
		int i = clicksAir.size();
		if (i == 0)
			return 0;
		return clicksAir.get(i - 1) + clicksEntity.get(i - 1);
	}

	public List<Integer> getEntityClicks() {
		return clicksEntity;
	}

	public long getLastAlert() {
		return lastAlert;
	}

	public int getMaxCPS() {
		return maxCPS;
	}

	public void removeAirClick() {
		--clickAir;
	}

	public void resetAirClick() {
		clickAir = 0;
	}

	public void resetEntityClick() {
		clickEntity = 0;
	}

	public void setAlreadyTeleportedPlayers(List<Player> alreadyTeleportedPlayers) {
		this.alreadyTeleportedPlayers = alreadyTeleportedPlayers.stream().map(Player::getUniqueId).collect(Collectors.toList());
	}

	public void setLastAlert(long lastAlert) {
		this.lastAlert = lastAlert;
	}

	public void setMaxCPS(int maxCPS) {
		this.maxCPS = maxCPS;
	}
}
