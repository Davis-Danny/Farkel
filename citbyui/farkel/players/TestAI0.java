package citbyui.farkel.players;

import java.util.ArrayList;

import citbyui.farkel.dice.Opportunity;
import citbyui.farkel.dice.Roll;
import citbyui.farkel.helpers.Scorer;
import citbyui.farkel.helpers.StatBean;
import citbyui.farkel.helpers.StatBuilder;
import citbyui.farkel.helpers.UI;

public class TestAI0 extends AdvancedAI {
	StatBuilder statBuilder = new StatBuilder(new Scorer());

	public TestAI0(String name) {
		super(name);
	}

	public boolean worthRolling(Roll roll) {
		Boolean choice = null;
		int dice = roll.getDiceLeft();
		int score = roll.getScore();
		StatBean stats = statBuilder.getBean(dice);
		double farkelChance = stats.getFarkelChance();
		double avgScore = stats.getAvgScore();
		double rerollChance = stats.getRerollChance();

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

		// experiment- try building scores based on probability
		double rollScore = 0 - score * farkelChance;
		rollScore += avgScore * (1 - farkelChance);
		rollScore += 450 * rerollChance;

		choice = 0 < rollScore;

		if (dice >= 6) {
			choice = true;
		}

		return choice;
	}

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

	@SuppressWarnings("unchecked")
	@Override
	protected ArrayList<Opportunity> analyzeChoices(
			ArrayList<Opportunity> choices, Roll oldRoll) {
		ArrayList<Opportunity> newChoices = new ArrayList<Opportunity>();
		for (Opportunity choice : choices) {
			if (choice.getLeft().size() == 0) {
				newChoices.add(choice);
			}
		}
		if (newChoices.size() > 0) {
			return (ArrayList<Opportunity>) newChoices.clone();
		}
		double topScore = 0;
		Opportunity bestChoice = null;

		for (Opportunity choice : choices) {
			int score = choice.getScore() + oldRoll.getScore();
			StatBean bean = statBuilder.getBean(choice.getLeft().size());
			Roll roll = new Roll(choice.getLeft().size(), score);
			double rollScore = score;

			if (worthRolling(roll)) {
				rollScore -= score * bean.getFarkelChance();
				rollScore += bean.getAvgScore() * (1 - bean.getFarkelChance());
				//rollScore += 450 * bean.getRerollChance();
			}
			if (rollScore >= topScore) {
				bestChoice = choice;
				topScore = rollScore;
			}
		}
		/*
		 * for (Opportunity choice : choices) { if (choice.getScore() >
		 * topScore) { bestChoice = choice; topScore = choice.getScore(); } }
		 */

		if (bestChoice != null) {
			choices.clear();
			choices.add(bestChoice);
		}

		return choices;
	}
}
