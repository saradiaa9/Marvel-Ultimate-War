package model.effects;

import java.util.ArrayList;

import model.world.Champion;

public class Silence extends Effect {

	public Silence( int duration) {
		super("Silence", duration, EffectType.DEBUFF);
		
	}

	
	public void apply(Champion c) {
		int max=c.getMaxActionPointsPerTurn();
		max=max+2;
		c.setMaxActionPointsPerTurn(max);
		int curr=c.getCurrentActionPoints();
		curr=curr+2;
		c.setCurrentActionPoints(curr);
		
	}

	
	public void remove(Champion c) {
		
		int max=c.getMaxActionPointsPerTurn();
		max=max-2;
		c.setMaxActionPointsPerTurn(max);
		int curr=c.getCurrentActionPoints();
		curr=curr-2;
		c.setCurrentActionPoints(curr);
		
	}
	

}
