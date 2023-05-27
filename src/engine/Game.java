package engine;

import java.awt.Color;
import java.awt.Point;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import exceptions.AbilityUseException;
import exceptions.ChampionDisarmedException;
import exceptions.InvalidTargetException;
import exceptions.LeaderAbilityAlreadyUsedException;
import exceptions.LeaderNotCurrentException;
import exceptions.NotEnoughResourcesException;
import exceptions.UnallowedMovementException;
import model.abilities.Ability;
import model.abilities.AreaOfEffect;
import model.abilities.CrowdControlAbility;
import model.abilities.DamagingAbility;
import model.abilities.HealingAbility;
import model.effects.Disarm;
import model.effects.Dodge;
import model.effects.Effect;
import model.effects.EffectType;
import model.effects.Embrace;
import model.effects.PowerUp;
import model.effects.Root;
import model.effects.Shield;
import model.effects.Shock;
import model.effects.Silence;
import model.effects.SpeedUp;
import model.effects.Stun;
import model.world.AntiHero;
import model.world.Champion;
import model.world.Condition;
import model.world.Cover;
import model.world.Damageable;
import model.world.Direction;
import model.world.Hero;
import model.world.Villain;


public class Game {
	private static ArrayList<Champion> availableChampions;
	private static ArrayList<Ability> availableAbilities;
	private Player firstPlayer;
	private Player secondPlayer;
	private Object[][] board;
	private PriorityQueue turnOrder;
	private boolean firstLeaderAbilityUsed;
	private boolean secondLeaderAbilityUsed;
	private final static int BOARDWIDTH = 5;
	private final static int BOARDHEIGHT = 5;

	public Game(Player first, Player second) throws IOException {
		firstPlayer = first;

		secondPlayer = second;
		availableChampions = new ArrayList<Champion>();
		availableAbilities = new ArrayList<Ability>();
		board = new Object[BOARDWIDTH][BOARDHEIGHT];
		turnOrder = new PriorityQueue(6);
		placeChampions();
		placeCovers();
		prepareChampionTurns();
		loadAbilities("Abilities.csv");
		loadChampions("Champions.csv");
		
	}

	public static void loadAbilities(String filePath) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(filePath));
		String line = br.readLine();
		while (line != null) {
			String[] content = line.split(",");
			Ability a = null;
			AreaOfEffect ar = null;
			switch (content[5]) {
			case "SINGLETARGET":
				ar = AreaOfEffect.SINGLETARGET;
				break;
			case "TEAMTARGET":
				ar = AreaOfEffect.TEAMTARGET;
				break;
			case "SURROUND":
				ar = AreaOfEffect.SURROUND;
				break;
			case "DIRECTIONAL":
				ar = AreaOfEffect.DIRECTIONAL;
				break;
			case "SELFTARGET":
				ar = AreaOfEffect.SELFTARGET;
				break;

			}
			Effect e = null;
			if (content[0].equals("CC")) {
				switch (content[7]) {
				case "Disarm":
					e = new Disarm(Integer.parseInt(content[8]));
					break;
				case "Dodge":
					e = new Dodge(Integer.parseInt(content[8]));
					break;
				case "Embrace":
					e = new Embrace(Integer.parseInt(content[8]));
					break;
				case "PowerUp":
					e = new PowerUp(Integer.parseInt(content[8]));
					break;
				case "Root":
					e = new Root(Integer.parseInt(content[8]));
					break;
				case "Shield":
					e = new Shield(Integer.parseInt(content[8]));
					break;
				case "Shock":
					e = new Shock(Integer.parseInt(content[8]));
					break;
				case "Silence":
					e = new Silence(Integer.parseInt(content[8]));
					break;
				case "SpeedUp":
					e = new SpeedUp(Integer.parseInt(content[8]));
					break;
				case "Stun":
					e = new Stun(Integer.parseInt(content[8]));
					break;
				}
			}
			switch (content[0]) {
			case "CC":
				a = new CrowdControlAbility(content[1], Integer.parseInt(content[2]), Integer.parseInt(content[4]),
						Integer.parseInt(content[3]), ar, Integer.parseInt(content[6]), e);
				break;
			case "DMG":
				a = new DamagingAbility(content[1], Integer.parseInt(content[2]), Integer.parseInt(content[4]),
						Integer.parseInt(content[3]), ar, Integer.parseInt(content[6]), Integer.parseInt(content[7]));
				break;
			case "HEL":
				a = new HealingAbility(content[1], Integer.parseInt(content[2]), Integer.parseInt(content[4]),
						Integer.parseInt(content[3]), ar, Integer.parseInt(content[6]), Integer.parseInt(content[7]));
				break;
			}
			availableAbilities.add(a);
			line = br.readLine();
		}
		br.close();
	}

	public static void loadChampions(String filePath) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(filePath));
		String line = br.readLine();
		while (line != null) {
			String[] content = line.split(",");
			Champion c = null;
			switch (content[0]) {
			case "A":
				c = new AntiHero(content[1], Integer.parseInt(content[2]), Integer.parseInt(content[3]),
						Integer.parseInt(content[4]), Integer.parseInt(content[5]), Integer.parseInt(content[6]),
						Integer.parseInt(content[7]));
				break;

			case "H":
				c = new Hero(content[1], Integer.parseInt(content[2]), Integer.parseInt(content[3]),
						Integer.parseInt(content[4]), Integer.parseInt(content[5]), Integer.parseInt(content[6]),
						Integer.parseInt(content[7]));
				break;
			case "V":
				c = new Villain(content[1], Integer.parseInt(content[2]), Integer.parseInt(content[3]),
						Integer.parseInt(content[4]), Integer.parseInt(content[5]), Integer.parseInt(content[6]),
						Integer.parseInt(content[7]));
				break;
			}

			c.getAbilities().add(findAbilityByName(content[8]));
			c.getAbilities().add(findAbilityByName(content[9]));
			c.getAbilities().add(findAbilityByName(content[10]));
			availableChampions.add(c);
			line = br.readLine();
		}
		br.close();
	}

	private static Ability findAbilityByName(String name) {
		for (Ability a : availableAbilities) {
			if (a.getName().equals(name))
				return a;
		}
		return null;
	}

	public void placeCovers() {
		int i = 0;
		while (i < 5) {
			int x = ((int) (Math.random() * (BOARDWIDTH - 2))) + 1;
			int y = (int) (Math.random() * BOARDHEIGHT);

			if (board[x][y] == null) {
				board[x][y] = new Cover(x, y);
				i++;
			}
		}

	}

	public void placeChampions() {
		int i = 1;
		for (Champion c : firstPlayer.getTeam()) {
			board[0][i] = c;
			c.setLocation(new Point(0, i));
			i++;
		}
		i = 1;
		for (Champion c : secondPlayer.getTeam()) {
			board[BOARDHEIGHT - 1][i] = c;
			c.setLocation(new Point(BOARDHEIGHT - 1, i));
			i++;
		}

	}

	public static ArrayList<Champion> getAvailableChampions() {
		return availableChampions;
	}

	public static ArrayList<Ability> getAvailableAbilities() {
		return availableAbilities;
	}

	public Player getFirstPlayer() {
		return firstPlayer;
	}

	public Player getSecondPlayer() {
		return secondPlayer;
	}

	public Object[][] getBoard() {
		return board;
	}

	public PriorityQueue getTurnOrder() {
		return turnOrder;
	}

	public boolean isFirstLeaderAbilityUsed() {
		return firstLeaderAbilityUsed;
	}

	public boolean isSecondLeaderAbilityUsed() {
		return secondLeaderAbilityUsed;
	}

	public static int getBoardwidth() {
		return BOARDWIDTH;
	}

	public static int getBoardheight() {
		return BOARDHEIGHT;
	}

	public Champion getCurrentChampion() {

		return (Champion) turnOrder.peekMin();

	}

	public Player checkGameOver() { // team empty
		ArrayList<Champion> p1 = firstPlayer.getTeam();
		ArrayList<Champion> p2 = secondPlayer.getTeam();
		if (p1.size() <= 0)
			return secondPlayer;
		if (p2.size() <= 0)
			return firstPlayer;
		return null;
	}

	public void move(Direction d) throws UnallowedMovementException, NotEnoughResourcesException {
		Champion x = (Champion) turnOrder.peekMin();
		if (x.getCurrentActionPoints() == 0) {
			throw new NotEnoughResourcesException();
		}
		if (x.getCondition() == Condition.ROOTED)
			throw new UnallowedMovementException();
		if (d == Direction.UP) {
			Point loc = new Point(x.getLocation());
			if (loc.getX() == 4 || board[(int) (loc.getX() + 1)][(int) loc.getY()] != null)
				throw new UnallowedMovementException();
			else if (board[(int) (loc.getX() + 1)][(int) loc.getY()] == null) {
				board[(int) loc.getX()][(int) loc.getY()] = null;
				board[(int) loc.getX() + 1][(int) loc.getY()] = x;
				x.setLocation(new Point((int) loc.getX() + 1, (int) loc.getY()));

				
			}
		} else if (d == Direction.DOWN) {
			Point loc = new Point(x.getLocation());
			if (loc.getX() == 0 || board[(int) (loc.getX() - 1)][(int) loc.getY()] != null)
				throw new UnallowedMovementException();
			else if (board[(int) (loc.getX() - 1)][(int) loc.getY()] == null) {
				board[(int) loc.getX()][(int) loc.getY()] = null;
				board[(int) loc.getX() - 1][(int) loc.getY()] = x;
				x.setLocation(new Point((int) loc.getX() - 1, (int) loc.getY()));
			}
		} else if (d == Direction.RIGHT) {
			Point loc = new Point(x.getLocation());
			if (loc.getY() == 4 || board[(int) (loc.getX())][(int) loc.getY() + 1] != null)
				throw new UnallowedMovementException();
			else if (board[(int) (loc.getX())][(int) (loc.getY() + 1)] == null) {
				board[(int) loc.getX()][(int) loc.getY()] = null;
				board[(int) loc.getX()][(int) loc.getY() + 1] = x;
				x.setLocation(new Point((int) loc.getX(), (int) loc.getY() + 1));
			}
		} else if (d == Direction.LEFT) {
			Point loc = new Point(x.getLocation());
			if (loc.getY() == 0 || board[(int) (loc.getX())][(int) loc.getY() - 1] != null)
				throw new UnallowedMovementException();
			else if (board[(int) (loc.getX())][(int) (loc.getY() - 1)] == null) {
				board[(int) loc.getX()][(int) loc.getY()] = null;
				board[(int) loc.getX()][(int) loc.getY() - 1] = x;
				x.setLocation(new Point((int) loc.getX(), (int) loc.getY() - 1));
			}
		}
		x.setCurrentActionPoints(x.getCurrentActionPoints() - 1);
	}

	public void attack(Direction d)
			throws NotEnoughResourcesException, InvalidTargetException, AbilityUseException, ChampionDisarmedException {
		Champion x = this.getCurrentChampion();
		if (x.getCurrentActionPoints() < 2)
			throw new NotEnoughResourcesException();
		ArrayList<Effect> eff = x.getAppliedEffects();
		for (int i = 0; i < eff.size(); i++) {
			if (eff.get(i) instanceof Disarm) {
				throw new ChampionDisarmedException();
			}

		}
		ArrayList<Champion> p1 = firstPlayer.getTeam();
		ArrayList<Champion> p2 = secondPlayer.getTeam();
		boolean first = true;
		for (int i = 0; i < p1.size(); i++) {
			if (x == p1.get(i))
				first = true;
		}
		for (int i = 0; i < p2.size(); i++) {
			if (x == p2.get(i))
				first = false;
		}
		x.setCurrentActionPoints(x.getCurrentActionPoints() - 2);
		Point loc = new Point(x.getLocation());
		Damageable kill = null;
		for (int i = 1; i <= x.getAttackRange(); i++) {
			if (d == Direction.UP) {
				if (loc.x + i > 4)
					return;
				if (board[(int) loc.getX() + i][(int) loc.getY()] instanceof Damageable) {
					kill = (Damageable) board[(int) loc.getX() + i][(int) loc.getY()];
					break;
				}
			} else if (d == Direction.DOWN) {
				if (loc.x - i < 0)
					return;
				if (board[(int) loc.getX() - i][(int) loc.getY()] instanceof Damageable) {
					kill = (Damageable) board[(int) loc.getX() - i][(int) loc.getY()];
					break;
				}
			} else if (d == Direction.LEFT) {
				if (loc.y - i < 0)
					return;
				if (board[(int) loc.getX()][(int) loc.getY() - i] instanceof Damageable) {
					kill = (Damageable) board[(int) loc.getX()][(int) loc.getY() - i];
					break;
				}
			} else if (d == Direction.RIGHT) {
				if (loc.y + i > 4)
					return;
				if (board[(int) loc.getX()][(int) loc.getY() + i] instanceof Damageable) {

					kill = (Damageable) board[(int) loc.getX()][(int) loc.getY() + i];
					break;
				}
			}

		}

		if (kill == null) {

			return;

		}
		
		if (kill instanceof Cover) { 
			kill.setCurrentHP((int) (kill.getCurrentHP() - x.getAttackDamage()));
			if (kill.getCurrentHP() <= 0) {
				Point loc1 = kill.getLocation();
				board[(int) loc1.x][(int) loc1.y] = null;
				kill = null;
			}
			return;
			
		} else if (kill instanceof Champion) {
			if (first) {
				if (p1.contains(kill))
					return;

			} else {
				if (p2.contains(kill))
					return;
			}
			ArrayList<Effect> eff1 = ((Champion) kill).getAppliedEffects();
			for (int i = 0; i < eff1.size(); i++) {
				Effect e = eff1.get(i);
				if (e instanceof Shield) {
					e.remove((Champion) kill);
					eff1.remove(e);

					return;
				}
			}
			for (int i = 0; i < eff1.size(); i++) {
				if (eff1.get(i) instanceof Dodge) {
					int d1 = (int) (Math.random() * 100); 
					if (d1 < 50) {

						return;
					} else
						break;
				}
			}

			if (x.getClass() == (kill.getClass()))
				kill.setCurrentHP((int) (kill.getCurrentHP() - x.getAttackDamage()));

			else {
				int att = (int) (x.getAttackDamage() * 1.5);
				kill.setCurrentHP((int) (kill.getCurrentHP() - att));

			}
			if (kill.getCurrentHP() <= 0) {
				((Champion) kill).setCondition(Condition.KNOCKEDOUT);
				board[(int) kill.getLocation().x][(int) kill.getLocation().y] = null;
				if (first)
					p2.remove(kill);
				else
					p1.remove(kill);
				PriorityQueue temp = new PriorityQueue(turnOrder.size());

				while (!turnOrder.isEmpty()) {

					Champion r = (Champion) turnOrder.remove();
					if (r != kill)
						temp.equals(kill);
				}
				while (!temp.isEmpty()) {
					turnOrder.insert(temp.remove());
				}
			}
		}

		

	}
	private boolean hasEffect(Champion currentChampion, String s) {
		for (Effect e : currentChampion.getAppliedEffects()) {
			if (e.getName().equals(s))
				return true;
		}
		return false;
	}
	
	public void castAbility(Ability a) throws NotEnoughResourcesException, AbilityUseException,
			CloneNotSupportedException, InvalidTargetException {
		
		Champion x = getCurrentChampion();
		if (x.getCurrentActionPoints() < a.getRequiredActionPoints())
			throw new NotEnoughResourcesException();
		if (x.getMana() < a.getManaCost())
			throw new NotEnoughResourcesException();
		if (a.getCurrentCooldown() != 0)
			throw new AbilityUseException();
		for (int i = 0; i < x.getAppliedEffects().size(); i++) {
			if (x.getAppliedEffects().get(i) instanceof Silence) {
				throw new AbilityUseException();

			}
		}
		ArrayList<Champion> p1 = firstPlayer.getTeam();
		ArrayList<Champion> p2 = secondPlayer.getTeam();
		boolean first = true;
		for (int i = 0; i < p1.size(); i++) {
			if (x == p1.get(i))
				first = true;
		}
		for (int i = 0; i < p2.size(); i++) {
			if (x == p2.get(i))
				first = false;
		}
		ArrayList<Damageable> allt = new ArrayList<Damageable>();
		if (a instanceof HealingAbility) {
			if (a.getCastArea() == AreaOfEffect.SELFTARGET) {
				allt.add(x);
				a.execute(allt);
				x.setCurrentActionPoints(x.getCurrentActionPoints() - a.getRequiredActionPoints());
				x.setMana(x.getMana() - a.getManaCost());
				return;
			}
			if (a.getCastArea() == AreaOfEffect.TEAMTARGET) {
				if (first) {
					int range = a.getCastRange();
					Point loc = new Point(x.getLocation());
					for (Champion play1 : p1) {
						int man = (int) (Math.abs(loc.getX() - play1.getLocation().getX())
								+ Math.abs(loc.getY() - play1.getLocation().getY()));
						if (man <= range)
							allt.add(play1);
					}
				} else {
					int range = a.getCastRange();
					Point loc = new Point(x.getLocation());
					for (Champion play2 : p2) {
						int man = (int) (Math.abs(loc.getX() - play2.getLocation().getX())
								+ Math.abs(loc.getY() - play2.getLocation().getY()));
						if (man <= range)
							allt.add(play2);
					}
				}
				if(allt.isEmpty()) {
					x.setCurrentActionPoints(x.getCurrentActionPoints() - a.getRequiredActionPoints());
					x.setMana(x.getMana() - a.getManaCost());
					return;
				}
				a.execute(allt);
				x.setCurrentActionPoints(x.getCurrentActionPoints() - a.getRequiredActionPoints());
				x.setMana(x.getMana() - a.getManaCost());

				return;
			}
			if (a.getCastArea() == AreaOfEffect.SURROUND) {
				Point loc = new Point(x.getLocation());
				int locX = loc.x;
				int locY = loc.y;

				if (locX + 1 <= 4 && board[locX + 1][locY] instanceof Champion) // up
					allt.add((Damageable) board[locX + 1][locY]);
				if (locY + 1 <= 4 && board[locX][locY + 1] instanceof Champion) // right
					allt.add((Damageable) board[locX][locY + 1]);
				if (locX - 1 >= 0 && board[locX - 1][locY] instanceof Champion) // down
					allt.add((Damageable) board[locX - 1][locY]);

				if (locY - 1 >= 0 && board[locX][locY - 1] instanceof Champion) // left
					allt.add((Damageable) board[locX][locY - 1]);

				if (locX + 1 <= 4 && locY + 1 <= 4 && board[locX + 1][locY + 1] instanceof Champion) // up-right
					allt.add((Damageable) board[locX + 1][locY + 1]);
				if (locX + 1 <= 4 && locY - 1 >= 0 && board[locX + 1][locY - 1] instanceof Champion) // up -left
					allt.add((Damageable) board[locX + 1][locY - 1]);
				if (locX - 1 >= 0 && locY + 1 <= 4 && board[locX - 1][locY + 1] instanceof Champion) // down-right
					allt.add((Damageable) board[locX - 1][locY + 1]);
				if (locX - 1 >= 0 && locY - 1 >= 0 && board[locX - 1][locY - 1] instanceof Champion) // down-left
					allt.add((Damageable) board[locX - 1][locY - 1]);

				if (allt.isEmpty()) {
					x.setCurrentActionPoints(x.getCurrentActionPoints() - a.getRequiredActionPoints());
					x.setMana(x.getMana() - a.getManaCost());
					return;
				}
				ArrayList<Damageable> remov = new ArrayList<Damageable>();
				if (p1.contains(x)) {
					for (Champion play1 : p1) {
						if (allt.contains(play1))
							remov.add(play1);
					}
				} else if (p2.contains(x)) {

					for (Champion play1 : p2) {
						if (allt.contains(play1))
							remov.add(play1);
					}

				}

				if (remov.isEmpty()) {
					x.setCurrentActionPoints(x.getCurrentActionPoints() - a.getRequiredActionPoints());
					x.setMana(x.getMana() - a.getManaCost());
					return;
				}
				a.execute(remov);
				x.setCurrentActionPoints(x.getCurrentActionPoints() - a.getRequiredActionPoints());
				x.setMana(x.getMana() - a.getManaCost());

			}
		}
		if (a instanceof DamagingAbility) {
			if (a.getCastArea() == AreaOfEffect.SELFTARGET) {
				throw new InvalidTargetException();
			} else if (a.getCastArea() == AreaOfEffect.TEAMTARGET) {
				if (first) {
					int range = a.getCastRange();
					Point loc = new Point(x.getLocation());
					for (int i = 0; i < p2.size(); i++) {
						int man = (int) (Math.abs(loc.getX() - p2.get(i).getLocation().getX())
								+ Math.abs(loc.getY() - p2.get(i).getLocation().getY()));
						if (man <= range) {
							if(!hasEffect(p2.get(i),"dodge")) {
							allt.add(p2.get(i));
							}
						}
					}
				} else {
					int range = a.getCastRange();
					Point loc = new Point(x.getLocation());
					for (int i = 0; i < p1.size(); i++) {
						int man = (int) (Math.abs(loc.getX() - p1.get(i).getLocation().getX())
								+ Math.abs(loc.getY() - p1.get(i).getLocation().getY()));

						if (man <= range) {
							if(!hasEffect(p1.get(i),"dodge")) {
							allt.add(p1.get(i));
							}
						}
					}

				}
				ArrayList<Damageable> remov = new ArrayList<Damageable>();
				for (int i = 0; i < allt.size(); i++) {
					for (int j = 0; j < ((Champion) allt.get(i)).getAppliedEffects().size(); j++) {
						if (((Champion) allt.get(i)).getAppliedEffects().get(j) instanceof Shield) {
							((Champion) allt.get(i)).getAppliedEffects().get(j).remove((Champion) allt.get(i));
							((Champion) allt.get(i)).getAppliedEffects().remove(j);
							remov.add(allt.get(i));
							break;
						}
					}
				}
				for (int i = 0; i < remov.size(); i++) {
					allt.remove(remov.get(i));
				}
				if (allt.isEmpty()) {
					x.setCurrentActionPoints(x.getCurrentActionPoints() - a.getRequiredActionPoints());
					x.setMana(x.getMana() - a.getManaCost());
					return;
				}
				a.execute(allt);
				x.setCurrentActionPoints(x.getCurrentActionPoints() - a.getRequiredActionPoints());
				x.setMana(x.getMana() - a.getManaCost());

			} else if (a.getCastArea() == AreaOfEffect.SURROUND) {
				Point loc = new Point(x.getLocation());
				int locX = loc.x;
				int locY = loc.y;

				if (locX + 1 <= 4 && board[locX + 1][locY] instanceof Damageable) // up
					allt.add((Damageable) board[locX + 1][locY]);

				if (locY + 1 <= 4 && board[locX][locY + 1] instanceof Damageable) // right
					allt.add((Damageable) board[locX][locY + 1]);
				if (locX - 1 >= 0 && board[locX - 1][locY] instanceof Damageable) // down
					allt.add((Damageable) board[locX - 1][locY]);

				if (locY - 1 >= 0 && board[locX][locY - 1] instanceof Damageable) // left
					allt.add((Damageable) board[locX][locY - 1]);

				if (locX + 1 <= 4 && locY + 1 <= 4 && board[locX + 1][locY + 1] instanceof Damageable) // up-right
					allt.add((Damageable) board[locX + 1][locY + 1]);
				if (locX + 1 <= 4 && locY - 1 >= 0 && board[locX + 1][locY - 1] instanceof Damageable) // up -left
					allt.add((Damageable) board[locX + 1][locY - 1]);
				if (locX - 1 >= 0 && locY + 1 <= 4 && board[locX - 1][locY + 1] instanceof Damageable) // down-right
					allt.add((Damageable) board[locX - 1][locY + 1]);
				if (locX - 1 >= 0 && locY - 1 >= 0 && board[locX - 1][locY - 1] instanceof Damageable) // down-left
					allt.add((Damageable) board[locX - 1][locY - 1]);

				if (allt.isEmpty()) {
					x.setCurrentActionPoints(x.getCurrentActionPoints() - a.getRequiredActionPoints());
					x.setMana(x.getMana() - a.getManaCost());
					return;
				}
				if (first) {

					for (int i = 0; i < p1.size(); i++) {
						if (allt.contains(p1.get(i)))
							allt.remove(p1.get(i));
					}

				}

				else {
					for (int i = 0; i < p2.size(); i++) {
						if (allt.contains(p2.get(i)))
							allt.remove(p2.get(i));
					}
				}
				ArrayList<Champion> check = new ArrayList<Champion>();
				for (int i = 0; i < allt.size(); i++) {
					if (allt.get(i) instanceof Champion)
						check.add((Champion) allt.get(i));
				}
				if (!check.isEmpty()) {
					for (int i = 0; i < check.size(); i++) {

						for (int j = 0; j < check.get(i).getAppliedEffects().size(); j++) {
							if (check.get(i).getAppliedEffects().get(j) instanceof Shield) {
								Shield s = (Shield) check.get(i).getAppliedEffects().get(j);
								s.remove(check.get(i));
								check.get(i).getAppliedEffects().remove(s);
								allt.remove(check.get(i));
								break;
							}
						}

					}

				}
				if(allt.isEmpty()) {
					x.setCurrentActionPoints(x.getCurrentActionPoints() - a.getRequiredActionPoints());
					x.setMana(x.getMana() - a.getManaCost());
					return;
				}

				a.execute(allt);

				x.setCurrentActionPoints(x.getCurrentActionPoints() - a.getRequiredActionPoints());
				x.setMana(x.getMana() - a.getManaCost());

			}
			for (Damageable ts : allt) {
				if (ts instanceof Cover) {
					if (ts.getCurrentHP() <= 0) {
						board[ts.getLocation().x][ts.getLocation().y] = null;
					}
				}
				if (ts instanceof Champion) {
					if (ts.getCurrentHP() <= 0) {
						((Champion) ts).setCondition(Condition.KNOCKEDOUT);
						board[ts.getLocation().x][ts.getLocation().y] = null;

						if (p1.contains(ts))
							p1.remove(ts);

						if (p2.contains(ts))
							p2.remove(ts);

						PriorityQueue temp = new PriorityQueue(turnOrder.size());
						while (!turnOrder.isEmpty()) {

							Champion r = (Champion) turnOrder.remove();
							if (r != ts)
								temp.insert(r);
						}
						while (!temp.isEmpty()) {
							turnOrder.insert(temp.remove());
						}
					}
				}
			}

		}

		if (a instanceof CrowdControlAbility) {
			Effect eff = ((CrowdControlAbility) a).getEffect();
			if (eff.getType() == EffectType.BUFF) {
				if (a.getCastArea() == AreaOfEffect.SELFTARGET) {
					allt.add(x);
					a.execute(allt);

					x.setCurrentActionPoints(x.getCurrentActionPoints() - a.getRequiredActionPoints());
					x.setMana(x.getMana() - a.getManaCost());
					return;
				}
				if (a.getCastArea() == AreaOfEffect.TEAMTARGET) {
					if (first) {
						int range = a.getCastRange();
						Point loc = new Point(x.getLocation());
						for (int i = 0; i < p1.size(); i++) {
							int man = (Math.abs((int) loc.getX() - (int) p1.get(i).getLocation().getX())
									+ Math.abs((int) loc.getY() - (int) p1.get(i).getLocation().getY()));
							if (man <= range)
								allt.add(p1.get(i));
						}
					} else {
						int range = a.getCastRange();
						Point loc = new Point(x.getLocation());
						for (int i = 0; i < p2.size(); i++) {
							int man = (int) ((int) Math.abs(loc.getX() - (int) p2.get(i).getLocation().getX())
									+ Math.abs((int) loc.getY() - (int) p2.get(i).getLocation().getY()));
							if (man <= range)
								allt.add(p2.get(i));
						}
					}
					if(allt.isEmpty()) {
						x.setCurrentActionPoints(x.getCurrentActionPoints() - a.getRequiredActionPoints());
						x.setMana(x.getMana() - a.getManaCost());
						return;
					}
					a.execute(allt);

					x.setCurrentActionPoints(x.getCurrentActionPoints() - a.getRequiredActionPoints());
					x.setMana(x.getMana() - a.getManaCost());
					return;
				}
				if (a.getCastArea() == AreaOfEffect.SURROUND) {
					Point loc = new Point(x.getLocation());
					int locX = loc.x;
					int locY = loc.y;

					if (locX + 1 <= 4 && board[locX + 1][locY] instanceof Champion) // up
						allt.add((Damageable) board[locX + 1][locY]);

					if (locY + 1 <= 4 && board[locX][locY + 1] instanceof Champion) // right
						allt.add((Damageable) board[locX][locY + 1]);
					if (locX - 1 >= 0 && board[locX - 1][locY] instanceof Champion) // down
						allt.add((Damageable) board[locX - 1][locY]);

					if (locY - 1 >= 0 && board[locX][locY - 1] instanceof Champion) // left
						allt.add((Damageable) board[locX][locY - 1]);

					if (locX + 1 <= 4 && locY + 1 <= 4 && board[locX + 1][locY + 1] instanceof Champion) // up-right
						allt.add((Damageable) board[locX + 1][locY + 1]);
					if (locX + 1 <= 4 && locY - 1 >= 0 && board[locX + 1][locY - 1] instanceof Champion) // up -left
						allt.add((Damageable) board[locX + 1][locY - 1]);
					if (locX - 1 >= 0 && locY + 1 <= 4 && board[locX - 1][locY + 1] instanceof Champion) // down-right
						allt.add((Damageable) board[locX - 1][locY + 1]);
					if (locX - 1 >= 0 && locY - 1 >= 0 && board[locX - 1][locY - 1] instanceof Champion) // down-left
						allt.add((Damageable) board[locX - 1][locY - 1]);
					ArrayList<Damageable> remov = new ArrayList<Damageable>();
					

					if (first) {
						for(int i=0;i<p1.size();i++) {
							if(allt.contains(p1.get(i)))
								remov.add(p1.get(i));
						}
						}
					 else {
						 for(int i=0;i<p2.size();i++) {
								if(allt.contains(p2.get(i)))
									remov.add(p2.get(i));
							}
					}
					
					if(remov.isEmpty()) {
						x.setCurrentActionPoints(x.getCurrentActionPoints() - a.getRequiredActionPoints());
						x.setMana(x.getMana() - a.getManaCost());
						return;
					}
					a.execute(remov);

					x.setCurrentActionPoints(x.getCurrentActionPoints() - a.getRequiredActionPoints());
					x.setMana(x.getMana() - a.getManaCost());
					
				}

			} else if (eff.getType() == EffectType.DEBUFF) {
				if (a.getCastArea() == AreaOfEffect.SELFTARGET) {
					throw new InvalidTargetException();
				} else if (a.getCastArea() == AreaOfEffect.TEAMTARGET) {
					if (first) {
						int range = a.getCastRange();
						Point loc = new Point(x.getLocation());
						for (int i = 0; i < p2.size(); i++) {
							int man = Math.abs(loc.x - p2.get(i).getLocation().x)
									+ Math.abs(loc.y - p2.get(i).getLocation().y);
							if (man <= range) {
								if(!hasEffect(p2.get(i),"dodge")) {
									allt.add(p2.get(i));
								}
							}
						}
					} else {
						int range = a.getCastRange();
						Point loc = new Point(x.getLocation());
						for (int i = 0; i < p1.size(); i++) {
							int man = Math.abs(loc.x - p1.get(i).getLocation().x)
									+ Math.abs(loc.y - p1.get(i).getLocation().y);
							if (man <= range) {
								if(!hasEffect(p1.get(i),"dodge")) {
								allt.add(p1.get(i));
								}
							}
						}
					}
					if(allt.isEmpty()) {
						x.setCurrentActionPoints(x.getCurrentActionPoints() - a.getRequiredActionPoints());
						x.setMana(x.getMana() - a.getManaCost());
						return;
					}
					a.execute(allt);

					x.setCurrentActionPoints(x.getCurrentActionPoints() - a.getRequiredActionPoints());
					x.setMana(x.getMana() - a.getManaCost());
					return;
				} else if (a.getCastArea() == AreaOfEffect.SURROUND) {
					Point loc = new Point(x.getLocation());
					int locX = loc.x;
					int locY = loc.y;

					if (locX + 1 <= 4 && board[locX + 1][locY] instanceof Champion) // up
						allt.add((Damageable) board[locX + 1][locY]);

					if (locY + 1 <= 4 && board[locX][locY + 1] instanceof Champion) // right
						allt.add((Damageable) board[locX][locY + 1]);
					if (locX - 1 >= 0 && board[locX - 1][locY] instanceof Champion) // down
						allt.add((Damageable) board[locX - 1][locY]);

					if (locY - 1 >= 0 && board[locX][locY - 1] instanceof Champion) // left
						allt.add((Damageable) board[locX][locY - 1]);

					if (locX + 1 <= 4 && locY + 1 <= 4 && board[locX + 1][locY + 1] instanceof Champion) // up-right
						allt.add((Damageable) board[locX + 1][locY + 1]);
					if (locX + 1 <= 4 && locY - 1 >= 0 && board[locX + 1][locY - 1] instanceof Champion) // up -left
						allt.add((Damageable) board[locX + 1][locY - 1]);
					if (locX - 1 >= 0 && locY + 1 <= 4 && board[locX - 1][locY + 1] instanceof Champion) // down-right
						allt.add((Damageable) board[locX - 1][locY + 1]);
					if (locX - 1 >= 0 && locY - 1 >= 0 && board[locX - 1][locY - 1] instanceof Champion) // down-left
						allt.add((Damageable) board[locX - 1][locY - 1]);

					if (allt.isEmpty()) {
						x.setCurrentActionPoints(x.getCurrentActionPoints() - a.getRequiredActionPoints());
						x.setMana(x.getMana() - a.getManaCost());
						return;
					}

					ArrayList<Damageable> t = new ArrayList<Damageable>();
					if (p1.contains(x)) {
						for (int i = 0; i < p2.size(); i++) {
							if (allt.contains(p2.get(i))) {
								t.add(p2.get(i));
							}

						}
					} else {

						for (int i = 0; i < p1.size(); i++) {
							if (allt.contains(p1.get(i))) {
								t.add(p1.get(i));
							}
						}

					}
					if (t.isEmpty()) {
						x.setCurrentActionPoints(x.getCurrentActionPoints() - a.getRequiredActionPoints());
						x.setMana(x.getMana() - a.getManaCost());
						return;
					}
					a.execute(t);

					x.setCurrentActionPoints(x.getCurrentActionPoints() - a.getRequiredActionPoints());
					x.setMana(x.getMana() - a.getManaCost());

				}
				for (int i = 0; i < allt.size(); i++) {
					if (allt.get(i) instanceof Cover) {
						if (allt.get(i).getCurrentHP() <= 0) {
							board[allt.get(i).getLocation().x][allt.get(i).getLocation().y] = null;
						}
					}
					if (allt.get(i) instanceof Champion) {
						if (allt.get(i).getCurrentHP() <= 0) {
							((Champion) allt.get(i)).setCondition(Condition.KNOCKEDOUT);
							board[allt.get(i).getLocation().x][allt.get(i).getLocation().y] = null;
							if (p1.contains(allt.get(i)))
								p1.remove(allt.get(i));

							if (p2.contains(allt.get(i)))
								p2.remove(allt.get(i));

							PriorityQueue temp = new PriorityQueue(turnOrder.size());
							while (!turnOrder.isEmpty()) {

								Champion r = (Champion) turnOrder.remove();
								if (r != allt.get(i))
									temp.insert(r);
							}
							while (!temp.isEmpty()) {
								turnOrder.insert(temp.remove());
							}
						}
					}
				}

			}

		}

	}

	public void castAbility(Ability a, Direction d)
			throws AbilityUseException, NotEnoughResourcesException, CloneNotSupportedException {
		Champion c = (Champion) turnOrder.peekMin();
		ArrayList<Champion> p1 = firstPlayer.getTeam();
		ArrayList<Champion> p2 = secondPlayer.getTeam();
		Point loc = new Point(c.getLocation());
		if (c.getCurrentActionPoints() < a.getRequiredActionPoints())
			throw new NotEnoughResourcesException();
		if (c.getMana() < a.getManaCost())
			throw new NotEnoughResourcesException();
		if (a.getCurrentCooldown() != 0)
			throw new AbilityUseException();

		for (int i = 0; i < c.getAppliedEffects().size(); i++)
			if (c.getAppliedEffects().get(i) instanceof Silence)
				throw new AbilityUseException();

		ArrayList<Damageable> t = new ArrayList<Damageable>();
		boolean first = true;
		if (p1.contains(c))
			first = true;

		else if (p2.contains(c))
			first = false;

		int locx = c.getLocation().x;
		int locy = c.getLocation().y;
		for (int i = 1; i <= a.getCastRange(); i++) {
			if (d == Direction.UP) {
				if (locx == 4)
					break;
				if (c.getLocation().x + i <= 4 && board[c.getLocation().x + i][c.getLocation().y] instanceof Damageable)
					t.add((Damageable) board[c.getLocation().x + i][c.getLocation().y]);

			} else if (d == Direction.DOWN) {
				if (locx == 0)
					break;
				if (c.getLocation().x - i >= 0 && board[c.getLocation().x - i][c.getLocation().y] instanceof Damageable)
					t.add((Damageable) board[c.getLocation().x - i][c.getLocation().y]);
			}

			else if (d == Direction.LEFT) {
				if (locy == 0)
					break;
				if (c.getLocation().y - i >= 0 && board[c.getLocation().x][c.getLocation().y - i] instanceof Damageable)
					t.add((Damageable) board[c.getLocation().x][c.getLocation().y - i]);
			}

			else if (d == Direction.RIGHT) {
				if (locy == 4)
					break;
				if (c.getLocation().y + i <= 4 && board[c.getLocation().x][c.getLocation().y + i] instanceof Damageable)
					t.add((Damageable) board[c.getLocation().x][c.getLocation().y + i]);

			}
		}

		if (a instanceof HealingAbility) {
			ArrayList<Damageable> remov = new ArrayList<Damageable>();
			for (int i = 0; i < t.size(); i++) {
				if (t.get(i) instanceof Cover) {
					remov.add(t.get(i));
				}
				if (t.get(i) instanceof Champion) {
					if (first) {
						for (int j = 0; j < p2.size(); j++) {
							if (t.get(i) == p2.get(j))
								remov.add(t.get(i));
						}
					} else {
						for (int j = 0; j < p1.size(); j++) {
							if (t.get(i) == p1.get(j))
								remov.add(t.get(i));
						}
					}
				}
			}
			for (int i = 0; i < remov.size(); i++) {
				t.remove(remov.get(i));
			}
			if(t.isEmpty()) {
				c.setCurrentActionPoints(c.getCurrentActionPoints() - a.getRequiredActionPoints());
				c.setMana(c.getMana() - a.getManaCost());
				return;
			}
			a.execute(t);
			c.setCurrentActionPoints(c.getCurrentActionPoints() - a.getRequiredActionPoints());
			c.setMana(c.getMana() - a.getManaCost());
		} else if (a instanceof DamagingAbility) {
			ArrayList<Damageable> remov = new ArrayList<Damageable>();
			for (int i = 0; i < t.size(); i++) {
				if (t.get(i) instanceof Champion) {
					if (first) {
						for (int j = 0; j < p1.size(); j++) {
							if (t.get(i) == p1.get(j))
								remov.add(t.get(i));
						}
					} else {
						for (int j = 0; j < p2.size(); j++) {
							if (t.get(i) == p2.get(j))
								remov.add(t.get(i));
						}
					}
					for (int j = 0; j < ((Champion) t.get(i)).getAppliedEffects().size(); j++) {
						if (((Champion) t.get(i)).getAppliedEffects().get(j) instanceof Shield) {
							((Champion) t.get(i)).getAppliedEffects().get(j).remove((Champion) t.get(i));
							((Champion) t.get(i)).getAppliedEffects().remove(j);
							remov.add(t.get(i));
							break;
						}
					}
				}

			}

			for (int i = 0; i < remov.size(); i++) {
				t.remove(remov.get(i));
			}
			if(t.isEmpty()) {
				c.setCurrentActionPoints(c.getCurrentActionPoints() - a.getRequiredActionPoints());
				c.setMana(c.getMana() - a.getManaCost());
				return;
			}
			a.execute(t);
			c.setCurrentActionPoints(c.getCurrentActionPoints() - a.getRequiredActionPoints());
			c.setMana(c.getMana() - a.getManaCost());
			for (int i = 0; i < t.size(); i++) {
				int locx1 = t.get(i).getLocation().x;
				int locy1 = t.get(i).getLocation().y;
				if (t.get(i) instanceof Cover) {
					if (t.get(i).getCurrentHP() <= 0) {
						board[locx1][locy1] = null;
					}
				} else if (t.get(i) instanceof Champion) {
					if (t.get(i).getCurrentHP() <= 0) {
						((Champion) t.get(i)).setCondition(Condition.KNOCKEDOUT);
						board[locx1][locy1] = null;

						if (p1.contains(t.get(i)))
							p1.remove(t.get(i));

						if (p2.contains(t.get(i)))
							p2.remove(t.get(i));

						PriorityQueue temp = new PriorityQueue(turnOrder.size());

						while (!turnOrder.isEmpty()) {

							Champion r = (Champion) turnOrder.remove();
							if (r != t.get(i))
								temp.insert(r);
						}
						while (!temp.isEmpty()) {
							turnOrder.insert(temp.remove());
						}
					}
				}
			}

		} else if (a instanceof CrowdControlAbility) {
			Effect eff = ((CrowdControlAbility) a).getEffect();
			if (eff.getType() == EffectType.BUFF) {

				ArrayList<Damageable> remov = new ArrayList<Damageable>();
				for (int i = 0; i < t.size(); i++) {
					if (t.get(i) instanceof Cover) {
						remov.add(t.get(i));
					}
					if (t.get(i) instanceof Champion) {
						if (first) {
							for (int j = 0; j < p2.size(); j++) {
								if (t.get(i) == p2.get(j))
									remov.add(t.get(i));
							}
						} else {
							for (int j = 0; j < p1.size(); j++) {
								if (t.get(i) == p1.get(j))
									remov.add(t.get(i));
							}
						}

					}
				}
				for (int i = 0; i < remov.size(); i++) {
					t.remove(remov.get(i));
				}
				if(t.isEmpty()) {
					c.setCurrentActionPoints(c.getCurrentActionPoints() - a.getRequiredActionPoints());
					c.setMana(c.getMana() - a.getManaCost());
					return;
				}
				a.execute(t);
				c.setCurrentActionPoints(c.getCurrentActionPoints() - a.getRequiredActionPoints());
				c.setMana(c.getMana() - a.getManaCost());

			} else if (eff.getType() == EffectType.DEBUFF) {

				ArrayList<Damageable> remov = new ArrayList<Damageable>();
				for (int i = 0; i < t.size(); i++) {
					if (t.get(i) instanceof Champion) {
						if (first) {
							if (p2.contains(t.get(i)))
								remov.add(t.get(i));
						} else {
							if (p1.contains(t.get(i)))
								remov.add(t.get(i));
						}
					}
				}
				if (remov.isEmpty()) {
					c.setCurrentActionPoints(c.getCurrentActionPoints() - a.getRequiredActionPoints());
					c.setMana(c.getMana() - a.getManaCost());
					return;
				}
				a.execute(remov);
				c.setCurrentActionPoints(c.getCurrentActionPoints() - a.getRequiredActionPoints());
				c.setMana(c.getMana() - a.getManaCost());

				for (int i = 0; i < t.size(); i++) {
					int locx1 = t.get(i).getLocation().x;
					int locy1 = t.get(i).getLocation().y;
					if (t.get(i) instanceof Cover) {
						if (t.get(i).getCurrentHP() <= 0) {
							board[locx1][locy1] = null;
						}
					} else if (t.get(i) instanceof Champion) {
						if (t.get(i).getCurrentHP() <= 0) {
							((Champion) t.get(i)).setCondition(Condition.KNOCKEDOUT);
							board[locx1][locy1] = null;

							if (p1.contains(t.get(i)))
								p1.remove(t.get(i));

							if (p2.contains(t.get(i)))
								p2.remove(t.get(i));

							PriorityQueue temp = new PriorityQueue(turnOrder.size());

							while (!turnOrder.isEmpty()) {

								Champion r = (Champion) turnOrder.remove();
								if (r != t.get(i))
									temp.equals(r);
							}
							while (!temp.isEmpty()) {
								turnOrder.insert(temp.remove());
							}
						}
					}
				}
			}
		}

	}

	public void castAbility(Ability a, int x, int y) throws NotEnoughResourcesException, AbilityUseException,
			InvalidTargetException, CloneNotSupportedException {
		Champion c = (Champion) turnOrder.peekMin();

		ArrayList<Champion> p1 = firstPlayer.getTeam();
		ArrayList<Champion> p2 = secondPlayer.getTeam();
		Point loc = new Point(c.getLocation());
		int man = Math.abs((int) loc.getX() - x) + Math.abs((int) loc.getY() - y);
		if (c.getCurrentActionPoints() < a.getRequiredActionPoints())
			throw new NotEnoughResourcesException();
		if (c.getMana() < a.getManaCost())
			throw new NotEnoughResourcesException();
		if (a.getCurrentCooldown() != 0)
			throw new AbilityUseException();
		for (int i = 0; i < c.getAppliedEffects().size(); i++)
			if (c.getAppliedEffects().get(i) instanceof Silence)
				throw new AbilityUseException();
		if (board[x][y] == null)
			throw new InvalidTargetException();
		if (man > a.getCastRange())
			throw new AbilityUseException();

		ArrayList<Damageable> t = new ArrayList<Damageable>();
		if (board[x][y] instanceof Cover) {
			if (a instanceof HealingAbility)
				throw new InvalidTargetException();
			else if (a instanceof DamagingAbility) {
				t.add((Damageable) board[x][y]);
				a.execute(t);
				c.setCurrentActionPoints(c.getCurrentActionPoints() - a.getRequiredActionPoints());
				c.setMana(c.getMana() - a.getManaCost());
				if (((Cover) board[x][y]).getCurrentHP() <= 0) {
					board[x][y] = null;
				}
				return;

			} else if (a instanceof CrowdControlAbility) {
				throw new InvalidTargetException();
			}

		} else if (board[x][y] instanceof Champion) {
			boolean first = true;

			if (p1.contains(c))
				first = true;

			if (p2.contains(c))
				first = false;

			if (a instanceof HealingAbility) {

				if (first) {
					for (int i = 0; i < p1.size(); i++) {
						if (p1.contains(board[x][y])) {
							t.add((Damageable) board[x][y]);
							break;
						}
					}

				} else {
					for (int i = 0; i < p2.size(); i++) {
						if (p2.get(i).getLocation().getX() == x && p2.get(i).getLocation().getY() == y) {
							t.add((Damageable) board[x][y]);
							break;
						}
					}
				}
				if (t.isEmpty()) {
					throw new InvalidTargetException();
				}
				a.execute(t);
				c.setCurrentActionPoints(c.getCurrentActionPoints() - a.getRequiredActionPoints());
				c.setMana(c.getMana() - a.getManaCost());
				return;
			} else if (a instanceof DamagingAbility) {

				if (first) {
					for (int i = 0; i < p2.size(); i++) {
						if (p2.get(i).getLocation().getX() == x && p2.get(i).getLocation().getY() == y) {
							t.add((Damageable) board[x][y]);
						}
					}
				}

				else {
					for (int i = 0; i < p1.size(); i++) {
						if (p1.get(i).getLocation().getX() == x && p1.get(i).getLocation().getY() == y) {
							t.add((Damageable) board[x][y]);
						}
					}
				}
				if (t.isEmpty())
					throw new InvalidTargetException();
				for (int j = 0; j < ((Champion) board[x][y]).getAppliedEffects().size(); j++) {
					if (((Champion) board[x][y]).getAppliedEffects().get(j) instanceof Shield) {
						((Champion) board[x][y]).getAppliedEffects().get(j).remove((Champion) board[x][y]);
						((Champion) board[x][y]).getAppliedEffects().remove(j);
						c.setCurrentActionPoints(c.getCurrentActionPoints() - a.getRequiredActionPoints());
						c.setMana(c.getMana() - a.getManaCost());
						return;
					}
				}

				a.execute(t);
				for (int i = 0; i < t.size(); i++) {
					if (t.get(i) instanceof Champion) {
						if (t.get(i).getCurrentHP() <= 0) {
							((Champion) t.get(i)).setCondition(Condition.KNOCKEDOUT);
							board[t.get(i).getLocation().x][t.get(i).getLocation().y] = null;
							if (p1.contains(t.get(i)))
								p1.remove(t.get(i));

							if (p2.contains(t.get(i)))
								p2.remove(t.get(i));

							PriorityQueue temp = new PriorityQueue(turnOrder.size());

							while (!turnOrder.isEmpty()) {

								Champion r = (Champion) turnOrder.remove();
								if (r != t.get(i))
									temp.insert(r);
							}
							while (!temp.isEmpty()) {
								turnOrder.insert(temp.remove());
							}
						}
					}
				}
				c.setCurrentActionPoints(c.getCurrentActionPoints() - a.getRequiredActionPoints());
				c.setMana(c.getMana() - a.getManaCost());

				return;
			} else if (a instanceof CrowdControlAbility) {
				Effect eff = ((CrowdControlAbility) a).getEffect();
				if (eff.getType() == EffectType.BUFF) {
					if (first) {
						for (int i = 0; i < p1.size(); i++) {
							if (p1.get(i) == board[x][y]) {
								t.add((Damageable) board[x][y]);
							}
						}

					} else {
						for (int i = 0; i < p2.size(); i++) {
							if (p2.get(i) == board[x][y]) {
								t.add((Damageable) board[x][y]);
							}
						}
					}
					if (t.isEmpty()) {
						throw new InvalidTargetException();
					}
					a.execute(t);

					c.setCurrentActionPoints(c.getCurrentActionPoints() - a.getRequiredActionPoints());
					c.setMana(c.getMana() - a.getManaCost());
					return;
				} else {
					if (first) {
						for (int i = 0; i < p2.size(); i++) {
							if (p2.get(i) == board[x][y]) {
								t.add((Damageable) board[x][y]);
							}
						}
					}

					else {
						for (int i = 0; i < p2.size(); i++) {
							if (p2.get(i) == board[x][y]) {
								t.add((Damageable) board[x][y]);
							}
						}
					}
					if (t.isEmpty())
						throw new InvalidTargetException();
					a.execute(t);

					for (int i = 0; i < t.size(); i++) {
						if (t.get(i) instanceof Champion) {
							if (t.get(i).getCurrentHP() <= 0) {
								((Champion) t.get(i)).setCondition(Condition.KNOCKEDOUT);
								board[t.get(i).getLocation().x][t.get(i).getLocation().y] = null;
								if (p1.contains(t.get(i)))
									p1.remove(t.get(i));

								if (p2.contains(t.get(i)))
									p2.remove(t.get(i));

								PriorityQueue temp = new PriorityQueue(turnOrder.size());
								while (!turnOrder.isEmpty()) {

									Champion r = (Champion) turnOrder.remove();
									if (r != t.get(i))
										temp.insert(r);
								}
								while (!temp.isEmpty()) {
									turnOrder.insert(temp.remove());
								}
							}
						}
					}
					c.setCurrentActionPoints(c.getCurrentActionPoints() - a.getRequiredActionPoints());
					c.setMana(c.getMana() - a.getManaCost());
					return;
				}
			}

		}

	}

	public void useLeaderAbility()
			throws LeaderNotCurrentException, LeaderAbilityAlreadyUsedException, InvalidTargetException {
		ArrayList<Champion> p1 = firstPlayer.getTeam();
		ArrayList<Champion> p2 = secondPlayer.getTeam();
		Champion x = (Champion) turnOrder.peekMin();
		ArrayList<Champion> t = new ArrayList<Champion>();
		boolean first=false;
		if(p1.contains(x))
			first=true;
		else if (p2.contains(x))
			first=false;
		if (x == firstPlayer.getLeader() || x == secondPlayer.getLeader()) {

			if (x.equals(firstPlayer.getLeader()) && firstLeaderAbilityUsed)
				throw new LeaderAbilityAlreadyUsedException();
			if (x.equals(secondPlayer.getLeader()) && secondLeaderAbilityUsed)
				throw new LeaderAbilityAlreadyUsedException();
			if (x.equals(firstPlayer.getLeader())) {
				if (x instanceof Hero) {
					for (int i = 0; i < p1.size(); i++) {

						t.add(p1.get(i));

					}
				 
					x.useLeaderAbility(t);
				} else if (x instanceof Villain) {
					for (int i = 0; i < p2.size(); i++) {
						int per = p2.get(i).getMaxHP() * (30 / 100);
						if (p2.get(i).getCurrentHP() < per)
							t.add(p2.get(i));
					}

					x.useLeaderAbility(t);
					for (int i = 0; i < t.size(); i++) {
						if (t.get(i) instanceof Champion) {
							if (t.get(i).getCurrentHP() <= 0) {
								((Champion) t.get(i)).setCondition(Condition.KNOCKEDOUT);
								board[t.get(i).getLocation().x][t.get(i).getLocation().y] = null;
								if (p1.contains(t.get(i)))
									p1.remove(t.get(i));

								if (p2.contains(t.get(i)))
									p2.remove(t.get(i));

								PriorityQueue temp = new PriorityQueue(turnOrder.size());
								while (!turnOrder.isEmpty()) {

									Champion r = (Champion) turnOrder.remove();
									if (r != t.get(i))
										temp.insert(r);
								}
								while (!temp.isEmpty()) {
									turnOrder.insert(temp.remove());
								}
							}
						}
					}
				} else if (x instanceof AntiHero) {
					for (int i = 0; i < p1.size(); i++) {
						if (p1.get(i) != firstPlayer.getLeader()) {
							t.add(p1.get(i));
						}
					}
					for (int i = 0; i < p2.size(); i++) {
						if (p2.get(i) != secondPlayer.getLeader()) {
							t.add(p2.get(i));
						}
					}

					x.useLeaderAbility(t);
				}
				firstLeaderAbilityUsed = true;
			} else {

				if (x instanceof Hero) {
					for (int i = 0; i < p2.size(); i++) {

						t.add(p2.get(i));

					}

					x.useLeaderAbility(t);
				} else if (x instanceof Villain) {
					for (int i = 0; i < p1.size(); i++) {
						int per = p1.get(i).getMaxHP() * (30 / 100);
						if (p1.get(i).getCurrentHP() < per)
							t.add(p1.get(i));
					}

					x.useLeaderAbility(t);
					for (int i = 0; i < t.size(); i++) {
						if (t.get(i) instanceof Champion) {
							if (t.get(i).getCurrentHP() <= 0) {
								((Champion) t.get(i)).setCondition(Condition.KNOCKEDOUT);
								board[t.get(i).getLocation().x][t.get(i).getLocation().y] = null;
								if (p1.contains(t.get(i)))
									p1.remove(t.get(i));

								if (p2.contains(t.get(i)))
									p2.remove(t.get(i));

								PriorityQueue temp = new PriorityQueue(6);
								while (!turnOrder.isEmpty()) {
									if (turnOrder.peekMin() != t.get(i))
										temp.insert(turnOrder.remove());
									else
										turnOrder.remove();
								}
								while (!temp.isEmpty()) {
									turnOrder.insert(temp.remove());
								}
							}
						}
					}
				} else if (x instanceof AntiHero) {
					if(first) {
						for(int i=0;i<p2.size();i++) {
							t.add(p2.get(i));
						}
					}
					else {
						for(int i=0;i<p1.size();i++) {
							t.add(p1.get(i));
						}
					}

					x.useLeaderAbility(t);

				}
				secondLeaderAbilityUsed = true;
			}

		} else
			throw new LeaderNotCurrentException();

	}

	public void endTurn() {
		turnOrder.remove();
		if (turnOrder.isEmpty())
			prepareChampionTurns();

		else {
			ArrayList<Champion> p1 = firstPlayer.getTeam();
			ArrayList<Champion> p2 = secondPlayer.getTeam();
			Champion c = (Champion) turnOrder.peekMin();
			while (!turnOrder.isEmpty() && c.getCondition() == Condition.INACTIVE) {
				turnOrder.remove();

			}
			for (int i = 0; i < p1.size(); i++) {
				ArrayList<Ability> abi = p1.get(i).getAbilities();
				for (int j = 0; j < abi.size(); j++) {
					Ability a = abi.get(j);
					a.setCurrentCooldown((a.getCurrentCooldown()) - 1);
				}
				ArrayList<Effect> eff = p1.get(i).getAppliedEffects();
				ArrayList<Effect> removed = new ArrayList<Effect>();
				for (int j = 0; j < eff.size(); j++) {
					Effect e = eff.get(j);
					e.setDuration(e.getDuration() - 1);
					if (e.getDuration() == 0) {
						e.remove(p1.get(i));
						removed.add(e);
					}
				}
				for (int j = 0; j < removed.size(); j++) {
					eff.remove(removed.get(j));
				}
			}

			for (int i = 0; i < p2.size(); i++) {
				ArrayList<Ability> abi = p2.get(i).getAbilities();
				for (int j = 0; j < abi.size(); j++) {
					Ability a = abi.get(j);
					a.setCurrentCooldown((a.getCurrentCooldown()) - 1);
				}
				ArrayList<Effect> eff = p2.get(i).getAppliedEffects();
				ArrayList<Effect> removed = new ArrayList<Effect>();
				for (int j = 0; j < eff.size(); j++) {
					Effect e = eff.get(j);
					e.setDuration(e.getDuration() - 1);
					if (e.getDuration() == 0) {
						e.remove(p2.get(i));
						removed.add(e);
					}
				}
				for (int j = 0; j < removed.size(); j++) {
					eff.remove(removed.get(j));
				}
			}

			c.setCurrentActionPoints(c.getMaxActionPointsPerTurn());
		}
	}

	public void prepareChampionTurns() {
		if (checkGameOver() == null) {
			ArrayList<Champion> p1 = firstPlayer.getTeam();
			ArrayList<Champion> p2 = secondPlayer.getTeam();
			for (int i = 0; i < p1.size(); i++) {
				if (p1.get(i).getCondition() == Condition.ACTIVE || p1.get(i).getCondition() == Condition.INACTIVE
						|| p1.get(i).getCondition() == Condition.ROOTED)
					turnOrder.insert(p1.get(i));
			}
			for (int i = 0; i < p2.size(); i++) {
				if (p2.get(i).getCondition() == Condition.ACTIVE || p2.get(i).getCondition() == Condition.INACTIVE
						|| p2.get(i).getCondition() == Condition.ROOTED)
					turnOrder.insert(p2.get(i));
			}
			((Champion)turnOrder.peekMin()).setCurrentActionPoints(((Champion)turnOrder.peekMin()).getMaxActionPointsPerTurn());
		}
	}
	

}
