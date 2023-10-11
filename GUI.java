package blackjack;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowEvent;
import java.util.*;

public class GUI extends JFrame {
	
	//randomize cards
	Random rand = new Random();
	
	//temporary integer used for used status
	int tempS;
	
	//boolean is used to indicate whether the dealer is thinking or not
	boolean dHitter = false;
	
	//list of cards
	ArrayList<Card> Cards = new ArrayList<Card>();
	
	//list of messages
	ArrayList<Message> mes = new ArrayList<Message>();
	
	//fonts used
	Font fontCard = new Font("Times New Roman", Font.PLAIN, 40);
	Font fontQuest = new Font("Times New Roman", Font.BOLD, 40);
	Font fontButton = new Font("Times New Roman", Font.PLAIN, 25);
	Font fontMes = new Font("Times New Roman", Font.ITALIC, 30);
	
	//Log message colors
	Color colorDealer = Color.red;
	Color colorPlayer = new Color(25,55,255);
	
	//strings used
	String questHitStay = new String("Hit or Stay?");
	String questPlayMore = new String("Play more?");
	
	//colors used
	Color colorBackground = new Color(39,119,20);
	Color colorButton = new Color(204,204,0);
	
	//buttons used
	JButton bHit = new JButton();
	JButton bStay = new JButton();
	JButton bYes = new JButton();
	JButton bNo = new JButton();
	
	//screen resolution
	int sWidth = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
	int sHeight = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
	
	//window resolution
	int wWidth = 1300;
	int wHeight = 800;
	
	//card grid position and dimensions
	int gridX = 50;
	int gridY = 50;
	int gridWidth = 900;
	int gridHeight = 400;
	
	//card spacing and dimensions
	int spacing = 10;
	int rounding = 10;
	int tCardWidth = (int) gridWidth/6;
	int tCardHeight = (int) gridHeight/2;
	int cardW = tCardWidth - spacing*2;
	int cardH = tCardHeight - spacing*2;
	
	//booleans about phases
	boolean hit_stay_q = true;
	boolean dealer_turn = false;
	boolean play_more_q = false;
	
	//player and dealer card array
	ArrayList<Card> playerCards = new ArrayList<Card>();
	ArrayList<Card> dealerCards = new ArrayList<Card>();
	
	//player and dealer totals
	int pMinTot = 0;
	int pMaxTot = 0;
	int dMinTot = 0;
	int dMaxTot = 0;
	
	//polygons for diamond shapes
	int[] polyX = new int[4];
	int[] polyY = new int[4];
	
	public GUI() {
		this.setTitle("Blackjack");
		this.setBounds((sWidth-wWidth-6)/2, (sHeight-wHeight-29)/2, wWidth+6, wHeight+29);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		
		Board board = new Board();
		this.setContentPane(board);
		board.setLayout(null);
		
		Move move = new Move();
		this.addMouseMotionListener(move);
		
		Click click = new Click();
		this.addMouseListener(click);
		
		//button actions
		
		ActHit actHit = new ActHit();
		bHit.addActionListener(actHit);
		bHit.setBounds(1000, 200, 100, 50);
		bHit.setBackground(colorButton);
		bHit.setFont(fontButton);
		bHit.setText("HIT");
		board.add(bHit);
		
		ActStay actStay = new ActStay();
		bStay.addActionListener(actStay);
		bStay.setBounds(1150, 200, 100, 50);
		bStay.setBackground(colorButton);
		bStay.setFont(fontButton);
		bStay.setText("STAY");
		board.add(bStay);
		
		ActYes actYes = new ActYes();
		bYes.addActionListener(actYes);
		bYes.setBounds(1000, 600, 100, 50);
		bYes.setBackground(colorButton);
		bYes.setFont(fontButton);
		bYes.setText("YES");
		board.add(bYes);
		
		ActNo actNo = new ActNo();
		bNo.addActionListener(actNo);
		bNo.setBounds(1150, 600, 100, 50);
		bNo.setBackground(colorButton);
		bNo.setFont(fontButton);
		bNo.setText("NO");
		board.add(bNo);
		
		//creating all cards
		
		String temp_str = "starting_temp_str_name";
		for (int i = 0; i < 52; i++) {
			if (i % 4 == 0) {
				temp_str = "Spades";
			} else if (i % 4 == 1) {
				temp_str = "Hearts";
			} else if (i % 4 == 2) {
				temp_str = "Diamonds";
			} else if (i % 4 == 3) {
				temp_str = "Clubs";
			}
			Cards.add(new Card((i/4) + 1, temp_str, i));
		}
		
		
		tempS = rand.nextInt(52);
		playerCards.add(Cards.get(tempS));
		Cards.get(tempS).setUsed();
	
		
		tempS = rand.nextInt(52);
		while (Cards.get(tempS).used == true) {
			tempS = rand.nextInt(52);
		}
		dealerCards.add(Cards.get(tempS));
		Cards.get(tempS).setUsed();
	
		
		tempS = rand.nextInt(52);
		while (Cards.get(tempS).used == true) {
			tempS = rand.nextInt(52);
		}
		playerCards.add(Cards.get(tempS));
		Cards.get(tempS).setUsed();
	
		
		tempS = rand.nextInt(52);
		while (Cards.get(tempS).used == true) {
			tempS = rand.nextInt(52);
		}
		dealerCards.add(Cards.get(tempS));
		Cards.get(tempS).setUsed();
	
	}
	
	public void totalsChecker() {
		
		int acesCount;
		
		//calculation of player's totals
		pMinTot = 0;
		pMaxTot = 0;
		acesCount = 0;
		
		for (Card c : playerCards) {
			pMinTot += c.num;
			pMaxTot += c.num;
			if (c.card == "Ace")
				acesCount++;
			
		}
		
		if (acesCount > 0)
			pMaxTot += 10;
		
		dMinTot = 0;
		dMaxTot = 0;
		acesCount = 0;
		
		for (Card c : dealerCards) {
			dMinTot += c.num;
			dMaxTot += c.num;
			if (c.card == "Ace")
				acesCount++;
			
		}
		
		if (acesCount > 0)
			dMaxTot += 10;
	}
	
	public void setWinner() {
		int pPoints = 0;
		int dPoints = 0;
		
		if (pMaxTot > 21) {
			pPoints = pMinTot;
		} else {
			pPoints = pMaxTot;
		}
		
		if (dMaxTot > 21) {
			dPoints = dMinTot;
		} else {
			dPoints = dMaxTot;
		}
		
		if (pPoints > 21 && dPoints > 21) {
			mes.add(new Message("Nobody wins!", "Dealer"));
		} else if (dPoints > 21) {
			mes.add(new Message("You win!", "Player"));
			Main.playerWins++;
		} else if (pPoints > 21) {
			mes.add(new Message("Dealer wins!", "Dealer"));
			Main.dealerWins++;
		} else if (pPoints > dPoints) {
			mes.add(new Message("You win!", "Player"));
			Main.playerWins++;
		} else {
			mes.add(new Message("Dealer wins!", "Dealer"));
			Main.dealerWins++;
		}
		
	}
	
	public void dealerHitStay() {
		dHitter = true;
		
		int dAvailable = 0;
		if (dMaxTot > 21) {
			dAvailable = dMinTot;
		} else {
			dAvailable = dMaxTot;
		}
		
		int pAvailable = 0;
		if (pMaxTot > 21) {
			pAvailable = pMinTot;
		} else {
			pAvailable = pMaxTot;
		}
		
		repaint();
		
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if ((dAvailable < pAvailable && pAvailable <= 21) || dAvailable < 16) {
			int tempMax = 0;
			if (dMaxTot <= 21) {
				tempMax = dMaxTot;
			} else {
				tempMax = dMinTot;
			}
			String mess = ("Dealer decided to hit! (total: " + Integer.toString(tempMax) + ")");
			mes.add(new Message(mess, "Dealer"));
		
			tempS = rand.nextInt(52);
			while (Cards.get(tempS).used == true) {
				tempS = rand.nextInt(52);
			}
			dealerCards.add(Cards.get(tempS));
			Cards.get(tempS).setUsed();
	
		} else {
			int tempMax = 0;
			if (dMaxTot <= 21) {
				tempMax = dMaxTot;
			} else {
				tempMax = dMinTot;
			}
			String mess = ("Dealer decided to stay! (total: " + Integer.toString(tempMax) + ")");
			mes.add(new Message(mess, "Dealer"));
			setWinner();
			dealer_turn = false;
			play_more_q = true;
		}
		dHitter = false;
	}
	
	public void refresher() {
		
		if (hit_stay_q == true) {
			bHit.setVisible(true);
			bStay.setVisible(true);
		} else {
			bHit.setVisible(false);
			bStay.setVisible(false);
		}
		
		if (dealer_turn == true) {
			if (dHitter == false)
				dealerHitStay();
		}
		
		if (play_more_q == true) {
			bYes.setVisible(true);
			bNo.setVisible(true);
		} else {
			bYes.setVisible(false);
			bNo.setVisible(false);
		}
		
		totalsChecker();
		
		if ((pMaxTot == 21 || pMinTot >= 21) && hit_stay_q == true) {
			int tempMax = 0;
			if (pMaxTot <= 21) {
				tempMax = pMaxTot;
			} else {
				tempMax = pMinTot;
			}
			String mess = ("Auto pass! (total: " + Integer.toString(tempMax) + ")");
			mes.add(new Message(mess, "Player"));
			hit_stay_q = false;
			dealer_turn = true;
		}
		
		if ((dMaxTot == 21 || dMinTot >= 21) && dealer_turn == true) {
			int tempMax = 0;
			if (dMaxTot <= 21) {
				tempMax = dMaxTot;
			} else {
				tempMax = dMinTot;
			}
			String mess = ("Dealer auto pass! (total: " + Integer.toString(tempMax) + ")");
			mes.add(new Message(mess, "Dealer"));
			setWinner();
			dealer_turn = false;
			play_more_q = true;
		}
		
		repaint();
	}
	
public class Board extends JPanel {
		
		public void paintComponent(Graphics g) {
			//background
			g.setColor(colorBackground);
			g.fillRect(0, 0, wWidth, wHeight);
			
			//questions
			if (hit_stay_q == true) {
				g.setColor(Color.black);
				g.setFont(fontQuest);
				g.drawString(questHitStay, gridX+gridWidth+60, gridY+90);
				g.drawString("Total:", gridX+gridWidth+60, gridY+290);
				if (pMinTot == pMaxTot) {
					g.drawString(Integer.toString(pMaxTot), gridX+gridWidth+60, gridY+350);
				} else if (pMaxTot <= 21) {
					g.drawString(Integer.toString(pMinTot) + " or " + Integer.toString(pMaxTot), gridX+gridWidth+60, gridY+350);
				} else {
					g.drawString(Integer.toString(pMinTot), gridX+gridWidth+60, gridY+350);
				}
			} else if (play_more_q == true) {
				g.setColor(Color.black);
				g.setFont(fontQuest);
				g.drawString(questPlayMore, gridX+gridWidth+70, gridY+490);
			}
		
			g.setColor(Color.black);
			g.fillRect(gridX, gridY+gridHeight+50, gridWidth, 500);
			
			//Log
			g.setFont(fontMes);
			int logIndex = 0;
			for (Message L : mes) {
				if (L.getWho().equalsIgnoreCase("Dealer")) {
					g.setColor(colorDealer);
				} else {
					g.setColor(colorPlayer);
				}
				g.drawString(L.getMessage(), gridX+20, gridY+480+logIndex*35);
				logIndex++;
			}
			
			//score
			g.setColor(Color.BLACK);
			g.setFont(fontQuest);
			String score = ("Score: " + Integer.toString(Main.playerWins) + " - " + Integer.toString(Main.dealerWins));
			g.drawString(score, gridX+gridWidth+70, gridY+gridHeight+300);
			
			//player cards
			int index = 0;
			for (Card c : playerCards) {
				g.setColor(Color.white);
				g.fillRect(gridX+spacing+tCardWidth*index+rounding, gridY+spacing, cardW-rounding*2, cardH);
				g.fillRect(gridX+spacing+tCardWidth*index, gridY+spacing+rounding, cardW, cardH-rounding*2);
				g.fillOval(gridX+spacing+tCardWidth*index, gridY+spacing, rounding*2, rounding*2);
				g.fillOval(gridX+spacing+tCardWidth*index, gridY+spacing+cardH-rounding*2, rounding*2, rounding*2);
				g.fillOval(gridX+spacing+tCardWidth*index+cardW-rounding*2, gridY+spacing, rounding*2, rounding*2);
				g.fillOval(gridX+spacing+tCardWidth*index+cardW-rounding*2, gridY+spacing+cardH-rounding*2, rounding*2, rounding*2);
				
				g.setFont(fontCard);
				if (c.shape.equalsIgnoreCase("Hearts") || c.shape.equalsIgnoreCase("Diamonds")) {
					g.setColor(Color.red);
				} else {
					g.setColor(Color.black);
				}
				
				g.drawString(c.symbol, gridX+spacing+tCardWidth*index+rounding, gridY+spacing+cardH-rounding);
				//drawing heart shape
				if (c.shape.equalsIgnoreCase("Hearts")) {
					g.fillOval(gridX+tCardWidth*index+42, gridY+70, 35, 35);
					g.fillOval(gridX+tCardWidth*index+73, gridY+70, 35, 35);
					g.fillArc(gridX+tCardWidth*index+30, gridY+90, 90, 90, 51, 78);
					//drawing diamond
				} else if (c.shape.equalsIgnoreCase("Diamonds")) {
					polyX[0] = gridX+tCardWidth*index+75;
					polyX[1] = gridX+tCardWidth*index+50;
					polyX[2] = gridX+tCardWidth*index+75;
					polyX[3] = gridX+tCardWidth*index+100;
					polyY[0] = gridY+60;
					polyY[1] = gridY+100;
					polyY[2] = gridY+140;
					polyY[3] = gridY+100;
					g.fillPolygon(polyX, polyY, 4);
					//drawing spade
				} else if (c.shape.equalsIgnoreCase("Spades")) {
					g.fillOval(gridX+tCardWidth*index+42, gridY+90, 35, 35);
					g.fillOval(gridX+tCardWidth*index+73, gridY+90, 35, 35);
					g.fillArc(gridX+tCardWidth*index+30, gridY+15, 90, 90, 51+180, 78);
					g.fillRect(gridX+tCardWidth*index+70, gridY+100, 10, 40);
					//drawing club
					
				} else {
					g.fillOval(gridX+tCardWidth*index+40, gridY+90, 35, 35);
					g.fillOval(gridX+tCardWidth*index+75, gridY+90, 35, 35);
					g.fillOval(gridX+tCardWidth*index+58, gridY+62, 35, 35);
					g.fillRect(gridX+tCardWidth*index+70, gridY+75, 10, 70);
				}
				
				//-------------------------
				index++;
			}
			
			if (dealer_turn == true || play_more_q == true) {
				//dealer cards
				index = 0;
				for (Card c : dealerCards) {
					g.setColor(Color.white);
					g.fillRect(gridX+spacing+tCardWidth*index+rounding, gridY+spacing+200, cardW-rounding*2, cardH);
					g.fillRect(gridX+spacing+tCardWidth*index, gridY+spacing+rounding+200, cardW, cardH-rounding*2);
					g.fillOval(gridX+spacing+tCardWidth*index, gridY+spacing+200, rounding*2, rounding*2);
					g.fillOval(gridX+spacing+tCardWidth*index, gridY+spacing+cardH-rounding*2+200, rounding*2, rounding*2);
					g.fillOval(gridX+spacing+tCardWidth*index+cardW-rounding*2, gridY+spacing+200, rounding*2, rounding*2);
					g.fillOval(gridX+spacing+tCardWidth*index+cardW-rounding*2, gridY+spacing+cardH-rounding*2+200, rounding*2, rounding*2);
					
					g.setFont(fontCard);
					if (c.shape.equalsIgnoreCase("Hearts") || c.shape.equalsIgnoreCase("Diamonds")) {
						g.setColor(Color.red);
					} else {
						g.setColor(Color.black);
					}
					
					g.drawString(c.symbol, gridX+spacing+tCardWidth*index+rounding, gridY+spacing+cardH-rounding+200);
					
					if (c.shape.equalsIgnoreCase("Hearts")) {
						g.fillOval(gridX+tCardWidth*index+42, gridY+70+200, 35, 35);
						g.fillOval(gridX+tCardWidth*index+73, gridY+70+200, 35, 35);
						g.fillArc(gridX+tCardWidth*index+30, gridY+90+200, 90, 90, 51, 78);
					} else if (c.shape.equalsIgnoreCase("Diamonds")) {
						polyX[0] = gridX+tCardWidth*index+75;
						polyX[1] = gridX+tCardWidth*index+50;
						polyX[2] = gridX+tCardWidth*index+75;
						polyX[3] = gridX+tCardWidth*index+100;
						polyY[0] = gridY+60+200;
						polyY[1] = gridY+100+200;
						polyY[2] = gridY+140+200;
						polyY[3] = gridY+100+200;
						g.fillPolygon(polyX, polyY, 4);
					} else if (c.shape.equalsIgnoreCase("Spades")) {
						g.fillOval(gridX+tCardWidth*index+42, gridY+90+200, 35, 35);
						g.fillOval(gridX+tCardWidth*index+73, gridY+90+200, 35, 35);
						g.fillArc(gridX+tCardWidth*index+30, gridY+15+200, 90, 90, 51+180, 78);
						g.fillRect(gridX+tCardWidth*index+70, gridY+100+200, 10, 40);
					} else {
						g.fillOval(gridX+tCardWidth*index+40, gridY+90+200, 35, 35);
						g.fillOval(gridX+tCardWidth*index+75, gridY+90+200, 35, 35);
						g.fillOval(gridX+tCardWidth*index+58, gridY+62+200, 35, 35);
						g.fillRect(gridX+tCardWidth*index+70, gridY+75+200, 10, 70);
					}
					
					//-------------------------
					index++;
				}
				
				g.setColor(Color.black);
				g.setFont(fontQuest);
				g.drawString("Your total: ", gridX+gridWidth+60, gridY+40);
				if (pMaxTot <= 21) {
					g.drawString(Integer.toString(pMaxTot), gridX+gridWidth+60, gridY+120);
				} else {
					g.drawString(Integer.toString(pMinTot), gridX+gridWidth+60, gridY+120);
				}
				g.drawString("Dealer's total: ", gridX+gridWidth+60, gridY+240);
				if (dMaxTot <= 21) {
					g.drawString(Integer.toString(dMaxTot), gridX+gridWidth+60, gridY+320);
				} else {
					g.drawString(Integer.toString(dMinTot), gridX+gridWidth+60, gridY+320);
				}
			}
			
		}
		
	}
	
	public class Move implements MouseMotionListener {

		@Override
		public void mouseDragged(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseMoved(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	public class Click implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mousePressed(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	public class ActHit implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (hit_stay_q == true) {
			//	System.out.println("You clicked 'HIT'");
				
				int tempMax = 0;
				if (pMaxTot <= 21) {
					tempMax = pMaxTot;
				} else {
					tempMax = pMinTot;
				}
				String mess = ("You decided to hit! (total: " + Integer.toString(tempMax) + ")");
				mes.add(new Message(mess, "Player"));
				
				tempS = rand.nextInt(52);
				while (Cards.get(tempS).used == true) {
					tempS = rand.nextInt(52);
				}
				playerCards.add(Cards.get(tempS));
				Cards.get(tempS).setUsed();
			//	System.out.println("Card " + pCards.get(pCards.size()-1).name + " of " + pCards.get(pCards.size()-1).shape + " added to the player's cards.");
			}
		}
		
	}
	
	public class ActStay implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (hit_stay_q == true) {
			//	System.out.println("You clicked 'STAY'");
				
				int tempMax = 0;
				if (pMaxTot <= 21) {
					tempMax = pMaxTot;
				} else {
					tempMax = pMinTot;
				}
				String mess = ("You decided to stay! (total: " + Integer.toString(tempMax) + ")");
				mes.add(new Message(mess, "Player"));
				
				hit_stay_q = false;
				dealer_turn = true;
			}
		}
		
	}
	
	public class ActYes implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
		//	System.out.println("You clicked 'YES'");
			
			for (Card c : Cards) {
				c.setNotUsed();
			}
			
			playerCards.clear();
			dealerCards.clear();
			mes.clear();
			
			play_more_q = false;
			hit_stay_q = true;
			
			tempS = rand.nextInt(52);
			playerCards.add(Cards.get(tempS));
			Cards.get(tempS).setUsed();
	//		System.out.println("Card "+name+" of "+shape+" added to the player's cards.");
			
			tempS = rand.nextInt(52);
			while (Cards.get(tempS).used == true) {
				tempS = rand.nextInt(52);
			}
			dealerCards.add(Cards.get(tempS));
			Cards.get(tempS).setUsed();
	//		System.out.println("Card " + name + " of " + shape + " added to the dealer's cards.");
			
			tempS = rand.nextInt(52);
			while (Cards.get(tempS).used == true) {
				tempS = rand.nextInt(52);
			}
			playerCards.add(Cards.get(tempS));
			Cards.get(tempS).setUsed();
	//		System.out.println("Card " + name + " of " + shape + " added to the player's cards.");
			
			tempS = rand.nextInt(52);
			while (Cards.get(tempS).used == true) {
				tempS = rand.nextInt(52);
			}
			dealerCards.add(Cards.get(tempS));
			Cards.get(tempS).setUsed();
		//	System.out.println("Card " + name + " of " + .shape + " added to the dealer's cards.");
			
		}
		
	}
	
	public class ActNo implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
		//	System.out.println("You clicked 'NO'");
			Main.terminator = true;
			dispose();
		}
		
	}
	
}
