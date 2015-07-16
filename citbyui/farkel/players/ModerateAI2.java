package citbyui.farkel.players;

import java.util.ArrayList;
import java.util.Random;

import citbyui.farkel.dice.Opportunity;
import citbyui.farkel.dice.Roll;
import citbyui.farkel.helpers.UI;

public class ModerateAI2 extends AI {
	Random rng;

	public ModerateAI2(String name) {
		super(name);
		rng = new Random();

	}

	@Override
	public boolean worthTaking(Roll roll) {
		Boolean choice = null;
		int dice = roll.getDiceLeft();
		int score = roll.getScore();
		if(dice>=4 ){
			choice = true;
		}else if(dice == 3){
			choice = score > 500;
		}else if(dice == 2){
			choice = score > 800;
		}else if(dice == 1){
			choice = score > 1000;
		}else{
			UI.error("take in ModerateAI recieved unexpected roll.getDiceLeft() \n Expected: <1-6> Received: "+dice);
		}
		return choice;
	}
	
	@Override
	public boolean worthRolling(Roll roll){
		Boolean choice = null;
		int dice = roll.getDiceLeft();
		int score = roll.getScore();
		if(isFinalTurn()){
			int totalScore = getScore() + score;
			for(Player player:getGame().getPlayers()){
				if(player.getScore()>totalScore){
					return true;
				}
			}
		}
		if(dice>=4 ){
			choice = true;
		}else if(dice == 3){
			choice = score < 2500;
		}else if(dice == 2){
			choice = score < 800;
		}else if(dice == 1){
			choice = score < 200;
		}else{
			UI.error("keepRolling in ModerateAI recieved unexpected roll.getDiceLeft() \n Expected: <1-6> Received: "+dice);
		}
		return choice;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Opportunity analyzeChoices(ArrayList<Opportunity> choices,Roll oldRoll) {
		ArrayList<Opportunity> newChoices = new ArrayList<Opportunity>();
		for (Opportunity choice : choices) {
			if (choice.getLeft().size() == 0) {
				newChoices.add(choice);
			}
		}
		if (newChoices.size() > 0) {
			choices = (ArrayList<Opportunity>) newChoices.clone();
		}
		int topScore = 0;
		Opportunity bestChoice = null;
		for (Opportunity choice : choices) {
			if (choice.getScore() > topScore) {
				bestChoice = choice;
				topScore = choice.getScore();
			}
		}
		return bestChoice;
	}

}
