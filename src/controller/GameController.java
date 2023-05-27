package controller;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.event.MouseInputListener;

import engine.Game;
import engine.Player;
import exceptions.AbilityUseException;
import exceptions.ChampionDisarmedException;
import exceptions.InvalidTargetException;
import exceptions.LeaderAbilityAlreadyUsedException;
import exceptions.LeaderNotCurrentException;
import exceptions.NotEnoughResourcesException;
import exceptions.UnallowedMovementException;
import model.abilities.Ability;
import model.abilities.AreaOfEffect;
import model.abilities.DamagingAbility;
import model.effects.Disarm;
import model.effects.Effect;
import model.world.AntiHero;
import model.world.Champion;
import model.world.Cover;
import model.world.Direction;
import model.world.Hero;
import model.world.Villain;
import views.start;

public class GameController implements ActionListener,MouseInputListener, KeyListener {
	Game game;
	start start;
	Player player1;
	Player player2;
	Ability a;
	Boolean flag=false;
	public GameController() throws IOException  {
		start= new start();
		player1=new Player(start.getName1());
		player2= new Player(start.getName2());
		game= new Game(player1,player2);
		start.getDone().addActionListener(this);
		for(JButton c1:start.c) {
			c1.addMouseListener(this);
			c1.addActionListener(this);
		}
		start.leader1.addActionListener(this);
		for(JButton c1:start.buttons1) {
			c1.addActionListener(this);
		}
		for(JButton c2:start.buttons2) {
			c2.addActionListener(this);
		}
		start.doneleader.addActionListener(this);
		start.apeff1.addActionListener(this);
		start.apeff2.addActionListener(this);
		start.ability1.addActionListener(this);
		start.ability2.addActionListener(this);
		start.ability3.addActionListener(this);
		start.ability1.addMouseListener(this);
		start.ability2.addMouseListener(this);
		start.ability3.addMouseListener(this);
		start.mUp.addActionListener(this);
		start.mDown.addActionListener(this);
		start.mLeft.addActionListener(this);
		start.mRight.addActionListener(this);
		start.aUp.addActionListener(this);
		start.aDown.addActionListener(this);
		start.aLeft.addActionListener(this);
		start.aRight.addActionListener(this);
		for(int i=0;i<5;i++) {
			for(int j=0;j<5;j++) {
				start.b[i][j].addMouseListener(this);
			}
				
		}
		start.up.addActionListener(this);
		start.down.addActionListener(this);
		start.left.addActionListener(this);
		start.right.addActionListener(this);
		start.end.addActionListener(this);
		start.leaderab.addActionListener(this);
		start.punch.addActionListener(this);
	}
	public static void main(String[] args) throws IOException {
		GameController gameplay= new GameController();
		
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==start.leaderab) {
			try {
				game.useLeaderAbility();
				updateBoard();
				updateTeam1();
				updateTeam2();
				updateTurn();
			} catch (LeaderNotCurrentException e1) {
				JOptionPane.showMessageDialog(start, "Leader not current");
				 
			} catch (LeaderAbilityAlreadyUsedException e1) {
				JOptionPane.showMessageDialog(start, "Leader Ability already used");
				 
			} catch (InvalidTargetException e1) {
				JOptionPane.showMessageDialog(start, "Invalid target");
				 
			}
		}
		if(e.getSource()==start.end) {
			Player p=game.checkGameOver();
			if(p==null) {
				game.endTurn();
				updateBoard();
				updateTeam1();
				updateTeam2();
				updateTurn();
			}
			else{
				start.board.setVisible(false);
				for(int i=0;i<5;i++) {
					for(int j=0;j<5;j++) {
						start.b[i][j].setVisible(false);
					}
				}
				start.ana1.setVisible(false);
				start.ana2.setVisible(false);
				start.apeff1.setVisible(false);
				start.apeff2.setVisible(false);
				start.turn.setVisible(false);
				start.mUp.setVisible(false);
				start.mDown.setVisible(false);
				start.mLeft.setVisible(false);
				start.mRight.setVisible(false);
				start.aUp.setVisible(false);
				start.aDown.setVisible(false);
				start.aLeft.setVisible(false);
				start.aRight.setVisible(false);
				start.g.setVisible(false);
				start.ability1.setVisible(false);
				start.ability2.setVisible(false);
				start.ability3.setVisible(false);
				start.small.setVisible(false);
				start.move.setVisible(false);
				start.attack.setVisible(false);
				start.end.setVisible(false);
				start.setSize(900,550);
				start.win.setVisible(true);
				start.winner.setVisible(true);
				start.winpic.setVisible(true);
				if(p==game.getFirstPlayer())
					start.winner.setText("The Winner is "+ start.getName1() +"!!!!!!!");
				else {
					start.winner.setText("The Winner is "+ start.getName2() +"!!!!!!!");
				}
			}
			
		}
		if(e.getSource()==start.getDone()) {
			int i=0;
			for(Champion c:Game.getAvailableChampions()) {
				start.c.get(i).setText(c.getName());
				i++;
			}
		}
		for(int i=0;i<start.c.size();i++) {
			if(e.getSource()==start.c.get(i)) {
				Champion c1=game.getAvailableChampions().get(i);
				if(game.getFirstPlayer().getTeam().size()<3) {
					game.getFirstPlayer().getTeam().add(c1);
					String s=start.player1.getText();
					s+='\n'+ c1.getName();
					start.player1.setText(s);
					start.c.get(i).setVisible(false);
					if(game.getFirstPlayer().getTeam().size()==3 && game.getSecondPlayer().getTeam().size()==3) 
						start.leader1.setVisible(true);
					
				}
				else if(game.getSecondPlayer().getTeam().size()<3) {
					game.getSecondPlayer().getTeam().add(c1);
					String s=start.player2.getText();
					s+='\n'+ c1.getName();
					start.player2.setText(s);
					start.c.get(i).setVisible(false);
					if(game.getFirstPlayer().getTeam().size()==3 && game.getSecondPlayer().getTeam().size()==3) 
						start.leader1.setVisible(true);
					
				}
				
			}
				
		}
		if(e.getSource()==start.leader1) {
			int i=0;
			for(Champion t1:game.getFirstPlayer().getTeam()) {
				start.buttons1.get(i).setText(t1.getName());
				i++;
				
			}
			i=0;
			for(Champion t2:game.getSecondPlayer().getTeam()) {
				start.buttons2.get(i).setText(t2.getName());
				i++;
			}
			game.placeChampions();
			game.prepareChampionTurns();
		}
		for(JButton b:start.buttons1) {
			if(e.getSource()==b) {
				if(game.getFirstPlayer().getLeader()==null) {
				Champion c=null;
				for(Champion c1:game.getAvailableChampions()) {
					if(c1.getName().equals(b.getText())) {
						c=c1;
						break;
					}
				}
				game.getFirstPlayer().setLeader(c);
				b.setVisible(false);
				break;
			}
				if(game.getSecondPlayer().getLeader()!=null)
					start.doneleader.setVisible(true);
			}
			
		}
		for(JButton b:start.buttons2) {
			if(e.getSource()==b) {
				if(game.getSecondPlayer().getLeader()==null) {
				Champion c=null;
				for(Champion c1:game.getAvailableChampions()) {
					if(c1.getName().equals(b.getText())) {
						c=c1;
						break;
					}
				}
				game.getSecondPlayer().setLeader(c);
				b.setVisible(false);
				break;
			}
				if(game.getFirstPlayer().getLeader()!=null)
					start.doneleader.setVisible(true);	
			}
			
		}
		if(e.getSource()==start.punch) {
			a=new DamagingAbility("Punch",0,1,1,AreaOfEffect.SINGLETARGET,1,50);
			flag=true;
			JOptionPane.showMessageDialog(start, "Click on place you want to cast ability on board");
		
		}
		if(e.getSource()==start.doneleader) {
			updateBoard();
			updateTeam1();
			updateTeam2();
			updateTurn();
		}
		if(e.getSource()==start.apeff1) {
			String s="";
			for(Champion c:game.getFirstPlayer().getTeam()) {
				s+=c.getName() +'\n';
				for(Effect f:c.getAppliedEffects()) {
					s+=f.toString() + '\n';
				}
			}
			JOptionPane.showMessageDialog(start, s);
		}
		if(e.getSource()==start.apeff2) {
			String s="";
			for(Champion c:game.getSecondPlayer().getTeam()) {
				s+=c.getName() +'\n';
				for(Effect f:c.getAppliedEffects()) {
					s+=f.toString() +'\n';
				}
			}
			JOptionPane.showMessageDialog(start, s);
		}
		if(e.getSource()==start.ability1) {
			a=game.getCurrentChampion().getAbilities().get(0);
			if(a.getCastArea()!=AreaOfEffect.DIRECTIONAL || a.getCastArea()!=AreaOfEffect.SINGLETARGET) {
				try {
					game.castAbility(a);
					updateBoard();
					updateTeam1();
					updateTeam2();
					updateTurn();
				} catch (NotEnoughResourcesException e1) {
					JOptionPane.showMessageDialog(start, "Don't have enough resources to perform this action");
					 
				} catch (AbilityUseException e1) {
					JOptionPane.showMessageDialog(start, "Can't cast this Ability");
					 
				} catch (InvalidTargetException e1) {
					JOptionPane.showMessageDialog(start, "Invalid target");
					 
				} catch (CloneNotSupportedException e1) {
					 
				}
			}
			if(a.getCastArea()==AreaOfEffect.DIRECTIONAL) {
				start.up.setVisible(true);
				start.down.setVisible(true);
				start.left.setVisible(true);
				start.right.setVisible(true);
			}
			if(a.getCastArea()==AreaOfEffect.SINGLETARGET) {
				JOptionPane.showMessageDialog(start, "Click on place you want to cast ability on board");
				flag=true;
			}
		}
		if(e.getSource()==start.ability2) {
			a=game.getCurrentChampion().getAbilities().get(1);
			if(a.getCastArea()!=AreaOfEffect.DIRECTIONAL || a.getCastArea()!=AreaOfEffect.SINGLETARGET) {
				try {
					game.castAbility(a);
					updateBoard();
					updateTeam1();
					updateTeam2();
					updateTurn();
				} catch (NotEnoughResourcesException e1) {
					JOptionPane.showMessageDialog(start, "Don't have enough resources to perform this action");
					 
				} catch (AbilityUseException e1) {
					JOptionPane.showMessageDialog(start, "Can't cast this Ability");
					 
				} catch (InvalidTargetException e1) {
					JOptionPane.showMessageDialog(start, "Invalid target");
					 
				} catch (CloneNotSupportedException e1) {
					 
				}
			}
			if(a.getCastArea()==AreaOfEffect.DIRECTIONAL) {
				start.up.setVisible(true);
				start.down.setVisible(true);
				start.left.setVisible(true);
				start.right.setVisible(true);
			}
			if(a.getCastArea()==AreaOfEffect.SINGLETARGET) {
				JOptionPane.showMessageDialog(start, "Click on place you want to cast ability on board");
				flag=true;
			}
		
		}
		if(e.getSource()==start.ability3) {
			a=game.getCurrentChampion().getAbilities().get(2);
			if(a.getCastArea()!=AreaOfEffect.DIRECTIONAL || a.getCastArea()!=AreaOfEffect.SINGLETARGET) {
				try {
					game.castAbility(a);
					updateBoard();
					updateTeam1();
					updateTeam2();
					updateTurn();
				} catch (NotEnoughResourcesException e1) {
					JOptionPane.showMessageDialog(start, "Don't have enough resources to perform this action");
					 
				} catch (AbilityUseException e1) {
					JOptionPane.showMessageDialog(start, "Can't cast this Ability");
					 
				} catch (InvalidTargetException e1) {
					JOptionPane.showMessageDialog(start, "Invalid target");
					 
				} catch (CloneNotSupportedException e1) {
					 
				}
			}
			if(a.getCastArea()==AreaOfEffect.DIRECTIONAL) {
				start.up.setVisible(true);
				start.down.setVisible(true);
				start.left.setVisible(true);
				start.right.setVisible(true);
			}
			if(a.getCastArea()==AreaOfEffect.SINGLETARGET) {
				JOptionPane.showMessageDialog(start, "Click on place you want to cast ability on board");
				flag=true;
			}
		}
		if(e.getSource()==start.mUp) {
			try {
				game.move(Direction.UP);
				updateBoard();
				updateTeam1();
				updateTeam2();
				updateTurn();
			} catch (UnallowedMovementException e1) {
				JOptionPane.showMessageDialog(start, "Can't move here!");
			} catch (NotEnoughResourcesException e1) {
				JOptionPane.showMessageDialog(start, "Don't have enough resources to perform this action");
				
			}
			
		}
		if(e.getSource()==start.mDown) {
			try {
				game.move(Direction.DOWN);
				updateBoard();
				updateTeam1();
				updateTeam2();
				updateTurn();
			} catch (UnallowedMovementException e1) {
				JOptionPane.showMessageDialog(start, "Can't move here!");
				
			} catch (NotEnoughResourcesException e1) {
				JOptionPane.showMessageDialog(start, "Don't have enough resources to perform this action");
				
			}
			
		}
		if(e.getSource()==start.mLeft) {
			try {
				game.move(Direction.LEFT);
				updateBoard();
				updateTeam1();
				updateTeam2();
				updateTurn();
			} catch (UnallowedMovementException e1) {
				JOptionPane.showMessageDialog(start, "Can't move here!");
				
			} catch (NotEnoughResourcesException e1) {
				JOptionPane.showMessageDialog(start, "Don't have enough resources to perform this action");
				
			}
			
		}
		if(e.getSource()==start.mRight) {
			try {
				game.move(Direction.RIGHT);
				updateBoard();
				updateTeam1();
				updateTeam2();
				updateTurn();
			} catch (UnallowedMovementException e1) {
				JOptionPane.showMessageDialog(start, "Can't move here!");
				
			} catch (NotEnoughResourcesException e1) {
				JOptionPane.showMessageDialog(start, "Don't have enough resources to perform this action");
				
			}
			
		}
		if(e.getSource()==start.aUp) {
			try {
				game.attack(Direction.UP);
				updateBoard();
				updateTeam1();
				updateTeam2();
				updateTurn();
			} catch (NotEnoughResourcesException e1) {
				JOptionPane.showMessageDialog(start, "Don't have enough resources to perform this action");
				
			} catch (InvalidTargetException e1) {
				JOptionPane.showMessageDialog(start, "Invalid Target");
				
			} catch (AbilityUseException e1) {
				JOptionPane.showMessageDialog(start, "Can't Use this ability");
				 
			} catch (ChampionDisarmedException e1) {
				JOptionPane.showMessageDialog(start, "You are disarmed");
				 
			}
		}
		if(e.getSource()==start.aDown) {
			try {
				game.attack(Direction.DOWN);
				updateBoard();
				updateTeam1();
				updateTeam2();
				updateTurn();
			} catch (NotEnoughResourcesException e1) {
				JOptionPane.showMessageDialog(start, "Don't have enough resources to perform this action");
				 
			} catch (InvalidTargetException e1) {
				JOptionPane.showMessageDialog(start, "Invalid Target");
				 
			} catch (AbilityUseException e1) {
				JOptionPane.showMessageDialog(start, "Can't Use this ability");
				 
			} catch (ChampionDisarmedException e1) {
				JOptionPane.showMessageDialog(start, "You are disarmed");
				 
			}
		}
		if(e.getSource()==start.aLeft) {
			try {
				game.attack(Direction.LEFT);
				updateBoard();
				updateTeam1();
				updateTeam2();
				updateTurn();
			} catch (NotEnoughResourcesException e1) {
				JOptionPane.showMessageDialog(start, "Don't have enough resources to perform this action");
				 
			} catch (InvalidTargetException e1) {
				JOptionPane.showMessageDialog(start, "Invalid Target");
				 
			} catch (AbilityUseException e1) {
				JOptionPane.showMessageDialog(start, "Can't Use this ability");
				 
			} catch (ChampionDisarmedException e1) {
				JOptionPane.showMessageDialog(start, "You are disarmed");
				 
			}
		}
		if(e.getSource()==start.aRight) {
			try {
				game.attack(Direction.RIGHT);
				updateBoard();
				updateTeam1();
				updateTeam2();
				updateTurn();
			} catch (NotEnoughResourcesException e1) {
				JOptionPane.showMessageDialog(start, "Don't have enough resources to perform this action");
				 
			} catch (InvalidTargetException e1) {
				JOptionPane.showMessageDialog(start, "Invalid Target");
				 
			} catch (AbilityUseException e1) {
				JOptionPane.showMessageDialog(start, "Can't Use this ability");
				 
			} catch (ChampionDisarmedException e1) {
				JOptionPane.showMessageDialog(start, "You are disarmed");
				 
			}
		}
		
		if(e.getSource()==start.up) {
			try {
				start.up.setVisible(false);
				start.down.setVisible(false);
				start.left.setVisible(false);
				start.right.setVisible(false);
				game.castAbility(a, Direction.UP);
				updateBoard();
				updateTeam1();
				updateTeam2();
				updateTurn();
			} catch (AbilityUseException e1) {
				JOptionPane.showMessageDialog(start, "Can't Use this ability");
				 
			} catch (NotEnoughResourcesException e1) {
				JOptionPane.showMessageDialog(start, "Don't have enough resources to perform this action");
				 
			} catch (CloneNotSupportedException e1) {
				 
			}
			
		}
		if(e.getSource()==start.down) {
			try {
				start.up.setVisible(false);
				start.down.setVisible(false);
				start.left.setVisible(false);
				start.right.setVisible(false);
				game.castAbility(a, Direction.DOWN);
				updateBoard();
				updateTeam1();
				updateTeam2();
				updateTurn();
			} catch (AbilityUseException e1) {
				JOptionPane.showMessageDialog(start, "Can't Use this ability");
				 
			} catch (NotEnoughResourcesException e1) {
				JOptionPane.showMessageDialog(start, "Don't have enough resources to perform this action");
				 
			} catch (CloneNotSupportedException e1) {
				 
			}
			
		}
		if(e.getSource()==start.left) {
			try {
				start.up.setVisible(false);
				start.down.setVisible(false);
				start.left.setVisible(false);
				start.right.setVisible(false);
				game.castAbility(a, Direction.LEFT);
				updateBoard();
				updateTeam1();
				updateTeam2();
				updateTurn();
			} catch (AbilityUseException e1) {
				JOptionPane.showMessageDialog(start, "Can't Use this ability");
				 
			} catch (NotEnoughResourcesException e1) {
				JOptionPane.showMessageDialog(start, "Don't have enough resources to perform this action");
				 
			} catch (CloneNotSupportedException e1) {
				 
			}
			
		}
		if(e.getSource()==start.right) {
			try {
				start.up.setVisible(false);
				start.down.setVisible(false);
				start.left.setVisible(false);
				start.right.setVisible(false);
				game.castAbility(a, Direction.RIGHT);
				updateBoard();
				updateTeam1();
				updateTeam2();
				updateTurn();
			} catch (AbilityUseException e1) {
				JOptionPane.showMessageDialog(start, "Can't Use this ability");
				 
			} catch (NotEnoughResourcesException e1) {
				JOptionPane.showMessageDialog(start, "Don't have enough resources to perform this action");
				 
			} catch (CloneNotSupportedException e1) {
				 
			}
			
		}
		
		
		
		
		
		
		
		
	}
	public void updateTurn() {
		if(game.getTurnOrder().isEmpty()) {
			game.prepareChampionTurns();
		}
			Champion c=(Champion) game.getTurnOrder().remove();
			String s="               CURRENTLY PLAYING: "+ c.getName();
			if(game.getTurnOrder().isEmpty()) {
				s+= " NO NEXT CHAMPIONS!!";
			}
			else {
				s+= " NEXT: "+((Champion) game.getTurnOrder().peekMin()).getName();
			}
			start.turn.setText(s);
			game.getTurnOrder().insert(c);
		start.ability1.setText(c.getAbilities().get(0).getName());
		start.ability2.setText(c.getAbilities().get(1).getName());
		start.ability3.setText(c.getAbilities().get(2).getName());
		if(game.getCurrentChampion()==game.getFirstPlayer().getLeader() || game.getCurrentChampion()==game.getSecondPlayer().getLeader()) {
			start.leaderab.setVisible(true);
		}
		for(Effect e:game.getCurrentChampion().getAppliedEffects()) {
			if(e instanceof Disarm) {
				start.punch.setVisible(true);
				break;
			}
		}
	}

	public void updateTeam2() {
		String s2=start.getName2();
		for(Champion c:game.getSecondPlayer().getTeam()) {
			s2=s2 + '\n' +c.deets();
			if(c instanceof Hero) {
				s2+= '\n' + "Type: Hero";
			}
			if(c instanceof AntiHero) {
				s2+= '\n' + "Type: AntiHero";
			}
			if(c instanceof Villain) {
				s2+= '\n' + "Type: Villain";
			}
			if(c==game.getSecondPlayer().getLeader()) {
				s2+= '\n' + "Leader: YES";
			}
			if(c!=game.getSecondPlayer().getLeader()) {
				s2+= '\n' + "Leader: NO";
			}
			
		}
		if(game.isSecondLeaderAbilityUsed()) {
			s2+= '\n' + "Leader Ability: USED";
		}
		else{
			s2+= '\n' + "Leader Ability: NOT USED";
		}
		start.ana2.setText(s2);
	}
	public void updateTeam1() {
		String s1=start.getName1();
		for(Champion c:game.getFirstPlayer().getTeam()) {
			s1=s1 + '\n' +c.deets();
			if(c instanceof Hero) {
				s1+= '\n' + "Type: Hero";
			}
			if(c instanceof AntiHero) {
				s1+= '\n' + "Type: AntiHero";
			}
			if(c instanceof Villain) {
				s1+= '\n' + "Type: Villain";
			}
			if(c==game.getFirstPlayer().getLeader()) {
				s1+= '\n' + "Leader: YES";
			}
			if(c!=game.getFirstPlayer().getLeader()) {
				s1+= '\n' + "Leader: NO";
			}
			
		}
		if(game.isFirstLeaderAbilityUsed()) {
			s1+= '\n' + "Leader Ability: USED";
		}
		else {
			s1+= '\n' + "Leader Ability: NOT USED";
		}
		start.ana1.setText(s1);
	}
	public void updateBoard() {
		for(int i=0;i<5;i++) {
			for(int j=0;j<5;j++) {
				start.b[i][j].setText(null);
			}
		}
		for(int j=4;j>=0;j--) {
			for(int i=0;i<game.getBoard()[j].length;i++) {
				Object o=game.getBoard()[j][i];
				if(o instanceof Cover) {
					start.b[j][i].setText("Cover "+"HP:"+'\n'  +((Cover)o).getCurrentHP());
					start.b[j][i].setForeground(Color.gray);
					
				}
				if (o instanceof Champion) {
					start.b[j][i].setText(((Champion)o).getName()+" " +'\n' +"HP:"+'\n'  + ((Champion)o).getCurrentHP());
					if(game.getFirstPlayer().getTeam().contains(o)) {
						start.b[j][i].setForeground(Color.red);
					}
					if(game.getSecondPlayer().getTeam().contains(o)) {
						start.b[j][i].setForeground(Color.black);
					}
				}
				
			}
		}
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		
		for(int i=0;i<5;i++) {
			for(int j=0;j<5;j++) {
				if(flag) {
				if(e.getSource()==start.b[i][j]) {
					try {
						game.castAbility(a,i,j);
						updateBoard();
						updateTeam1();
						updateTeam2();
						updateTurn();
					} catch (NotEnoughResourcesException e1) {
						JOptionPane.showMessageDialog(start, "Don't have enough resources to perform this action");
						 
					} catch (AbilityUseException e1) {
						JOptionPane.showMessageDialog(start, "Can't cast this ability");
						 
					} catch (InvalidTargetException e1) {
						JOptionPane.showMessageDialog(start, "Invalid Target");
						 
					} catch (CloneNotSupportedException e1) {
						 
					}
				break;
				}
			}
			}
		}
		
		
	}
	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseEntered(MouseEvent e) {
		
		for(int i=0;i<start.c.size();i++) {
			if(e.getSource()==start.c.get(i)) {
				Champion c1=game.getAvailableChampions().get(i);
				start.details.setText(c1.toString());
			}
				
		}
		if(e.getSource()==start.ability1) {
			start.abil.setVisible(true);
			start.abil.setText(game.getCurrentChampion().getAbilities().get(0).toString());
		}
		if(e.getSource()==start.ability2) {
			start.abil.setVisible(true);
			start.abil.setText(game.getCurrentChampion().getAbilities().get(1).toString());
		}
		if(e.getSource()==start.ability3) {
			start.abil.setVisible(true);
			start.abil.setText(game.getCurrentChampion().getAbilities().get(2).toString());
		}
		
		
		
	}
	@Override
	public void mouseExited(MouseEvent e) {
		for(int i=0;i<start.c.size();i++) {
			if(e.getSource()==start.c.get(i)) {
				start.details.setText(null);
			}
				
		}
		if(e.getSource()==start.ability1) {
			start.abil.setVisible(false);
		}
		if(e.getSource()==start.ability2) {
			start.abil.setVisible(false);
		}
		if(e.getSource()==start.ability3) {
			start.abil.setVisible(false);
		}
		
	}
	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void keyPressed(KeyEvent e) {
		
		
	}
	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	
	
	
	
	
	
	
	
	
	
	
	
}
