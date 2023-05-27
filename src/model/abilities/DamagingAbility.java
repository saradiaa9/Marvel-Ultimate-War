package model.abilities;

import java.util.ArrayList;

import model.world.Champion;
import model.world.Damageable;

public class DamagingAbility extends Ability {
	
	private int damageAmount;
	public DamagingAbility(String name, int cost, int baseCoolDown, int castRadius, AreaOfEffect area,int required,int damageAmount) {
		super(name, cost, baseCoolDown, castRadius, area,required);
		this.damageAmount=damageAmount;
	}
	public int getDamageAmount() {
		return damageAmount;
	}
	public void setDamageAmount(int damageAmount) {
		this.damageAmount = damageAmount;
	}
	
	public void execute(ArrayList<Damageable> targets) {
		for(Damageable target:targets) {
			target.setCurrentHP(target.getCurrentHP()-damageAmount);
		}
		this.setCurrentCooldown(getBaseCooldown());
	
		
	}
	@Override
	public String toString() {
		
			String s= " Name: "+ getName() + " Type: Damaging Ability" + '\n' + " Area Of Effect: "+ getCastArea() + '\n' + " Cast Range: "+ getCastRange() + '\n' + " ManaCost: "+ getManaCost() + '\n'+ " Action Cost: "+ getRequiredActionPoints() +'\n'+ " Current Cooldown: "+ getCurrentCooldown() + '\n'+ " Base Cooldown: "+ getBaseCooldown()+ '\n'+ " Damage Amount: "+ damageAmount;
			return s;
		
	}
	

}
