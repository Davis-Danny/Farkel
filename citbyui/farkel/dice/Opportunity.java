package citbyui.farkel.dice;

import java.util.ArrayList;

public class Opportunity implements Cloneable {

	ArrayList<Integer> neededDice;
	int score;
	ArrayList<Integer> leftDice;

	public Opportunity(ArrayList<Integer> neededDice, int score,
			ArrayList<Integer> leftDice) {
		this.neededDice = neededDice;
		this.score = score;
		this.leftDice = leftDice;
	}

	public Opportunity(Object[] array, int score) {
		for (int i = 0; i < array.length; i++) {
			neededDice.add(i, (int) array[i]);
		}
		this.score = score;
	}

	public ArrayList<Integer> getNeeded() {
		return neededDice;
	}

	public ArrayList<Integer> getLeft() {
		return leftDice;
	}

	public void setLeft(ArrayList<Integer> newLeft) {
		leftDice = newLeft;
	}

	public void addPoints(ArrayList<Integer> dice, int score) {
		for (int die : dice) {
			neededDice.add(die);
			leftDice.remove(leftDice.indexOf(die));
		}
		this.score += score;
	}

	public ArrayList<Integer> addPoints(int die, int score) {
		neededDice.add(die);
		leftDice.remove(leftDice.indexOf(die));
		this.score += score;
		return leftDice;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Opportunity clone() {
		Opportunity clone = new Opportunity(
				(ArrayList<Integer>) neededDice.clone(), score,
				(ArrayList<Integer>) leftDice.clone());
		clone.setLeft((ArrayList<Integer>) leftDice.clone());
		return clone;
	}

	public int getScore() {
		return score;
	}

}
