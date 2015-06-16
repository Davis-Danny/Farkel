package citbyui.farkel.players;

import java.util.ArrayList;
import java.util.Random;

import citbyui.farkel.dice.Opportunity;
import citbyui.farkel.dice.Roll;
import citbyui.farkel.exceptions.FarkelException;
import citbyui.farkel.helpers.UI;

public class TestAI0 extends Player {
	Random rng;

	public TestAI0(String name) {
		super(name);
		rng = new Random();

	}

	@Override
	public boolean take(Roll roll) {
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
		if(choice){
			UI.output(getName()+" has taken the roll");
		}else{
			UI.output(getName()+" has chosen not to take the roll");
		}
		if(getGame().isSlow()){
			UI.pause();
		}
		return choice;
	}
	
	@Override
	public boolean keepRolling(Roll roll){
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
			choice = score < 300;
		}else{
			UI.error("keepRolling in ModerateAI recieved unexpected roll.getDiceLeft() \n Expected: <1-6> Received: "+dice);
		}
		if(choice){
			UI.output(getName()+" has chosen to keep rolling");
		}else{
			UI.output(getName()+" has chosen not to keep rolling");
		}
		if(getGame().isSlow()){
			UI.pause();
		}
		return choice;
	}

	@Override
	public Roll choose(ArrayList<Opportunity> choices, Roll roll)
			throws FarkelException {
		Opportunity choice = null;
		choices = analyzeChoices(choices);

		if (choice == null) {
			int i = rng.nextInt(choices.size());
			choice = choices.get(i);
		}
		UI.output(getName() + " is choosing:");
		UI.displayDice(choice.getNeeded());
		roll.setDiceLeft(choice.getLeft().size());
		roll.addPoints(choice.getScore());
		if(getGame().isSlow()){
			UI.pause();
		}
		return roll;
	}

	@SuppressWarnings("unchecked")
	private ArrayList<Opportunity> analyzeChoices(ArrayList<Opportunity> choices) {
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
		if (bestChoice != null) {
			choices.clear();
			choices.add(bestChoice);
		}
		return choices;
	}

}
