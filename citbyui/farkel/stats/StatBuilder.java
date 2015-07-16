package citbyui.farkel.stats;

import java.util.ArrayList;

import citbyui.farkel.dice.Opportunity;
import citbyui.farkel.dice.Roll;
import citbyui.farkel.exceptions.FarkelException;
import citbyui.farkel.helpers.Scorer;
import citbyui.farkel.helpers.UI;
import citbyui.farkel.main.Game;
import citbyui.farkel.players.AdvancedAI;
import citbyui.farkel.players.Player;

public class StatBuilder {
	private static Scorer scorer = new Scorer();;
	private static StatBean[] beans = new StatBean[6];
	private static boolean built = false;

	public StatBuilder(Scorer rules) {
		boolean build = false;
		for (int i = 0; i < 6; i++) {
			if (beans[i] == null) {
				build = true;
			}
		}
		if (build) {
			buildBeans();
		}
	}

	public static void buildBeans() {
		UI.debug("Building statBeans...");
		boolean oldDisplay = UI.isDisplay();
		UI.setDisplay(false);
		for (int i = 0; i < 6; i++) {
			if (beans[i] == null) {
				beans[i] = buildBean(i + 1);
			}
		}
		UI.setDisplay(oldDisplay);
		UI.debug("statBean building complete.");
		built = true;
	}

	public static StatBean getBean(int num) {
		if (!built) {
			buildBeans();
		}
		return beans[num - 1];

	}

	public static StatBean buildBean(int num) {
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
				int topScore = 0;
				Opportunity choice = null;
				for(Opportunity option:choices){
					if(option.getScore()>topScore){
						choice = option;
					}
				}

				if (scores.contains(choice.getScore())) {
					int index = scores.indexOf(choice.getScore());
					scoreCount.set(index, scoreCount.get(index) + 1);
				} else {
					scoreCount.add(1);
					scores.add(choice.getScore());
				}
				if (choice.getLeft().size() == 6) {
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
		int avgScore = (int) (round(totalScore / rolls));
		
		double rollWorth;
		switch(num){
			case 1:
				rollWorth = 66.5;
				break;
			case 2:
				rollWorth = 67.2;
				break;
			case 3:
				rollWorth = 98.5;
				break;
			case 4:
				rollWorth = 168.2;
				break;
			case 5:
				rollWorth = 292.5;
				break;
			case 6:
				rollWorth = 573;
				break;
			default:
				UI.error("unexpected value in UI.buildbean");
				return null;
		}	
		

		return new StatBean(farkelChance, avgScore, rerollChance,rollWorth);

	}

	public static double round(double input) {
		double output = input;
		output *= 100;
		output = Math.floor(output);
		output /= 100;
		return output;
	}

	public static boolean isBuilt() {
		return built;
	}

	public static void setBuilt(boolean built) {
		StatBuilder.built = built;
	}
	
}
