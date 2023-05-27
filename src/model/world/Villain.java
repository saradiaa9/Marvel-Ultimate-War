package model.world;

import java.util.ArrayList;

import exceptions.InvalidTargetException;

public class Villain extends Champion {

	public Villain(String name, int maxHP, int maxMana, int actions, int speed, int attackRange, int attackDamage) {
		super(name, maxHP, maxMana, actions, speed, attackRange, attackDamage);

	}



	
	public void useLeaderAbility(ArrayList<Champion> targets) throws InvalidTargetException {
		
		int length=targets.size();
		for(int i=0;i<length;i++) {
			Champion x=targets.get(i);
			x.setCondition(Condition.KNOCKEDOUT);
			
				
			}
			
		
		}
		
	}

	

