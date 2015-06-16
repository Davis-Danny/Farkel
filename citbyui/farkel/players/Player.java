package citbyui.farkel.players;

import java.util.ArrayList;

import citbyui.farkel.dice.Opportunity;
import citbyui.farkel.dice.Roll;
import citbyui.farkel.exceptions.FarkelException;
import citbyui.farkel.main.Game;

public abstract class Player {
	private boolean finalTurn = false;
	private int score;
	private String name;
	private Game game;
	
	public Player(String name){
		score = 0;
		this.name = name;
	}
	
	public int getScore(){
		return score;
	}
		
	public abstract boolean take(Roll roll);
	
	public String getName(){
		return name;
	}
	
	public void addPoints(int points){
		score += points;
	}
	
	public boolean isFinalTurn(){
		return finalTurn;
	}
	public void setFinalTurn(boolean isFinished){
		finalTurn = isFinished;
	}

	public abstract Roll choose(ArrayList<Opportunity> choices, Roll roll)
			throws FarkelException;
	
	public boolean keepRolling(Roll roll){
		return take(roll);
	}
	
	public void setGame(Game game){
		this.game = game;
	}
	
	public Game getGame(){
		return game;
	}
}