/**
 * Hand
 * 
 * A class used to represent a hand of playing cards.
 * Cards objects are drawn from the Deck class.
 * The hand has the ability to evaluate itself - checkHand()
 * Also included is a method used for testing purposes - testHand()
 * 
 * @author endamccarthy
 * Last Modified: 14/03/2020
 */

package org.openjfx.PokerGame;
import java.util.Arrays;

public class Player {

	private Card[] hand;
	private boolean[] valuableCards;
	private int highestCard, ignore, tempHighestCard, coins;
	
	public Player() {
		this.hand = new Card[Main.CARDS_PER_PLAYER];
		this.valuableCards = new boolean[Main.CARDS_PER_PLAYER];
		for(int i = 0; i < valuableCards.length; i++)
			valuableCards[i] = false;
		
		generateHand();
		setCoins(Main.STARTING_COINS);
	}
	
	public void setCoins(int coins) {
		this.coins += coins;
	}
	
	public int getCoins() {
		return coins;
	}
	
	public void sortHand() {
		Arrays.sort(hand);
	}
	
	public boolean checkIfValuableCard(int cardToCheck) {
		return valuableCards[cardToCheck];
	}
	
	public void replaceCard(int cardToReplace) {
		if(Main.deck.deckSize() >= 1)
			hand[cardToReplace] = Main.deck.drawCard();
	}
	
	public void generateHand() {
		for(int i = 0; i < Main.CARDS_PER_PLAYER; i++)
			hand[i] = Main.deck.drawCard();
		sortHand();
	}
	
	public int checkHand() {
		for(int i = 0; i < valuableCards.length; i++)
			valuableCards[i] = false;
		ignore = -1; // set ignore variable to an invalid number
		highestCard = hand[hand.length - 1].getValue();
		if (checkForRoyalFlush(hand)) return 900 + highestCard;
		else if (checkForStraightFlush(hand)) return 800 + highestCard;
		else if (checkForFourOfAKind(hand)) return 700 + highestCard;
		else if (checkForFullHouse(hand)) return 600 + highestCard;
		else if (checkForFlush(hand)) return 500 + highestCard;
		else if (checkForStraight(hand)) return 400 + highestCard;
		else if (checkForTriple(hand)) return 300 + highestCard;
		else if (checkForTwoPair(hand)) return 200 + highestCard;
		else if (checkForPair(hand)) {
			determineValuableKickers();
			return 100 + highestCard;
		}
		else {
			valuableCards[valuableCards.length - 1] = true;
			determineValuableKickers();
			return highestCard;
		}
	}
	
	private boolean checkForRoyalFlush(Card[] hand) {
		if (checkForFlush(hand) && checkForStraight(hand) && highestCard == Card.VALUES.length - 1) {
			for(int i = 0; i < valuableCards.length; i++)
				valuableCards[i] = true;
			return true;
		}
		return false;
	}
	

	private boolean checkForStraightFlush(Card[] hand) {
		if (checkForFlush(hand) && checkForStraight(hand))
			return true;
		return false;
	}
	

	private boolean checkForFourOfAKind(Card[] hand) {
		for(int i = 0; i < valuableCards.length; i++)
			valuableCards[i] = false;
		for(int i = 0; i < hand.length; i++)
			for(int j = i + 1; j < hand.length; j++)
				for(int k = j + 1; k < hand.length; k++)
					for(int l = k + 1; l < hand.length; l++)
						if ((hand[i].getValue() == hand[j].getValue()) && (hand[j].getValue() == hand[k].getValue()) && (hand[k].getValue() == hand[l].getValue())) {
							highestCard = hand[i].getValue();
							valuableCards[i] = true;
							valuableCards[j] = true;
							valuableCards[k] = true;
							valuableCards[l] = true;
							determineValuableKickers();
							return true;
						}
		return false;
	}
	

	private boolean checkForFullHouse(Card[] hand) {
		if (checkForTriple(hand)) {
			tempHighestCard = highestCard;
			// checkForPair will ignore the triple cards already found
			if (checkForPair(hand, highestCard)) {
				highestCard = tempHighestCard;
				for(int i = 0; i < valuableCards.length; i++)
					valuableCards[i] = true;
				return true;
			}
		}
		return false;
	}
	

	private boolean checkForFlush(Card[] hand) {
		for(int i = 1; i < hand.length; i++)
			if (hand[0].getSuit() != hand[i].getSuit())
				return false;
		highestCard = hand[hand.length - 1].getValue();
		for(int i = 0; i < valuableCards.length; i++)
			valuableCards[i] = true;
		return true;
	}
	

	private boolean checkForStraight(Card[] hand) {
		for(int i = 0; i < hand.length - 1; i++)
			if (hand[i].getValue() != (hand[i + 1].getValue() - 1))
				return false;
		highestCard = hand[hand.length - 1].getValue();
		for(int i = 0; i < valuableCards.length; i++)
			valuableCards[i] = true;
		return true;
	}
	

	private boolean checkForTriple(Card[] hand) {
		for(int i = 0; i < hand.length; i++)
			for(int j = i + 1; j < hand.length; j++)
				for(int k = j + 1; k < hand.length; k++)
					if ((hand[i].getValue() == hand[j].getValue()) && (hand[j].getValue() == hand[k].getValue())) {
						highestCard = hand[i].getValue();
						valuableCards[i] = true;
						valuableCards[j] = true;
						valuableCards[k] = true;
						determineValuableKickers();
						return true;
					}
		return false;
	}
	

	private boolean checkForTwoPair(Card[] hand) {
		// as the hand is sorted the second half of the conditional will find the highest pair
		if (checkForPair(hand) && checkForPair(hand, highestCard)) {
			determineValuableKickers();
			return true;
		}
		return false;
	}
	

	private boolean checkForPair(Card[] hand, int ignore) {
		this.ignore = ignore;
		return checkForPair(hand);
	}
	

	private boolean checkForPair(Card[] hand) {
		for(int i = 0; i < hand.length; i++) {
			// if a valid ignore value exists the search will skip this value
			if (ignore >= 0 && ignore < Card.VALUES.length)
				if (hand[i].getValue() == ignore)
					continue;
			for(int j = i + 1; j < hand.length; j++)
				if ((hand[i].getValue() == hand[j].getValue())) {
					highestCard = hand[i].getValue();
					ignore = -1; // reset ignore to invalid value
					valuableCards[i] = true;
					valuableCards[j] = true;
					return true;
				}
		}
		ignore = -1; // reset ignore to invalid value
		return false;
	}
	
	
	private void determineValuableKickers() {
		// if each kicker card is a 10 (index 8) or higher, then keep this card also
		for(int i = 0; i < valuableCards.length; i++)
			if(!valuableCards[i] && hand[i].getValue() >= 8)
				valuableCards[i] = true;
	}
	

	public String toString() {
		String toString = "";
		for(int i = 0; i < Main.CARDS_PER_PLAYER; i++)
			toString += " " + hand[i] + " ";
		return toString;
	}
	
}
