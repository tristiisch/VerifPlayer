package fr.tristiisch.verifplayer.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.binary.Base64;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.google.common.primitives.Primitives;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import fr.tristiisch.verifplayer.utils.Reflection.ClassEnum;

public class NBTEditor {

	private static HashMap<String, Method> methodCache;
	private static HashMap<Class<?>, Constructor<?>> constructorCache;
	private static HashMap<Class<?>, Class<?>> NBTClasses;
	private static HashMap<Class<?>, Field> NBTTagFieldCache;
	private static Field NBTListData;
	private static Field NBTCompoundMap;
	private static String version;

	static {
		version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];

		NBTClasses = new HashMap<>();
		try {
			NBTClasses.put(Byte.class, Reflection.getClassWithoutCache(ClassEnum.NMS, "NBTTagByte"));
			NBTClasses.put(String.class, Reflection.getClassWithoutCache(ClassEnum.NMS, "NBTTagString"));
			NBTClasses.put(Double.class, Reflection.getClassWithoutCache(ClassEnum.NMS, "NBTTagDouble"));
			NBTClasses.put(Integer.class, Reflection.getClassWithoutCache(ClassEnum.NMS, "NBTTagInt"));
			NBTClasses.put(Long.class, Reflection.getClassWithoutCache(ClassEnum.NMS, "NBTTagLong"));
			NBTClasses.put(Short.class, Reflection.getClassWithoutCache(ClassEnum.NMS, "NBTTagShort"));
			NBTClasses.put(Float.class, Reflection.getClassWithoutCache(ClassEnum.NMS, "NBTTagFloat"));
			NBTClasses.put(Class.forName("[B"), Reflection.getClassWithoutCache(ClassEnum.NMS, "NBTTagByteArray"));
			NBTClasses.put(Class.forName("[I"), Reflection.getClassWithoutCache(ClassEnum.NMS, "NBTTagIntArray"));
		} catch(final ClassNotFoundException e) {
			e.printStackTrace();
		}

		methodCache = new HashMap<>();
		try {
			methodCache.put("get", getNMSClass("NBTTagCompound").getMethod("get", String.class));
			methodCache.put("set", getNMSClass("NBTTagCompound").getMethod("set", String.class, getNMSClass("NBTBase")));
			methodCache.put("hasKey", getNMSClass("NBTTagCompound").getMethod("hasKey", String.class));
			methodCache.put("setIndex", getNMSClass("NBTTagList").getMethod("a", int.class, getNMSClass("NBTBase")));
			methodCache.put("add", getNMSClass("NBTTagList").getMethod("add", getNMSClass("NBTBase")));

			methodCache.put("hasTag", getNMSClass("ItemStack").getMethod("hasTag"));
			methodCache.put("getTag", getNMSClass("ItemStack").getMethod("getTag"));
			methodCache.put("setTag", getNMSClass("ItemStack").getMethod("setTag", getNMSClass("NBTTagCompound")));
			methodCache.put("asNMSCopy", getNMSClass("CraftItemStack").getMethod("asNMSCopy", ItemStack.class));
			methodCache.put("asBukkitCopy", getNMSClass("CraftItemStack").getMethod("asBukkitCopy", getNMSClass("ItemStack")));

			methodCache.put("getEntityHandle", getNMSClass("CraftEntity").getMethod("getHandle"));
			methodCache.put("getEntityTag", getNMSClass("Entity").getMethod("c", getNMSClass("NBTTagCompound")));
			methodCache.put("setEntityTag", getNMSClass("Entity").getMethod("f", getNMSClass("NBTTagCompound")));

			if(version.contains("1_12")) {
				methodCache.put("setTileTag", getNMSClass("TileEntity").getMethod("load", getNMSClass("NBTTagCompound")));
			} else {
				methodCache.put("setTileTag", getNMSClass("TileEntity").getMethod("a", getNMSClass("NBTTagCompound")));
			}
			methodCache.put("getTileEntity", getNMSClass("World").getMethod("getTileEntity", getNMSClass("BlockPosition")));
			methodCache.put("getWorldHandle", getNMSClass("CraftWorld").getMethod("getHandle"));

			methodCache.put("setGameProfile", getNMSClass("TileEntitySkull").getMethod("setGameProfile", GameProfile.class));
		} catch(final Exception e) {
			e.printStackTrace();
		}

		try {
			methodCache.put("getTileTag", getNMSClass("TileEntity").getMethod("save", getNMSClass("NBTTagCompound")));
		} catch(final NoSuchMethodException exception) {
			try {
				methodCache.put("getTileTag", getNMSClass("TileEntity").getMethod("b", getNMSClass("NBTTagCompound")));
			} catch(final Exception exception2) {
				exception2.printStackTrace();
			}
		} catch(final Exception exception) {
			exception.printStackTrace();
		}

		constructorCache = new HashMap<>();
		try {
			constructorCache.put(getNBTTag(Byte.class), getNBTTag(Byte.class).getConstructor(byte.class));
			constructorCache.put(getNBTTag(String.class), getNBTTag(String.class).getConstructor(String.class));
			constructorCache.put(getNBTTag(Double.class), getNBTTag(Double.class).getConstructor(double.class));
			constructorCache.put(getNBTTag(Integer.class), getNBTTag(Integer.class).getConstructor(int.class));
			constructorCache.put(getNBTTag(Long.class), getNBTTag(Long.class).getConstructor(long.class));
			constructorCache.put(getNBTTag(Float.class), getNBTTag(Float.class).getConstructor(float.class));
			constructorCache.put(getNBTTag(Short.class), getNBTTag(Short.class).getConstructor(short.class));
			constructorCache.put(getNBTTag(Class.forName("[B")), getNBTTag(Class.forName("[B")).getConstructor(Class.forName("[B")));
			constructorCache.put(getNBTTag(Class.forName("[I")), getNBTTag(Class.forName("[I")).getConstructor(Class.forName("[I")));

			constructorCache.put(getNMSClass("BlockPosition"), getNMSClass("BlockPosition").getConstructor(int.class, int.class, int.class));
		} catch(final Exception e) {
			e.printStackTrace();
		}

		NBTTagFieldCache = new HashMap<>();
		try {
			for(final Class<?> clazz : NBTClasses.values()) {
				final Field data = clazz.getDeclaredField("data");
				data.setAccessible(true);
				NBTTagFieldCache.put(clazz, data);
			}
		} catch(final Exception e) {
			e.printStackTrace();
		}

		try {
			NBTListData = getNMSClass("NBTTagList").getDeclaredField("list");
			NBTListData.setAccessible(true);
			NBTCompoundMap = getNMSClass("NBTTagCompound").getDeclaredField("map");
			NBTCompoundMap.setAccessible(true);
		} catch(final Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Gets an NBT tag in a given block with the specified keys
	 *
	 * @param block
	 * The block to get the keys from
	 * @param key
	 * The keys to fetch; an integer after a key value indicates that it should get the nth place of
	 * the previous compound because it is a list;
	 * @return
	 * The item represented by the keys, and an integer if it is showing how long a list is.
	 */
	public static Object getBlockTag(final Block block, final Object... keys) {
		try {
			if(!getNMSClass("CraftBlockState").isInstance(block.getState())) {
				return null;
			}

			final Object tileEntity = getMethod("getTileEntity").invoke(block.getState());

			final Object tag = getMethod("getTileTag").invoke(tileEntity, getNMSClass("NBTTagCompound").newInstance());

			return getTag(tag, keys);
		} catch(final Exception exception) {
			exception.printStackTrace();
			return null;
		}
	}

	public static Constructor<?> getConstructor(final Class<?> clazz) {
		return constructorCache.containsKey(clazz) ? constructorCache.get(clazz) : null;
	}

	/**
	 * Gets an NBT tag in a given entity with the specified keys
	 *
	 * @param block
	 * The entity to get the keys from
	 * @param key
	 * The keys to fetch; an integer after a key value indicates that it should get the nth place of
	 * the previous compound because it is a list;
	 * @return
	 * The item represented by the keys, and an integer if it is showing how long a list is.
	 */
	public static Object getEntityTag(final Entity entity, final Object... keys) {
		try {
			final Object NMSEntity = getMethod("getEntityHandle").invoke(entity);

			final Object tag = getNMSClass("NBTTagCompound").newInstance();

			getMethod("getEntityTag").invoke(NMSEntity, tag);

			return getTag(tag, keys);
		} catch(final Exception exception) {
			exception.printStackTrace();
			return null;
		}
	}

	public final static ItemStack getHead(final String skinURL) {
		final ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
		if(skinURL == null || skinURL.isEmpty()) {
			return head;
		}
		final ItemMeta headMeta = head.getItemMeta();
		final GameProfile profile = new GameProfile(UUID.randomUUID(), null);
		final byte[] encodedData = Base64.encodeBase64(String.format("{textures:{SKIN:{\"url\":\"%s\"}}}", skinURL).getBytes());
		profile.getProperties().put("textures", new Property("textures", new String(encodedData)));
		Field profileField = null;
		try {
			profileField = headMeta.getClass().getDeclaredField("profile");
		} catch(NoSuchFieldException | SecurityException e) {
			e.printStackTrace();
		}
		profileField.setAccessible(true);
		try {
			profileField.set(headMeta, profile);
		} catch(IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
		head.setItemMeta(headMeta);
		return head;
	}

	/**
	 * Gets an NBT tag in a given item with the specified keys
	 *
	 * @param item
	 * The itemstack to get the keys from
	 * @param key
	 * The keys to fetch; an integer after a key value indicates that it should get the nth place of
	 * the previous compound because it is a list;
	 * @return
	 * The item represented by the keys, and an integer if it is showing how long a list is.
	 */
	public static Object getItemTag(final ItemStack item, final Object... keys) {
		try {
			Object stack = null;
			stack = getMethod("asNMSCopy").invoke(null, item);

			Object tag = null;

			if(getMethod("hasTag").invoke(stack).equals(true)) {
				tag = getMethod("getTag").invoke(stack);
			} else {
				tag = getNMSClass("NBTTagCompound").newInstance();
			}

			return getTag(tag, keys);
		} catch(final Exception exception) {
			exception.printStackTrace();
			return null;
		}
	}

	public static String getMatch(final String string, final String regex) {
		final Pattern pattern = Pattern.compile(regex);
		final Matcher matcher = pattern.matcher(string);
		if(matcher.find()) {
			return matcher.group(1);
		} else {
			return null;
		}
	}

	public static Method getMethod(final String name) {
		return methodCache.containsKey(name) ? methodCache.get(name) : null;
	}

	public static Class<?> getNBTTag(final Class<?> primitiveType) {
		if(NBTClasses.containsKey(primitiveType)) {
			return NBTClasses.get(primitiveType);
		}
		return primitiveType;
	}

	public static Object getNBTVar(final Object object) {
		if(object == null) {
			return null;
		}
		final Class<?> clazz = object.getClass();
		try {
			if(NBTTagFieldCache.containsKey(clazz)) {
				return NBTTagFieldCache.get(clazz).get(object);
			}
		} catch(final Exception exception) {
			exception.printStackTrace();
		}
		return null;
	}

	public static Class<?> getNMSClass(final String name) {
		return Reflection.getClass(ClassEnum.NMS, name);
	}

	public static Class<?> getPrimitiveClass(final Class<?> clazz) {
		return Primitives.unwrap(clazz);
	}

	private static Object getTag(final Object tag, final Object... keys) throws Exception {
		if(keys.length == 0) {
			return getTags(tag);
		}

		Object notCompound = tag;

		for(final Object key : keys) {
			if(notCompound == null) {
				return null;
			}
			if(getNMSClass("NBTTagCompound").isInstance(notCompound)) {
				notCompound = getMethod("get").invoke(notCompound, (String) key);
			} else if(getNMSClass("NBTTagList").isInstance(notCompound)) {
				notCompound = ((List<?>) NBTListData.get(notCompound)).get((int) key);
			} else {
				return getNBTVar(notCompound);
			}
		}
		if(notCompound == null) {
			return null;
		}
		if(getNMSClass("NBTTagList").isInstance(notCompound)) {
			return getTags(notCompound);
		} else if(getNMSClass("NBTTagCompound").isInstance(notCompound)) {
			return getTags(notCompound);
		} else {
			return getNBTVar(notCompound);
		}
	}

	@SuppressWarnings("unchecked")
	private static Object getTags(final Object tag) {
		final HashMap<Object, Object> tags = new HashMap<>();
		try {
			if(getNMSClass("NBTTagCompound").isInstance(tag)) {
				final Map<String, Object> tagCompound = (Map<String, Object>) NBTCompoundMap.get(tag);
				for(final String key : tagCompound.keySet()) {
					final Object value = tagCompound.get(key);
					if(getNMSClass("NBTTagEnd").isInstance(value)) {
						continue;
					}
					tags.put(key, getTag(value));
				}
			} else if(getNMSClass("NBTTagList").isInstance(tag)) {
				final List<Object> tagList = (List<Object>) NBTListData.get(tag);
				for(int index = 0; index < tagList.size(); index++) {
					final Object value = tagList.get(index);
					if(getNMSClass("NBTTagEnd").isInstance(value)) {
						continue;
					}
					tags.put(index, getTag(value));
				}
			} else {
				return getNBTVar(tag);
			}
			return tags;
		} catch(final Exception e) {
			e.printStackTrace();
			return tags;
		}
	}

	public final static String getTexture(final ItemStack head) {
		final ItemMeta meta = head.getItemMeta();
		Field profileField = null;
		try {
			profileField = meta.getClass().getDeclaredField("profile");
		} catch(NoSuchFieldException | SecurityException e) {
			e.printStackTrace();
		}
		profileField.setAccessible(true);
		try {
			final GameProfile profile = (GameProfile) profileField.get(meta);
			if(profile == null) {
				return null;
			}

			for(final Property prop : profile.getProperties().values()) {
				if(prop.getName().equals("textures")) {
					final String texture = new String(Base64.decodeBase64(prop.getValue()));
					return getMatch(texture, "\\{\"url\":\"(.*?)\"\\}");
				}
			}
			return null;
		} catch(IllegalArgumentException | IllegalAccessException | SecurityException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Sets an NBT tag in an block with the provided keys and value
	 *
	 * @param item
	 * The block to set
	 * @param key
	 * The keys to set, String for NBTCompound, int or null for an NBTTagList
	 * @param value
	 * The value to set
	 * @return
	 * A new ItemStack with the updated NBT tags
	 */
	public static void setBlockTag(final Block block, final Object value, final Object... keys) {
		try {
			final Location location = block.getLocation();

			final Object blockPosition = getConstructor(getNMSClass("BlockPosition")).newInstance(location.getBlockX(), location.getBlockY(), location.getBlockZ());

			final Object nmsWorld = getMethod("getWorldHandle").invoke(location.getWorld());

			final Object tileEntity = getMethod("getTileEntity").invoke(nmsWorld, blockPosition);

			final Object tag = getMethod("getTileTag").invoke(tileEntity, getNMSClass("NBTTagCompound").newInstance());

			setTag(tag, value, keys);

			getMethod("setTileTag").invoke(tileEntity, tag);
		} catch(final Exception exception) {
			exception.printStackTrace();
			return;
		}
	}

	/**
	 * Sets an NBT tag in an entity with the provided keys and value
	 *
	 * @param item
	 * The entity to set
	 * @param key
	 * The keys to set, String for NBTCompound, int or null for an NBTTagList
	 * @param value
	 * The value to set
	 * @return
	 * A new ItemStack with the updated NBT tags
	 */
	public static void setEntityTag(final Entity entity, final Object value, final Object... keys) {
		try {
			final Object NMSEntity = getMethod("getEntityHandle").invoke(entity);

			final Object tag = getNMSClass("NBTTagCompound").newInstance();

			getMethod("getEntityTag").invoke(NMSEntity, tag);

			setTag(tag, value, keys);

			getMethod("setEntityTag").invoke(NMSEntity, tag);
		} catch(final Exception exception) {
			exception.printStackTrace();
			return;
		}
	}

	/**
	 * Sets an NBT tag in an item with the provided keys and value
	 *
	 * @param item
	 * The itemstack to set
	 * @param key
	 * The keys to set, String for NBTCompound, int or null for an NBTTagList
	 * @param value
	 * The value to set
	 * @return
	 * A new ItemStack with the updated NBT tags
	 */
	public static ItemStack setItemTag(final ItemStack item, final Object value, final Object... keys) {
		try {
			final Object stack = getMethod("asNMSCopy").invoke(null, item);

			Object tag = null;

			if(getMethod("hasTag").invoke(stack).equals(true)) {
				tag = getMethod("getTag").invoke(stack);
			} else {
				tag = getNMSClass("NBTTagCompound").newInstance();
			}

			setTag(tag, value, keys);
			getMethod("setTag").invoke(stack, tag);
			return (ItemStack) getMethod("asBukkitCopy").invoke(null, stack);
		} catch(final Exception exception) {
			exception.printStackTrace();
			return null;
		}
	}

	public static void setSkullTexture(final Block block, final String texture) {
		final GameProfile profile = new GameProfile(UUID.randomUUID(), null);
		profile.getProperties()
				.put("textures", new com.mojang.authlib.properties.Property("textures", new String(Base64.encodeBase64(String.format("{textures:{SKIN:{\"url\":\"%s\"}}}", texture).getBytes()))));

		try {
			final Location location = block.getLocation();

			final Object blockPosition = getConstructor(getNMSClass("BlockPosition")).newInstance(location.getBlockX(), location.getBlockY(), location.getBlockZ());

			final Object nmsWorld = getMethod("getWorldHandle").invoke(location.getWorld());

			final Object tileEntity = getMethod("getTileEntity").invoke(nmsWorld, blockPosition);

			getMethod("setGameProfile").invoke(tileEntity, profile);
		} catch(final Exception exception) {
			exception.printStackTrace();
		}
	}

	private static void setTag(final Object tag, final Object value, final Object... keys) throws Exception {
		final Object notCompound = getConstructor(getNBTTag(value.getClass())).newInstance(value);

		Object compound = tag;
		for(int index = 0; index < keys.length; index++) {
			final Object key = keys[index];
			if(index + 1 == keys.length) {
				if(key == null) {
					getMethod("add").invoke(compound, notCompound);
				} else if(key instanceof Integer) {
					getMethod("setIndex").invoke(compound, (int) key, notCompound);
				} else {
					getMethod("set").invoke(compound, key, notCompound);
				}
				break;
			}
			final Object oldCompound = compound;
			if(key instanceof Integer) {
				compound = ((List<?>) NBTListData.get(compound)).get((int) key);
			} else if(key != null) {
				compound = getMethod("get").invoke(compound, (String) key);
			}
			if(compound == null || key == null) {
				if(keys[index + 1] == null || keys[index + 1] instanceof Integer) {
					compound = getNMSClass("NBTTagList").newInstance();
				} else {
					compound = getNMSClass("NBTTagCompound").newInstance();
				}
				if(oldCompound.getClass().getSimpleName().equals("NBTTagList")) {
					getMethod("add").invoke(oldCompound, compound);
				} else {
					getMethod("set").invoke(oldCompound, key, compound);
				}
			}
		}
	}
}