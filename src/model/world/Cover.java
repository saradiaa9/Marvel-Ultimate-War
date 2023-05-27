package model.world;

import java.awt.Point;

public class Cover implements Damageable  {
	private int currentHP;

	private Point location;

	public Cover(int x, int y) {
		this.currentHP = (int)(( Math.random() * 900) + 100);
		location = new Point(x, y);
	}

	
	public Point getLocation() {
		return location;
	}


	
	public int getCurrentHP() {
		return currentHP;
	}


	
	public void setCurrentHP(int hp) {
		if(hp<0) {
			currentHP=0;
		}
		else {
			currentHP=hp;
		}
		
	}

	

	

}
