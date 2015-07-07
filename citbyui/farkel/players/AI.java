package citbyui.farkel.players;

import java.util.ArrayList;
import citbyui.farkel.dice.Opportunity;
import citbyui.farkel.dice.Roll;
import citbyui.farkel.exceptions.FarkelException;
import citbyui.farkel.helpers.UI;

public abstract class AI extends Player {

	public AI(String name) {
		super(name);

	}

	@Override
	public boolean take(Roll roll) {
		Boolean choice = worthTaking(roll);
		
		if (choice) {
			UI.output(getName() + " has taken the roll");
		} else {
			UI.output(getName() + " has chosen not to take the roll");
		}
		if (getGame().isSlow()) {
			UI.pause();
		}
		return choice;
	}

	@Override
	public boolean keepRolling(Roll roll) {
		boolean choice = worthRolling(roll);
		
		// display choice
		if (choice) {
			UI.output(getName() + " has chosen to keep rolling");
		} else {
			UI.output(getName() + " has chosen not to keep rolling");
		}
		if (getGame().isSlow()) {
			UI.pause();
		}
		return choice;
	}

	@Override
	public Roll choose(ArrayList<Opportunity> choices, Roll roll)
			throws FarkelException {
		Opportunity choice = analyzeChoices(choices, roll);

		//display choice
		UI.output(getName() + " is choosing:");
		UI.displayDice(choice.getNeeded());
		roll.setDiceLeft(choice.getLeft().size());
		roll.addPoints(choice.getScore());
		if (getGame().isSlow()) {
			UI.pause();
		}
		return roll;
	}

	protected abstract Opportunity analyzeChoices(
			ArrayList<Opportunity> choices, Roll oldRoll);

	public abstract boolean worthRolling(Roll roll);

	public abstract boolean worthTaking(Roll roll);
}
