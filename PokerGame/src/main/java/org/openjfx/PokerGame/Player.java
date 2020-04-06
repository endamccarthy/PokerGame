package org.openjfx.PokerGame;

public class Player {
	
	final private static int STARTING_COINS = 10;
	private Hand hand;
	private int coins;
	private int minOpen = 109;
	
	public Player() {
		setCoins(STARTING_COINS);
		setHand();
	}
	
	public int getCoins() {
		return coins;
	}
	
	public void setCoins(int coinsValue) {
		coins += coinsValue;
	}
	
	public String getHand() {
		return hand.toString();
	}
	
	public void setHand() {
		hand = new Hand();
		hand.generateHand();
	}
	
	public boolean canOpen() {
		if(hand.checkHand() >= minOpen) return true;
		else return false;
	}
	
	public void replaceCard(int cardToReplace) {
		hand.hand[cardToReplace] = Main.deck.drawCard();
	}

}
