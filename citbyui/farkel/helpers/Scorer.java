package citbyui.farkel.helpers;

import java.util.ArrayList;

import citbyui.farkel.dice.Opportunity;
import citbyui.farkel.dice.Roll;
import citbyui.farkel.exceptions.FarkelException;

public class Scorer {

	public static ArrayList<Opportunity> score(Roll roll)throws FarkelException {
		ArrayList<Opportunity> choices = new ArrayList<Opportunity>();
		ArrayList<Integer> dice = convertRoll(roll);
		if (dice.size() == 6) {
			Opportunity choice = checkSix(dice);
			if(choice != null){
				choices.add(choice);
			}
		}
		for (int i = 5; i >= 3; i--) {
			if (dice.size() >= i) {
				Opportunity choice = checkForOfAKind(i, dice);
				if (choice != null) {
					choices.add(choice);
				}
			}
		}

		ArrayList<Opportunity> oldChoices = new ArrayList<Opportunity>();
		choices.add(new Opportunity(new ArrayList<Integer>(),0,roll.getDice()));
		for (Opportunity choice : choices) {
			removeNeeded(choice,dice);
			ArrayList<Integer> left = choice.getLeft();
			while(left.contains(1)){
				oldChoices.add(choice.clone());
				left = choice.addPoints(1, 100);
			}
			while(left.contains(5)){
				oldChoices.add(choice.clone());
				left = choice.addPoints(5, 50);
			}
			
		}
		choices.addAll(oldChoices);
		ArrayList<Integer> toRemove = new ArrayList<Integer>();
		for(Opportunity choice:choices){
			if(choice.getScore()==0){
				toRemove.add(choices.indexOf(choice));
			}
		}
		int offset = 0;
		for(int num: toRemove){
			choices.remove(num-offset);
			offset++;
		}
		if(choices.isEmpty()){
			throw new FarkelException();
		}

		return choices;
	}

	public static ArrayList<Integer> convertRoll(Roll roll) {
		ArrayList<Integer> newList = new ArrayList<Integer>();
		for (int die : roll.getDice()) {
			newList.add(die);
		}
		return newList;
	}

	public static int countOf(int target, ArrayList<Integer> dice) {
		int count = 0;
		for (int die : dice) {
			if (target == die) {
				count++;
			}
		}
		return count;
	}

	@SuppressWarnings("unchecked")
	public static Opportunity checkForOfAKind(int num, ArrayList<Integer> dice) {
		int points;
		ArrayList<Integer> diceClone = (ArrayList<Integer>) dice.clone();
		for (int i = 1; i <= 6; i++) {
			if (countOf(i, dice) == num) {
				ArrayList<Integer> needed = new ArrayList<Integer>();
				for (int j = 0; j < num; j++) {
					needed.add(i);
					diceClone.remove(diceClone.indexOf(i));
				}
				switch (num) {
				case 3:
					if (i == 1) {
						return null;
					} else {
						points = i * 100;
					}
					break;
				case 4:
					points = 1000;
					break;
				case 5:
					points = 2000;
					break;
				case 6:
					points = 3000;
					break;
				default:
					System.out
							.println("ERROR: bad number sent to checkForOfAKind(): Expected 3,4,5,or 6, got: "
									+ num);
					return null;
				}
				return new Opportunity(needed, points,diceClone);
			}
		}
		return null;
	}

	public static Opportunity checkSix(ArrayList<Integer> dice) {
		// checking for six of a kind
		Opportunity choice = checkForOfAKind(6, dice);
		if (choice != null) {
			return choice;
		}

		// checking for 1-6 run
		boolean run = true;
		for (int i = 1; i <= 6; i++) {
			if (dice.contains(i)) {
			} else {
				run = false;
				break;
			}
		}
		if (run) {
			ArrayList<Integer> needed = new ArrayList<Integer>();
			for (int i = 1; i <= 6; i++) {
				needed.add(i);
			}
			return new Opportunity(needed, 1500,new ArrayList<Integer>());
		}

		// checking for 3 pairs
		ArrayList<Integer> needed = new ArrayList<Integer>();
		int pairCount = 0;
		for (int i = 1; i <= 6; i++) {
			if (countOf(i, dice) == 2) {
				pairCount++;
				needed.add(i);
				needed.add(i);
			}
		}
		if (pairCount == 3) {
			return new Opportunity(needed, 1500,new ArrayList<Integer>());
		}

		// checking for 2 triplets
		needed = new ArrayList<Integer>();
		int tripleCount = 0;
		for (int i = 1; i <= 6; i++) {
			if (countOf(i, dice) == 3) {
				tripleCount++;
				needed.add(i);
				needed.add(i);
				needed.add(i);
			}
		}
		if (tripleCount == 2) {
			return new Opportunity(needed, 2500,new ArrayList<Integer>());
		}
		return null;
	}

	public static void removeNeeded(Opportunity choice,ArrayList<Integer>dice){
		ArrayList<Integer> needed= choice.getNeeded();
		@SuppressWarnings("unchecked")
		ArrayList<Integer> left = (ArrayList<Integer>) dice.clone();
		for(int need:needed){
			left.remove(left.indexOf(need));
		}
		choice.setLeft(left);
	}

}
