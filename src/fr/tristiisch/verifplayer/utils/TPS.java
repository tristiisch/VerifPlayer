package fr.tristiisch.verifplayer.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import fr.tristiisch.verifplayer.utils.Reflection.ClassEnum;

public class TPS {

	static boolean usePaper;
	private static Method getServerMethod;
	private static Field recentTpsField;

	static {
		final Class<?> spigotServerClass = Reflection.getClass("org.bukkit.Server$Spigot");
		if(spigotServerClass != null) {
			usePaper = Reflection.makeMethod(spigotServerClass, "getTPS") != null;
		}

		final Class<?> minecraftServerClass = Reflection.getClass(ClassEnum.NMS, "MinecraftServer");
		if(minecraftServerClass != null) {
			getServerMethod = Reflection.makeMethod(minecraftServerClass, "getServer");
			recentTpsField = Reflection.makeField(minecraftServerClass, "recentTps");
		}
		final Method getSpigotMethod = Reflection.makeMethod(Bukkit.class, "spigot");

		usePaper = getSpigotMethod != null && usePaper;
	}

	private static boolean canGetWithPaper() {
		return usePaper;
	}

	public static String getColor(final double tps) {
		if(tps >= 18) {
			return ChatColor.GREEN + String.valueOf(tps);
		} else if(tps >= 16) {
			return ChatColor.GOLD + String.valueOf(tps);
		} else {
			return ChatColor.RED + String.valueOf(tps);
		}
	}

	private static double[] getNMSRecentTps() {
		if(getServerMethod == null || recentTpsField == null) {
			try {
				throw new Exception("Can't get TPS from NMS");
			} catch(final Exception e) {
				e.printStackTrace();
			}
		}
		final Object server = Reflection.callMethod(getServerMethod, null);
		final double[] recent = Reflection.getFieldValue(recentTpsField, server);
		return recent;
	}

	private static double[] getPaperRecentTps() {
		if(!canGetWithPaper()) {
			throw new UnsupportedOperationException("Can't get TPS from Paper");
		}
		final Object server = Reflection.callMethod(getServerMethod, null);
		final double[] recent = Reflection.getFieldValue(recentTpsField, server);
		return recent;
	}

	public static double getTPS() {
		return getTPS(1);
	}

	public static double getTPS(final int time) {
		final double[] recentTps = getTPSArray();
		switch(time) {
		case 1:
			return recentTps[0];
		case 5:
			return recentTps[1];
		case 15:
			return recentTps[2];
		default:
			throw new IllegalArgumentException("Unsupported tps measure time " + time);
		}
	}

	public static double[] getTPSArray() {
		double[] recentTps;
		if(canGetWithPaper()) {
			recentTps = getPaperRecentTps();
		} else {
			recentTps = getNMSRecentTps();
		}
		for(int i = 0; recentTps.length > i; i++) {
			recentTps[i] = Math.min(Math.round(recentTps[i] * 100.0) / 100.0, 20.0);
		}
		return recentTps;
	}

	public static int getTPSint() {
		return getTPSint(1);
	}

	public static int getTPSint(final int time) {
		return (int) Math.round(getTPS(time));
	}
}
