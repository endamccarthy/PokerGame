package org.openjfx.PokerGame;

import java.util.Arrays;

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
		if(Main.deck.deckSize() >= 1)
			hand.hand[cardToReplace] = Main.deck.drawCard();
	}
	
	public void sortHand() {
		Arrays.sort(hand.hand);
	}
	
	public boolean checkIfValuableCard(int cardIndex) {
		return hand.valuableCards[cardIndex];
	}
	
	public void test() {
		int[] testValues = new int[] {2,3,4,5,6};
		int[] testSuites = new int[] {3,2,3,1,3};
		hand.testHand(testValues, testSuites);
	}

}
