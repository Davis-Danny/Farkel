package citbyui.farkel.main;

import citbyui.farkel.helpers.UI;
import citbyui.farkel.players.*;

public class Startup {

	public static void setup(){
		int playerNum = UI.intPrompt("How many players will there be?");
		Player[] players = new Player[playerNum];
		for(int i = 0;i<playerNum;i++){
			String name = UI.stringPrompt("What is player "+(i+1)+"'s name?");
			String type = UI.stringPrompt("Is "+name+" a human(H) or computer(C) player?");
			players[i] = buildPlayer(name,type);
		}
		do{
		Game game = new Game(players);
		game.play();
		for (Player player : players) {
			player.setScore(0);
			player.setFinalTurn(false);
		}
		}while(UI.confirm("Would you like to play again?"));
			
		
	}
	
   public static Player buildPlayer(String name,String type){
	   switch(type){
	   case "H":
		   return new Human(name);
	   case "C":
		   type = UI.stringPrompt("What level? Basic(B), Moderate(M), or Advanced(A)?");
		   return buildPlayer(name,type);
	   case "B":
		   return new BasicAI(name);
	   case "M":
		   return new ModerateAI(name);
	   case "A":
		   return new AdvancedAI(name);
	   default:
		   type = UI.stringPrompt("Input not recognized, please try again.");
		   return buildPlayer(name,type);
	   }   
   }
}
