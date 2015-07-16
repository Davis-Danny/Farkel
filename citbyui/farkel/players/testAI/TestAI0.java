package citbyui.farkel.players.testAI;

import citbyui.farkel.dice.Roll;
import citbyui.farkel.players.AdvancedAI;
import citbyui.farkel.stats.StatBean;
import citbyui.farkel.stats.StatBuilder;

public class TestAI0 extends AdvancedAI {

	public TestAI0(String name) {
		super(name);
	}

	public boolean worthTaking(Roll roll) {
		//UI.debug(getName() + " has a chance to take " + roll.getScore()
				//+ " points and " + roll.getDiceLeft() + " dice.");

		// calculate worth of offered roll
		StatBean bean = StatBuilder.getBean(roll.getDiceLeft());
		double offered = bean.getAvgScore() + roll.getScore();
		offered *= (1 - bean.getFarkelChance());
		offered += 450 * bean.getRerollChance();


		// calculate worth of new roll
		double newRoll = 0;
		bean = StatBuilder.getBean(6);
		newRoll += bean.getAvgScore() * (1 - bean.getFarkelChance());
		newRoll += 450 * bean.getRerollChance();
		if (offered >= 538) {
			//UI.debug(getName() + " is choosing to take the roll");
		} else {
			//UI.debug(getName() + " is choosing not to take the roll");
		}
		//UI.pause();
		return offered >= 538;
	}

}
