package fr.tristiisch.verifplayer.task;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

import fr.tristiisch.verifplayer.VerifPlayerPlugin;

public class TaskHandler {

	private final VerifPlayerPlugin plugin;

	private final HashMap<String, Integer> taskList = new HashMap<>();

	public TaskHandler(final VerifPlayerPlugin plugin) {
		this.plugin = plugin;
	}

	public void addTask(final String name, final int id) {
		this.taskList.put(name, id);
	}

	public void cancelAllTask() {
		for(final int taskId : this.taskList.values()) {
			this.getScheduler().cancelTask(taskId);
		}
	}

	public void cancelTask(final int id) {
		this.getScheduler().cancelTask(id);
	}

	public boolean cancelTask(final String taskName) {
		if(this.taskExist(taskName)) {
			final int taskId = this.getTaskId(taskName);
			this.taskList.remove(taskName);
			this.getScheduler().cancelTask(taskId);
			return true;
		}
		return false;
	}

	public void checkIfExist(final String taskName) {
		if(this.taskExist(taskName)) {
			this.cancelTask(taskName);
		}
	}

	private BukkitScheduler getScheduler() {
		return this.plugin.getServer().getScheduler();
	}

	public BukkitTask getTask(final int id) {
		final BukkitTask task = null;
		if(id > 0) {
			for(final BukkitTask pendingTask : this.getScheduler().getPendingTasks()) {
				if(pendingTask.getTaskId() == id) {
					return task;
				}
			}
		}
		return null;
	}

	public BukkitTask getTask(final String taskName) {
		return this.getTask(this.getTaskId(taskName));
	}

	public int getTaskId(final String taskName) {
		if(this.taskExist(taskName)) {
			return this.taskList.get(taskName);
		}
		return 0;
	}

	public String getTaskName(final int id) {
		for(final Map.Entry<String, Integer> entry : this.taskList.entrySet()) {
			if(entry.getValue() == id) {
				return entry.getKey();
			}
		}
		return null;
	}

	public String getTaskName(final String string) {
		String taskName;
		for(taskName = string + "_" + new Random().nextInt(99999); this.taskExist(taskName); taskName = string + "_" + new Random().nextInt(99999)) {
		}
		return taskName;
	}

	public void removeTask(final String taskName) {
		this.taskList.remove(taskName);
	}

	public BukkitTask runTask(final Runnable runnable) {
		return this.getScheduler().runTask(this.plugin, runnable);
	}

	public BukkitTask runTaskAsynchronously(final Runnable runnable) {
		return this.getScheduler().runTaskAsynchronously(this.plugin, runnable);
	}

	public BukkitTask runTaskLater(final Runnable runnable, final int tick) {
		return this.getScheduler().runTaskLater(this.plugin, runnable, tick);
	}

	public BukkitTask runTaskLater(final String taskName, final Runnable task, final int duration) {
		final BukkitTask bukkitTask = this.getScheduler().runTaskLater(this.plugin, task, duration);
		final int id = bukkitTask.getTaskId();
		this.addTask(taskName, id);
		this.runTaskLater(() -> {
			if(this.taskList.get(taskName) != null && this.taskList.get(taskName) == id) {
				this.taskList.remove(taskName);
			}
		}, duration);
		return bukkitTask;
	}

	public BukkitTask runTaskLaterAsynchronously(final Runnable runnable, final int duration) {
		return this.getScheduler().runTaskLaterAsynchronously(this.plugin, runnable, duration);
	}

	public BukkitTask runTaskTimerAsynchronously(final String taskName, final Runnable runnable, final long delay, final long refresh) {
		this.cancelTask(taskName);
		final BukkitTask task = this.getScheduler().runTaskTimerAsynchronously(this.plugin, runnable, delay, delay);
		this.taskList.put(taskName, task.getTaskId());
		return task;
	}

	public BukkitTask scheduleSyncRepeatingTask(final String taskName, final Runnable runnable, final long delay, final long refresh) {
		this.cancelTask(taskName);
		final BukkitTask task = this.getScheduler().runTaskTimer(this.plugin, runnable, delay, refresh);
		this.taskList.put(taskName, task.getTaskId());
		return task;
	}

	public boolean taskExist(final String taskName) {
		return this.taskList.containsKey(taskName);
	}
}
