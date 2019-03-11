package fr.tristiisch.verifplayer.utils.gui.api;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.stream.Collectors;

import fr.tristiisch.verifplayer.Main;
import fr.tristiisch.verifplayer.utils.gui.GuiCreator;

public abstract class GuiPage {

	public enum GuiPageVariable {
		PLAYER("PLAYER", 0),
		ATTACKER("ATTACKER", 1),
		VICTIM("VICTIM", 2),
		MODERATOR("MODERATOR", 3),
		ARG1("ARG1", 4),
		ARG2("ARG2", 5),
		ARG3("ARG3", 6),
		ARG4("ARG4", 7);

		private GuiPageVariable(final String s, final int n) {
		}

		public String getName() {
			return this.toString().toLowerCase();
		}

		public String getRegex() {
			return "\\%" + this.getName() + "\\%";
		}
	}

	private final String id;
	private String title;
	private int rowSize;

	private List<GuiPageVariable> variables;

	public GuiPage(final String title, final int rowSize) {
		this.id = title.toLowerCase();
		this.title = title;
		this.rowSize = rowSize;
	}

	public GuiPage(final String title, final int rowSize, final GuiPageVariable... variables) {
		this.id = title.toLowerCase();
		this.title = title;
		this.rowSize = rowSize;
		this.variables = Arrays.asList(variables);
	}

	public int getRowSize() {
		return this.rowSize;
	}

	public int getSize() {
		return this.rowSize * GuiCreator.nbBoxPerRow;
	}

	public String getTitle() {
		return this.title;
	}

	public List<GuiPageVariable> getVariables() {
		return this.variables;
	}

	public String getVariablesNames() {
		return String.join(", ", this.variables.stream().map(GuiPageVariable::getName).collect(Collectors.toSet()));
	}

	public boolean isSamePage(final GuiPage guiPage) {
		return this.id.equals(guiPage.id);
	}

	public void replaceVariable(final GuiPageVariable guiPageVariable, final String replacement) {
		if(this.variables.contains(guiPageVariable)) {
			this.title = this.title.replaceAll(guiPageVariable.getRegex(), replacement);
		} else {
			Main.getInstance().getLogger().log(Level.WARNING, "DEV ERROR: The page called '" + this.getTitle() + "' does not use a variable " + guiPageVariable.getName() + "!");
		}
	}

	public void replaceVariable(final String variableName, final String replacement) {
		this.title = this.title.replaceAll("\\%" + variableName + "\\%", replacement);
	}

	public void setRowSize(final int rowSize) {
		this.rowSize = rowSize;
	}

	public void setTitle(final String title) {
		this.title = title;
	}
}
