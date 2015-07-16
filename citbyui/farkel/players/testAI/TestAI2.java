package citbyui.farkel.players.testAI;

import citbyui.farkel.dice.Roll;
import citbyui.farkel.players.AdvancedAI;
import citbyui.farkel.stats.StatBuilder;

public class TestAI2 extends AdvancedAI {

	public TestAI2(String name) {
		super(name);
	}
	
	@Override
	public boolean keepRolling(Roll roll){
		if((35750/roll.getScore())>StatBuilder.getBean(roll.getDiceLeft()).getFarkelChance()*100){
			return true;
		}else{
			return false;
		}
	}

}
