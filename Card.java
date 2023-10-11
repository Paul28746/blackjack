package blackjack;

public class Card {
	
	String card;
	int num;
	String shape;
	boolean used = false;
	int id;
	String symbol;
	
	public Card(int n, String s, int z) {
		if (n > 1 && n < 11) {
			this.card = Integer.toString(n);
			this.num = n;
			this.symbol = this.card;
		} else if (n > 10) {
			this.num = 10;
			if (n == 11) {
				this.card = "Jack";
				this.symbol = "J";
			} else if (n == 12) {
				this.card = "Queen";
				this.symbol = "Q";
			} else if (n == 13) {
				this.card = "King";
				this.symbol = "K";
			}
		} else if (n == 1) {
			this.num = 1;
			this.card = "Ace";
			this.symbol = "A";
		}
		this.shape = s;
		this.id = z;
	
	}
	//if the card has been used
	public void setUsed() {
		used = true;
	
	}
	//if the card hasn't been used
	public void setNotUsed() {
		used = false;
	
	}
	
}
