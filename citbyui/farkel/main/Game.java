package citbyui.farkel.main;

import java.util.ArrayList;
import java.util.Random;

import citbyui.farkel.dice.Opportunity;
import citbyui.farkel.dice.Roll;
import citbyui.farkel.exceptions.FarkelException;
import citbyui.farkel.exceptions.FinishedException;
import citbyui.farkel.helpers.Scorer;
import citbyui.farkel.helpers.UI;
import citbyui.farkel.players.Player;

public class Game {
	Player[] players;
	Roll lastRoll;
	boolean finalTurn = false;
	boolean slow = true;
	private Player winner = null;
	public int rounds = 0;
	Scorer scorer = new Scorer();

	public Game(Player[] players) {
		this.players = players;
		for(Player player : players){
			player.setGame(this);
		}
	}

	public Game(Player[] players, boolean slow) {
		this(players);
		this.slow = slow;
	}

	public void play(){
		try {
			while (true) {
				rounds++;
				for (Player player : players) {
					try {
						playTurn(player);
					} catch (FarkelException e) {
						lastRoll = null;
						UI.output("Farkel! \n \n");
						player.getStats().addTurn(0);
						if (slow) {
							UI.pause();
						}

					}
				}
			}
		} catch (FinishedException e) {
			finish();
		}

	}

	public void playTurn(Player player) throws FarkelException,
			FinishedException {
		Roll roll;
		ArrayList<Integer> dice;
		boolean repeat;
		if (player.isFinalTurn()) {
			throw new FinishedException();
		}
		if (finalTurn) {
			player.setFinalTurn(true);
		}
		UI.output("Scores:");
		for (Player scorePlayer : players) {
			UI.output(scorePlayer.getName() + ": " + scorePlayer.getScore());
		}
		UI.output("\n");
		if (player.getScore() < 500) {
			UI.output("Sorry "
					+ player.getName()
					+ ", you're not on the board yet, you need to hit 500 points.");
			lastRoll = null;
		}
		if (lastRoll != null && player.take(lastRoll)) {
			roll = lastRoll;
		} else {
			roll = new Roll();
		}
		repeat = true;
		while (repeat) {
			dice = roll.roll();
			UI.displayRoll(player, dice);
			ArrayList<Opportunity> choices = scorer.score(roll);
			roll = player.choose(choices, roll);
			if ((player.getScore() + roll.getScore()) < 500) {
				UI.output(player.getName()
						+ ", you need to keep rolling until you hit 500. You have "
						+ roll.getScore() + ", so you need "
						+ (500 - roll.getScore()) + " more.");
			} else {
				repeat = player.keepRolling(roll);
			}
		}
		lastRoll = roll;
		player.addPoints(roll.getScore());
		UI.output(player.getName() + " added " + roll.getScore()
				+ " points to get a total of " + player.getScore() + "\n\n");
		if (player.getScore() >= 10000 && !finalTurn) {
			UI.output(player.getName()
					+ " has reached 10,000 points! Everyone else has one more turn, try to beat "
					+ player.getScore() + " points!");
			player.setFinalTurn(true);
			finalTurn = true;
		}
		player.getStats().addTurn(roll.getScore());
	}

	public void finish() {
		int topScore = 0;
		for (Player player : players) {
			if (player.getScore() > topScore) {
				topScore = player.getScore();
				winner = player;
			}
		}
		UI.output(winner.getName() + " has won! The final scores are:");
		for (Player player : players) {
			UI.output(player.getName() + ": " + player.getScore());
		}
	}
	public Player[] randomizeOrder(Player[] players){
		Random rng = new Random();
		Player[] newPlayers = new Player[players.length];
		for(Player player : players){
			int i;
			do{
				i = rng.nextInt(newPlayers.length);
				if(newPlayers[i] == null){
					newPlayers[i] = player;
				}
			}while(newPlayers[i] != player);
		}
		return newPlayers;
	}

	public Player[] getPlayers() {
		return players;
	}

	public boolean isSlow() {
		return slow;
	}

	public void setSlow(boolean slow) {
		this.slow = slow;
	}

	public Player getWinner() {
		return winner;
	}
}
