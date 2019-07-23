package fr.tristiisch.verifplayer.task;

import java.util.concurrent.TimeUnit;

public class Duration {

	final private TimeUnit timeUnit;
	final private int time;

	public Duration(final TimeUnit timeUnit, final int time) {
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
