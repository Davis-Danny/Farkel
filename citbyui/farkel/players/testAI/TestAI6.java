package citbyui.farkel.players.testAI;

import java.util.ArrayList;

import citbyui.farkel.dice.Opportunity;
import citbyui.farkel.dice.Roll;
import citbyui.farkel.main.Game;
import citbyui.farkel.players.AdvancedAI;
import citbyui.farkel.players.Player;
import citbyui.farkel.stats.StatBean;
import citbyui.farkel.stats.StatBuilder;

public class TestAI6 extends AdvancedAI {


	public TestAI6(String name) {
		super(name);
	}

	@Override
	public boolean worthRolling(Roll roll) {
		Boolean choice = null;
		int dice = roll.getDiceLeft();
		int score = roll.getScore();
		StatBean stats = StatBuilder.getBean(dice);
		double farkelChance = stats.getFarkelChance();
		stats.getAvgScore();
		stats.getRerollChance();

		// If it's the final turn, keep rolling until you beat the top score or
			//farkel
		if (isFinalTurn()) {
			int totalScore = getScore() + score;
			for (Player player : getGame().getPlayers()) {
				if (player.getScore() > totalScore) {
					return true;
				}
			}
		}

		// use calculated probabilities to determine whether it is worth it to roll
		double rollScore = 0 - score * farkelChance;
		rollScore += stats.getRollWorth();
		choice = 0 < rollScore;
		
		return choice;
	}

	public int getTopScore() {
		Game game = getGame();
		int topScore = 0;
		for (Player player : game.getPlayers()) {
			if(player==this){
				continue;
			}else if (player.getScore() > topScore) {
				topScore = player.getScore();
			}
		}
		return topScore;
	}
	
	public double getCompMult(){
		int left = 10000-getScore();
		int otherLeft = 10000-getTopScore();
		double mult = (double)left/(otherLeft);
		if(mult<1){
			return 1;
		}
		
		return 1.25;
	}
	
	protected Opportunity analyzeChoices(
			ArrayList<Opportunity> choices, Roll oldRoll) {

		// if there are choices that use all the dice, set those aside
		ArrayList<Opportunity> newChoices = new ArrayList<Opportunity>();
		for (Opportunity choice : choices) {
			if (choice.getLeft().size() == 0) {
				newChoices.add(choice);
				ArrayList<Integer> left = new ArrayList<Integer>();
				for(int i=0;i<6;i++){
					left.add(1);
				}
				choice.setLeft(left);
			}
		}

		// if any choices were set aside, replace the full list with that one
		if (newChoices.size() > 0) {
			choices = newChoices;
		}

		// iterate through all the options, assigning a score to each one
		double topScore = 0;
		Opportunity bestChoice = null;
		for (Opportunity choice : choices) {
			int score = choice.getScore() + oldRoll.getScore();
			StatBean bean = StatBuilder.getBean(choice.getLeft().size());
			Roll roll = new Roll(choice.getLeft().size(), score);
			double rollScore = score;

			// if we're going to roll if we choose this option, build a score
			// based on
			// the probability of farkeling or gaining more points
			if (worthRolling(roll)) {
				rollScore -= score * bean.getFarkelChance();
				rollScore += bean.getRollWorth();
			}

			// if the calculated score for this option is better than any we've
			// already found,
			// record the score and the option
			if (rollScore >= topScore) {
				bestChoice = choice;
				topScore = rollScore;
			}
		}
		
		// return the choice with the highest score
		return bestChoice;
	}
	
	public boolean worthTaking(Roll roll) {

		// calculate worth of offered roll
		StatBean bean = StatBuilder.getBean(roll.getDiceLeft());
		double offered = roll.getScore();
		offered *= (1 - bean.getFarkelChance());
		offered += bean.getRollWorth()*getCompMult();


		// calculate worth of new roll
		bean = StatBuilder.getBean(6);
		double newRoll = bean.getRollWorth();		
		
		//compare the two values- if the offered roll is more valuable than a new roll,
			//take it.
		return offered >= newRoll;
	}

}
