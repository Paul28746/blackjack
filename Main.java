package blackjack;

public class Main implements Runnable {
	
	long xTime = System.nanoTime();
	public static boolean terminator = false;
	public static int playerWins = 0; 
	
	
	public static int dealerWins = 0;
	
	//screen refresh rate
	public int Hz = 100;
	
	GUI blackjack = new GUI();
	
	public static void main(String[] args) {
		new Thread(new Main()).start();
	}
	
	@Override
	public void run() {
		while(terminator == false) {
			if (System.nanoTime() - xTime >= 1000000000/Hz) {
				blackjack.refresher();
				blackjack.repaint();
				xTime = System.nanoTime();
			}
		}
	}
	
}
