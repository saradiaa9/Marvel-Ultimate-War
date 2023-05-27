package model.abilities;

import java.util.ArrayList;

import model.world.Champion;
import model.world.Damageable;

public  class HealingAbility extends Ability {
	private int healAmount;

	public HealingAbility(String name,int cost, int baseCoolDown, int castRadius, AreaOfEffect area,int required, int healingAmount) {
		super(name,cost, baseCoolDown, castRadius, area,required);
		this.healAmount = healingAmount;
	}

	public int getHealAmount() {
		return healAmount;
	}

	public void setHealAmount(int healAmount) {
		this.healAmount = healAmount;
	}


	public void execute(ArrayList<Damageable> targets) {
		for(Damageable t:targets) {
			t.setCurrentHP(t.getCurrentHP()+healAmount);
		
	}
		this.setCurrentCooldown(getBaseCooldown());
	}
	public String toString() {
		
		String s= " Name: "+ getName() + " Type: Healing Ability" + '\n' + " Area Of Effect: "+ getCastArea() + '\n' + " Cast Range: "+ getCastRange() + '\n' + " ManaCost: "+ getManaCost() + '\n'+ " Action Cost: "+ getRequiredActionPoints() +'\n'+ " Current Cooldown: "+ getCurrentCooldown() + '\n'+ " Base Cooldown: "+ getBaseCooldown()+ '\n'+ " Heal Amount: "+ healAmount;
		return s;
	
}

	

	

}
