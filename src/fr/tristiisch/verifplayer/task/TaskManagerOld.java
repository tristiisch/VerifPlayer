package fr.tristiisch.verifplayer.task;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

import fr.tristiisch.verifplayer.VerifPlayerPlugin;

public class TaskManagerOld {

	private static HashMap<String, Integer> taskList = new HashMap<>();
	public static BukkitScheduler scheduler = getPlugin().getServer().getScheduler();

	public static void addTask(final String name, final int id) {
		TaskManagerOld.taskList.put(name, id);
	}

	public static void cancelAllTask() {
		for (final int taskId : TaskManagerOld.taskList.values()) {
			TaskManagerOld.scheduler.cancelTask(taskId);
		}
	}

	public static void cancelTaskById(final int id) {
		TaskManagerOld.scheduler.cancelTask(id);
	}

	public static boolean cancelTaskByName(final String taskName) {
		if (taskExist(taskName)) {
			final int taskId = getTaskId(taskName);
			TaskManagerOld.taskList.remove(taskName);
			TaskManagerOld.scheduler.cancelTask(taskId);
			return true;
		}
		return false;
	}

	public static void checkIfExist(final String taskName) {
		if (taskExist(taskName)) {
			cancelTaskByName(taskName);
		}
	}

	private static Plugin getPlugin() {
		return VerifPlayerPlugin.getInstance();
	}

	public static BukkitTask getTask(final int id) {
		final BukkitTask task = null;
		if (id > 0) {
			for (final BukkitTask pendingTask : TaskManagerOld.scheduler.getPendingTasks()) {
				if (pendingTask.getTaskId() == id) {
					return task;
				}
			}
		}
		return null;
	}

	public static BukkitTask getTask(final String taskName) {
		return getTask(getTaskId(taskName));
	}

	public static int getTaskId(final String taskName) {
		if (taskExist(taskName)) {
			return TaskManagerOld.taskList.get(taskName);
		}
		return 0;
	}

	public static String getTaskName(final String string) {
		String taskName;
		for (taskName = string + "_" + new Random().nextInt(99999); taskExist(taskName); taskName = string + "_" + new Random().nextInt(99999)) {
		}
		return taskName;
	}

	public static String getTaskNameById(final int id) {
		for (final Map.Entry<String, Integer> entry : TaskManagerOld.taskList.entrySet()) {
			if (entry.getValue() == id) {
				return entry.getKey();
			}
		}
		return null;
	}

	public static void removeTaskByName(final String taskName) {
		TaskManagerOld.taskList.remove(taskName);
	}

	public static BukkitTask runTask(final Runnable runnable) {
		return TaskManagerOld.scheduler.runTask(TaskManagerOld.getPlugin(), runnable);
	}

	public static BukkitTask runTaskAsynchronously(final Runnable runnable) {
		return TaskManagerOld.scheduler.runTaskAsynchronously(TaskManagerOld.getPlugin(), runnable);
	}

	public static BukkitTask runTaskLater(final Runnable runnable, final int tick) {
		return TaskManagerOld.scheduler.runTaskLater(TaskManagerOld.getPlugin(), runnable, tick);
	}

	public static BukkitTask runTaskLater(final String taskName, final Runnable task, final int duration) {
		final BukkitTask bukkitTask = TaskManagerOld.scheduler.runTaskLater(TaskManagerOld.getPlugin(), task, duration);
		final int id = bukkitTask.getTaskId();
		addTask(taskName, id);
		runTaskLater(() -> {
			if (TaskManagerOld.taskList.get(taskName) != null && TaskManagerOld.taskList.get(taskName) == id) {
				TaskManagerOld.taskList.remove(taskName);
			}
		}, duration);
		return bukkitTask;
	}

	public static BukkitTask runTaskTimerAsynchronously(final String taskName, final Runnable runnable, final long delay, final long refresh) {
		cancelTaskByName(taskName);
		final BukkitTask task = TaskManagerOld.scheduler.runTaskTimerAsynchronously(TaskManagerOld.getPlugin(), runnable, delay, delay);
		TaskManagerOld.taskList.put(taskName, task.getTaskId());
		return task;
	}

	public static BukkitTask scheduleSyncRepeatingTask(final String taskName, final Runnable runnable, final long delay, final long refresh) {
		cancelTaskByName(taskName);
		final BukkitTask task = TaskManagerOld.scheduler.runTaskTimer(TaskManagerOld.getPlugin(), runnable, delay, refresh);
		TaskManagerOld.taskList.put(taskName, task.getTaskId());
		return task;
	}

	public static boolean taskExist(final String taskName) {
		return TaskManagerOld.taskList.containsKey(taskName);
	}
}
