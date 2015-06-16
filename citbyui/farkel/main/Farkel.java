package citbyui.farkel.main;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import citbyui.farkel.helpers.UI;
import citbyui.farkel.players.*;

public class Farkel {
	public static int carlScore;
	public static int steveScore;

	public static void main(String[] args) {
		test(2000);
	}

	public static void test(int iterNum) {
		carlScore = 0;
		steveScore = 0;
		for (int i = 0; i < iterNum; i++) {
			Player[] players;
			if (i % 2 == 1) {
				Player[] newPlayers = { new TestAI0("Carl"),
						new AdvancedAI("Steve") };
				players = newPlayers;
			} else {
				Player[] newPlayers = { new TestAI1("Steve"),
						new ModerateAI("Carl") };
				players = newPlayers;
			}
			Game testGame = new Game(players, false);
			testGame.play();
			if (testGame.getWinner().getName() == "Carl") {
				carlScore++;
			} else if (testGame.getWinner().getName() == "Steve") {
				steveScore++;
			}
		}
		UI.output("Final scores: \n Steve: " + steveScore + "\n Carl: "
				+ carlScore);
		UI.output("Win percentage: "
				+ (100 * ((double) steveScore / (steveScore + carlScore)))
				+ "%.");
	}

	public static void threadTest(int iterNum) {
		carlScore = 0;
		steveScore = 0;
		Executor executor = Executors.newFixedThreadPool(50);
		Farkel dummyFarkel = new Farkel();
		for (int i = 0; i < iterNum; i++) {
			executor.execute(dummyFarkel.new FarkelTest());
		}
		executor.execute(dummyFarkel.new Notifier());
		try {
			executor.wait();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		UI.output("Final scores: \n Steve: " + steveScore + "\n Carl: "
				+ carlScore);
		UI.output("Win percentage: "
				+ (100 * ((double) steveScore / (steveScore + carlScore)))
				+ "%.");
	}

	public static void play() {
		Player[] players = { new Human("Danny"), new ModerateAI("Steve") };
		Game testGame = new Game(players);
		testGame.play();
	}

	public class FarkelTest implements Runnable {
		Player winner;

		@Override
		public void run() {
			Player[] players = { new ModerateAI("Carl"),
					new AdvancedAI("Steve") };
			Game testGame = new Game(players, false);
			testGame.play();
			winner = testGame.getWinner();

			if (testGame.getWinner().getName() == "Carl") {
				carlScore++;
			} else if (testGame.getWinner().getName() == "Steve") {
				steveScore++;
			}
		}

	}

	public class Notifier implements Runnable {
		public void run() {
			notify();
		}
	}
}
