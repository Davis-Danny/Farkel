package citbyui.farkel.players;

import citbyui.farkel.dice.Roll;
import citbyui.farkel.helpers.StatBean;
import citbyui.farkel.helpers.StatBuilder;

public class TestAI0 extends AdvancedAI {

	public TestAI0(String name) {
		super(name);
	}

	
	public boolean worthTaking(Roll roll){
		
		//calculate worth of offered roll
		StatBean bean = StatBuilder.getBean(roll.getDiceLeft());
		double offered = bean.getAvgScore()+roll.getScore();
		offered += 450 * bean.getRerollChance();
		offered *= (1 - bean.getFarkelChance());
		
		//calculate worth of new roll
		double newRoll = 0;
		bean = StatBuilder.getBean(6);
		newRoll += bean.getAvgScore() * (1 - bean.getFarkelChance());
		newRoll += 450 * bean.getRerollChance();
		
		return offered>=newRoll;
	}
	
	public double calculateWorth(Roll roll){
		double rollScore = roll.getScore();
		StatBean bean = StatBuilder.getBean(roll.getDiceLeft());
		rollScore -= roll.getScore() * bean.getFarkelChance();
		rollScore += bean.getAvgScore() * (1 - bean.getFarkelChance());
		rollScore += 450 * bean.getRerollChance();
		return rollScore;
	}
	
}
