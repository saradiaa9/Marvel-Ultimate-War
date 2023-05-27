package model.effects;

import java.util.ArrayList;

import model.world.Champion;

public class SpeedUp extends Effect{

	public SpeedUp(int duration) {
		super("SpeedUp",duration,EffectType.BUFF);
	}

	
	public void apply(Champion c) {
		int speed=c.getSpeed();
		speed=(int)(speed*1.15);
		c.setSpeed(speed);
		int max=c.getMaxActionPointsPerTurn();
		max=max+1;
		c.setMaxActionPointsPerTurn(max);
		int curr=c.getCurrentActionPoints();
		curr=curr+1;
		c.setCurrentActionPoints(curr);
		
	}

	
	public void remove(Champion c) {
		
		int speed=c.getSpeed();
		speed=(int)(speed*(1/1.15));
		c.setSpeed(speed);
		int max=c.getMaxActionPointsPerTurn();
		max=max-1;
		c.setMaxActionPointsPerTurn(max);
		int curr=c.getCurrentActionPoints();
		curr=curr-1;
		c.setCurrentActionPoints(curr);
		
	}
	
	

}
