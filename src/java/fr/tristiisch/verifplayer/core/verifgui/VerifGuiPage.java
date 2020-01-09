package fr.tristiisch.verifplayer.core.verifgui;

import fr.tristiisch.verifplayer.gui.objects.GuiPage;
import fr.tristiisch.verifplayer.gui.objects.GuiPageVariable;
import fr.tristiisch.verifplayer.utils.config.ConfigGet;

public class VerifGuiPage /* extends GuiPage */ {

	public static GuiPage HOME = new GuiPage(ConfigGet.MESSAGES_VERIFGUI_NAME.getString(), "&2VerifPlayer &7| &a%player%", 6, GuiPageVariable.PLAYER);

	/*
	 * private VerifGuiPage(String title, String description, int rowSize) {
	 * super(title, description, rowSize); }
	 *
	 * private VerifGuiPage(String title, String description, int rowSize,
	 * GuiPageVariable... guiPageVariables) { super(title, description, rowSize,
	 * guiPageVariables); }
	 */
}
