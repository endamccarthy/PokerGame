

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
	
	public static Deck deck;
	private static Player computer;
	private static Player you;
	private static int pot, round;
	
	private static String actionLabelText;
	private static Label coinsLabel, potLabel, handLabel, actionLabel;
	private static Button startButton, openButton, nextRoundButton, refreshCardsButton, playAgainButton, quitGameButton;
	private static Scene sceneLandingPage, sceneMainPage;
	private static Stage window;

	private static VBox layoutLandingPage, layoutMainPage;
	private static HBox layoutHorizontalButtons;
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		deck = new Deck();
		computer = new Player();
		you = new Player();
		
		// setup javafx application window
		window = primaryStage;
		window.setTitle("Poker Game");
		startButton = new Button("Start Game");
		openButton = new Button("Yes");
		nextRoundButton = new Button();
		refreshCardsButton = new Button("Refresh Cards");
		playAgainButton = new Button("Play Again");
		quitGameButton = new Button("Quit Game");
		
		layoutLandingPage = new VBox(10);
		layoutMainPage = new VBox(10);
		layoutHorizontalButtons = new HBox(10);
		
		layoutLandingPage.setAlignment(Pos.CENTER);
		layoutLandingPage.setPadding(new Insets(20, 20, 20, 20));
		layoutLandingPage.getChildren().addAll(startButton);
		sceneLandingPage = new Scene(layoutLandingPage, 300, 300);
		window.setScene(sceneLandingPage);
		
		// when the game is started...
		startButton.setOnAction(e -> {
			setUpGame();
		});
		
		// when the round is opened...
		openButton.setOnAction(e -> {
			openGame();
		});
		
		// when moving onto the next round...
		nextRoundButton.setOnAction(e -> {
			nextRound();
		});
		
		// when moving onto the next round...
		refreshCardsButton.setOnAction(e -> {
			refreshCards();
		});
		
		// if user wants to play again show home screen
		quitGameButton.setOnAction(e -> {
			window.setScene(sceneLandingPage);
		});
		
		window.show();
	}
	
	private void setUpGame() {
		deck = new Deck();
		computer = new Player();
		you = new Player();
		pot = 0;
		round = 1;
		coinsLabel = new Label();
		potLabel = new Label();
		handLabel = new Label();
		actionLabel = new Label();
		checkOpen();
		updateLabels();
	}
	
	private void checkOpen() {
		layoutHorizontalButtons.getChildren().clear();
		if(computer.canOpen()) {
			actionLabelText = "Computer can open.\nDo you want to play?";
			nextRoundButton.setText("No");
			layoutHorizontalButtons.getChildren().addAll(openButton, nextRoundButton);
		}
		else {
			if(you.canOpen()) {
				actionLabelText = "Computer cannot open.\nYou can open. Do you want to play?";
				nextRoundButton.setText("No");
				layoutHorizontalButtons.getChildren().addAll(openButton, nextRoundButton);
			}
			else {
				actionLabelText = "Computer cannot open.\nYou cannot open.";
				nextRoundButton.setText("Next Round");
				layoutHorizontalButtons.getChildren().addAll(nextRoundButton);
			}
		}
	}
	
	private void openGame() {
		layoutHorizontalButtons.getChildren().clear();
		computer.setCoins(-1);
		you.setCoins(-1);
		pot += 2;
		actionLabelText = "Please Select Cards To Refresh:";
		layoutHorizontalButtons.getChildren().addAll(refreshCardsButton);
		updateLabels();
	}
	
	private void nextRound() {
		deck = new Deck();
		computer.setHand();
		you.setHand();
		round++;
		checkOpen();
		updateLabels();
	}
	
	private void refreshCards() {
		you.replaceCard(1);
		updateLabels();
	}
	
	private void updateLabels() {
		layoutMainPage = new VBox(10);
		coinsLabel.setText(String.format("Coins:\nComputer:\t%d\nYou:\t\t%d", computer.getCoins(), you.getCoins()));
		potLabel.setText(String.format("Pot: %d", pot));
		handLabel.setText(String.format("Computer's Hand:\n%s\nYour Hand: \n%s", computer.getHand(), you.getHand()));
		actionLabel.setText(actionLabelText);
		layoutMainPage.setPadding(new Insets(20, 20, 20, 20));
		layoutMainPage.getChildren().addAll(coinsLabel, potLabel, handLabel, actionLabel, layoutHorizontalButtons, quitGameButton);
		sceneMainPage = new Scene(layoutMainPage, 400, 600);
		window.setScene(sceneMainPage);
	}
	
}