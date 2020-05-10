package fr.tristiisch.verifplayer.task;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

import fr.tristiisch.verifplayer.VerifPlayerPlugin;

public class TaskHandler {

	private VerifPlayerPlugin plugin;

	private HashMap<String, Integer> taskList = new HashMap<>();

	public TaskHandler(VerifPlayerPlugin plugin) {
		this.plugin = plugin;
	}

	public void addTask(String name, int id) {
		this.taskList.put(name, id);
	}

	public void cancelAllTask() {
		for (int taskId : this.taskList.values()) {
			this.getScheduler().cancelTask(taskId);
		}
	}

	public void cancelTask(int id) {
		this.getScheduler().cancelTask(id);
	}

	public boolean cancelTask(String taskName) {
		if (this.taskExist(taskName)) {
			int taskId = this.getTaskId(taskName);
			this.taskList.remove(taskName);
			this.getScheduler().cancelTask(taskId);
			return true;
		}
		return false;
	}

	public void checkIfExist(String taskName) {
		if (this.taskExist(taskName)) {
			this.cancelTask(taskName);
		}
	}

	private BukkitScheduler getScheduler() {
		return this.plugin.getServer().getScheduler();
	}

	public BukkitTask getTask(int id) {
		BukkitTask task = null;
		if (id > 0) {
			for (BukkitTask pendingTask : this.getScheduler().getPendingTasks()) {
				if (pendingTask.getTaskId() == id) {
					return task;
				}
			}
		}
		return null;
	}

	public BukkitTask getTask(String taskName) {
		return this.getTask(this.getTaskId(taskName));
	}

	public int getTaskId(String taskName) {
		if (this.taskExist(taskName)) {
			return this.taskList.get(taskName);
		}
		return 0;
	}

	public String getTaskName(int id) {
		for (Map.Entry<String, Integer> entry : this.taskList.entrySet()) {
			if (entry.getValue() == id) {
				return entry.getKey();
			}
		}
		return null;
	}

	public String getTaskName(String string) {
		String taskName;
		do {
			taskName = string + "_" + new Random().nextInt(99999);
		} while (this.taskExist(taskName));
		return taskName;
	}

	public void removeTask(String taskName) {
		this.taskList.remove(taskName);
	}

	public BukkitTask runTask(Runnable runnable) {
		return this.getScheduler().runTask(this.plugin, runnable);
	}

	public BukkitTask runTaskAsynchronously(Runnable runnable) {
		return this.getScheduler().runTaskAsynchronously(this.plugin, runnable);
	}

	public BukkitTask runTaskLater(Runnable runnable, int tick) {
		return this.getScheduler().runTaskLater(this.plugin, runnable, tick);
	}

	public BukkitTask runTaskLater(String taskName, Runnable task, int duration) {
		BukkitTask bukkitTask = this.getScheduler().runTaskLater(this.plugin, task, duration);
		int id = bukkitTask.getTaskId();
		this.addTask(taskName, id);
		this.runTaskLater(() -> {
			if (this.taskList.get(taskName) != null && this.taskList.get(taskName) == id) {
				this.taskList.remove(taskName);
			}
		}, duration);
		return bukkitTask;
	}

	public BukkitTask runTaskLaterAsynchronously(Runnable runnable, int duration) {
		return this.getScheduler().runTaskLaterAsynchronously(this.plugin, runnable, duration);
	}

	public BukkitTask runTaskTimerAsynchronously(String taskName, Runnable runnable, long delay, long refresh) {
		this.cancelTask(taskName);
		BukkitTask task = this.getScheduler().runTaskTimerAsynchronously(this.plugin, runnable, delay, delay);
		this.taskList.put(taskName, task.getTaskId());
		return task;
	}

	public BukkitTask scheduleSyncRepeatingTask(String taskName, Runnable runnable, long delay, long refresh) {
		this.cancelTask(taskName);
		BukkitTask task = this.getScheduler().runTaskTimer(this.plugin, runnable, delay, refresh);
		this.taskList.put(taskName, task.getTaskId());
		return task;
	}

	public boolean taskExist(String taskName) {
		return this.taskList.containsKey(taskName);
	}
}
