package fr.tristiisch.verifplayer.utils;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;

/**
 * @author Vinetos
 */
public class Reflection {

	public enum ClassEnum {
		NMS("net.minecraft.server." + getNmsVersion() + "."),
		NM("net.minecraft." + getNmsVersion() + "."),
		CB("org.bukkit.craftbukkit." + getNmsVersion() + ".");

		private String classType;

		ClassEnum(String classType) {
			this.classType = classType;
		}

		@Override
		public String toString() {
			return this.classType;
		}
	}

	public interface ConstructorInvoker {

		Object invoke(Object... p0);
	}

	public interface FieldAccessor<T> {

		T get(Object p0);

		boolean hasField(Object p0);

		void set(Object p0, Object p1);
	}

	/*
	 * // Get a enum value public static Enum getEnum(String enumClass, String
	 * enumValue) { return Enum.valueOf((Class<? extends Enum>)
	 * Reflection.getClass(enumClass), enumValue); }
	 */

	public interface MethodInvoker {

		Object invoke(Object p0, Object... p1);
	}

	private static Map<String, Class<?>> classCache = new HashMap<>();

	@SuppressWarnings("unchecked")
	public static <T> T callMethod(Method method, Object instance, Object... paramaters) {
		if (method == null) {
			throw new RuntimeException("No such method");
		}
		method.setAccessible(true);
		try {
			return (T) method.invoke(instance, paramaters);
		} catch (InvocationTargetException ex) {
			throw new RuntimeException(ex.getCause());
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	// Get a class as Array
	public static Class<?> getArrayClass(String classname, int arraySize) {
		try {
			return Array.newInstance(getClass(classname), arraySize).getClass();
		} catch (Throwable t) {
			t.printStackTrace();
			return null;
		}
	}

	// Get a Fields who is an Array
	public static ArrayList<Field> getArraysFields(Object instance, Class<?> fieldType) throws Exception {
		String[] values = fieldType.toString().split(" ");
		String fieldName = values[values.length - 1];
		Field[] fields = instance.getClass().getDeclaredFields();
		ArrayList<Field> fieldArrayList = new ArrayList<>();
		for (Field field : fields) {
			if (field.getType().isArray()) {
				if (field.getType().toString().contains(fieldName)) {
					field.setAccessible(true);
					fieldArrayList.add(field);
				}
			}
		}
		return fieldArrayList;
	}

	public static Class<?> getClass(ClassEnum classEnum, String classname) {
		return getClass(classEnum + classname);
	}

	public static Class<?> getClass(String classname) {

		if (classCache.containsKey(classname)) {
			return classCache.get(classname);
		}

		Class<?> clazz = getClassWithoutCache(classname);
		classCache.put(classname, clazz);
		return clazz;

	}

	public static Class<?> getClassWithoutCache(ClassEnum classEnum, String classname) {
		return getClassWithoutCache(classEnum + classname);
	}

	// Get a class
	public static Class<?> getClassWithoutCache(String classname) {
		try {
			return Class.forName(classname);
		} catch (Throwable t) {
			t.printStackTrace();
			return null;
		}

	}

	// get a field
	public static Field getField(Class<?> clazz, String fieldName) {
		Field field = null;
		try {
			field = clazz.getDeclaredField(fieldName);
			field.setAccessible(true);
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}
		return field;
	}

	// Getting fields
	public static ArrayList<Field> getFields(Object instance, Class<?> fieldType) throws Exception {
		Field[] fields = instance.getClass().getDeclaredFields();
		ArrayList<Field> fieldArrayList = new ArrayList<>();
		for (Field field : fields) {
			if (field.getType() == fieldType) {
				field.setAccessible(true);
				fieldArrayList.add(field);
			}
		}

		return fieldArrayList;
	}

	// Get a field value
	@SuppressWarnings("unchecked")
	public static <T> T getFieldValue(Field field, Object obj) {
		try {
			return (T) field.get(obj);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	// Get a field value
	@SuppressWarnings("rawtypes")
	public static Object getFieldValue(Object instance, Class clazz, String fieldName) throws Exception {
		Field field = clazz.getDeclaredField(fieldName);
		field.setAccessible(true);
		return field.get(instance);
	}

	// Get a field value
	public static Object getFieldValue(Object instance, String fieldName) throws Exception {
		Field field = instance.getClass().getDeclaredField(fieldName);
		field.setAccessible(true);
		return field.get(instance);
	}

	// Get the first field by her type
	public static Field getFirstFieldByType(Class<?> clazz, Class<?> type) {
		for (Field field : clazz.getDeclaredFields()) {
			field.setAccessible(true);
			if (field.getType() == type) {
				return field;
			}
		}
		return null;
	}

	public static MethodInvoker getMethod(Class<?> clazz, String methodName, Class<?>... params) {
		return getTypedMethod(clazz, methodName, null, params);
	}

	// Get a nms player
	public static Object getNmsPlayer(Player p) throws Exception {
		Method getHandle = p.getClass().getMethod("getHandle");
		return getHandle.invoke(p);
	}

	// Get a nms scoreboard
	public static Object getNmsScoreboard(Scoreboard s) throws Exception {
		Method getHandle = s.getClass().getMethod("getHandle");
		return getHandle.invoke(s);
	}

	// Get a net.minecraft.server version
	public static String getNmsVersion() {
		return Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
	}

	// Get the ping of player
	public static int getPing(Player p) {
		try {
			Object nmsPlayer = Reflection.getNmsPlayer(p);
			return Integer.valueOf(getFieldValue(nmsPlayer, "ping").toString());
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	// Get player connection
	public static Object getPlayerConnection(Player player) throws Exception {
		Object nmsPlayer = getNmsPlayer(player);
		return nmsPlayer.getClass().getField("playerConnection").get(nmsPlayer);
	}

	public static MethodInvoker getTypedMethod(Class<?> clazz, String methodName, Class<?> returnType, Class<?>... params) {
		Method[] declaredMethods;
		for (int length = (declaredMethods = clazz.getDeclaredMethods()).length, i = 0; i < length; ++i) {
			Method method = declaredMethods[i];
			if ((methodName == null || method.getName().equals(methodName)) && returnType == null || method.getReturnType().equals(returnType) && Arrays.equals(method.getParameterTypes(), params)) {
				method.setAccessible(true);
				return (target, arguments) -> {
					try {
						return method.invoke(target, arguments);
					} catch (Exception e) {
						throw new RuntimeException("Cannot invoke method " + method, e);
					}
				};
			}
		}
		if (clazz.getSuperclass() != null) {
			return getMethod(clazz.getSuperclass(), methodName, params);
		}
		throw new IllegalStateException(String.format("Unable to find method %s (%s).", methodName, Arrays.asList(params)));
	}

	public static Field makeField(Class<?> clazz, String name) {
		try {
			return clazz.getDeclaredField(name);
		} catch (NoSuchFieldException ex) {
			return null;
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	public static Method makeMethod(Class<?> clazz, String methodName, Class<?>... paramaters) {
		try {
			return clazz.getDeclaredMethod(methodName, paramaters);
		} catch (NoSuchMethodException ex) {
			return null;
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	// Send a message with IChatBaseComponent
	public static void sendMessage(Player p, Object message) throws Exception {
		Object nmsPlayer = getNmsPlayer(p);
		nmsPlayer.getClass().getMethod("sendMessage", Reflection.getClass("{nms}.IChatBaseComponent")).invoke(nmsPlayer, message);

	}

	// Send packet to a list of player
	public static void sendPacket(Collection<? extends Player> players, Object packet) {
		if (packet == null) {
			return;
		}
		try {
			for (Player p : players) {
				sendPacket(getPlayerConnection(p), packet);
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	// Send packet to all connected player
	public static void sendPacket(Object packet) throws Exception {
		sendPacket(Bukkit.getOnlinePlayers(), packet);
	}

	// Send packet to a connection player
	public static void sendPacket(Object connection, Object packet) throws Exception {
		connection.getClass().getMethod("sendPacket", Reflection.getClass(ClassEnum.NMS, "Packet")).invoke(connection, packet);
	}

	// Send a packet to a player
	public static void sendPacket(Player p, Object packet) {
		ArrayList<Player> list = new ArrayList<>();
		list.add(p);
		sendPacket(list, packet);
	}

	public static void setField(Object obj, String field, Object value) {
		try {
			Field maxUsesField = obj.getClass().getDeclaredField(field);
			maxUsesField.setAccessible(true);
			maxUsesField.set(obj, value);
			maxUsesField.setAccessible(!maxUsesField.isAccessible());
		} catch (Exception e) {
			e.printStackTrace();
			Bukkit.getLogger().severe("Reflection failed for changeField " + obj.getClass().getName() + " field > " + field + " value > " + value);
			Bukkit.getServer().shutdown();
		}
	}

	// Set a field value
	public static void setFieldValue(Object instance, Field field, Object value) {
		field.setAccessible(true);
		try {
			field.set(instance, value);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	// Set a field value
	public static void setFieldValue(Object instance, String field, Object value) {
		try {
			Field f = instance.getClass().getDeclaredField(field);
			f.setAccessible(true);
			f.set(instance, value);
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
}