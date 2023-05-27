package model.effects;

import java.util.ArrayList;

import model.abilities.Ability;
import model.abilities.DamagingAbility;
import model.abilities.HealingAbility;
import model.world.Champion;

public class PowerUp extends Effect {
	

	public PowerUp(int duration) {
		super("PowerUp", duration, EffectType.BUFF);
		
	}

	
	public void apply(Champion c) {
		ArrayList<Ability> ab=c.getAbilities();
		for(int i=0;i<c.getAbilities().size();i++) {
			if(c.getAbilities().get(i) instanceof DamagingAbility) {
				int damage=((DamagingAbility) c.getAbilities().get(i)).getDamageAmount();
				damage=(int)(damage*1.2);
				((DamagingAbility) c.getAbilities().get(i)).setDamageAmount(damage);
			}
			else if(c.getAbilities().get(i) instanceof HealingAbility) {
				int heal=((HealingAbility) c.getAbilities().get(i)).getHealAmount();
				heal=(int)(heal*1.2);
				((HealingAbility) c.getAbilities().get(i)).setHealAmount(heal);
			}
		}
		
	}

	public void remove(Champion c) {
		for(int i=0;i<c.getAbilities().size();i++) {
			if(c.getAbilities().get(i) instanceof DamagingAbility) {
				int damage=((DamagingAbility) c.getAbilities().get(i)).getDamageAmount();
				damage=(int)(damage*(1/1.2));
				((DamagingAbility) c.getAbilities().get(i)).setDamageAmount(damage);
			}
			else if(c.getAbilities().get(i) instanceof HealingAbility) {
				int heal=((HealingAbility) c.getAbilities().get(i)).getHealAmount();
				heal=(int)(heal*(1/1.2));
				((HealingAbility) c.getAbilities().get(i)).setHealAmount(heal);
			}
		}
		
	}
	

	
}
