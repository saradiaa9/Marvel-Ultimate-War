package model.effects;

import java.util.ArrayList;

import model.world.Champion;
import model.world.Condition;

public class Stun extends Effect {

	public Stun(int duration) {
		super("Stun", duration, EffectType.DEBUFF);
	}

	public void apply(Champion c) {
		c.setCondition(Condition.INACTIVE);
	     
		
	}

	
	public void remove(Champion c) {// check for root
		for(int i=0;i<c.getAppliedEffects().size();i++) {
			if(c.getAppliedEffects().get(i) instanceof Root) {
				c.setCondition(Condition.ROOTED);
				break;
			}
		}
		if(c.getCondition()==Condition.INACTIVE)
			c.setCondition(Condition.ACTIVE);
	
		
	}
	
	


}
