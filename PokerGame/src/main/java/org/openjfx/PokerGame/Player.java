/**
 * Player
 * 
 * A class used to represent a player.
 * Contains a players hand and coins.
 * Cards objects are drawn from the Deck class and saved to a hand.
 * 
 * @author endamccarthy
 * Last Modified: 12/04/2020
 */

package org.openjfx.PokerGame;
import java.util.Arrays;


public class Player {

	/** Class constants and variables */
	final private static int MIN_VALUABLE_KICKER_CARD = 8; // 8 is the index representing a ten
	private Card[] hand;
	private boolean[] valuableCards;
	private int highestCard, ignore, tempHighestCard, coins;
	
	/**
	 * CONSTRUCTOR
	 * 
	 * Instantiates hand as a new array of Cards.
	 * Instantiates valuableHands as a new array of false booleans.
	 * Sets up players coin balance.
	 * Generates a new hand for player.
	 */
	public Player() {
		this.hand = new Card[Main.CARDS_PER_PLAYER];
		this.valuableCards = new boolean[Main.CARDS_PER_PLAYER];
		for(int i = 0; i < valuableCards.length; i++)
			valuableCards[i] = false;
		setCoins(Main.STARTING_COINS);
		generateHand();
	}
	
	/**
	 * GET COINS
	 * 
	 * Takes an integer value and adds value to players coin balance.
	 * 
	 * @param coins
	 */
	public void setCoins(int coins) {
		this.coins += coins;
	}
	
	/**
	 * GET COINS
	 * 
	 * @return number of coins currently belonging to player
	 */
	public int getCoins() {
		return coins;
	}
	
	/**
	 * SORT HAND
	 * 
	 * Sort a players hand from least valuable card to most.
	 */
	public void sortHand() {
		Arrays.sort(hand);
	}
	
	/**
	 * CHECK IF VALUABLE CARD
	 * 
	 * Used automatically refresh non-valuable cards in computers hand and
	 * suggest cards to refresh for player.
	 * 
	 * @param cardToCheck
	 * @return boolean
	 */
	public boolean checkIfValuableCard(int cardToCheck) {
		return valuableCards[cardToCheck];
	}
	
	/**
	 * REPLACE CARD
	 * 
	 * Used to refresh players cards.
	 * 
	 * @param cardToReplace
	 */
	public void replaceCard(int cardToReplace) {
		if(Main.deck.deckSize() >= 1) hand[cardToReplace] = Main.deck.drawCard();
	}
	
	/**
	 * GENERATE HAND
	 * 
	 * Draws cards from the deck created in Main and saves them to the hand array.
	 * Sorts the hand array by values of cards (the compareTo() method of Card is used for this).
	 */
	public void generateHand() {
		for(int i = 0; i < Main.CARDS_PER_PLAYER; i++)
			hand[i] = Main.deck.drawCard();
		sortHand();
	}
	
	/**
	 * CHECK HAND
	 * 
	 * Checks hand for each outcome starting with the strongest.
	 * If any of the checks are true, an integer value is returned.
	 * The value returned represents the strength of the hand.
	 * If no checks are true the value of the highest card is marked as valuable and returned.
	 * 
	 * @return integer value representing the strength of the hand
	 */
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
	
	/**
	 * CHECK FOR ROYAL FLUSH
	 * 
	 * Returns true if the hand is a flush, a straight and if the highest card is an Ace.
	 * (e.g. {♠10, ♠J, ♠Q, ♠K, ♠A})
	 * All cards will already be marked as valuable from internal methods.
	 * 
	 * @param hand
	 * @return boolean
	 */
	private boolean checkForRoyalFlush(Card[] hand) {
		if (checkForFlush(hand) && checkForStraight(hand) && highestCard == Card.VALUES.length - 1) return true;
		return false;
	}
	
	/**
	 * CHECK FOR STRAIGHT FLUSH
	 * 
	 * Returns true if the hand is a flush and a straight (e.g. {♠5, ♠6, ♠7, ♠8, ♠9}).
	 * All cards will already be marked as valuable from internal methods.
	 * 
	 * @param hand
	 * @return boolean
	 */
	private boolean checkForStraightFlush(Card[] hand) {
		if (checkForFlush(hand) && checkForStraight(hand)) return true;
		return false;
	}
	
	/**
	 * CHECK FOR FOUR OF A KIND
	 * 
	 * Returns true if the hand contains 4 equal values (e.g. {♠10, ♡10, ♣10, ♢10, ♠K}).
	 * If true, highestCard will be saved as the value of the 4 cards.
	 * Four of a kind cards are marked as valuable.
	 * 
	 * @param hand
	 * @return boolean
	 */
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
	
	/**
	 * CHECK FOR FULL HOUSE
	 * 
	 * Returns true if the hand contains a triple and a pair (e.g. {♠10, ♡10, ♣10, ♢K, ♠K}).
	 * If true, the value of the triple cards is saved to highestCard (e.g. 10).
	 * Because checkForPair might change the value of highestCard, the highestCard found 
	 * in checkForTriple is saved in temp and saved back to highestCard later.
	 * All cards are marked as valuable.
	 * 
	 * @param hand
	 * @return boolean
	 */
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
	
	/**
	 * CHECK FOR FLUSH
	 * 
	 * Returns true if the hand contains a flush (all the same suit) (e.g. {♠3, ♠6, ♠8, ♠J, ♠A}).
	 * All cards are marked as valuable.
	 * 
	 * @param hand
	 * @return boolean
	 */
	private boolean checkForFlush(Card[] hand) {
		for(int i = 1; i < hand.length; i++)
			if (hand[0].getSuit() != hand[i].getSuit()) return false;
		highestCard = hand[hand.length - 1].getValue();
		for(int i = 0; i < valuableCards.length; i++)
			valuableCards[i] = true;
		return true;
	}
	
	/**
	 * CHECK FOR STRAIGHT
	 * 
	 * Returns true if the hand contains a straight (e.g. {♠5, ♡6, ♢7, ♠8, ♣9}).
	 * All cards are marked as valuable.
	 * 
	 * @param hand
	 * @return boolean
	 */
	private boolean checkForStraight(Card[] hand) {
		for(int i = 0; i < hand.length - 1; i++)
			if (hand[i].getValue() != (hand[i + 1].getValue() - 1)) return false;
		highestCard = hand[hand.length - 1].getValue();
		for(int i = 0; i < valuableCards.length; i++)
			valuableCards[i] = true;
		return true;
	}
	
	/**
	 * CHECK FOR TRIPLE
	 * 
	 * Returns true if the hand contains a triple (e.g. {♠5, ♡6, ♢6, ♠6, ♣9}).
	 * If true, the value of the triple cards is saved to highestCard (e.g. 6).
	 * Triple cards are marked as valuable.
	 * 
	 * @param hand
	 * @return boolean
	 */
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
	 * Paired cards are marked as valuable.
	 * 
	 * @param hand
	 * @return boolean
	 */
	private boolean checkForPair(Card[] hand) {
		for(int i = 0; i < hand.length; i++) {
			// if a valid ignore value exists the search will skip this value
			if (ignore >= 0 && ignore < Card.VALUES.length)
				if (hand[i].getValue() == ignore) continue;
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
	
	/**
	 * DETERMINE VALUABLE KICKERS
	 * 
	 * Used to ensure all non-valuable cards which are greater than a certain value
	 * are marked as valuable.
	 */
	private void determineValuableKickers() {
		for(int i = 0; i < valuableCards.length; i++)
			if(!valuableCards[i] && hand[i].getValue() >= MIN_VALUABLE_KICKER_CARD) valuableCards[i] = true;
	}
	
	/**
	 * TO STRING
	 * 
	 * @return string containing the suit and the value of every card object in the hand
	 */
	public String toString() {
		String toString = "";
		for(int i = 0; i < Main.CARDS_PER_PLAYER; i++)
			toString += " " + hand[i] + " ";
		return toString;
	}
	
}
