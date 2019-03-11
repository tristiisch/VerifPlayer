package fr.tristiisch.verifplayer.verifgui;

import fr.tristiisch.verifplayer.utils.ConfigUtils;
import fr.tristiisch.verifplayer.utils.gui.api.GuiPage;

public class VerifGuiPage extends GuiPage {

	public static VerifGuiPage HOME = new VerifGuiPage(ConfigUtils.VERIFGUI_NAME.getString(), 6, new GuiPageVariable[] { GuiPageVariable.PLAYER });

	private VerifGuiPage(final String title, final int rowSize) {
		super(title, rowSize);
	}

	private VerifGuiPage(final String title, final int rowSize, final GuiPageVariable... guiPageVariables) {
		super(title, rowSize, guiPageVariables);
	}
}
