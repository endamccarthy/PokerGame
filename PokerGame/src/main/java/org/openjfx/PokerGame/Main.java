/**
 * CardGameFX 
 * (A JavaFX Application)
 * 
 * 
 * *********************************************************************************************
 * HOW IT WORKS
 *  
 * Simulates a very basic game of poker:
 * 1. Asks the user to enter number of players (only 2 -7 players allowed)
 * 2. Deals 5 cards to each player
 * 3. Evaluates each players hand and prints the results
 * 4. Asks the user if they want to play again
 * *********************************************************************************************
 * 
 * 
 * *********************************************************************************************
 * TESTING
 * 
 * There is a block of code included further down to enable test hands to be entered manually.
 * Just uncomment block and insert values to test.
 * *********************************************************************************************
 * 
 * 
 * @author endamccarthy
 * Last Modified: 14/03/2020
 */

package org.openjfx.PokerGame;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;


/**
 * JavaFX App
 */
public class Main extends Application {

	final public static int CARDS_PER_PLAYER = 5;
	final private static String[] OUTCOMES = new String[] {"High Card", "Pair", "Two Pair", "Triple", "Straight", "Flush", 
			"Full House", "Four Of A Kind", "Straight Flush", "Royal Flush"};
	private static Deck deck;
	private static Hand[] hands;
	private static int[] results;
	private static int numOfPlayers, winningValue, i; // winningValue - the highest value in the results array
	private static String pattern, resultString, result;
	private static Label numOfPlayersLabel, statusLabel, resultLabel;
	private static Button startButton, playAgainButton;
	private static Scene sceneHome, sceneResults;
	private static Stage window;
	
	
	/**
	 * MAIN
	 * 
	 * Launches a JavaFX application.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		launch(args);
	}

	/**
	 * START
	 * 
	 * Called from the launch method in main.
	 * 
	 * @param primaryStage
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		// setup javafx application window
		window = primaryStage;
		window.setTitle("Card Game");
		numOfPlayersLabel = new Label("Enter number of players (between 2-7):");
		TextField inputField = new TextField();
		statusLabel = new Label();
		startButton = new Button("Start Game");
		playAgainButton = new Button("Play Again");
		VBox layoutHome = new VBox(10);
		layoutHome.setPadding(new Insets(20, 20, 20, 20));
		layoutHome.getChildren().addAll(numOfPlayersLabel, inputField, startButton, statusLabel);
		sceneHome = new Scene(layoutHome, 400, 600);
		window.setScene(sceneHome);
		
		// when the start button is clicked...
		startButton.setOnAction(e -> {
			String input = inputField.getText();
			pattern = "[2-7]{1}";
			
			if (!input.matches(pattern))
				statusLabel.setText("Invalid input, try again");
			else {
				statusLabel.setText(""); // reset statusLabel
				numOfPlayers = Integer.parseInt(input);
				deck = new Deck();
				hands = new Hand[numOfPlayers];
				results = new int[numOfPlayers];
				resultString = ""; // reset resultsString
				winningValue = 0; // reset winningValue
				
				// deal cards to all players
				for(i = 0; i < numOfPlayers; i++) {
					hands[i] = new Hand();
					// save over the deck with the drawn cards removed
					deck = hands[i].generateHand(deck); 
				}
				
				
				/* *********************************************************
				 * 
				 * TESTING
				 * 
				 **********************************************************/
				
				/* <- remove or comment out this line to test
				 
				// Player One
				int[] testValues1 = new int[] {8,8,10,11,12}; // 0-12 => Two-Ace
				int[] testSuites1 = new int[] {3,2,3,1,3};
				hands[0].testHand(testValues1, testSuites1);
				// Player Two
				int[] testValues2 = new int[] {1,1,7,8,8};
				int[] testSuites2 = new int[] {0,3,0,1,0};
				hands[1].testHand(testValues2, testSuites2);
				/* *********************************************************/
				
				
				// evaluate hands and store results in resultString
				for(i = 0; i < numOfPlayers; i++) {
					results[i] = hands[i].checkHand();
					if (results[i] > winningValue) {
						winningValue = results[i];
						result = String.format(("Player %d wins!"), i+1);
					}
					else if (results[i] == winningValue && winningValue > 0)
						result = "It's a Draw!";
					else if (winningValue == 0)
						result = "No winner!";
					resultString = resultString +
								   String.format(("Player %d:\n" + hands[i] + "\n"), i+1) +
								   (OUTCOMES[results[i] / 100] + "\n\n");
				}
				
				// results screen layout
				resultLabel = new Label(resultString + "\n" + result);
				VBox layoutResults = new VBox(10);
				layoutResults.setPadding(new Insets(20, 20, 20, 20));
				layoutResults.getChildren().addAll(resultLabel, playAgainButton);
				sceneResults = new Scene(layoutResults, 400, 600);
				window.setScene(sceneResults);
			}
		});
		
		// if user wants to play again show home screen
		playAgainButton.setOnAction(e -> {
			window.setScene(sceneHome);
		});
		
		window.show();
	}

}