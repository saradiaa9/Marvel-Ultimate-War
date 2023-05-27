package views;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.awt.*;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.border.Border;
import javax.swing.event.MouseInputListener;

import engine.Game;

public class start extends JFrame implements ActionListener{
	JButton t2;
	JPanel s;
	JPanel nameSelection;
	JButton done;
	JTextField play1;
	JTextField play2;
	JLabel pic;
	JLabel p1;
	JLabel p2;
	JLabel t1;
	JLabel t3;
	String name1="";
	String name2="";
	JPanel champions;
	JPanel players;
	public JTextArea player1;
	public JTextArea player2;
	public JTextArea details;
	public JButton leader1;
	public JPanel leaderA;
	public JTextArea leaderB;
	public JPanel leaderC;
	public JTextArea leaderD;
	public ArrayList<JButton> buttons1= new ArrayList<JButton>(3);
	public ArrayList<JButton> buttons2= new ArrayList<JButton>(3);
	public ArrayList<JButton> c= new ArrayList<JButton>(15);
	public JButton doneleader;
	public JPanel p;
	public JPanel board;
	public JLabel [][] b= new JLabel[5][5];
	public JTextArea ana1;
	public JTextArea ana2;
	public JButton apeff1;
	public JButton apeff2;
	public JLabel turn;
	public JPanel g;
	public JButton ability1;
	public JButton ability2;
	public JButton ability3;
	public JButton leaderab;
	public JPanel small;
	public JButton mUp;
	public JButton mDown;
	public JButton mRight;
	public JButton mLeft;
	public JButton aUp;
	public JButton aDown;
	public JButton aRight;
	public JButton aLeft;
	public JLabel move;
	public JLabel attack;
	public JRadioButton up;
	public JRadioButton down;
	public JRadioButton left;
	public JRadioButton right;
	public JButton end;
	public JPanel win;
	public JLabel winner;
	public JLabel winpic;
	public JButton punch;
	public JTextPane abil;
	public JButton getDone() {
		return done;
	}

	public JTextField getPlay1() {
		return play1;
	}

	public JTextField getPlay2() {
		return play2;
	}
	public start() {
		//start screen panel
		s= new JPanel();
		t1= new JLabel("PRESS");
		t1.setFont(new Font("Century Gothic",Font.BOLD,60));
		t1.setForeground(Color.red);
		t1.setBounds(350,0,900,80);
		t2 = new JButton();
		t2.setIcon(new ImageIcon("LOGO2.png"));
		t2.setBounds(40,100,790,300);
		t2.setBorder(null);
		t3= new JLabel("TO START!");
		t3.setFont(new Font("Century Gothic",Font.BOLD,60));
		t3.setForeground(Color.red);
		t3.setBounds(310,420,900,80);
		s.setBackground(Color.white);
		s.setBounds(0,0,900,550);
		s.setLayout(null);
		s.add(t1);
		s.add(t2);
		s.add(t3);
		t2.addActionListener(this);
		s.setVisible(true);
		
		//choose names panel
		pic= new JLabel();
		pic.setIcon(new ImageIcon("LOGO2.png"));
		pic.setBounds(40,0,790,300);
		p1= new JLabel("PLAYER 1");
		p1.setFont(new Font("Century Gothic",Font.BOLD,40));
		p1.setBounds(150,290,200,100);
		play1= new JTextField("Enter name here");
		play1.setFont(new Font("Century Gothic",Font.PLAIN,20));
		play1.setBounds(80,380,300,50);
		p2= new JLabel("PLAYER 2");
		p2.setFont(new Font("Century Gothic",Font.BOLD,40));
		p2.setBounds(530,290,200,100);
		play2= new JTextField("Enter name here");
		play2.setFont(new Font("Century Gothic",Font.PLAIN,20));
		play2.setBounds(480,380,300,50);
		done= new JButton("DONE");
		done.setFont(new Font("Century Gothic",Font.BOLD,30));
		done.setBounds(330,450,200,50);
		done.setBackground(Color.red);
		done.setForeground(Color.white);
		done.setBorder(null);
		done.addActionListener(this);
		nameSelection= new JPanel();
		nameSelection.setBackground(Color.white);
		nameSelection.setBounds(0,0,900,550);
		nameSelection.setLayout(null);
		nameSelection.setVisible(false);
		nameSelection.add(pic);
		nameSelection.add(p1);
		nameSelection.add(play1);
		nameSelection.add(p2);
		nameSelection.add(play2);
		nameSelection.add(done);
		pic.setVisible(false);
		p1.setVisible(false);
		play1.setVisible(false);
		p2.setVisible(false);
		play2.setVisible(false);
		done.setVisible(false);
		
		//choose champions panel
		champions= new JPanel();
		champions.setBackground(Color.white);
		champions.setBounds(0,0,833,640);
		champions.setLayout(new GridLayout(3,5));
		for(int i=0;i<15;i++) {
			JButton s= new JButton(i+"");
			s.setBackground(Color.white);
			s.setForeground(Color.red);
			s.setIcon(new ImageIcon("champion" + i +".jpg"));
			s.setFont(new Font("Century Gothic",Font.BOLD,14));
			c.add(s);
		}
		for(JButton c1:c) {
			champions.add(c1);
		}
		champions.setVisible(false);
		player1= new JTextArea();
		player1.setBounds(833,0,208,350);
		player1.setFont(new Font("Century Gothic",Font.BOLD,25));
		player1.setForeground(Color.black);
		player1.setVisible(false);
		player2= new JTextArea();
		player2.setBounds(1041,0,209,350);
		player2.setFont(new Font("Century Gothic",Font.BOLD,25));
		player2.setForeground(Color.black);
		player2.setVisible(false);
		details= new JTextArea();
		details.setBounds(833,390,417,250);
		details.setFont(new Font("Century Gothic",Font.BOLD,14));
		details.setForeground(Color.black);
		details.setVisible(false);
		leader1=new JButton("CHOOSE LEADER");
		leader1.setFont(new Font("Century Gothic",Font.BOLD,25));
		leader1.setBackground(Color.red);
		leader1.setForeground(Color.white);
		leader1.setBounds(833,350,417,40);
		leader1.setVisible(false);
		leader1.addActionListener(this);
		
		//Choose leader panel
		leaderB= new JTextArea();
		leaderB.setBounds(0,0,1250,60);
		leaderB.setFont(new Font("Century Gothic",Font.BOLD,40));
		leaderB.setVisible(false);
		leaderA= new JPanel();
		leaderA.setLayout(new GridLayout(1,3));
		leaderA.setBounds(0,60,1250,260);
		leaderA.setVisible(false);
		leaderD= new JTextArea();
		leaderD.setBounds(0,320,1000,60);
		leaderD.setFont(new Font("Century Gothic",Font.BOLD,40));
		leaderD.setVisible(false);
		leaderC= new JPanel();
		leaderC.setLayout(new GridLayout(1,3));
		leaderC.setBounds(0,380,1250,260);
		leaderC.setVisible(false);
		for(int i=0;i<3;i++) {
			JButton s= new JButton();
			s.setBackground(Color.white);
			s.setFont(new Font("Century Gothic",Font.BOLD,20));
			s.setForeground(Color.red);
			buttons1.add(s);
			
		}
		for(int i=0;i<3;i++) {
			JButton s= new JButton();
			s.setBackground(Color.white);
			s.setFont(new Font("Century Gothic",Font.BOLD,20));
			s.setForeground(Color.red);
			buttons2.add(s);
		}
		for(int i=0;i<3;i++) {
			leaderA.add(buttons1.get(i));
			leaderC.add(buttons2.get(i));
			
		}
		p= new JPanel();
		p.setBackground(Color.white);
		p.setBounds(1000,320,250,60);
		p.setLayout(null);
		doneleader = new JButton("DONE");
		doneleader.setBackground(Color.red);
		doneleader.setForeground(Color.white);
		doneleader.setFont(new Font("Century Gothic",Font.BOLD,20));
		doneleader.setBounds(0,0,250,60);
		p.add(doneleader);
		p.setVisible(false);
		doneleader.setVisible(false);
		doneleader.addActionListener(this);
		
		
		// board panel
		board=new JPanel();
		board.setLayout(new GridLayout(5,5));
		board.setBounds(325,0,650,500);
		board.setBackground(Color.white);
		for(int i=0;i<5;i++) {
			for(int j=0;j<5;j++) {
				b[i][j]=new JLabel();
				b[i][j].setVisible(false);
				b[i][j].setBorder(BorderFactory.createLineBorder(Color.red, 1));
			}
			}
			
		for(int i=4;i>=0;i--) {
			for(int j=0;j<5;j++) {
				board.add(b[i][j]);
			}
		}
		ana1=new JTextArea();
		ana1.setBackground(Color.white);
		ana1.setForeground(Color.red);
		ana1.setFont(new Font("Century Gothic",Font.BOLD,12));
		ana1.setBounds(0,0,325,550);
		ana2=new JTextArea();
		ana2.setBackground(Color.white);
		ana2.setForeground(Color.black);
		ana2.setFont(new Font("Century Gothic",Font.BOLD,12));
		ana2.setBounds(975,0,325,550);
		apeff1=new JButton("APPLIED EFFECTS");
		apeff1.setBackground(Color.red);
		apeff1.setForeground(Color.white);
		apeff1.setFont(new Font("Century Gothic",Font.BOLD,10));
		apeff1.setBounds(0,0,325,20);
		apeff2=new JButton("APPLIED EFFECTS");
		apeff2.setBackground(Color.black);
		apeff2.setForeground(Color.white);
		apeff2.setFont(new Font("Century Gothic",Font.BOLD,10));
		apeff2.setBounds(975,0,325,20);
		abil=new JTextPane();
		abil.setBackground(Color.white);
		abil.setForeground(Color.red);
		abil.setFont(new Font("Century Gothic",Font.BOLD,7));
		abil.setBounds(430,0,220,140);
		abil.setVisible(false);
		turn = new JLabel();
		turn.setForeground(Color.gray);
		turn.setFont(new Font("Century Gothic",Font.BOLD,20));
		turn.setBounds(0,0,650,20);
		ability1= new JButton();
		ability1.setBackground(Color.gray);
		ability1.setForeground(Color.white);
		ability1.setFont(new Font("Century Gothic",Font.BOLD,10));
		ability1.setBounds(0,20,150,20);
		ability2= new JButton();
		ability2.setBackground(Color.gray);
		ability2.setForeground(Color.white);
		ability2.setFont(new Font("Century Gothic",Font.BOLD,10));
		ability2.setBounds(150,20,150,20);
		ability3= new JButton();
		ability3.setBackground(Color.gray);
		ability3.setForeground(Color.white);
		ability3.setFont(new Font("Century Gothic",Font.BOLD,10));
		ability3.setBounds(300,20,150,20);
		leaderab= new JButton("LEADER ABILITY");
		leaderab.setBackground(Color.gray);
		leaderab.setForeground(Color.white);
		leaderab.setFont(new Font("Century Gothic",Font.BOLD,10));
		leaderab.setBounds(450,20,150,20);
		punch=new JButton("PUNCH");
		punch.setBackground(Color.gray);
		punch.setForeground(Color.white);
		punch.setFont(new Font("Century Gothic",Font.BOLD,10));
		punch.setBounds(820,0,100,20);
		punch.setVisible(false);
		small= new JPanel();
		small.setLayout(null);
		small.setBackground(Color.white);
		small.setBounds(325,500,650,50);
		small.add(turn);
		small.add(ability1);
		small.add(ability2);
		small.add(ability3);
		small.add(leaderab);
		mUp= new JButton("U");
		mUp.setBackground(Color.black);
		mUp.setForeground(Color.white);
		mUp.setFont(new Font("Century Gothic",Font.BOLD,10));
		mUp.setBounds(330,0,50,30);
		mLeft= new JButton("L");
		mLeft.setBackground(Color.black);
		mLeft.setForeground(Color.white);
		mLeft.setFont(new Font("Century Gothic",Font.BOLD,10));
		mLeft.setBounds(280,30,50,30);
		mRight= new JButton("R");
		mRight.setBackground(Color.black);
		mRight.setForeground(Color.white);
		mRight.setFont(new Font("Century Gothic",Font.BOLD,10));
		mRight.setBounds(380,30,50,30);
		mDown= new JButton("D");
		mDown.setBackground(Color.black);
		mDown.setForeground(Color.white);
		mDown.setFont(new Font("Century Gothic",Font.BOLD,10));
		mDown.setBounds(330,60,50,30);
		move=new JLabel("MOVE");
		move.setBackground(Color.white);
		move.setForeground(Color.black);
		move.setFont(new Font("Century Gothic",Font.BOLD,10));
		move.setBounds(330,30,50,30);
		aUp= new JButton("U");
		aUp.setBackground(Color.red);
		aUp.setForeground(Color.white);
		aUp.setFont(new Font("Century Gothic",Font.BOLD,10));
		aUp.setBounds(700,0,50,30);
		aLeft= new JButton("L");
		aLeft.setBackground(Color.red);
		aLeft.setForeground(Color.white);
		aLeft.setFont(new Font("Century Gothic",Font.BOLD,10));
		aLeft.setBounds(650,30,50,30);
		aRight= new JButton("R");
		aRight.setBackground(Color.red);
		aRight.setForeground(Color.white);
		aRight.setFont(new Font("Century Gothic",Font.BOLD,10));
		aRight.setBounds(750,30,50,30);
		aDown= new JButton("D");
		aDown.setBackground(Color.red);
		aDown.setForeground(Color.white);
		aDown.setFont(new Font("Century Gothic",Font.BOLD,10));
		aDown.setBounds(700,60,50,30);
		attack=new JLabel("ATTACK");
		attack.setBackground(Color.white);
		attack.setForeground(Color.red);
		attack.setFont(new Font("Century Gothic",Font.BOLD,10));
		attack.setBounds(700,30,50,30);
		up=new JRadioButton("UP");
		up.setFont(new Font("Century Gothic",Font.BOLD,8));
		up.setForeground(Color.black);
		up.setBounds(450,0,40,30);
		down=new JRadioButton("DOWN");
		down.setFont(new Font("Century Gothic",Font.BOLD,8));
		down.setForeground(Color.black);
		down.setBounds(450,30,40,30);
		left=new JRadioButton("LEFT");
		left.setFont(new Font("Century Gothic",Font.BOLD,8));
		left.setForeground(Color.black);
		left.setBounds(490,0,40,30);
		right=new JRadioButton("RIGHT");
		right.setFont(new Font("Century Gothic",Font.BOLD,8));
		right.setForeground(Color.black);
		right.setBounds(490,30,40,30);
		up.setVisible(false);
		down.setVisible(false);
		left.setVisible(false);
		right.setVisible(false);
		end=new JButton("END TURN!");
		end.setBackground(Color.gray);
		end.setForeground(Color.black);
		end.setFont(new Font("Century Gothic",Font.BOLD,10));
		end.setBounds(820,30,100,50);
		end.setVisible(false);
		g=new JPanel();
		g.setLayout(null);
		g.setBackground(Color.white);
		g.setBounds(0,550,1300,140);
		g.add(end);
		g.add(up);
		g.add(down);
		g.add(left);
		g.add(right);
		g.add(attack);
		g.add(apeff1);
		g.add(apeff2);
		g.add(mUp);
		g.add(move);
		g.add(mDown);
		g.add(mLeft);
		g.add(mRight);
		g.add(aUp);
		g.add(aDown);
		g.add(aLeft);
		g.add(aRight);
		g.add(abil);
		g.add(punch);
		attack.setVisible(false);
		move.setVisible(false);
		mUp.setVisible(false);
		mDown.setVisible(false);
		mLeft.setVisible(false);
		mRight.setVisible(false);
		aUp.setVisible(false);
		aDown.setVisible(false);
		aLeft.setVisible(false);
		aRight.setVisible(false);
		small.setVisible(false);
		leaderab.setVisible(false);
		ability1.setVisible(false);
		ability2.setVisible(false);
		ability3.setVisible(false);
		g.setVisible(false);
		turn.setVisible(false);
		apeff1.setVisible(false);
		apeff2.setVisible(false);
		board.setVisible(false);
		ana1.setVisible(false);
		ana2.setVisible(false);
		
		//Winner
		win=new JPanel();
		win.setBounds(0,0,900,550);
		win.setLayout(null);
		winner=new JLabel();
		winner.setForeground(Color.black);
		winner.setFont(new Font("Century Gothic",Font.BOLD,30));
		winner.setBounds(40,320,790,200);
		winpic=new JLabel();
		winpic.setIcon(new ImageIcon("LOGO2.png"));
		winpic.setBounds(40,0,790,300);
		win.add(winpic);
		win.add(winner);
		win.setVisible(false);
		winner.setVisible(false);
		winpic.setVisible(false);
		
		
		
		//frame
		ImageIcon image= new ImageIcon("Marvel_Logo.svg.png");
		this.setTitle("Marvel-Ultimate War");
		this.setIconImage(image.getImage());
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setBackground(Color.white);
		this.setSize(900,550);
		this.setVisible(true);
		this.setResizable(false);
		this.setLayout(null);
		this.add(s);
		this.add(nameSelection);
		this.add(champions);
		this.add(player1);
		this.add(details);
		this.add(player2);
		this.add(leader1);
		this.add(leaderA);
		this.add(leaderB);
		this.add(leaderC);
		this.add(leaderD);
		this.add(p);
		this.add(board);
		this.add(ana1);
		this.add(ana2);
		this.add(g);
		this.add(small);
		this.add(win);
	}

	public String getName1() {
		return name1;
	}

	public String getName2() {
		return name2;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==t2) {
			s.setVisible(false);
			nameSelection.setVisible(true);
			pic.setVisible(true);
			p1.setVisible(true);
			play1.setVisible(true);
			p2.setVisible(true);
			play2.setVisible(true);
			done.setVisible(true);
		}
		if(e.getSource()==done) {
			name1=play1.getText();
			name2=play2.getText();
			if(name1.equals("Enter name here") || name2.equals("Enter name here") || name1.equals("") || name2.equals("")) {
				JOptionPane.showMessageDialog(this,"You have to enter names");
			}
			else {
				nameSelection.setVisible(false);
				player1.setText( name1);
				player2.setText(name2);
				this.setSize(1250,640);
				champions.setVisible(true);
				for(JButton cs:c) {
					cs.setVisible(true);
				}
				player1.setVisible(true);
				player2.setVisible(true);
				details.setVisible(true);
				
			}
		}
		if(e.getSource()==leader1) {
			champions.setVisible(false);
			for(JButton cs:c) {
				cs.setVisible(false);
			}
			player1.setVisible(false);
			player2.setVisible(false);
			details.setVisible(false);
			leader1.setVisible(false);
			leaderB.setText(name1);
			leaderD.setText(name2);
			leaderB.setVisible(true);
			leaderD.setVisible(true);
			leaderA.setVisible(true);
			leaderC.setVisible(true);
			p.setVisible(true);
			
			
		}
		if(e.getSource()==doneleader) {
			leaderB.setVisible(false);
			leaderD.setVisible(false);
			leaderA.setVisible(false);
			leaderC.setVisible(false);
			p.setVisible(false);
			doneleader.setVisible(false);
			this.setSize(1300,690);
			board.setVisible(true);
			for(int i=0;i<5;i++) {
				for(int j=0;j<5;j++) {
					b[i][j].setVisible(true);
				}
			}
			ana1.setVisible(true);
			ana2.setVisible(true);
			apeff1.setVisible(true);
			apeff2.setVisible(true);
			turn.setVisible(true);
			mUp.setVisible(true);
			mDown.setVisible(true);
			mLeft.setVisible(true);
			mRight.setVisible(true);
			aUp.setVisible(true);
			aDown.setVisible(true);
			aLeft.setVisible(true);
			aRight.setVisible(true);
			g.setVisible(true);
			ability1.setVisible(true);
			ability2.setVisible(true);
			ability3.setVisible(true);
			small.setVisible(true);
			move.setVisible(true);
			attack.setVisible(true);
			end.setVisible(true);
		}
		
		
	}

	
}
