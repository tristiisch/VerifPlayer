package fr.tristiisch.verifplayer.task;

import java.util.concurrent.TimeUnit;

public class Duration {

	private TimeUnit timeUnit;
	private int time;

	public Duration(TimeUnit timeUnit, int time) {
		this.timeUnit = timeUnit;
		this.time = time;
	}

	public long getTime() {
		return this.timeUnit.toSeconds(this.time) / 10;
	}

	public long getTimeInTicks() {
		return this.getTime() / 20;
	}
}
