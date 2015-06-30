package citbyui.farkel.players;

import citbyui.farkel.dice.Roll;
import citbyui.farkel.helpers.UI;

public class TestAI0 extends AdvancedAI {

	public TestAI0(String name) {
		super(name);
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
		double rollScore = 0 - score * farkelChance;
		rollScore += avgScore * (1 - farkelChance);
		rollScore += 450 * rerollChance;

		choice = 0 < rollScore;

		if (dice >= 6) {
			choice = true;
		}

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
}
