package fr.tristiisch.verifplayer.utils;

import java.lang.reflect.Constructor;

import org.bukkit.entity.Player;

import fr.tristiisch.verifplayer.utils.Reflection.ClassEnum;

public class Title {

	public static void clearTitle(Player player) {
		sendTitle(player, "", "", 0, 0, 0);
	}

	/**
	 * Permet de mettre du texte en plein milieu de l\"écran (fadeIn = 20, stay =
	 * 40, fadeOut = 20)
	 *
	 * @param title    String avec gestion des couleurs
	 * @param subtitle String avec gestion des couleurs
	 */
	public static void sendTitle(Player player, String title, String subtitle) {
		sendTitle(player, title, subtitle, 20, 40, 20);
	}

	/**
	 * Permet de mettre du texte en plein milieu de l\"écran
	 *
	 * @param fadeIn   Integer Temps de l\"animation fondu quand le texte apparait
	 *                 (0 = pas d\"animation)
	 * @param stay     Integer Temps de l\"affichage du texte (sans compter les
	 *                 animations)
	 * @param fadeOut  Integer Temps de l\"animation fondu quand le texte
	 *                 disapparait (0 = pas d\"animation)
	 * @param title    String avec gestion des couleurs
	 * @param subtitle String avec gestion des couleurs
	 */
	public static void sendTitle(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
		title = SpigotUtils.color(title);
		subtitle = SpigotUtils.color(subtitle);
		try {

			Object enumTitle = Reflection.getClass(ClassEnum.NMS, "PacketPlayOutTitle").getDeclaredClasses()[0].getField("TITLE").get(null);
			Object titleChat = Reflection.getClass(ClassEnum.NMS, "IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class).invoke(null, "{\"text\":\"" + title + "\"}");

			Object enumSubtitle = Reflection.getClass(ClassEnum.NMS, "PacketPlayOutTitle").getDeclaredClasses()[0].getField("SUBTITLE").get(null);
			Object subtitleChat = Reflection.getClass(ClassEnum.NMS, "IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class).invoke(null, "{\"text\":\"" + subtitle + "\"}");

			Constructor<?> titleConstructor = Reflection.getClass(ClassEnum.NMS, "PacketPlayOutTitle").getConstructor(Reflection.getClass(ClassEnum.NMS, "PacketPlayOutTitle").getDeclaredClasses()[0],
					Reflection.getClass(ClassEnum.NMS, "IChatBaseComponent"), int.class, int.class, int.class);
			Object titlePacket = titleConstructor.newInstance(enumTitle, titleChat, fadeIn, stay, fadeOut);
			Object subtitlePacket = titleConstructor.newInstance(enumSubtitle, subtitleChat, fadeIn, stay, fadeOut);

			Reflection.sendPacket(player, titlePacket);
			Reflection.sendPacket(player, subtitlePacket);

		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	/*
	 * public static void sendTitle(Player player, String title, String
	 * subtitle, Integer fadeIn, Integer stay, Integer fadeOut) {
	 * try { Object e; Object chatTitle; Object chatSubtitle; Constructor<?>
	 * subtitleConstructor; Object titlePacket; Object subtitlePacket;
	 * 
	 * if(title != null) { title = SpigotUtils.color(title); title =
	 * title.replaceAll("%player%", player.getDisplayName());
	 * 
	 * e = Reflection.getClass(ClassEnum.NMS,
	 * "PacketPlayOutTitle").getDeclaredClasses()[0].getField("TIMES").get((Object)
	 * null); chatTitle = Reflection.getClass(ClassEnum.NMS,
	 * "IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", new Class[] {
	 * String.class }) .invoke((Object) null, new Object[] { "{\"text\":\"" + title
	 * + "\"}" }); subtitleConstructor = Reflection.getClass(ClassEnum.NMS,
	 * "PacketPlayOutTitle") .getConstructor(new Class[] {
	 * Reflection.getClass(ClassEnum.NMS,
	 * "PacketPlayOutTitle").getDeclaredClasses()[0],
	 * Reflection.getClass(ClassEnum.NMS, "IChatBaseComponent"), Integer.TYPE,
	 * Integer.TYPE, Integer.TYPE }); titlePacket =
	 * subtitleConstructor.newInstance(new Object[] { e, chatTitle, fadeIn, stay,
	 * fadeOut }); Reflection.sendPacket(player, titlePacket);
	 * 
	 * e = Reflection.getClass(ClassEnum.NMS,
	 * "PacketPlayOutTitle").getDeclaredClasses()[0].getField("TITLE").get((Object)
	 * null); chatTitle = Reflection.getClass(ClassEnum.NMS,
	 * "IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", new Class[] {
	 * String.class }) .invoke((Object) null, new Object[] { "{\"text\":\"" + title
	 * + "\"}" }); subtitleConstructor = Reflection.getClass(ClassEnum.NMS,
	 * "PacketPlayOutTitle") .getConstructor(new Class[] {
	 * Reflection.getClass(ClassEnum.NMS,
	 * "PacketPlayOutTitle").getDeclaredClasses()[0],
	 * Reflection.getClass(ClassEnum.NMS, "IChatBaseComponent") }); titlePacket =
	 * subtitleConstructor.newInstance(new Object[] { e, chatTitle, fadeIn, stay,
	 * fadeOut }); Reflection.sendPacket(player, titlePacket); }
	 * 
	 * if(subtitle != null) { subtitle = SpigotUtils.color(subtitle); subtitle =
	 * subtitle.replaceAll("%player%", player.getDisplayName());
	 * 
	 * e = Reflection.getClass(ClassEnum.NMS,
	 * "PacketPlayOutTitle").getDeclaredClasses()[0].getField("TIMES").get((Object)
	 * null); chatSubtitle = Reflection.getClass(ClassEnum.NMS,
	 * "IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", new Class[] {
	 * String.class }) .invoke((Object) null, new Object[] { "{\"text\":\"" + title
	 * + "\"}" }); subtitleConstructor = Reflection.getClass(ClassEnum.NMS,
	 * "PacketPlayOutTitle") .getConstructor(new Class[] {
	 * Reflection.getClass(ClassEnum.NMS,
	 * "PacketPlayOutTitle").getDeclaredClasses()[0],
	 * Reflection.getClass(ClassEnum.NMS, "IChatBaseComponent"), Integer.TYPE,
	 * Integer.TYPE, Integer.TYPE }); subtitlePacket =
	 * subtitleConstructor.newInstance(new Object[] { e, chatSubtitle, fadeIn, stay,
	 * fadeOut }); Reflection.sendPacket(player, subtitlePacket);
	 * 
	 * e = Reflection.getClass(ClassEnum.NMS,
	 * "PacketPlayOutTitle").getDeclaredClasses()[0].getField("SUBTITLE").get((
	 * Object) null); chatSubtitle = Reflection.getClass(ClassEnum.NMS,
	 * "IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", new Class[] {
	 * String.class }) .invoke((Object) null, new Object[] { "{\"text\":\"" +
	 * subtitle + "\"}" }); subtitleConstructor = Reflection.getClass(ClassEnum.NMS,
	 * "PacketPlayOutTitle") .getConstructor(new Class[] {
	 * Reflection.getClass(ClassEnum.NMS,
	 * "PacketPlayOutTitle").getDeclaredClasses()[0],
	 * Reflection.getClass(ClassEnum.NMS, "IChatBaseComponent"), Integer.TYPE,
	 * Integer.TYPE, Integer.TYPE }); subtitlePacket =
	 * subtitleConstructor.newInstance(new Object[] { e, chatSubtitle, fadeIn, stay,
	 * fadeOut }); Reflection.sendPacket(player, subtitlePacket); } } catch(final
	 * Exception e) { e.printStackTrace(); } }
	 */
}
