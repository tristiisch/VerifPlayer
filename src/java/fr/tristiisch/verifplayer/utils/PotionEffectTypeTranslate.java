package fr.tristiisch.verifplayer.utils;

import net.md_5.bungee.api.chat.TranslatableComponent;

public enum PotionEffectTypeTranslate {

	EMPTY(),
	SPEED("moveSpeed"),
	SLOW("moveSlowdown"),
	FAST_DIGGING("digSpeed"),
	SLOW_DIGGING("digSlowDown"),
	INCREASE_DAMAGE("damageBoost"),
	HEAL(),
	HARM(),
	JUMP(),
	CONFUSION(),
	REGENERATION(),
	DAMAGE_RESISTANCE("resistance"),
	FIRE_RESISTANCE(),
	WATER_BREATHING(),
	INVISIBILITY(),
	BLINDNESS(),
	NIGHT_VISION(),
	HUNGER(),
	WEAKNESS(),
	POISON(),
	WITHER(),
	HEALTH_BOOST(),
	ABSORPTION(),
	SATURATION();

	String path;

	private PotionEffectTypeTranslate(String path) {
		this.path = path;
	}

	private PotionEffectTypeTranslate() {
		this.path = new StringBuilder("potion.").append(this.toString().toLowerCase().replace("_", "")).toString();
	}

	public String getPath() {
		return this.path;
	}

	public TranslatableComponent getTranslate() {
		TranslatableComponent potionEffectTransltated = new TranslatableComponent(this.getPath());
		return potionEffectTransltated;
	}
}
