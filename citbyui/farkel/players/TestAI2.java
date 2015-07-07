package citbyui.farkel.players;

import citbyui.farkel.dice.Roll;
import citbyui.farkel.helpers.StatBuilder;

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
