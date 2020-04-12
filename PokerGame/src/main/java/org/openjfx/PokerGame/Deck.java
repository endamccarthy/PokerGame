/**
 * Deck
 * 
 * A class used to represent a deck of playing cards (ArrayList of Card classes).
 * 
 * @author endamccarthy
 * Last Modified: 12/04/2020
 */

package org.openjfx.PokerGame;
import java.util.ArrayList;
import java.util.Random;


public class Deck {
	
	/** Class variables */
	private ArrayList<Card> deck;
	private Card card;
	private Random random;
	
	/**
	 * CONSTRUCTOR
	 * 
	 * Calls method to set the deck.
	 */
	public Deck() {
		setDeck();
	}
	
	/**
	 * SET DECK
	 * 
	 * Instantiates deck as a new ArrayList of Cards of length 52 (assuming 13 values and 4 suits).
	 * Generates new Cards and adds them to the deck.
	 * Order they are added in is by suit first, then value (e.g. 2♠, 3♠, 4♠....) 
	 */
	public void setDeck() {
		deck = new ArrayList<Card>(Card.SUITES.length * Card.VALUES.length);
		for(int i = 0; i < Card.SUITES.length; i++)
			for(int j = 0; j < Card.VALUES.length; j++) {
				card = new Card(i, j);
				deck.add(card);
			}
	}
	
	/**
	 * DRAW CARD
	 *
	 * Checks if deck has one or more cards left.
	 * If so, removes a random card from the deck, saves it to drawnCard and returns drawnCard.
	 * 
	 * @return a Card object representing a card drawn from the deck
	 */
	public Card drawCard() {
		if(deck.size() >= 1) {
			random = new Random();
			Card drawnCard = deck.get(random.nextInt(deck.size()));
			deck.remove(drawnCard);
			return drawnCard;
		}
		else return null;
	}
	
	/**
	 * DECK SIZE
	 * 
	 * @return the amount of cards currently in the deck
	 */
	public int deckSize() {
		return deck.size();
	}
	
	/**
	 * TO STRING
	 * 
	 * @return string containing the suit and the value of every card object in the deck
	 */
	public String toString() {
		String toString = "";
		for(int i = 0; i < deck.size(); i++)
			toString += deck.get(i).toString() + "\n";
		return toString;
	}

}
