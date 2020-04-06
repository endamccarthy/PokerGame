

package org.openjfx.PokerGame;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import javafx.geometry.Pos;


public class Main extends Application {

	final public static int CARDS_PER_PLAYER = 5;
	final private static String[] OUTCOMES = new String[] {"High Card", "Pair", "Two Pair", "Triple", "Straight", "Flush", 
			"Full House", "Four Of A Kind", "Straight Flush", "Royal Flush"};
	public static Deck deck;
	
	private static Player computer, you;
	private static int pot;
	
	private static String openLabelText;
	private static Label coinsLabel, potLabel, handLabel, openLabel;
	private static Button startButton, openButton, nextRoundButton, refreshCardsButton, playAgainButton, quitGameButton;
	private static Scene sceneLandingPage, sceneMainPage;
	private static Stage window;

	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		// setup javafx application window
		window = primaryStage;
		window.setTitle("Poker Game");
		startButton = new Button("Start Game");
		openButton = new Button("Yes");
		nextRoundButton = new Button();
		refreshCardsButton = new Button("Refresh Cards");
		playAgainButton = new Button("Play Again");
		quitGameButton = new Button("Quit Game");
		VBox layoutLandingPage = new VBox(10);
		layoutLandingPage.setAlignment(Pos.CENTER);
		layoutLandingPage.setPadding(new Insets(20, 20, 20, 20));
		layoutLandingPage.getChildren().addAll(startButton);
		sceneLandingPage = new Scene(layoutLandingPage, 300, 300);
		window.setScene(sceneLandingPage);
		
		deck = new Deck();
		Player computer = new Player();
		Player you = new Player();
		pot = 0;
		
		// when the start button is clicked...
		startButton.setOnAction(e -> {
			HBox buttonLayout = new HBox(10);
			setUpGame(buttonLayout);
		});
		
		// ...
		openButton.setOnAction(e -> {
			HBox buttonLayout = new HBox(10);
			openGame(buttonLayout);
		});
		
		// ...
		nextRoundButton.setOnAction(e -> {
			HBox buttonLayout = new HBox(10);
			nextRound(buttonLayout);
		});
		
		// if user wants to play again show home screen
		quitGameButton.setOnAction(e -> {
			window.setScene(sceneLandingPage);
		});
		
		window.show();
	}
	
	private void setUpGame(HBox buttonLayout) {
		
		deck = new Deck();
		computer = new Player();
		you = new Player();
		pot = 0;
		
		coinsLabel = new Label();
		potLabel = new Label();
		handLabel = new Label();
		openLabel = new Label();
		
		checkOpen(buttonLayout);
		updateLabels(buttonLayout);
	}
	
	private void checkOpen(HBox buttonLayout) {
		if(computer.canOpen()) {
			openLabelText = "Computer can open.\nDo you want to play?";
			nextRoundButton.setText("No");
			buttonLayout.getChildren().addAll(openButton, nextRoundButton);
		}
		else {
			if(you.canOpen()) {
				openLabelText = "Computer cannot open.\nYou can open. Do you want to play?";
				nextRoundButton.setText("No");
				buttonLayout.getChildren().addAll(openButton, nextRoundButton);
			}
			else {
				openLabelText = "Computer cannot open.\nYou cannot open.";
				nextRoundButton.setText("Next Round");
				buttonLayout.getChildren().addAll(nextRoundButton);
			}
		}
	}
	
	private void openGame(HBox buttonLayout) {
		
		computer.openGame();
		you.openGame();
		pot = pot + 2;
		buttonLayout.getChildren().addAll(refreshCardsButton);
		updateLabels(buttonLayout);
	}
	
	private void nextRound(HBox buttonLayout) {
		
		deck = new Deck();
		computer.setHand();
		you.setHand();
		
		checkOpen(buttonLayout);
		updateLabels(buttonLayout);
		
	}
	
	private void updateLabels(HBox buttonLayout) {
		VBox layoutMainPage = new VBox(10);
		coinsLabel.setText(String.format("Coins:\nComputer:\t%d\nYou:\t\t%d", computer.getCoins(), you.getCoins()));
		potLabel.setText(String.format("Pot: %d", pot));
		handLabel.setText(String.format("Computer's Hand:\n%s\nYour Hand: \n%s", computer.getHand(), you.getHand()));
		openLabel.setText(openLabelText);
		layoutMainPage.setPadding(new Insets(20, 20, 20, 20));
		layoutMainPage.getChildren().addAll(coinsLabel, potLabel, handLabel, openLabel, buttonLayout, quitGameButton);
		sceneMainPage = new Scene(layoutMainPage, 400, 600);
		window.setScene(sceneMainPage);
	}
	

	
	

}