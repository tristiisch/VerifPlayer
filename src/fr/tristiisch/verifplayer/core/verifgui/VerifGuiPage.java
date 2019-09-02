package fr.tristiisch.verifplayer.core.verifgui;

import fr.tristiisch.verifplayer.gui.objects.GuiPage;
import fr.tristiisch.verifplayer.gui.objects.GuiPageVariable;
import fr.tristiisch.verifplayer.utils.config.ConfigGet;

public class VerifGuiPage /* extends GuiPage */ {

	public static GuiPage HOME = new GuiPage(ConfigGet.MESSAGES_VERIFGUI_NAME.getString(), "VerifPlayer of player '%player%'", 6, GuiPageVariable.PLAYER);

	/*
	 * private VerifGuiPage(final String title, final String description, final int
	 * rowSize) { super(title, description, rowSize); }
	 * 
	 * private VerifGuiPage(final String title, final String description, final int
	 * rowSize, final GuiPageVariable... guiPageVariables) { super(title,
	 * description, rowSize, guiPageVariables); }
	 */
}
