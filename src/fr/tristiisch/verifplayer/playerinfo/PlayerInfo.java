package fr.tristiisch.verifplayer.playerinfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerInfo {

	private Integer clickAir;
	private Integer clickEntity;
	private final List<Integer> clicksAir = new ArrayList<>();
	private final List<Integer> clicksEntity = new ArrayList<>();
	private final Map<Long, Integer> alertHistory = new HashMap<>();
	private long lastAlert;
	private int maxCPS;
	private int nbAlerts;

	public PlayerInfo() {
		this.clickAir = 0;
		this.clickEntity = 0;
		this.lastAlert = 0L;
		this.maxCPS = 0;
		this.nbAlerts = 0;
	}

	public void addClickAir() {
		++this.clickAir;
	}

	public void addClickEntity() {
		++this.clickEntity;
	}

	public void addNbAlerts() {
		++this.nbAlerts;
	}

	public Map<Long, Integer> getAlertHistory() {
		return this.alertHistory;
	}

	public Integer getClickAir() {
		return this.clickAir;
	}

	public Integer getClickEntity() {
		return this.clickEntity;
	}

	public List<Integer> getClicksAir() {
		return this.clicksAir;
	}

	public List<Integer> getClicksEntity() {
		return this.clicksEntity;
	}

	public int getCurrentClicks() {
		final int i = this.clicksAir.size();
		if(i == 0) {
			return 0;
		}
		return this.clicksAir.get(i - 1) + this.clicksEntity.get(i - 1);
	}

	public long getLastAlert() {
		return this.lastAlert;
	}

	public int getMaxCPS() {
		return this.maxCPS;
	}

	public int getNbAlerts() {
		return this.nbAlerts;
	}

	public void putAlertHistory(final long time, final int value) {
		this.alertHistory.put(time, value);
	}

	public void removeClickAir() {
		--this.clickAir;
	}

	public void resetClickAir() {
		this.clickAir = 0;
	}

	public void resetClickEntity() {
		this.clickEntity = 0;
	}

	public void setLastAlert(final long timestamp) {
		this.lastAlert = timestamp;
	}

	public void setMaxCPS(final int maxCPS) {
		this.maxCPS = maxCPS;
	}
}
