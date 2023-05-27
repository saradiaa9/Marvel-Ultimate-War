package model.world;

import java.awt.Point;
import java.util.ArrayList;

import exceptions.InvalidTargetException;
import model.abilities.Ability;
import model.effects.Effect;

public abstract class Champion implements Damageable, Comparable{
	private String name;
	private int maxHP;
	private int currentHP;
	private int mana;
	private int maxActionPointsPerTurn;
	private int currentActionPoints;
	private int attackRange;
	private int attackDamage;
	private int speed;
	private ArrayList<Ability> abilities;
	private ArrayList<Effect> appliedEffects;
	private Condition condition;
	private Point location;
	

	public Champion(String name, int maxHP, int mana, int actions, int speed, int attackRange, int attackDamage) {
		this.name = name;
		this.maxHP = maxHP;
		this.mana = mana;
		this.currentHP = this.maxHP;
		this.maxActionPointsPerTurn = actions;
		this.speed = speed;
		this.attackRange = attackRange;
		this.attackDamage = attackDamage;
		this.condition = Condition.ACTIVE;
		this.abilities = new ArrayList<Ability>();
		this.appliedEffects = new ArrayList<Effect>();
		this.currentActionPoints=maxActionPointsPerTurn;
	}

	public int getMaxHP() {
		return maxHP;
	}

	public String getName() {
		return name;
	}

	

	public ArrayList<Effect> getAppliedEffects() {
		return appliedEffects;
	}

	public int getMana() {
		return mana;
	}

	public void setMana(int mana) {
		this.mana = mana;
	}

	public int getAttackDamage() {
		return attackDamage;
	}

	public void setAttackDamage(int attackDamage) {
		this.attackDamage = attackDamage;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int currentSpeed) {
		if (currentSpeed < 0)
			this.speed = 0;
		else
			this.speed = currentSpeed;
	}

	public Condition getCondition() {
		return condition;
	}

	public void setCondition(Condition condition) {
		this.condition = condition;
	}

	public Point getLocation() {
		return location;
	}

	public void setLocation(Point currentLocation) {
		this.location = currentLocation;
	}

	public int getAttackRange() {
		return attackRange;
	}

	public ArrayList<Ability> getAbilities() {
		return abilities;
	}

	public int getCurrentActionPoints() {
		return currentActionPoints;
	}

	public void setCurrentActionPoints(int currentActionPoints) {
		if(currentActionPoints>maxActionPointsPerTurn)
			currentActionPoints=maxActionPointsPerTurn;
		else 
			if(currentActionPoints<0)
			currentActionPoints=0;
		this.currentActionPoints = currentActionPoints;
	}

	public int getMaxActionPointsPerTurn() {
		return maxActionPointsPerTurn;
	}

	public void setMaxActionPointsPerTurn(int maxActionPointsPerTurn) {
		this.maxActionPointsPerTurn = maxActionPointsPerTurn;
	}
	public int compareTo(Object o) {
		Champion x=(Champion)o;
		int s1= x.speed;
		int s2= speed;
		if(s2>s1)
			return -1;
		else if (s1>s2)
			return 1;
		else {
			String n1=x.name;
			String n2=name;
			return n2.compareTo(n1);
			
		}
	}
	public abstract void useLeaderAbility(ArrayList<Champion> targets) throws InvalidTargetException ;

	
	public int getCurrentHP() {
		return currentHP;
	}

	
	public void setCurrentHP(int hp) {
		if(hp<0)
			currentHP=0;
		else if(hp>=maxHP)
			currentHP=maxHP;
		else
			currentHP=hp;
		
	}
	public String toString() {
		String s= " "+name+'\n'+" MaxHp: " + maxHP+'\n' + " MaxActionPointsPerTurn: "+ maxActionPointsPerTurn +'\n'+ " Speed: "+ speed+ '\n'+" AttackDamage: " +attackDamage+'\n'+ " AttackRange: "+ attackRange+'\n'+ " Ability 1: "+ abilities.get(0).getName()+'\n' +" Ability 2: "+ abilities.get(1).getName()+'\n'+ " Ability 3: "+ abilities.get(2).getName(); 
		return s;
	}
	public String deets() {
		String s= " "+name+'\n'+" CurrentHp: " + currentHP+'\n' +"Mana: "+ mana + '\n'+ " MaxActionPointsPerTurn: "+ maxActionPointsPerTurn+ '\n' +"CurrentActionPoints: "+ currentActionPoints +'\n'+ " Speed: "+ speed+ '\n'+" AttackDamage: " +attackDamage+'\n'+ " AttackRange: "+ attackRange; 
		return s;
	}
	

	
	

}
