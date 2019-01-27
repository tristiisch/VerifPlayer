package fr.tristiisch.verifplayer.utils;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.apache.commons.lang.WordUtils;

import net.md_5.bungee.api.ChatColor;

public class Utils {

	public static List<String> color(final List<String> l) {
		return l.stream().map(s -> Utils.color(s)).collect(Collectors.toList());
	}

	public static String color(final String s) {
		return s != null ? ChatColor.translateAlternateColorCodes('&', s) : "";
	}

	public static List<String> replaceAll(final List<String> list, final Map<String, String> replace) {
		for(int i = 0; list.size() > i; i++) {
			final String s = list.get(i);
			final List<Entry<String, String>> entryToReplace = replace.entrySet().stream().filter(entry -> s.contains(entry.getKey())).collect(Collectors.toList());
			if(!entryToReplace.isEmpty()) {
				for(final Entry<String, String> entry : entryToReplace) {
					list.set(i, s.replaceAll(entry.getKey(), entry.getValue()));
				}
			}
		}
		return list;
	}

	public static double round(double value, final int x) {
		if(x < 0) {
			throw new IllegalArgumentException();
		}

		final long factor = (long) Math.pow(10, x);
		value = value * factor;
		final long tmp = Math.round(value);
		return (double) tmp / factor;
	}

	public static String secondsToCalendar(final int s) {
		if(s > 60 * 60 * 24) {
			return "**:**";
		} else {
			return LocalTime.ofSecondOfDay(s).toString();
		}
	}

	public static String tickToTime(final int s) {
		return LocalTime.ofSecondOfDay(s / 20).toString();
	}

	public static String timestampToDate(final long timestamp) {
		final SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		return format.format(new Date(timestamp * 1000));
	}

	public static String timestampToDateAndHour(final long timestamp) {
		final SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		return format.format(new Date(timestamp * 1000));
	}

	public static String translateEffects(String s) {
		s = WordUtils.capitalize(s.toLowerCase()).replaceAll("_", " ");
		switch(s) {
		case "Absorption":
			return "Absorption";
		case "Blindness":
			return "aveuglement";
		case "Confusion":
			return "Nausée";
		case "Damage resistance":
			return "Résistance";
		case "Speed":
			return "Vitesse";
		case "Slowness":
			return "Lenteur";
		case "Strength":
			return "Force";
		case "Instant health":
			return "Soin instantané";
		case "Instant damage":
			return "Dégats instatanée";
		case "Regeneration":
			return "Régénération";
		case "Water breathing":
			return "Apnée";
		case "Invisibility":
			return "Invisibilité";
		case "Weakness":
			return "Faiblesse";
		case "Fire resistance":
			return "Résistance au feu";
		case "Night vision":
			return "Vision nocturne";
		case "Jump":
			return "Sauts améliorés";
		default:
			return s;
		}
	}

	public static String withOrWithoutS(final int i) {
		return i < 2 ? "" : "s";
	}
}
