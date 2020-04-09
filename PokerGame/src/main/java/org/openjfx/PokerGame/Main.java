

package org.openjfx.PokerGame;
import java.util.HashMap;
import java.util.Map;

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
import javafx.geometry.Insets;
import javafx.geometry.Pos;


public class Main extends Application {

	final public static int STARTING_COINS = 10;
	final public static int CARDS_PER_PLAYER = 5;
	final private static int ALLOWED_REFRESHABLE_CARDS = 4;
	final private static int MIN_OPEN = 109; // pair of jacks
	final private static int MIN_COMPUTER_PROCEED = 200; // two pair
	final private static int MAX_BET_AMOUNT = 3;
	final private static String[] OUTCOMES = new String[] {"High Card", "Pair", "Two Pair", "Triple", "Straight", "Flush", 
			"Full House", "Four Of A Kind", "Straight Flush", "Royal Flush"};
	
	public static Deck deck;
	private static Player computer, you;
	private static int pot, round;
	private static int[] scores = new int[2];
	private static String actionLabelText;
	
	private static Label[] labels = new Label[5];
	private static Scene sceneLandingPage, sceneMainPage;
	private static Stage window;
	private static VBox layoutLandingPage, layoutMainPage;
	private static HBox layoutHorizontalButtons;
	private static Separator[] separators = new Separator[3];
	private static CheckBox[] checkboxes = new CheckBox[CARDS_PER_PLAYER];
	private static RadioButton[] radioButtons = new RadioButton[MAX_BET_AMOUNT];
	private static Map<String, Button> buttons = new HashMap<String, Button>();
	
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		// setup javafx application window
		window = primaryStage;
		window.setTitle("Poker Game");
		
		buttons.put("startButton", new Button("Start Game"));
		buttons.put("openButton", new Button("Yes"));
		buttons.put("nextRoundButton", new Button("Next Round"));
		buttons.put("refreshCardsButton", new Button("Refresh Cards"));
		buttons.put("checkButton", new Button("Check"));
		buttons.put("betButton", new Button("Bet"));
		buttons.put("submitBetButton", new Button("Submit Bet"));
		buttons.put("quitGameButton", new Button("Restart Game"));
		
		layoutLandingPage = new VBox(10);
		layoutLandingPage.setAlignment(Pos.CENTER);
		layoutLandingPage.setPadding(new Insets(20, 20, 20, 20));
		layoutLandingPage.getChildren().addAll(buttons.get("startButton"));
		sceneLandingPage = new Scene(layoutLandingPage, 300, 300);
		window.setScene(sceneLandingPage);
		
		// when the game is started...
		buttons.get("startButton").setOnAction(e -> setUpGame());
		
		// when the round is opened...
		buttons.get("openButton").setOnAction(e -> openGame());
		
		// when moving onto the next round...
		buttons.get("nextRoundButton").setOnAction(e -> nextRound());
		
		// when the user has selected to refresh certain cards...
		buttons.get("refreshCardsButton").setOnAction(e -> refreshCards());
		
		// when the user has selected to check...
		buttons.get("checkButton").setOnAction(e -> check());
				
		// when the user has selected to bet...
		buttons.get("betButton").setOnAction(e -> bet());
		
		// when the user has submitted their bet...
		buttons.get("submitBetButton").setOnAction(e -> submitBet());
		
		// if user wants to play again show home screen
		buttons.get("quitGameButton").setOnAction(e -> window.setScene(sceneLandingPage));
		
		window.show();
	}
	
	private void setUpGame() {
		deck = new Deck();
		computer = new Player();
		you = new Player();
		pot = 0;
		round = 1;
		layoutHorizontalButtons = new HBox(10);
		for(int i = 0; i < separators.length; i++)
			separators[i] = new Separator();
		for(int i = 0; i < labels.length; i++)
			labels[i] = new Label();
		labels[0].setStyle("-fx-font: 24 arial;");
		labels[1].setStyle("-fx-font: 13 arial; -fx-font-weight: bold;");
		labels[2].setStyle("-fx-font: 13 arial; -fx-font-weight: bold;");
		labels[3].setStyle("-fx-font: 15 arial; -fx-font-weight: bold;");
		labels[4].setStyle("-fx-font: 13 arial;");
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
		buttons.get("nextRoundButton").setText("No");
		if(scores[0] >= MIN_OPEN) {
			actionLabelText = "Computer can open.\n\nDo you want to play?";
			layoutHorizontalButtons.getChildren().addAll(buttons.get("openButton"), buttons.get("nextRoundButton"));
		}
		else {
			if(scores[1] >= MIN_OPEN) {
				actionLabelText = "Computer cannot open.\nYou can open.\n\nDo you want to play?";
				layoutHorizontalButtons.getChildren().addAll(buttons.get("openButton"), buttons.get("nextRoundButton"));
			}
			else {
				actionLabelText = "Computer cannot open.\nYou cannot open.";
				buttons.get("nextRoundButton").setText("Next Round");
				layoutHorizontalButtons.getChildren().addAll(buttons.get("nextRoundButton"));
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
		
		layoutHorizontalButtons.getChildren().addAll(checkboxes[0], checkboxes[1], checkboxes[2], checkboxes[3], checkboxes[4], buttons.get("refreshCardsButton"));
		updateLabels();
	}
	
	private void nextRound() {
		deck = new Deck();
		computer.generateHand();
		you.generateHand();
		round++;
		evaluateHands();
		checkOpen();
		updateLabels();
	}
	
	private void refreshCards() {
		int tempCounter = 0;
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
			layoutHorizontalButtons.getChildren().addAll(buttons.get("checkButton"));
		else
			layoutHorizontalButtons.getChildren().addAll(buttons.get("checkButton"), buttons.get("betButton"));
		
		actionLabelText = String.format("(Computer has refreshed %d cards)\n\nWhat is your next move?", tempCounter);
		
		updateLabels();
	}
	
	private void check() {
		layoutHorizontalButtons.getChildren().clear();
		actionLabelText = String.format("Computer's Hand:\n\n%s \t(%s)\n\n", computer, OUTCOMES[scores[0] / 100]);
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
			buttons.get("quitGameButton").setText("Play Again");
			updateLabels();
			return;
		}
		buttons.get("nextRoundButton").setText("Next Round");
		layoutHorizontalButtons.getChildren().addAll(buttons.get("nextRoundButton"));
		updateLabels();
	}
	
	private void bet() {
		layoutHorizontalButtons.getChildren().clear();
		
		ToggleGroup betAmountToggle = new ToggleGroup();
		for(int i = 0; i < radioButtons.length; i++) {
			radioButtons[i] = new RadioButton(String.format("%d", i + 1));
			radioButtons[i].setToggleGroup(betAmountToggle);
		}
        
        if(you.getCoins() >= 3 && computer.getCoins() >= 3)
        	layoutHorizontalButtons.getChildren().addAll(radioButtons[0], radioButtons[1], radioButtons[2], buttons.get("submitBetButton"));
        else if(you.getCoins() >= 2 && computer.getCoins() >= 2)
        	layoutHorizontalButtons.getChildren().addAll(radioButtons[0], radioButtons[1], buttons.get("submitBetButton"));
        else if(you.getCoins() >= 1 && computer.getCoins() >= 1)
        	layoutHorizontalButtons.getChildren().addAll(radioButtons[0], buttons.get("submitBetButton"));
        else
        	return;
		actionLabelText = "How much would you like to bet?";
		updateLabels();
	}
	
	private void submitBet() {
		int betAmount = 0;
		if(radioButtons[0].isSelected())
			betAmount = 1;
		else if(radioButtons[1].isSelected())
			betAmount = 2;
		else if(radioButtons[2].isSelected())
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
			buttons.get("nextRoundButton").setText("Next Round");
			layoutHorizontalButtons.getChildren().addAll(buttons.get("nextRoundButton"));
			updateLabels();
		}
	}
	
	
	private void updateLabels() {
		layoutMainPage = new VBox(20);
		layoutMainPage.setStyle("-fx-background-color: #749c70;");
		labels[0].setText(String.format("Round: %d", round));
		labels[1].setText(String.format("Coins\nComputer:\t%d\nYou:\t\t\t%d", computer.getCoins(), you.getCoins()));
		labels[2].setText(String.format("Pot\n%d", pot));
		labels[3].setText(String.format("Your Hand:\n\n%s \t(%s)", you, OUTCOMES[scores[1] / 100]));
		labels[4].setText(actionLabelText);
		layoutMainPage.setPadding(new Insets(20, 20, 20, 20));
		layoutMainPage.getChildren().addAll(labels[0], separators[0], labels[1], labels[2], separators[1], labels[3], labels[4], layoutHorizontalButtons, separators[2], buttons.get("quitGameButton"));
		sceneMainPage = new Scene(layoutMainPage, 300, 600);
		window.setScene(sceneMainPage);
	}
	
}