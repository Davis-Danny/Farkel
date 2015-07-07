package citbyui.farkel.helpers;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import citbyui.farkel.dice.Opportunity;
import citbyui.farkel.dice.Roll;
import citbyui.farkel.exceptions.FarkelException;
import citbyui.farkel.main.Game;
import citbyui.farkel.players.*;

public class Tester {
	static Scorer scorer = new Scorer();

	public static int carlScore;
	public static int steveScore;

	public static void test() {
		test(50000);
	}

	public static void test(int iterNum) {
		long startTime = System.nanoTime();
		carlScore = 0;
		steveScore = 0;
		UI.setDisplay(false);
		int totalRounds = 0;
		int leastRounds = 100;
		int percentComplete = 0;
		for (int i = 0; i < iterNum; i++) {
			Player[] players = { new TestAI0("Steve"), new AdvancedAI("Carl") };
			Game testGame = new Game(players, false);
			testGame.play();
			totalRounds += testGame.rounds;
			if (testGame.rounds < leastRounds) {
				leastRounds = testGame.rounds;
			}
			if (testGame.getWinner().getName() == "Carl") {
				carlScore++;
			} else if (testGame.getWinner().getName() == "Steve") {
				steveScore++;
			}
			if (i % (iterNum / 25) == (iterNum / 25 - 1)) {
				percentComplete += 4;
				UI.debug((i + 1) + " games complete. (" + percentComplete
						+ "%)");
			}
		}
		UI.setDisplay(true);
		long endTime = System.nanoTime();
		long elapsedTime = endTime - startTime;
		long time = TimeUnit.NANOSECONDS.toSeconds(elapsedTime);
		UI.output("Completed " + iterNum + " games in " + time + " seconds.");
		UI.output("Final scores: \n Steve: " + steveScore + "\n Carl: "
				+ carlScore);
		UI.output("Win percentage: "
				+ (100 * ((double) steveScore / (steveScore + carlScore)))
				+ "%.");
		UI.output("Average rounds taken:" + totalRounds / iterNum);
		UI.output("Shortest game:" + leastRounds + " rounds");
	}

	public static void tryAll(int num) {
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

		UI.output("Rolls:" + rolls);
		UI.output("Rerolls:" + rerolls + " Probability: "
				+ round((double) rerolls / rolls) + "%");
		UI.output("farkels: " + farkels);
		UI.output("Farkel chance: " + round((double) farkels / rolls) + "%");
		UI.output("Average score: " + round(totalScore / rolls) / 100);
		UI.output("Farkel adjusted average score: "
				+ round(totalScore / (rolls - farkels)) / 100);
		UI.output("Scores:");

		ArrayList<ArrayList<Integer>> sorted = sort(scoreCount, scores);
		scores = sorted.get(1);
		scoreCount = sorted.get(0);
		for (int i = 0; i < scores.size(); i++) {
			UI.output((scores.get(i) + ": " + scoreCount.get(i) + " Probability: ")
					+ round((double) scoreCount.get(i) / rolls) + "%");
		}
	}

	public static double round(double input) {
		double output = input;
		output *= 10000;
		output = Math.floor(output);
		output /= 100;
		return output;
	}

	public static ArrayList<ArrayList<Integer>> sort(
			ArrayList<Integer> subject, ArrayList<Integer> bystander) {
		ArrayList<Integer> newSubject = new ArrayList<Integer>();
		ArrayList<Integer> newBystander = new ArrayList<Integer>();
		while (!subject.isEmpty()) {
			int topNum = 0;
			int index = 0;
			for (int num : subject) {
				if (num > topNum) {
					topNum = num;
					index = subject.indexOf(num);
				}
			}
			newSubject.add(subject.remove(index));
			newBystander.add(bystander.remove(index));
		}
		ArrayList<ArrayList<Integer>> output = new ArrayList<ArrayList<Integer>>();
		output.add(newSubject);
		output.add(newBystander);
		return output;
	}

	public static void testRounds(int iterNum) {
		int totalRounds = 0;
		for (int i = 0; i < iterNum; i++) {
			Player[] players = { new AdvancedAI("Steve") };
			Game testGame = new Game(players, false);
			testGame.play();
			totalRounds += testGame.rounds;
		}
		UI.output("Average rounds taken:" + totalRounds / iterNum);
	}
}
