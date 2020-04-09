/**
i * Card
 * 
 * A class used to store the details of a single playing card.
 * Implements the Comparable interface in order to compare the values of card objects.
 * 
 * @author endamccarthy
 * Last Modified: 14/03/2020
 */

package org.openjfx.PokerGame;
import java.util.Arrays;

public class Card implements Comparable<Card>{
	
	final public static String[] SUITES = new String[] {"♠", "♡", "♣", "♢"};
	final public static String[] VALUES = new String[] {"2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A"};
	private String suit, value; // stores the suit and value of a Card object
	
	/**
	 * CONSTRUCTOR
	 * 
	 * Takes in 2 integers representing a suit (0-3) and a value (0-12).
	 * Calls methods to set the suit and value for the card object.
	 * 
	 * @param suit
	 * @param value
	 */
	public Card(int suit, int value) {
		setSuit(suit);
		setValue(value);
	}
	
	/**
	 * SET SUIT
	 * 
	 * Uses an integer value (0-3) to pick the suit from SUITES.
	 * 
	 * @param suit
	 */
	public void setSuit(int suit) {
		this.suit = SUITES[suit];
	}
	
	/**
	 * SET VALUE
	 * 
	 * Uses an integer value (0-12) to pick the value from VALUES.
	 * 
	 * @param value
	 */
	public void setValue(int value) {
		this.value = VALUES[value];
	}
	
	/**
	 * GET SUIT
	 * 
	 * @return index of object suit in SUITES
	 */
	public int getSuit() {
		return Arrays.asList(SUITES).indexOf(suit);
	}
	
	/**
	 * GET VALUE
	 * 
	 * @return index of object value in VALUES
	 */
	public int getValue() {
		return Arrays.asList(VALUES).indexOf(value);
	}
	
	/**
	 * TO STRING
	 * 
	 * @return string containing the suit and the value of card object (e.g. ♠A)
	 */
	public String toString() {
		return suit + value;
	}
	
	/**
	 * COMPARE TO
	 * 
	 * This is the implementation of the compareTo() method inherited from the Comparable interface.
	 * It compares the value of one card object to another.
	 * 
	 * @param otherCard
	 * @return 1, 0 or -1 (greater than, equal to or less than)
	 */
	public int compareTo(Card otherCard) {
        if(this.getValue() > otherCard.getValue())
            return 1;
        else if (this.getValue() == otherCard.getValue())
            return 0 ;
        return -1 ;
    }
	
}
