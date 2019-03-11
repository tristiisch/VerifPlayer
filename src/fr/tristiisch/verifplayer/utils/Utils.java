package fr.tristiisch.verifplayer.utils;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TimeZone;
import java.util.stream.Collectors;

import org.apache.commons.lang.WordUtils;
import org.bukkit.ChatColor;

public class Utils {

	public static List<String> color(final List<String> l) {
		return l.stream().map(s -> Utils.color(s)).collect(Collectors.toList());
	}

	public static String color(final String s) {
		return s != null ? ChatColor.translateAlternateColorCodes('&', s) : "";
	}

	public static long getCurrentTimeinSeconds() {
		return System.currentTimeMillis() / 1000L;
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

	public static String secondsToCalendar(final long s) {
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

	public static String timestampToDuration(final long timestamp) {

		final long now = Utils.getCurrentTimeinSeconds();
		final LocalDateTime timestamp2 = LocalDateTime.ofInstant(Instant.ofEpochSecond(timestamp), TimeZone.getDefault().toZoneId());
		final LocalDateTime now2 = LocalDateTime.ofInstant(Instant.ofEpochSecond(now), TimeZone.getDefault().toZoneId());

		LocalDateTime start;
		LocalDateTime end;
		if(timestamp > now) {
			start = now2;
			end = timestamp2;
		} else {
			start = timestamp2;
			end = now2;
		}

		Duration dur = Duration.between(start.toLocalTime(), end.toLocalTime());
		LocalDate e = end.toLocalDate();

		if(dur.isNegative()) {
			dur = dur.plusDays(1);
			e = e.minusDays(1);
		}
		final Period per = Period.between(start.toLocalDate(), e);

		final long year = per.getYears();
		final long month = per.getMonths();
		final long day = per.getDays();
		final long hour = dur.toHours();
		final long minute = dur.toMinutes() - 60 * dur.toHours();
		final long second = dur.getSeconds() - 60 * dur.toMinutes();

		final List<String> msg = new ArrayList<>();
		if(year > 1) {
			msg.add(year + " ans");
		} else if(year == 1) {
			msg.add(year + " an");
		}
		if(month != 0) {
			msg.add(month + " mois");
		}

		if(day > 1) {
			msg.add(day + " jours");
		} else if(day == 1) {
			msg.add(day + " jour");
		}
		if(hour > 1) {
			msg.add(hour + " heures");
		} else if(hour == 1) {
			msg.add(hour + " heure");
		}
		if(minute > 1) {
			msg.add(minute + " minutes");
		} else if(minute == 1) {
			msg.add(minute + " minute");
		}
		if(second > 1) {
			msg.add(second + " secondes");
		} else if(second == 1) {
			msg.add(second + " seconde");
		}
		// System.out.println("Timestamp " + timestamp + " " + year + "year " + month +
		// "month " + day + "day " + hour + "hour " + minute + "minute " + second +
		// "second ");

		final List<String> msgs = new ArrayList<>();
		for(final String message : msg) {
			if(message != null) {
				msgs.add(message);
			}
			if(msgs.size() >= 2) {
				break;
			}
		}
		return String.join(", ", msgs);
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
