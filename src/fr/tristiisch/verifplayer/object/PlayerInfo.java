package fr.tristiisch.verifplayer.object;

import java.util.ArrayList;
import java.util.List;

public class PlayerInfo {

	private Integer clickAir;
	private Integer clickEntity;
	private final List<Integer> clicksAir;
	private final List<Integer> clicksEntity;
	private long lastAlert;
	private int maxCPS;
	private int nbAlerts;

	public PlayerInfo() {
		this.clickAir = 0;
		this.clickEntity = 0;
		this.clicksAir = new ArrayList<>();
		this.clicksEntity = new ArrayList<>();
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
