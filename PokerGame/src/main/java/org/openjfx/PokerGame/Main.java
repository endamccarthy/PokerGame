

package org.openjfx.PokerGame;

import javafx.application.Application;
import javafx.beans.value.WritableBooleanValue;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Separator;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.geometry.Insets;
import javafx.geometry.Pos;


public class Main extends Application {

	final public static int CARDS_PER_PLAYER = 5;
	final public static int ALLOWED_REFRESHABLE_CARDS = 4;
	final private static int MIN_OPEN = 109; // pair of jacks
	final private static int MIN_COMPUTER_PROCEED = 200; // two pair
	final private static String[] OUTCOMES = new String[] {"High Card", "Pair", "Two Pair", "Triple", "Straight", "Flush", 
			"Full House", "Four Of A Kind", "Straight Flush", "Royal Flush"};
	
	public static Deck deck;
	private static Player computer;
	private static Player you;
	private static int pot, tempCounter, round, betAmount;
	private static int[] scores = new int[2];
	
	private static String actionLabelText;
	private static Label headLabel, coinsLabel, potLabel, handLabel, actionLabel;
	private static Button startButton, openButton, nextRoundButton, refreshCardsButton, checkButton, betButton, submitBetButton, quitGameButton;
	private static Scene sceneLandingPage, sceneMainPage;
	private static Stage window;

	private static VBox layoutLandingPage, layoutMainPage;
	private static HBox layoutHorizontalButtons;
	
	private static Separator separator1, separator2, separator3;
	
	CheckBox[] checkboxes = new CheckBox[CARDS_PER_PLAYER];
	RadioButton betOne = new RadioButton("1");
	RadioButton betTwo = new RadioButton("2");
	RadioButton betThree = new RadioButton("3");
	
	public static void main(String[] args) {
		launch(args);
		/*
		try {
			launch(args);
		}
		catch(Exception e) {
			System.out.println("Error: Could not start application");
		}		
		*/
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
		checkButton = new Button("Check");
		betButton = new Button("Bet");
		submitBetButton = new Button("Submit Bet");
		quitGameButton = new Button("Restart Game");
		
		layoutLandingPage = new VBox(10);
		layoutHorizontalButtons = new HBox(10);
		
		separator1 = new Separator();
		separator2 = new Separator();
		separator3 = new Separator();
		
		layoutLandingPage.setAlignment(Pos.CENTER);
		layoutLandingPage.setPadding(new Insets(20, 20, 20, 20));
		layoutLandingPage.getChildren().addAll(startButton);
		sceneLandingPage = new Scene(layoutLandingPage, 300, 300);
		window.setScene(sceneLandingPage);
		
		// when the game is started...
		startButton.setOnAction(e -> setUpGame());
		
		// when the round is opened...
		openButton.setOnAction(e -> openGame());
		
		// when moving onto the next round...
		nextRoundButton.setOnAction(e -> nextRound());
		
		// when the user has selected to refresh certain cards...
		refreshCardsButton.setOnAction(e -> refreshCards());
		
		// when the user has selected to check...
		checkButton.setOnAction(e -> check());
				
		// when the user has selected to bet...
		betButton.setOnAction(e -> bet());
		
		// when the user has submitted their bet...
		submitBetButton.setOnAction(e -> submitBet());
		
		// if user wants to play again show home screen
		quitGameButton.setOnAction(e -> window.setScene(sceneLandingPage));
		
		window.show();
	}
	
	private void setUpGame() {
		deck = new Deck();
		computer = new Player();
		you = new Player();
		pot = 0;
		round = 1;
		headLabel = new Label();
		coinsLabel = new Label();
		potLabel = new Label();
		handLabel = new Label();
		actionLabel = new Label();
		headLabel.setStyle("-fx-font: 24 arial;");
		coinsLabel.setStyle("-fx-font: 13 arial; -fx-font-weight: bold;");
		potLabel.setStyle("-fx-font: 13 arial; -fx-font-weight: bold;");
		handLabel.setStyle("-fx-font: 15 arial; -fx-font-weight: bold;");
		actionLabel.setStyle("-fx-font: 13 arial;");
		quitGameButton.setText("Restart game");
		evaluateHands();
		checkOpen();
		updateLabels();
	}
	
	private void evaluateHands() {
		scores[0] = computer.checkHand();
		scores[1] = you.checkHand();
	}
	
	private void checkOpen() {
		layoutHorizontalButtons.getChildren().clear();
		if(scores[0] >= MIN_OPEN) {
			actionLabelText = "Computer can open.\n\nDo you want to play?";
			nextRoundButton.setText("No");
			layoutHorizontalButtons.getChildren().addAll(openButton, nextRoundButton);
		}
		else {
			if(scores[1] >= MIN_OPEN) {
				actionLabelText = "Computer cannot open.\nYou can open.\n\nDo you want to play?";
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
		
		/* https://stackoverflow.com/questions/39045377/how-to-restrict-amount-of-selected-checkboxes-javafx */
		int maxSel = ALLOWED_REFRESHABLE_CARDS;
		for (int i = 0 ; i < checkboxes.length; i++) {
			checkboxes[i] = new CheckBox();
			if(!you.checkIfValuableCard(i))
				checkboxes[i].setSelected(true);
			checkboxes[i].selectedProperty().addListener((o, oldV, newV) -> {
		        if(newV) {
		            int selected = 0;
		            for(CheckBox box : checkboxes)
		                if(box.isSelected())
		                    selected++;
		            ((WritableBooleanValue) o).set(selected <= maxSel);
		        }
		    });
		};
		/* ************************************************************************************************* */
		
		layoutHorizontalButtons.getChildren().addAll(checkboxes[0], checkboxes[1], checkboxes[2], checkboxes[3], checkboxes[4], refreshCardsButton);
		updateLabels();
	}
	
	private void nextRound() {
		deck = new Deck();
		computer.setHand();
		you.setHand();
		
		//you.test();
		
		round++;
		evaluateHands();
		checkOpen();
		updateLabels();
	}
	
	private void refreshCards() {
		tempCounter = 0;
		layoutHorizontalButtons.getChildren().clear();
		for (int i = 0 ; i < checkboxes.length; i++) {
			if(!computer.checkIfValuableCard(i)) {
				computer.replaceCard(i);
				tempCounter++;
			}
			if(checkboxes[i].isSelected())
				you.replaceCard(i);
		}
		you.sortHand();
		computer.sortHand();
		evaluateHands();
        
		if(you.getCoins() <= 0 || computer.getCoins() <= 0)
			layoutHorizontalButtons.getChildren().addAll(checkButton);
		else
			layoutHorizontalButtons.getChildren().addAll(checkButton, betButton);
		
		actionLabelText = String.format("(Computer has refreshed %d cards)\n\nWhat is your next move?", tempCounter);
		
		updateLabels();
	}
	
	private void check() {
		layoutHorizontalButtons.getChildren().clear();
		actionLabelText = String.format("Computer's Hand:\n\n%s \t(%s)\n\n", computer.getHand(), OUTCOMES[scores[0] / 100]);
		if(computer.checkHand() > you.checkHand()) {
			actionLabelText += "Computer Wins :(";
			computer.setCoins(pot);
			pot = 0;
		}
		else if(computer.checkHand() < you.checkHand()){
			actionLabelText += "You Win!";
			you.setCoins(pot);
			pot = 0;
		}
		else {
			actionLabelText += "It's a draw!";
			computer.setCoins(pot / 2);
			you.setCoins(pot / 2);
			pot = 0;
		}
		if(computer.getCoins() <= 0 || you.getCoins() <= 0) {
			if(computer.getCoins() <= 0)
				actionLabelText += "\n\nGame Over, The Computer Is Out Of Money!\nCongratulations, You've Won The Game!";
			else if(you.getCoins() <= 0)
				actionLabelText += "\n\nGame Over, You Are Out Of Money!\nUnfortunately, You've Lost The Game";
			actionLabelText += "\n\nWould You Like To Play Again?";
			quitGameButton.setText("Play Again");
			updateLabels();
			return;
		}
		nextRoundButton.setText("Next Round");
		layoutHorizontalButtons.getChildren().addAll(nextRoundButton);
		updateLabels();
	}
	
	private void bet() {
		layoutHorizontalButtons.getChildren().clear();
		
		ToggleGroup betAmountToggle = new ToggleGroup(); 
		
        betOne.setToggleGroup(betAmountToggle); 
        betTwo.setToggleGroup(betAmountToggle); 
        betThree.setToggleGroup(betAmountToggle);
        
        if(you.getCoins() >= 3 && computer.getCoins() >= 3)
        	layoutHorizontalButtons.getChildren().addAll(betOne, betTwo, betThree, submitBetButton);
        else if(you.getCoins() >= 2 && computer.getCoins() >= 2)
        	layoutHorizontalButtons.getChildren().addAll(betOne, betTwo, submitBetButton);
        else if(you.getCoins() >= 1 && computer.getCoins() >= 1)
        	layoutHorizontalButtons.getChildren().addAll(betOne, submitBetButton);
        else
        	return;
		actionLabelText = "How much would you like to bet?";
		updateLabels();
	}
	
	private void submitBet() {
		if(betOne.isSelected())
			betAmount = 1;
		else if(betTwo.isSelected())
			betAmount = 2;
		else if(betThree.isSelected())
			betAmount = 3;
		else {
			check();
			return;
		}
		
		if(scores[0] >= MIN_COMPUTER_PROCEED) {
			computer.setCoins(-betAmount);
			you.setCoins(-betAmount);
			pot += betAmount * 2;
			check();
		}
		else {
			layoutHorizontalButtons.getChildren().clear();
			actionLabelText = "Computer Folds, You Win The Pot!";
			you.setCoins(pot);
			pot = 0;
			nextRoundButton.setText("Next Round");
			layoutHorizontalButtons.getChildren().addAll(nextRoundButton);
			updateLabels();
		}
	}
	
	
	private void updateLabels() {
		layoutMainPage = new VBox(20);
		layoutMainPage.setStyle("-fx-background-color: #749c70;");
		headLabel.setText(String.format("Round: %d", round));
		coinsLabel.setText(String.format("Coins\nComputer:\t%d\nYou:\t\t\t%d", computer.getCoins(), you.getCoins()));
		potLabel.setText(String.format("Pot\n%d", pot));
		handLabel.setText(String.format("Your Hand:\n\n%s \t(%s)", you.getHand(), OUTCOMES[scores[1] / 100]));
		actionLabel.setText(actionLabelText);
		layoutMainPage.setPadding(new Insets(20, 20, 20, 20));
		layoutMainPage.getChildren().addAll(headLabel, separator1, coinsLabel, potLabel, separator2, handLabel, actionLabel, layoutHorizontalButtons, separator3, quitGameButton);
		sceneMainPage = new Scene(layoutMainPage, 300, 550);
		window.setScene(sceneMainPage);
	}
	
}