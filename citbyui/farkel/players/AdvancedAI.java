package citbyui.farkel.players;

import java.util.ArrayList;

import citbyui.farkel.dice.Opportunity;
import citbyui.farkel.dice.Roll;
import citbyui.farkel.helpers.StatBean;
import citbyui.farkel.helpers.StatBuilder;
import citbyui.farkel.helpers.UI;

public class AdvancedAI extends AI {

	public AdvancedAI(String name) {
		super(name);

	}

	@Override
	public boolean worthTaking(Roll roll) {
		Boolean choice = null;
		int dice = roll.getDiceLeft();
		int score = roll.getScore();
		if (dice >= 4) {
			choice = true;
		} else if (dice == 3) {
			choice = score > 500;
		} else if (dice == 2) {
			choice = score > 800;
		} else if (dice == 1) {
			choice = score > 1000;
		} else {
			UI.error("take in ModerateAI recieved unexpected roll.getDiceLeft() \n Expected: <1-6> Received: "
					+ dice);
		}
		return choice;
	}

	@SuppressWarnings("unchecked")
	protected Opportunity analyzeChoices(
			ArrayList<Opportunity> choices, Roll oldRoll) {

		// if there are choices that use all the dice, set those aside
		ArrayList<Opportunity> newChoices = new ArrayList<Opportunity>();
		for (Opportunity choice : choices) {
			if (choice.getLeft().size() == 0) {
				newChoices.add(choice);
				ArrayList<Integer> left = new ArrayList<Integer>();
				for(int i=0;i<6;i++){
					left.add(1);
				}
				choice.setLeft(left);
			}
		}

		// if any choices were set aside, replace the full list with that one
		if (newChoices.size() > 0) {
			choices = (ArrayList<Opportunity>) newChoices.clone();
		}

		// iterate through all the options, assigning a score to each one
		double topScore = 0;
		Opportunity bestChoice = null;
		for (Opportunity choice : choices) {
			int score = choice.getScore() + oldRoll.getScore();
			StatBean bean = StatBuilder.getBean(choice.getLeft().size());
			Roll roll = new Roll(choice.getLeft().size(), score);
			double rollScore = score;

			// if we're going to roll if we choose this option, build a score
			// based on
			// the probability of farkeling or gaining more points
			if (worthRolling(roll)) {
				rollScore -= score * bean.getFarkelChance();
				rollScore += bean.getAvgScore() * (1 - bean.getFarkelChance());
				rollScore += 450 * bean.getRerollChance();
			}

			// if the calculated score for this option is better than any we've
			// already found,
			// record the score and the option
			if (rollScore >= topScore) {
				bestChoice = choice;
				topScore = rollScore;
			}
		}
		
		// return the choice with the highest score
		return bestChoice;
	}

	
	//determine whether it's better to roll the given dice or bank
	public boolean worthRolling(Roll roll) {
		Boolean choice = null;
		int dice = roll.getDiceLeft();
		int score = roll.getScore();
		StatBean stats = StatBuilder.getBean(dice);
		double farkelChance = stats.getFarkelChance();
		double avgScore = stats.getAvgScore();
		double rerollChance = stats.getRerollChance();

		// If it's the final turn, keep rolling until you beat the top score or
			//farkel
		if (isFinalTurn()) {
			int totalScore = getScore() + score;
			for (Player player : getGame().getPlayers()) {
				if (player.getScore() > totalScore) {
					return true;
				}
			}
		}

		// use calculated probabilities to determine whether it is worth it to roll
		double rollScore = 0 - score * farkelChance;
		rollScore += avgScore * (1 - farkelChance);
		rollScore += 450 * rerollChance;
		choice = 0 < rollScore;
		
		return choice;
	}
}
