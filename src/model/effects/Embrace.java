package model.effects;

import java.util.ArrayList;

import model.world.Champion;

public class Embrace extends Effect {
	

	public Embrace(int duration) {
		super("Embrace", duration, EffectType.BUFF);
	}

	
	public void apply(Champion c) {
		int max=c.getMaxHP();
		int current=c.getCurrentHP();
	    current=(int)(current+(max*0.2));
	    c.setCurrentHP(current);
	    int mana=c.getMana();
	    mana=(int)(mana+(0.2*mana));
	    c.setMana(mana);
	    c.setSpeed((int) (c.getSpeed()*1.2));
	    c.setAttackDamage((int) (c.getAttackDamage()*1.2));
		
	}

	
	public void remove(Champion c) {
		
	    c.setSpeed((int) (c.getSpeed()*(1/1.2)));
	    c.setAttackDamage((int) (c.getAttackDamage()*(1/1.2)));
	}
	
	

}
