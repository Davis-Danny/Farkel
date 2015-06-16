package citbyui.farkel.players;

import java.util.ArrayList;

import citbyui.farkel.dice.Opportunity;
import citbyui.farkel.dice.Roll;
import citbyui.farkel.exceptions.FarkelException;
import citbyui.farkel.helpers.UI;

public class Human extends Player {

	public Human(String name) {
		super(name);
	}

	public boolean take(Roll roll) {
		return UI.confirm(getName() + ", Do you want to roll "
				+ roll.getDiceLeft() + " dice? You currently have "
				+ roll.getScore() + " points.");
	}

	@Override
	public Roll choose(ArrayList<Opportunity> choices, Roll roll)
			throws FarkelException {
		Opportunity choice = UI.choose(choices);
		roll.setDiceLeft(choice.getLeft().size());
		roll.addPoints(choice.getScore());
		return roll;
	}

}
