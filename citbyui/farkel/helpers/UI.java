package citbyui.farkel.helpers;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

import citbyui.farkel.dice.Opportunity;
import citbyui.farkel.players.Player;

public class UI {
	static Scanner scanner = new Scanner(System.in);

	public static boolean confirm(String message) {
		System.out.println(message);
		String command = scanner.nextLine();
		ArrayList<String> yes = new ArrayList<String>();
		ArrayList<String> no = new ArrayList<String>();
		yes.add("Yes");
		yes.add("yes");
		yes.add("y");
		yes.add("1");
		no.add("No");
		no.add("no");
		no.add("n");
		no.add("0");
		if(yes.contains(command)){
			return true;
		}else if(no.contains(command)){
			return false;
		}else{
			UI.output("input not recognized, try again.");
			return confirm(message);
		}
	}

	public static void output(String message) {
		System.out.println(message);
	}

	public static int intPrompt(String message) {
		System.out.println(message);
		return scanner.nextInt();
	}

	public static void displayRoll(Player player, ArrayList<Integer> dice) {
		System.out.println(player.getName() + " rolled: ");
		displayDice(dice);
	}

	public static Opportunity choose(ArrayList<Opportunity> choices) {
		for (Opportunity choice : choices) {
			output("Option " + choices.indexOf(choice) + ": for "
					+ choice.getScore() + " points with "
					+ choice.getLeft().size() + " dice left to roll.");
			displayDice(choice.getNeeded());
		}
		int command;
		do {
			try{
			command = scanner.nextInt();
			}catch(InputMismatchException e){
				output("Invalid input. Please enter an integer");
				command = 0;
			}
		} while (command > choices.size() || command < 0);
		return choices.get(command);
	}

	public static void displayDice(ArrayList<Integer> dice) {
		
		String[] lines = { "", "", "", "", "" };
		for (int die : dice) {
			lines[0] += " +-----+";
			if (die == 1) {
				lines[1] += " |     |";
			} else if (die < 4) {
				lines[1] += " |0    |";
			} else {
				lines[1] += " |0   0|";
			}
			if (die % 2 == 1) {
				lines[2] += " |  0  |";
			} else if (die == 6) {
				lines[2] += " |0   0|";
			} else {
				lines[2] += " |     |";
			}
			if (die == 1) {
				lines[3] += " |     |";
			} else if (die < 4) {
				lines[3] += " |    0|";
			} else {
				lines[3] += " |0   0|";
			}
			lines[4] += " +-----+";
		}
		for (String line : lines) {
			output(line);
		}
	}
	
	public static void error(String message){
		System.out.println("Error: "+message);
	}
	
	public static void pause(){
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {}
	}

}
