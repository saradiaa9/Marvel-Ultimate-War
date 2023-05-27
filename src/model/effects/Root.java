package model.effects;

import java.util.ArrayList;

import model.world.Champion;
import model.world.Condition;

public class Root extends Effect {

	public Root( int duration) {
		super("Root", duration, EffectType.DEBUFF);
		
	}

	public void apply(Champion c) {
		if(c.getCondition()==Condition.INACTIVE)
			c.setCondition(Condition.INACTIVE);
		else
			c.setCondition(Condition.ROOTED);
	
		
	}

	
	public void remove(Champion c) {
	
		if(c.getCondition()==Condition.INACTIVE)
			c.setCondition(Condition.INACTIVE);
		for(int i=0;i<c.getAppliedEffects().size();i++) {
			if(c.getAppliedEffects().get(i) instanceof Root) {
				c.getAppliedEffects().remove(i);
				break;
			}
		}
		for(int i=0;i<c.getAppliedEffects().size();i++) {
			if(c.getAppliedEffects().get(i) instanceof Root) {
				c.setCondition(Condition.ROOTED);
			}
			else
				c.setCondition(Condition.ACTIVE);
		}
			
	}


}
