package model.abilities;

import java.util.ArrayList;

import model.effects.Effect;
import model.world.Champion;
import model.world.Damageable;

public class CrowdControlAbility extends Ability {
	private Effect effect;

	public CrowdControlAbility(String name, int cost, int baseCoolDown, int castRadius, AreaOfEffect area, int required,
			Effect effect) {
		super(name, cost, baseCoolDown, castRadius, area, required);
		this.effect = effect;

	}

	public Effect getEffect() {
		return effect;
	}

	
	public void execute(ArrayList<Damageable> targets) throws CloneNotSupportedException {
		
		
		for(Damageable target:targets) { 
			Effect e =(Effect) effect.clone();
			Champion c=(Champion) target;
			c.getAppliedEffects().add(e);
			e.apply(c);
			
		
		}
		this.setCurrentCooldown(getBaseCooldown());
		
	}

	@Override
	public String toString() {
		String s= " Name: "+ getName() + 
				" Type: Crowd Control Ability" + '\n' + 
				" Area Of Effect: "+ getCastArea() + '\n' +
				" Cast Range: "+ getCastRange() + '\n' + 
				" ManaCost: "+ getManaCost() + '\n'+ 
				" Action Cost: "+ getRequiredActionPoints() +'\n'+
				" Current Cooldown: "+ getCurrentCooldown() + '\n'+ 
				" Base Cooldown: "+ getBaseCooldown()+ '\n'+ effect.toString();
		return s;
	}

}
