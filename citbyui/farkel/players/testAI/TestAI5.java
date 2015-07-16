package citbyui.farkel.players.testAI;

import citbyui.farkel.dice.Roll;
import citbyui.farkel.stats.StatBean;
import citbyui.farkel.stats.StatBuilder;

public class TestAI5 extends TestAI4 {

	public TestAI5(String name) {
		super(name);
	}

	public boolean worthTaking(Roll roll) {
		//UI.debug(getName() + " has a chance to take " + roll.getScore()
				//+ " points and " + roll.getDiceLeft() + " dice.");

		// calculate worth of offered roll
		StatBean bean = StatBuilder.getBean(roll.getDiceLeft());
		double offered = roll.getScore();
		offered *= (1 - bean.getFarkelChance());
		offered += bean.getRollWorth();


		// calculate worth of new roll
		bean = StatBuilder.getBean(6);
		double newRoll = bean.getRollWorth();		
		
		if (offered >= 538) {
			//UI.debug(getName() + " is choosing to take the roll");
		} else {
			//UI.debug(getName() + " is choosing not to take the roll");
		}
		//UI.pause();
		return offered >= newRoll;
	}

}
