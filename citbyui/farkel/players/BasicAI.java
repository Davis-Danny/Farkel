package citbyui.farkel.players;

import java.util.ArrayList;
import java.util.Random;

import citbyui.farkel.dice.Opportunity;
import citbyui.farkel.dice.Roll;

public class BasicAI extends AI {
	Random rng;

	public BasicAI(String name) {
		super(name);
		rng = new Random();

	}
	
	@Override
	protected Opportunity analyzeChoices(ArrayList<Opportunity> choices,
			Roll oldRoll) {
		int i = rng.nextInt(choices.size());
		Opportunity choice = choices.get(i);
		return choice;
	}

	@Override
	public boolean worthRolling(Roll roll) {
		return rng.nextBoolean();
	}

	@Override
	public boolean worthTaking(Roll roll) {
		return rng.nextBoolean();
	}

}
