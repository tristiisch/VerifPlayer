package fr.tristiisch.verifplayer.gui.objects;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import fr.tristiisch.verifplayer.hook.PlaceholderAPIHook;
import fr.tristiisch.verifplayer.playerinfo.PlayerInfo;

public enum GuiPageVariable {

	PLAYER(PlayerInfo::getName),
	ATTACKER(PlayerInfo::getName),
	VICTIM(PlayerInfo::getName),
	CPS(PlayerInfo::getCurrentClicks),
	CPS_HISTORY_AIR(PlayerInfo::getAirClicks),
	CPS_HISTORY_ENTITY(PlayerInfo::getEntityClicks),
	NUMBER_ALERT(PlayerInfo::getNumberAlert),;

	private Function<? super PlayerInfo, ?> mapper;

	private GuiPageVariable(Function<? super PlayerInfo, ?> mapper) {
		this.mapper = mapper;
	}

	public String changeString(String s, PlayerInfo playerInfo) {

		PlaceholderAPIHook.getInstance().setPlaceholders(playerInfo.getPlayer(), s);
		return s.replaceFirst(this.getRegex(), this.getStringResult(playerInfo));
	}

	@SuppressWarnings("unchecked")
	public List<Integer> getListIntResult(PlayerInfo playerInfo) {
		return (ArrayList<Integer>) this.getResult(playerInfo);
	}

	public String getName() {
		return this.toString().toLowerCase();
	}

	public String getRegex() {
		return "\\%" + this.getName() + "\\%";
	}

	public Object getResult(PlayerInfo playerInfo) {
		return this.mapper.apply(playerInfo);
	}

	public String getStringResult(PlayerInfo playerInfo) {
		return (String) this.getResult(playerInfo);
	}
}
