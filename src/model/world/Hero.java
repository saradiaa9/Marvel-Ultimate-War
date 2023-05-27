package model.world;

import java.util.ArrayList;

import exceptions.InvalidTargetException;
import model.effects.Effect;
import model.effects.EffectType;
import model.effects.Embrace;

public class Hero extends Champion {

	public Hero(String name, int maxHP, int maxMana, int actions, int speed, int attackRange, int attackDamage) {
		super(name, maxHP, maxMana, actions, speed, attackRange, attackDamage);

	}



	
	public void useLeaderAbility(ArrayList<Champion> targets) throws InvalidTargetException {
		
		int length=targets.size();
		for(int i=0;i<length;i++) {
			Champion x=targets.get(i);
			ArrayList<Effect> y=x.getAppliedEffects();
			ArrayList<Effect> z=new ArrayList<Effect>();
			for(int j=0;j<y.size();j++) {
				if(y.get(j).getType()==EffectType.DEBUFF) {
					z.add(y.get(j));
				}
			}
			for(int j=0;j<z.size();j++) {
				y.remove(z.get(j));
			}
			Embrace e=new Embrace(2);
			x.getAppliedEffects().add(e);
			e.apply(x);
			
			
		}
		
	}

	
}
