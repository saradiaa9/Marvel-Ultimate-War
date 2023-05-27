package model.world;

import java.util.ArrayList;

import exceptions.InvalidTargetException;
import model.effects.Effect;
import model.effects.Stun;

public class AntiHero extends Champion {

	public AntiHero(String name, int maxHP, int maxMana, int actions, int speed, int attackRange, int attackDamage) {
		super(name, maxHP, maxMana, actions, speed, attackRange, attackDamage);

	}

	
	

	
	public void useLeaderAbility(ArrayList<Champion> targets) throws InvalidTargetException {
		
		for(int i=0;i<targets.size();i++) {
			Stun s=new Stun(0);
			Champion t=targets.get(i);
			if(t instanceof AntiHero) {
				s.setDuration(3);
			}
			if(t instanceof Villain) {
				s.setDuration(4);
				
			}
			if(t instanceof Hero) {
				s.setDuration(5);
				
			}
			t.getAppliedEffects().add(s);
			s.apply(t);
			
		}
		
		
	}
}
