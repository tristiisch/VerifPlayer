package fr.tristiisch.verifplayer.utils;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TimeZone;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.apache.commons.lang.WordUtils;

import fr.tristiisch.verifplayer.utils.config.ConfigGet;

public class Utils {

	public static String capitalizeFirstLetter(final String original) {
		if (original == null || original.length() == 0) {
			return original;
		}
		return original.substring(0, 1).toUpperCase() + original.substring(1).toLowerCase();
	}

	public static long getCurrentTimeinSeconds() {
		return System.currentTimeMillis() / 1000L;
	}

	public static List<String> replaceAll(final List<String> list, final Map<String, String> replace) {
		for (int i = 0; list.size() > i; i++) {
			String string = list.get(i);
			for (final Entry<String, String> entry : replace.entrySet()) {

				if (string.contains(entry.getKey())) {
					string = string.replaceAll(entry.getKey(), entry.getValue());
				}

			}
			list.add(i, string);
		}
		return list;
	}

	public static double round(double value, final int x) {
		if (x < 0) {
			throw new IllegalArgumentException();
		}

		final long factor = (long) Math.pow(10, x);
		value = value * factor;
		final long tmp = Math.round(value);
		return (double) tmp / factor;
	}

	public static String secondsToCalendar(final long s) {
		if (s > 60 * 60 * 24) {
			return "**:**";
		} else {
			return LocalTime.ofSecondOfDay(s).toString().replaceFirst("^(00:)", "");
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
		if (timestamp > now) {
			start = now2;
			end = timestamp2;
		} else {
			start = timestamp2;
			end = now2;
		}

		Duration dur = Duration.between(start.toLocalTime(), end.toLocalTime());
		LocalDate e = end.toLocalDate();

		if (dur.isNegative()) {
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

		final List<String> msgs = new ArrayList<>();
		if (year > 1) {
			msgs.add(year + " " + ConfigGet.MESSAGES_TIME_YEARS.getString());
		} else if (year == 1) {
			msgs.add(year + " " + ConfigGet.MESSAGES_TIME_YEAR.getString());
		}

		if (month > 1) {
			msgs.add(month + " " + ConfigGet.MESSAGES_TIME_MONTHS.getString());
		} else if (month == 1) {
			msgs.add(month + " " + ConfigGet.MESSAGES_TIME_MONTH.getString());
		}

		if (msgs.size() < 2) {
			if (day > 1) {
				msgs.add(day + " " + ConfigGet.MESSAGES_TIME_DAYS.getString());
			} else if (day == 1) {
				msgs.add(day + " " + ConfigGet.MESSAGES_TIME_DAY.getString());
			}

			if (msgs.size() < 2) {
				if (hour > 1) {
					msgs.add(hour + " " + ConfigGet.MESSAGES_TIME_HOURS.getString());
				} else if (hour == 1) {
					msgs.add(hour + " " + ConfigGet.MESSAGES_TIME_HOUR.getString());
				}

				if (msgs.size() < 2) {
					if (minute > 1) {
						msgs.add(minute + " " + ConfigGet.MESSAGES_TIME_MINUTES.getString());
					} else if (minute == 1) {
						msgs.add(minute + " " + ConfigGet.MESSAGES_TIME_MINUTE.getString());
					}

					if (msgs.size() < 2) {
						if (second > 1) {
							msgs.add(second + " " + ConfigGet.MESSAGES_TIME_SECONDS.getString());
						} else if (second == 1) {
							msgs.add(second + " " + ConfigGet.MESSAGES_TIME_SECOND.getString());
						}
					}

				}
			}

		}
		return String.join(", ", msgs);
	}

	@SuppressWarnings("unchecked")
	public static <T> Collector<T, ?, List<T>> toShuffledList() {
		Collectors.collectingAndThen(Collectors.toList(), list -> {
			Collections.shuffle(list);
			return (Collector<T, ?, List<T>>) list;
		});
		return null;
	}

	public static String translateEffects(String s) {
		s = WordUtils.capitalize(s.toLowerCase()).replaceAll("_", " ");
		switch (s) {
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
