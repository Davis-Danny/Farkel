package citbyui.farkel.helpers;

import java.util.ArrayList;

import citbyui.farkel.dice.Opportunity;
import citbyui.farkel.dice.Roll;
import citbyui.farkel.exceptions.FarkelException;
import citbyui.farkel.main.Game;
import citbyui.farkel.players.AdvancedAI;
import citbyui.farkel.players.Player;

public class StatBuilder {
	private static Scorer scorer = new Scorer();;
	private static StatBean[] beans = new StatBean[6];

	public StatBuilder(Scorer rules) {
		for (int i = 0; i < 6; i++) {
			if (beans[i] == null) {
				beans[i] = tryAll(i + 1);
			}
		}
	}

	public StatBean getBean(int num) {
		return beans[num - 1];
	}

	public static StatBean tryAll(int num) {
		Player dummy = new AdvancedAI("Dummy");
		Player[] dummyArray = { dummy };
		Game dummyGame = new Game(dummyArray);
		dummyGame.setSlow(false);
		int farkels = 0;
		int rolls = 0;
		int rerolls = 0;
		int totalScore = 0;
		ArrayList<Integer> scores = new ArrayList<Integer>();
		ArrayList<Integer> scoreCount = new ArrayList<Integer>();
		Roll roll;
		ArrayList<Integer> list;
		ArrayList<Opportunity> choices;
		dummy.setGame(dummyGame);
		list = new ArrayList<Integer>();
		for (int i = 0; i < num; i++) {
			list.add(1);
		}
		boolean repeat = true;
		int last = list.size() - 1;
		while (repeat) {
			roll = new Roll();
			UI.displayDice(list);
			roll.setDice(list);
			rolls++;
			try {
				// get the opportunities for roll
				// If the roll is a Farkel, throws FarkelException and moves to
				// catch
				choices = scorer.score(roll);
				// send the opportunities to a dummy AI to choose the best
				// option
				Roll choice = dummy.choose(choices, roll);

				if (scores.contains(choice.getScore())) {
					int index = scores.indexOf(choice.getScore());
					scoreCount.set(index, scoreCount.get(index) + 1);
				} else {
					scoreCount.add(1);
					scores.add(choice.getScore());
				}
				if (choice.getDiceLeft() == 6) {
					rerolls++;
				}
				totalScore += choice.getScore();
			} catch (FarkelException e) {
				farkels++;
				UI.output("Farkel");
			}

			// add one to the last die
			list.set(last, list.get(last) + 1);
			// check every die to see if it's over six
			for (int i = last; i >= 0; i--) {
				if (list.get(i) > 6) {
					if (i <= 0) {
						repeat = false;
						break;
					} else {
						list.set(i - 1, list.get(i - 1) + 1);
						list.set(i, 1);
					}
				}
			}
		}

		double rerollChance = round((double) rerolls / rolls);
		double farkelChance = round((double) farkels / rolls);
		int avgScore = (int) (round(totalScore / rolls) / 100);

		return new StatBean(farkelChance, avgScore, rerollChance);

	}

	public static double round(double input) {
		double output = input;
		output *= 10000;
		output = Math.floor(output);
		output /= 100;
		return output;
	}
}
