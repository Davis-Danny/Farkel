package citbyui.farkel.dice;

import java.util.ArrayList;
import java.util.Random;

import citbyui.farkel.exceptions.FarkelException;

public class Roll {
	private int diceLeft;
	private int score;
	private Random rng;
	private ArrayList<Integer > dice;

	public Roll(int diceLeft, int score) {
		this.diceLeft = diceLeft;
		this.score = score;
		rng = new Random();
	}

	public Roll() {
		this(6, 0);
	}

	public ArrayList<Integer> roll(int numDice) {
		ArrayList<Integer > rolls = new ArrayList<Integer>();
		for (int i = 0; i < numDice; i++) {
			rolls.add(1 + rng.nextInt(6));
		}
		dice = rolls;
		return rolls;
	}

	public ArrayList<Integer> roll() {
		return roll(diceLeft);
	}

	public int getScore() {
		return score;
	}

	public int getDiceLeft() {
		return diceLeft;
	}

	public ArrayList<Integer > getDice() {
		if (dice == null) {
			roll();
		}
		return dice;
	}

	public int addPoints(int newPoints) {
		score += newPoints;
		return score;
	}

	public void setDiceLeft(int newDiceLeft) {
		if (newDiceLeft == 0) {
			diceLeft = 6;
		} else {
			diceLeft = newDiceLeft;
		}
	}

	public void farkel() throws FarkelException {
		score = 0;
		diceLeft = 0;
		throw new FarkelException();
	}
}
