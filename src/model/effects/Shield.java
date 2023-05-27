package model.effects;

import java.util.ArrayList;

import model.world.Champion;

public class Shield extends Effect {

	public Shield( int duration) {
		super("Shield", duration, EffectType.BUFF);
		
	}

	
	public void apply(Champion c) {
		int speed=c.getSpeed();
		speed=(int)(speed*1.02);
		c.setSpeed(speed);
	
		
	}

	
	public void remove(Champion c) {
		
		int speed=c.getSpeed();
		speed=(int)(speed*(1/1.02));
		c.setSpeed(speed);
		
	}
	
	

}
