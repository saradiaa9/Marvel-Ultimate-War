package model.effects;

import java.util.ArrayList;

import model.world.Champion;

public class Shock extends Effect {

	public Shock(int duration) {
		super("Shock", duration, EffectType.DEBUFF);
		
	}

	
	public void apply(Champion c) {
		int speed=c.getSpeed();
		speed=(int)(speed*0.9);
		c.setSpeed(speed);
		int damage=c.getAttackDamage();
		damage=(int)(damage*0.9);
		c.setAttackDamage(damage);
		int max=c.getMaxActionPointsPerTurn();
		max=max-1;
		c.setMaxActionPointsPerTurn(max);
		int curr=c.getCurrentActionPoints();
		curr=curr-1;
		c.setCurrentActionPoints(curr);
		
	}

	
	public void remove(Champion c) {
		ArrayList<Effect> x=c.getAppliedEffects();
		
		int speed=c.getSpeed();
		speed=(int)(speed*(1/0.9));
		c.setSpeed(speed);
		int damage=c.getAttackDamage();
		damage=(int)(damage*(1/0.9));
		c.setAttackDamage(damage);
		int max=c.getMaxActionPointsPerTurn();
		max=max+1;
		c.setMaxActionPointsPerTurn(max);
		int curr=c.getCurrentActionPoints();
		curr=curr+1;
		c.setCurrentActionPoints(curr);
		
	}
	
	

}
