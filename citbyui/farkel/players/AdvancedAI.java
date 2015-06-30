package citbyui.farkel.players;

import java.util.ArrayList;
import java.util.Random;

import citbyui.farkel.dice.Opportunity;
import citbyui.farkel.dice.Roll;
import citbyui.farkel.exceptions.FarkelException;
import citbyui.farkel.helpers.UI;

public class AdvancedAI extends Player {
	Random rng;

	public AdvancedAI(String name) {
		super(name);
		rng = new Random();

	}

	@Override
	public boolean take(Roll roll) {
		Boolean choice = null;
		int dice = roll.getDiceLeft();
		int score = roll.getScore();
		if(dice>=4 ){
			choice = true;
		}else if(dice == 3){
			choice = score > 500;
		}else if(dice == 2){
			choice = score > 800;
		}else if(dice == 1){
			choice = score > 1000;
		}else{
			UI.error("take in ModerateAI recieved unexpected roll.getDiceLeft() \n Expected: <1-6> Received: "+dice);
		}
		if(choice){
			UI.output(getName()+" has taken the roll");
		}else{
			UI.output(getName()+" has chosen not to take the roll");
		}
		if(getGame().isSlow()){
			UI.pause();
		}
		return choice;
	}
	
	@Override
	public boolean keepRolling(Roll roll) {
		double farkelChance;
		double avgScore;
		double rerollChance;
		Boolean choice = null;
		int dice = roll.getDiceLeft();
		int score = roll.getScore();
		// If it's the final turn, keep rolling until you beat the top score or
		// farkel
		if (isFinalTurn()) {
			int totalScore = getScore() + score;
			for (Player player : getGame().getPlayers()) {
				if (player.getScore() > totalScore) {
					return true;
				}
			}
		}

		switch (dice) {
		case 1:
			farkelChance = .6666;
			avgScore = 25;
			rerollChance = .3333;
			break;
		case 2:
			farkelChance = .4444;
			avgScore = 50;
			rerollChance = .1111;
			break;
		case 3:
			farkelChance = .2777;
			avgScore = 83;
			rerollChance = .0555;
			break;
		case 4:
			farkelChance = .1574;
			avgScore = 132;
			rerollChance = .0401;
			break;
		case 5:
			farkelChance = .0771;
			rerollChance = .0303;
			avgScore = 203;
			break;
		case 6:
			farkelChance = .0231;
			avgScore = 384;
			rerollChance = .0779;
			break;
		default:
			UI.error("unexpected value in switch in keepRolling");
			return false;
		}

		// experiment- try building scores based on probability
		double rollScore = 0 - (score * farkelChance);
		rollScore += avgScore;
		rollScore += 450 * rerollChance;

		choice = 0 < rollScore;

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
		Opportunity choice = null;
		choices = analyzeChoices(choices);

		if (choice == null) {
			int i = rng.nextInt(choices.size());
			choice = choices.get(i);
		}
		UI.output(getName() + " is choosing:");
		UI.displayDice(choice.getNeeded());
		roll.setDiceLeft(choice.getLeft().size());
		roll.addPoints(choice.getScore());
		if(getGame().isSlow()){
			UI.pause();
		}
		return roll;
	}

	@SuppressWarnings("unchecked")
	protected ArrayList<Opportunity> analyzeChoices(ArrayList<Opportunity> choices) {
		ArrayList<Opportunity> newChoices = new ArrayList<Opportunity>();
		for (Opportunity choice : choices) {
			if (choice.getLeft().size() == 0) {
				newChoices.add(choice);
			}
		}
		if (newChoices.size() > 0) {
			choices = (ArrayList<Opportunity>) newChoices.clone();
		}
		int topScore = 0;
		Opportunity bestChoice = null;
		for (Opportunity choice : choices) {
			if (choice.getScore() > topScore) {
				bestChoice = choice;
				topScore = choice.getScore();
			}
		}
		if (bestChoice != null) {
			choices.clear();
			choices.add(bestChoice);
		}
		return choices;
	}

}
