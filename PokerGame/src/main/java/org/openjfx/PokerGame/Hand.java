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

public class Hand {

	/* highestCard	-- stores the value of the highest relevant card in the hand, for example: 
	 * 				   	Hand 1: {2, 5, 7, 7, 9}   =>  highestCard is 7 (value of pair)
	 * 				   	Hand 2: {2, 5, 7, 9, 11}  =>  highestCard is 11
	 * ignore		-- used to ignore a value when checking for a pair (used in full house and two pair)
	 */
	public Card[] hand;
	private int highestCard, ignore, temp, i, j, k, l, m;
	private String toString;
	public boolean[] valuableCards;
	
	/**
	 * CONSTRUCTOR
	 * 
	 * Instantiates hand as a new array of Cards of length 5 (assuming 5 is set for the number of cards per player).
	 */
	public Hand() {
		hand = new Card[Main.CARDS_PER_PLAYER];
		valuableCards = new boolean[Main.CARDS_PER_PLAYER];
		for(i = 0; i < valuableCards.length; i++)
			valuableCards[i] = false;
	}
	
	/**
	 * GENERATE HAND
	 * 
	 * Takes in a deck of cards to deal from.
	 * Draws cards from the deck and saves them to the hand array.
	 * Sorts the hand array by values of cards (the compareTo() method of Card is used for this).
	 * Returns the deck with the drawn cards removed.
	 * 
	 * @param deck
	 * @return deck
	 */
	public Deck generateHand(Deck deck) {
		for(i = 0; i < Main.CARDS_PER_PLAYER; i++)
			hand[i] = deck.drawCard();
		Arrays.sort(hand);
		return deck;
	}
	public void generateHand() {
		for(i = 0; i < Main.CARDS_PER_PLAYER; i++)
			hand[i] = Main.deck.drawCard();
		Arrays.sort(hand);
	}
	
	/**
	 * CHECK HAND
	 * 
	 * Checks hand for each outcome starting with the strongest.
	 * If any of the checks are true, an integer value is returned.
	 * The value returned represents the strength of the hand.
	 * If no checks are true the value of the highest card is returned.
	 * 
	 * @return integer value representing the strength of the hand
	 */
	public int checkHand() {
		// set ignore variable to an invalid number
		ignore = -1;
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
	
	/**
	 * CHECK FOR ROYAL FLUSH
	 * 
	 * Returns true if the hand is a flush, a straight and if the highest card is an Ace.
	 * (e.g. {♠10, ♠J, ♠Q, ♠K, ♠A})
	 * 
	 * @param hand
	 * @return boolean
	 */
	private boolean checkForRoyalFlush(Card[] hand) {
		if (checkForFlush(hand) && checkForStraight(hand) && highestCard == Card.VALUES.length - 1) {
			for(i = 0; i < valuableCards.length; i++)
				valuableCards[i] = true;
			return true;
		}
		return false;
	}
	
	/**
	 * CHECK FOR STRAIGHT FLUSH
	 * 
	 * Returns true if the hand is a flush and a straight (e.g. {♠5, ♠6, ♠7, ♠8, ♠9}).
	 * 
	 * @param hand
	 * @return boolean
	 */
	private boolean checkForStraightFlush(Card[] hand) {
		if (checkForFlush(hand) && checkForStraight(hand))
			return true;
		return false;
	}
	
	/**
	 * CHECK FOR FOUR OF A KIND
	 * 
	 * Returns true if the hand contains 4 equal values (e.g. {♠10, ♡10, ♣10, ♢10, ♠K}).
	 * If true, highestCard will be saved as the value of the 4 cards.
	 * 
	 * @param hand
	 * @return boolean
	 */
	private boolean checkForFourOfAKind(Card[] hand) {
		for(i = 0; i < valuableCards.length; i++)
			valuableCards[i] = false;
		for(i = 0; i < hand.length; i++)
			for(j = i + 1; j < hand.length; j++)
				for(k = j + 1; k < hand.length; k++)
					for(l = k + 1; l < hand.length; l++)
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
	
	/**
	 * CHECK FOR FULL HOUSE
	 * 
	 * Returns true if the hand contains a triple and a pair (e.g. {♠10, ♡10, ♣10, ♢K, ♠K}).
	 * If true, the value of the triple cards is saved to highestCard (e.g. 10).
	 * Because checkForPair might change the value of highestCard, the highestCard found 
	 * in checkForTriple is saved in temp and saved back to highestCard later.
	 * 
	 * @param hand
	 * @return boolean
	 */
	private boolean checkForFullHouse(Card[] hand) {
		if (checkForTriple(hand)) {
			temp = highestCard;
			// checkForPair will ignore the triple cards already found
			if (checkForPair(hand, highestCard)) {
				highestCard = temp;
				for(i = 0; i < valuableCards.length; i++)
					valuableCards[i] = true;
				return true;
			}
		}
		return false;
	}
	
	/**
	 * CHECK FOR FLUSH
	 * 
	 * Returns true if the hand contains a flush (all the same suit) (e.g. {♠3, ♠6, ♠8, ♠J, ♠A}).
	 * 
	 * @param hand
	 * @return boolean
	 */
	private boolean checkForFlush(Card[] hand) {
		for(i = 1; i < hand.length; i++)
			if (hand[0].getSuit() != hand[i].getSuit())
				return false;
		highestCard = hand[hand.length - 1].getValue();
		for(i = 0; i < valuableCards.length; i++)
			valuableCards[i] = true;
		return true;
	}
	
	/**
	 * CHECK FOR STRAIGHT
	 * 
	 * Returns true if the hand contains a straight (e.g. {♠5, ♡6, ♢7, ♠8, ♣9}).
	 * 
	 * @param hand
	 * @return boolean
	 */
	private boolean checkForStraight(Card[] hand) {
		for(i = 0; i < hand.length - 1; i++)
			if (hand[i].getValue() != (hand[i + 1].getValue() - 1))
				return false;
		highestCard = hand[hand.length - 1].getValue();
		for(i = 0; i < valuableCards.length; i++)
			valuableCards[i] = true;
		return true;
	}
	
	/**
	 * CHECK FOR TRIPLE
	 * 
	 * Returns true if the hand contains a triple (e.g. {♠5, ♡6, ♢6, ♠6, ♣9}).
	 * If true, the value of the triple cards is saved to highestCard (e.g. 6).
	 * 
	 * @param hand
	 * @return boolean
	 */
	private boolean checkForTriple(Card[] hand) {
		for(i = 0; i < hand.length; i++)
			for(j = i + 1; j < hand.length; j++)
				for(k = j + 1; k < hand.length; k++)
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
	
	/**
	 * CHECK FOR TWO PAIR
	 * 
	 * Returns true if the hand contains two pairs (e.g. {♠5, ♡5, ♢6, ♠6, ♣9}).
	 * If true, the value of the highest pair is saved to highestCard (e.g. 6).
	 * 
	 * @param hand
	 * @return boolean
	 */
	private boolean checkForTwoPair(Card[] hand) {
		// as the hand is sorted the second half of the conditional will find the highest pair
		if (checkForPair(hand) && checkForPair(hand, highestCard)) {
			determineValuableKickers();
			return true;
		}
		return false;
	}
	
	/**
	 * CHECK FOR PAIR (OVERLOADED)
	 * 
	 * The method takes an additional argument called ignore, this allows it to find a pair in
	 * checkForFullHouse() that is not already part of the triple and also allows it to 
	 * find a second pair in checkForTwoPair().
	 * Calls on overloaded version of itself and returns result.
	 * 
	 * @param hand
	 * @param ignore
	 * @return boolean
	 */
	private boolean checkForPair(Card[] hand, int ignore) {
		this.ignore = ignore;
		return checkForPair(hand);
	}
	
	/**
	 * CHECK FOR PAIR (OVERLOADED)
	 * 
	 * Returns true if the hand contains a pair (e.g. {♠2, ♡5, ♢6, ♠6, ♣9}).
	 * If true, the value of the pair is saved to highestCard (e.g. 6).
	 * 
	 * @param hand
	 * @return boolean
	 */
	private boolean checkForPair(Card[] hand) {
		for(i = 0; i < hand.length; i++) {
			// if a valid ignore value exists the search will skip this value
			if (ignore >= 0 && ignore < Card.VALUES.length)
				if (hand[i].getValue() == ignore)
					continue;
			for(j = i + 1; j < hand.length; j++)
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
		for(m = 0; m < valuableCards.length; m++)
			if(!valuableCards[m] && hand[m].getValue() >= 8)
				valuableCards[m] = true;
	}
	
	
	/**
	 * TEST HAND
	 * 
	 * Allows the developer to manually pass in a hand of cards to test.
	 * Called from the TEST section in Main.java
	 * 
	 * @param values
	 * @param suites
	 */
	public void testHand(int[] values, int[] suites) {
		for (i = 0; i < Main.CARDS_PER_PLAYER; i++) {
			hand[i].setValue(values[i]);
			hand[i].setSuit(suites[i]);
		}
	}
	
	/**
	 * TO STRING
	 * 
	 * @return string containing the suit and the value of every card object in the hand
	 */
	public String toString() {
		toString = "";
		for(i = 0; i < Main.CARDS_PER_PLAYER; i++)
			toString += " " + hand[i] + " ";
		return toString;
	}
	
}
