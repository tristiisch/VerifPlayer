package fr.tristiisch.verifplayer.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Reflector {

	@SuppressWarnings("unchecked")
	public static <T> T callMethod(final Method method, final Object instance, final Object... paramaters) {
		if(method == null) {
			throw new RuntimeException("No such method");
		}
		method.setAccessible(true);
		try {
			return (T) method.invoke(instance, paramaters);
		} catch(final InvocationTargetException ex) {
			throw new RuntimeException(ex.getCause());
		} catch(final Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	public static Class<?> getClass(final String classname) {
		try {
			final String version = getNmsVersion();
			final String path = classname.replace("{nms}", "net.minecraft.server." + version).replace("{nm}", "net.minecraft." + version).replace("{cb}", "org.bukkit.craftbukkit." + version);
			return Class.forName(path);
		} catch(final Throwable t) {
			t.printStackTrace();
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> T getFieldValue(final Field field, final Object obj) {
		try {
			return (T) field.get(obj);
		} catch(final Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Object getFieldValue(final Object instance, final String fieldName) throws Exception {
		final Field field = instance.getClass().getDeclaredField(fieldName);
		field.setAccessible(true);
		return field.get(instance);
	}

	public static Object getNmsPlayer(final Player p) throws Exception {
		final Method getHandle = p.getClass().getMethod("getHandle");
		return getHandle.invoke(p);
	}

	public static String getNmsVersion() {
		return Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
	}

	public static int getPing(final Player player) {
		try {
			final Object nmsPlayer = getNmsPlayer(player);
			return Integer.valueOf(getFieldValue(nmsPlayer, "ping").toString());
		} catch(final Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	public static Field makeField(final Class<?> clazz, final String name) {
		try {
			return clazz.getDeclaredField(name);
		} catch(final NoSuchFieldException ex) {
			return null;
		} catch(final Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	public static Method makeMethod(final Class<?> clazz, final String methodName, final Class<?>... paramaters) {
		try {
			return clazz.getDeclaredMethod(methodName, paramaters);
		} catch(final NoSuchMethodException ex) {
			return null;
		} catch(final Exception ex) {
			throw new RuntimeException(ex);
		}
	}
}