package citbyui.farkel.players;

import java.util.ArrayList;
import java.util.Random;

import citbyui.farkel.dice.Opportunity;
import citbyui.farkel.dice.Roll;
import citbyui.farkel.exceptions.FarkelException;
import citbyui.farkel.helpers.UI;

public class BasicAI extends Player {
	Random rng;

	public BasicAI(String name) {
		super(name);
		rng = new Random();

	}

	@Override
	public boolean take(Roll roll) {
		boolean choice = rng.nextBoolean();
		if(choice){
			UI.output(getName()+" has taken the roll");
		}else{
			UI.output(getName()+" has chosen not to take the roll");
		}
		return choice;
	}

	@Override
	public Roll choose(ArrayList<Opportunity> choices, Roll roll)
			throws FarkelException {
		int i = rng.nextInt(choices.size());
		Opportunity choice = choices.get(i);
		UI.output(getName() + " is choosing:");
		UI.displayDice(choice.getNeeded());
		roll.setDiceLeft(choice.getLeft().size());
		roll.addPoints(choice.getScore());
		return roll;
	}

}
