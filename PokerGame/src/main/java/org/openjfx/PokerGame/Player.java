package org.openjfx.PokerGame;

import java.util.Arrays;

public class Player {
	
	final private static int STARTING_COINS = 5;
	private Hand hand;
	private int coins;
	
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
	
	public int checkHand() {
		return hand.checkHand();
	}
	
	public void test() {
		int[] testValues = new int[] {6,6,9,9,12};
		int[] testSuites = new int[] {3,1,2,3,0};
		hand.testHand(testValues, testSuites);
	}

}
