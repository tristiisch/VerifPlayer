package fr.tristiisch.verifplayer.gui;

import fr.tristiisch.verifplayer.gui.objects.GuiPage;
import fr.tristiisch.verifplayer.gui.objects.GuiPageVariable;
import fr.tristiisch.verifplayer.utils.config.ConfigGet;

public class VerifPlayerGui {

	public static GuiPage VERIFGUI_HOME = new GuiPage(ConfigGet.MESSAGES_VERIFGUI_NAME.getString(), "&2VerifPlayer &7| &a%player%", 6, GuiPageVariable.PLAYER);
	public static GuiPage PLAYERS_HOME = new GuiPage("&2VerifPlayer &aPlayer List", "&2VerifPlayer &7| &aPlayer List", 6, GuiPageVariable.PLAYER);
	public static GuiPage PLAYERS_CPS = new GuiPage("&2VerifPlayer &aPlayer CPS", "&2VerifPlayer &7| &aPlayer CPS", 6, GuiPageVariable.PLAYER);

	/*
	 * private VerifPlayerGui(String title, String description, int rowSize) {
	 * super(title, description, rowSize); }
	 *
	 * private VerifPlayerGui(String title, String description, int rowSize,
	 * GuiPageVariable... guiPageVariables) { super(title, description, rowSize,
	 * guiPageVariables); }
	 */
}
