package fr.tristiisch.verifplayer.core.verifgui;

import org.bukkit.entity.Player;

import fr.tristiisch.verifplayer.gui.api.GuiPage;
import fr.tristiisch.verifplayer.utils.config.ConfigGet;

public class VerifGuiPage extends GuiPage {

	public static VerifGuiPage HOME = new VerifGuiPage(ConfigGet.MESSAGES_VERIFGUI_NAME.getString(), "VerifPlayer of player '%player%'", 6, new GuiPageVariable[] { GuiPageVariable.PLAYER });

	private VerifGuiPage(final String title, final String description, final int rowSize) {
		super(title, description, rowSize);
	}

	private VerifGuiPage(final String title, final String description, final int rowSize, final GuiPageVariable... guiPageVariables) {
		super(title, description, rowSize, guiPageVariables);
	}

	public String getDescription(final Player target) {
		final String description = super.getDescription().toString();
		return this.replaceVariable(description, GuiPageVariable.PLAYER, target.getName());
	}
}
