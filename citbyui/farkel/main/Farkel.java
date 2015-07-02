package citbyui.farkel.main;

import citbyui.farkel.helpers.Tester;
import citbyui.farkel.players.*;

public class Farkel {

	public static void main(String[] args) {
		Tester.tryAll(2);
		
	}

	public static void play() {
		Player[] players = { new Human("Danny"), new AdvancedAI("Steve") };
		Game testGame = new Game(players);
		testGame.play();
	}

}
