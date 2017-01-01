///////////////////////////////////////////////////////////////////////////////
//
// Title:            Program 2
// Files:            Sticks.java, Config.java, TestSticks.java
// Semester:         CS302 Fall 2016
//
// Author:           Xingmin Zhang
// Email:            xzhang66@wisc.edu
// CS Login:         none
// Lecturer's Name:  Jim Williams
// Lab Section:      none
//
//////////////////// PAIR PROGRAMMERS COMPLETE THIS SECTION ///////////////////
//
// Partner Name:     none
// Partner Email:    
// Partner CS Login: 
// Lecturer's Name:  
// Lab Section:      
// 
// VERIFY THE FOLLOWING BY PLACING AN X NEXT TO EACH TRUE STATEMENT:
//    ___ Write-up states that Pair Programming is allowed for this assignment.
//    ___ We have both read the CS302 Pair Programming policy.
//    ___ We have registered our team prior to the team registration deadline.
//
///////////////////////////// CREDIT OUTSIDE HELP /////////////////////////////
//
// Students who get help from sources other than their partner must fully 
// acknowledge and credit those sources of help here.  Instructors and TAs do 
// not need to be credited here, but tutors, friends, relatives, room mates 
// strangers, etc do.
//
// Persons:          none
// Online Sources:   how to generate random numbers with specified probability. 
//                   I got hint from this post. 
//                   http://stackoverflow.com/questions/20327958/random-number-with-probabilities
//
/////////////////////////////// 80 COLUMNS WIDE ///////////////////////////////

import java.util.Arrays;
import java.util.Scanner;

/* This is a game called Sticks. There are a defined number of sticks on the table.
 * Two players are required to play the game. A user can choose to play with a
 * friend (human), computer, or artificial intelligence.
 * Each player take a certain number of sticks each time.
 * Whoever take the last stick loses.  
 */


public class Sticks {
	
	/**
	 * This is the main method for the game of Sticks. 
	 * In milestone 1 this contains the whole program for playing
	 * against a friend.
	 * In milestone 2 this contains the welcome, name prompt, 
	 * how many sticks question, menu, calls appropriate methods
	 * and the thank you message at the end.
	 * One method called in multiple places is promptUserForNumber.
	 * When the menu choice to play against a friend is chosen,
	 * then playAgainstFriend method is called.
	 * When the menu choice to play against a computer is chosen,
	 * then playAgainstComputer method is called.  If the
	 * computer with AI option is chosen then trainAI is called
	 * before calling playAgainstComputer.  Finally, 
	 * call strategyTableToString to prepare a strategy table
	 * for printing. 
	 * 
	 * @param args (unused)
	 */
	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		
		//Welcome message
		System.out.println("Welcome to the Game of Sticks!");
		System.out.println("==============================");
		
		//let user input name and number of sticks to start with
		String userName;
		int sticksRemaining;
		int startSticks;
		String promptSticksStart = String.format("How many sticks are there on the "
				+ "table initially (%d-%d)? ", Config.MIN_STICKS, Config.MAX_STICKS);
		System.out.print("\nWhat is your name? ");
		userName = input.next().trim();
		System.out.println("Hello " + userName + ".");
		startSticks = promptUserForNumber(input, promptSticksStart,
				Config.MIN_STICKS, Config.MAX_STICKS);
		
		//let user choose an opponent
		int opponent;
		System.out.println("\nWould you like to:");
		System.out.println(" 1) Play against a friend");
		System.out.println(" 2) Play against computer (basic)");
		System.out.println(" 3) Play against computer with AI");
		String promptOpponent = "Which do you choose (1,2,3)? ";
		opponent = promptUserForNumber(input, promptOpponent, 1, 3);
				
		//start game
		switch (opponent) {
			case 1: //play with a friend
				String opponentName = ""; //store friend's name
				System.out.print("\nWhat is your friend's name? ");
				opponentName = input.next().trim();
				System.out.println("Hello " + opponentName + ".\n");
				printStartSticks(startSticks);
				playAgainstFriend(input, startSticks, userName, opponentName);	
				break;
				
			case 2: //play with computer
				System.out.println();
				printStartSticks(startSticks);
				Config.RNG.setSeed(432);
				playAgainstComputer(input, startSticks, userName, null);
				break;
			 
			case 3: //play with AI
				int numberOfGameToTrainAi = 0; //input the number of games to train AI
				String promptNumToTrainAi = String.format("How many games should the AI "
						+ "learn from (%d to %d)? ", Config.MIN_GAMES, Config.MAX_GAMES);
				System.out.println("");
				numberOfGameToTrainAi = promptUserForNumber(input, promptNumToTrainAi, 
						Config.MIN_GAMES, Config.MAX_GAMES);
				int [][] strategyTableAi = trainAi(startSticks, numberOfGameToTrainAi); //train
																				//AI and get the strategy table
				printStartSticks(startSticks);
				playAgainstComputer(input, startSticks, userName, strategyTableAi);//call the play
														//with computer method, but provide AI's strategy table
				System.out.print("Would you like to see the strategy table (Y/N)? "); //ask user
														//whether to print out the strategy table
				char displayStrategy = ' ';
				displayStrategy = input.next().toUpperCase().charAt(0);
				
				if (displayStrategy == 'Y') {
					System.out.println(strategyTableToString(strategyTableAi));
				}
				else {
					System.out.println("");
				}
				break;
		}
		
		input.close();
		
		//close game
		System.out.println("=========================================");
		System.out.println("Thank you for playing the Game of Sticks!");
		
	}

/**
 * This prints the start sticks. 
 * @param startSticks, number of sticks to start from
 */
	static void printStartSticks (int startSticks) {
		System.out.printf("There are %d sticks on the board.\n", startSticks);
	}
	/**
	 * This method encapsulates the code for prompting the user for a number and
	 * verifying the number is within the expected bounds.
	 * 
	 * @param input
	 *            The instance of the Scanner reading System.in.
	 * @param prompt
	 *            The prompt to the user requesting a number within a specific
	 *            range.
	 * @param min
	 *            The minimum acceptable number.
	 * @param max
	 *            The maximum acceptable number.
	 * @return The number entered by the user between and including min and max.
	 */
	static int promptUserForNumber(Scanner input, String prompt, 
			int min, int max) {
		int userNum = 0;
		String userInput = " ";
		boolean inputCorrect = false;
		
		do { //repeat this loop until inputCorrect is true
			System.out.print(prompt);
			if (input.hasNextInt()) {  //do the follows if input is an integer
				userNum = input.nextInt();
				if (userNum >= min && userNum <= max) { //if input within the range, 
																	 //break the loop
					inputCorrect = true;	
					input.nextLine();
				}
				else { //if input is not in range, ask user to input another value
					System.out.printf("Please enter a number between %d and %d.\n", min, max);
					input.nextLine();
				}
				
			}
			else { //if user input is not an integer, display an error message and
				    //ask user to input again
				userInput = input.nextLine();
				System.out.printf("Error: expected a number between %d and %d but found: " 
					+ userInput, min, max);
				System.out.println("");
			}
			} while(!inputCorrect);
		return userNum;
	}
	
	/**
	 * This method has one person play the Game of Sticks against another
	 * person.
	 * 
	 * @param input
	 *            An instance of Scanner to read user answers.
	 * @param startSticks
	 *            The number of sticks to start the game with.
	 * @param player1Name
	 *            The name of one player.
	 * @param player2Name
	 *            The name of the other player.
	 * 
	 *            As a courtesy, player2 is considered the friend and gets to
	 *            pick up sticks first.
	 * 
	 */
	static void playAgainstFriend(Scanner input, int startSticks, 
			String player1Name, String player2Name) {
		int sticksTaken = 0; //number of sticks a player wants to take
		int maxAction = Config.MAX_ACTION;
		int sticksRemaining = startSticks; //remaining sticks
		boolean player2Lose = false; //track whether player2 loses
		
		//prompts for user to play
		String promptPlay;
		do {
			promptPlay = promptChoice(sticksRemaining);//decides what prompt to use
			maxAction = maxAction(sticksRemaining);//decides what is the max 
																//allowed sticks to take
			sticksTaken = promptUserForNumber(input, (player2Name + promptPlay), 
					Config.MIN_ACTION, maxAction);//player2 takes sticks
			sticksRemaining = sticksCalc(sticksRemaining, sticksTaken);//track 
												//remaining sticks after player2's action
			if (isGameOver(sticksRemaining)) {//track whether player2 loses
				player2Lose = true;  //if player2 loses, stop the game.
				break;
			}
			
			promptPlay = promptChoice(sticksRemaining);//after player2's action, 
																	 //re-decide what prompt to use
			maxAction = maxAction(sticksRemaining);//after player2's action,
																//re-decide what is the max allowed 
																//sticks to take
			sticksTaken = promptUserForNumber(input, (player1Name + promptPlay), 
					Config.MIN_ACTION, maxAction);//player1 takes sticks
			sticksRemaining = sticksCalc(sticksRemaining, sticksTaken);//track 
			 															//remaining sticks after 
																		//player1's action
         if (isGameOver(sticksRemaining)) {//track whether player1 loses
				break;                         //if player1 loses, stop the game
			}
		}while(sticksRemaining >= 1);
		
		//display the result. 
		displayResult(player1Name, player2Name, player2Lose);
		
	}	
/**
 * This determines the max number of sticks one can take
 * The game rule set up a max number Config.MAX_ACTION, but when there are less 
 * sticks than Config.MAX_ACTION, the user should be asked to move less sticks  
 * @param sticksRemaining, remaining sticks on the board
 * @return the actual max sticks a player can take
 */
static int maxAction(int sticksRemaining) {
	int maxAction;
	if(sticksRemaining >= Config.MAX_ACTION) {
		maxAction = Config.MAX_ACTION;
	}
	else {
		maxAction = sticksRemaining;
	}
	return maxAction;
}
/**
 * This method is similar to the above. It choose what prompt to use. 
 * If there are less sticks than Config.MAX_ACTION, it displays the actual 
 * max allowed sticks
 * @param sticksRemaining
 * @return a prompt of choice
 */
static String promptChoice(int sticksRemaining) {
	int maxAction;
	String promptPlay;
	if(sticksRemaining >= Config.MAX_ACTION){
		maxAction = Config.MAX_ACTION;
		promptPlay = String.format(": How many sticks do you take (%d-%d)? ",
				Config.MIN_ACTION, maxAction);
	}
	else {
		maxAction = sticksRemaining;
		promptPlay = String.format(": How many sticks do you take (%d-%d)? ",
				Config.MIN_ACTION, maxAction);
	}
	return promptPlay;
}

	/**assess remaining sticks
	 * 
	 * @param sticksBeforeAction, number of sticks before taking more sticks
	 * @param sticksTaken, number of sticks during this action
	 * @return return number of sticks after this action. Display number of sticks 
	 * 		if at least 1 stick is remaining
	 */
static int sticksCalc(int sticksBeforeAction, int sticksTaken) {
	int sticksAfterAction = sticksBeforeAction - sticksTaken;
	if (sticksAfterAction > 1) {
		System.out.printf("There are %d sticks on the board.\n", sticksAfterAction);	
	}
	else if (sticksAfterAction == 1) {
		System.out.println("There is 1 stick on the board.");
	}
	return sticksAfterAction;
	}
	/** assess whether game over
	 * 
	 * @param sticksAfterAction, number of remaining sticks after last player 
	 * 		takes sticks
	 * @return if no sticks is left, declare game over. 
	 */
static boolean isGameOver (int sticksAfterAction) {
	boolean gameOver = false;
	if (sticksAfterAction == 0) {
		gameOver = true;
	}
	return gameOver;
	}

	/**
	 * This prints out the result. 
	 * @param winner, winner of the game.
	 * @param loser, loser of the game.
	 */
static void displayResult(String winner, String loser, boolean loserLose) {
	if (loserLose) {
		System.out.println(winner + " wins. " + loser + " loses.");
	}
	else {
		System.out.println(loser + " wins. " + winner + " loses.");
	}
}
	/**
	 * Make a choice about the number of sticks to pick up when given the number
	 * of sticks remaining.
	 * 
	 * Algorithm: If there are less than Config.MAX_ACTION sticks remaining, 
	 * then pick up the minimum number of sticks (Config.MIN_ACTION). 
	 * If Config.MAX_ACTION sticks remain, randomly choose a number between 
	 * Config.MIN_ACTION and Config.MAX_ACTION. Use Config.RNG.nextInt(?) 
	 * method to generate an appropriate random number.
	 * 
	 * @param sticksRemaining
	 *            The number of sticks remaining in the game.
	 * @return The number of sticks to pick up, or 0 if sticksRemaining is <= 0.
	 */
	static int basicChooseAction(int sticksRemaining) {
		int sticksTaken = -1;
		
		if (sticksRemaining >= Config.MAX_ACTION) {
			sticksTaken = Config.RNG.nextInt(Config.MAX_ACTION) + 1;
		}
		else if(sticksRemaining < Config.MAX_ACTION && 
				sticksRemaining >= Config.MIN_ACTION) {
			sticksTaken = Config.MIN_ACTION;
		} 
		else {
			sticksTaken = 0;
		}
		
		return sticksTaken;  
	}
	
	/**
	 * This method has a person play against a computer.
	 * Call the promptUserForNumber method to obtain user input.  
	 * Call the aiChooseAction method with the actionRanking row 
	 * for the number of sticks remaining. 
	 * 
	 * If the strategyTable is null, then this method calls the 
	 * basicChooseAction method to make the decision about how 
	 * many sticks to pick up. If the strategyTable parameter
	 * is not null, this method makes the decision about how many sticks to 
	 * pick up by calling the aiChooseAction method. 
	 * 
	 * @param input
	 *            An instance of Scanner to read user answers.
	 * @param startSticks
	 *            The number of sticks to start the game with.
	 * @param playerName
	 *            The name of one player.
	 * @param strategyTable
	 *            An array of action rankings. One action ranking for each stick
	 *            that the game begins with.
	 * 
	 */
	static void playAgainstComputer(Scanner input, int startSticks, 
			String playerName, int[][] strategyTable) {
		int sticksTaken = 0; //number of sticks a player wants to take
		int maxAction = Config.MAX_ACTION;
		int sticksRemaining = startSticks; //remaining sticks
		boolean humanLose = false; //track whether human loses
		
		String promptPlay;//prompts for human to play
		do {
			//first let human take sticks
			promptPlay = promptChoice(sticksRemaining);//decides what prompt to use
			maxAction = maxAction(sticksRemaining);//decides what is the max 
																//allowed sticks to take
			sticksTaken = promptUserForNumber(input, (playerName + promptPlay), 
					Config.MIN_ACTION, maxAction);//human player takes sticks
			sticksRemaining = sticksCalc(sticksRemaining, sticksTaken);//track 
												//remaining sticks after human's action
			if (isGameOver(sticksRemaining)) {//track whether human loses
				humanLose = true;  //if human loses, stop the game.
				break;
			}
			
			//then let computer take sticks
			if (strategyTable == null) {
				sticksTaken = basicChooseAction(sticksRemaining);//computer takes sticks
			}
			else {
				sticksTaken = aiChooseAction(sticksRemaining, strategyTable[sticksRemaining - 1]);
			}
			if (sticksTaken >= 2) { //print out how many sticks computer took
				System.out.println("Computer selects " + sticksTaken + " sticks." );
			}
			else {
				System.out.println("Computer selects " + sticksTaken + " stick." );
			}
			sticksRemaining = sticksCalc(sticksRemaining, sticksTaken);//track 
			 															//remaining sticks after 
																		//computer's action
         if (isGameOver(sticksRemaining)) {//track whether computer loses
				break;                         //if computer loses, stop the game
			}
		}while(sticksRemaining >= 1);
		
		displayResult("Computer", playerName, humanLose);//display result
	}
	
	/**
	 * This method chooses the number of sticks to pick up based on the
	 * sticksRemaining and actionRanking parameters.
	 * 
	 * Algorithm: If there are less than Config.MAX_ACTION sticks remaining 
	 * then the chooser must pick the minimum number of sticks (Config.MIN_ACTION). 
	 * For Config.MAX_ACTION or more sticks remaining then pick based on the 
	 * actionRanking parameter.
	 * 
	 * The actionRanking array has one element for each possible action. The 0
	 * index corresponds to Config.MIN_ACTION and the highest index corresponds
	 * to Config.MAX_ACTION. For example, if Config.MIN_ACTION is 1 and 
	 * Config.MAX_ACTION is 3, an action can be to pick up 1, 2 or 3 sticks. 
	 * actionRanking[0] corresponds to 1, actionRanking[1] corresponds to 2, etc. 
	 * The higher the element for an action in comparison to other elements, 
	 * the more likely the action should be chosen.
	 * 
	 * First calculate the total number of possibilities by summing all the
	 * element values. Then choose a particular action based on the relative
	 * frequency of the various rankings. 
	 * For example, if Config.MIN_ACTION is  1 and Config.MAX_ACTION is 3: 
	 * If the action rankings are {9,90,1}, the total is 100. Since 
	 * actionRanking[0] is 9, then an action of picking up 1 should be chosen 
	 * about 9/100 times. 2 should be chosen about 90/100 times and 1 should 
	 * be chosen about 1/100 times. Use Config.RNG.nextInt(?) method to 
	 * generate appropriate random numbers.
	 * 
	 * @param sticksRemaining
	 *            The number of sticks remaining to be picked up.
	 * @param actionRanking
	 *            The counts of each action to take. The 0 index corresponds to
	 *            Config.MIN_ACTION and the highest index corresponds to
	 *            Config.MAX_ACTION.
	 * @return The number of sticks to pick up. 0 is returned for the following
	 *         conditions: actionRanking is null, actionRanking has a length of
	 *         0, or sticksRemaining is <= 0.
	 * 
	 */
	static int aiChooseAction(int sticksRemaining, int[] actionRanking) {
		int sticksToTake = -1;
		
		if (sticksRemaining >= Config.MAX_ACTION) {
			int sum = 0;
			int SUM = 0;
			int i = 0;
			int randNum;
			for (i = 0; i < actionRanking.length; i++) { //get SUM of actionRanking elements
				SUM += actionRanking[i];
			}
		
			//assign i + 1 to sticksToTake based on their probability
			randNum = Config.RNG.nextInt(SUM) + 1; 
			for (i = 0; i < actionRanking.length; i++) {
				sum += actionRanking[i];
				if(randNum <= sum) {
					sticksToTake = i + 1;
					break;
				}
			}
		
		}
		
		else if (sticksRemaining >= Config.MIN_ACTION) {  //if sticksRemaining is between
																		  //MIN_ACTION and MAX_ACTION, take 
																		  //MIN_ACTION sticks
			sticksToTake = Config.MIN_ACTION;
		}
		else {
			sticksToTake = 0;
		}
	return sticksToTake; 
}
	/**
	 * This method initializes each element of the array to 1. If actionRanking
	 * is null then method simply returns.
	 * 
	 * @param actionRanking
	 *            The counts of each action to take. Use the length of the
	 *            actionRanking array rather than rely on constants for the
	 *            function of this method.
	 */
	static void initializeActionRanking(int []actionRanking) {
		if(actionRanking == null) {
			return;
		}
		else {
			for (int i = 0; i < actionRanking.length; i++) {
			actionRanking[i] = 1;
			}
			return;
		}
	}
	
	/**
	 * This method returns a string with the number of sticks left and the
	 * ranking for each action as follows.
	 * 
	 * An example: 10     3,4,11
	 * 
	 * The string begins with a number (number of sticks left), then is followed
	 * by 1 tab character, then a comma separated list of rankings, one for each
	 * action choice in the array. The string is terminated with a newline (\n) 
	 * character.
	 * 
	 * @param sticksLeft
	 *            The number of sticks left.
	 * @param actionRanking
	 *            The counts of each action to take. Use the length of the
	 *            actionRanking array rather than rely on constants for the
	 *            function of this method.
	 * @return A string formatted as described.
	 */
	static String actionRankingToString(int sticksLeft, int[]actionRanking) {
		String str = "";
		str = sticksLeft + "\t";
		for (int i = 0; i < actionRanking.length - 1; i++) {
			str = str + actionRanking[i] + ",";
		}
		str = str + actionRanking[actionRanking.length - 1] + "\n";
		return str; 
	}


	/**
	 * This method updates the actionRanking based on the action. Since the game
	 * was lost, the actionRanking for the action is decremented by 1, but not
	 * allowing the value to go below 1.
	 * 
	 * @param actionRanking
	 *            The counts of each action to take. The 0 index corresponds to
	 *            Config.MIN_ACTION and the highest index corresponds to
	 *            Config.MAX_ACTION.
	 * @param action
	 *            A specific action between and including Config.MIN_ACTION and
	 *            Config.MAX_ACTION.
	 */
	static void updateActionRankingOnLoss(int []actionRanking, int action) {
		if (actionRanking[action - 1] >= 2) {
			actionRanking[action - 1]--;
		};
	}
	
	/**
	 * This method updates the actionRanking based on the action. Since the game
	 * was won, the actionRanking for the action is incremented by 1.
	 * 
	 * @param actionRanking
	 *            The counts of each action to take. The 0 index corresponds to
	 *            Config.MIN_ACTION and the highest index corresponds to
	 *            Config.MAX_ACTION.
	 * @param action
	 *            A specific action between and including Config.MIN_ACTION and
	 *            Config.MAX_ACTION.
	 */
	static void updateActionRankingOnWin(int []actionRanking, int action) {
		actionRanking[action - 1]++;
	}
	
	/**
	 * Allocates and initializes a 2 dimensional array. The number of rows
	 * corresponds to the number of startSticks. Each row is an actionRanking
	 * with an element for each possible action. The possible actions range from
	 * Config.MIN_ACTION to Config.MAX_ACTION. Each actionRanking is initialized
	 * with the initializeActionRanking method.
	 * 
	 * @param startSticks
	 *            The number of sticks the game is starting with.
	 * @return The two dimensional strategyTable, properly initialized.
	 */
	static int[][] createAndInitializeStrategyTable(int startSticks) {
		int[][] strategyTable = new int[startSticks][Config.MAX_ACTION]; //create a 2D array
		for (int i = 0; i < startSticks; i++) {
			initializeActionRanking(strategyTable[i]);//initialize every row
		}
		return strategyTable;
	}	
		
	/**
	 * This formats the whole strategyTable as a string utilizing the
	 * actionRankingToString method. For example:
	 * 
	 * Strategy Table 
	 * Sticks Rankings 
	 * 10	  3,4,11 
	 * 9      6,2,5 
	 * 8      7,3,1 etc.
	 * 
	 * The title "Strategy Table" should be proceeded by a \n.
	 * 
	 * @param strategyTable
	 *            An array of actionRankings.
	 * @return A string containing the properly formatted strategy table.
	 */
	static String strategyTableToString(int[][] strategyTable) {
		String str = "";
		for(int i = strategyTable.length - 1; i >= 0; i--) { //format strategy to string row by row
			str = str + actionRankingToString(i + 1, strategyTable[i]);
		}
		str = "\nStrategy Table" + "\nSticks\tRankings\n" + str;
		return str; 
	}	
	
	
	/**
	 * This updates the strategy table since a game was won.
	 * 
	 * The strategyTable has the set of actionRankings for each number of sticks
	 * left. The actionHistory array records the number of sticks the user took 
	 * when a given number of sticks remained on the table. Remember that 
	 * indexing starts at 0. For example, if actionHistory at index 6 is 2, 
	 * then the user took 2 sticks when there were 7 sticks remaining on the 
	 * table.  
	 * For each action noted in the history, this calls the 
	 * updateActionRankingOnWin method passing the corresponding action 
	 * and actionRanking. After calling this method, the actionHistory is
	 * cleared (all values set to 0).
	 * 
	 * @param strategyTable
	 *            An array of actionRankings.
	 * 
	 * @param actionHistory
	 *            An array where the index indicates the sticks left and the
	 *            element is the action that was made.
	 */
	static void updateStrategyTableOnWin(int[][] strategyTable, int[] actionHistory) {//FIX
		for(int i = actionHistory.length - 1; i >= 0; i--) {
			if(actionHistory[i] > 0) {
				updateActionRankingOnWin(strategyTable[i], actionHistory[i]);
				actionHistory[i] = 0;
			}
		}
	}
	
	/**
	 * This updates the strategy table for a loss.
	 * 
	 * The strategyTable has the set of actionRankings for each number of sticks
	 * left. The actionHistory array records the number of sticks the user took 
	 * when a given number of sticks remained on the table. Remember that 
	 * indexing starts at 0. For example, if actionHistory at index 6 is 2, 
	 * then the user took 2 sticks when there were 7 sticks remaining on the 
	 * table. 
	 * For each action noted in the history, this calls the 
	 * updateActionRankingOnLoss method passing the corresponding action 
	 * and actionRanking. After calling this method, the actionHistory is 
	 * cleared (all values set to 0).
	 * 
	 * @param strategyTable
	 *            An array of actionRankings.
	 * @param actionHistory
	 *            An array where the index indicates the sticks left and the
	 *            element is the action that was made.
	 */
	static void updateStrategyTableOnLoss(int[][] strategyTable, int[] actionHistory) {
		
		for(int i = actionHistory.length - 1; i >= 0; i--) { 
			if(actionHistory[i] > 0) {
				updateActionRankingOnLoss(strategyTable[i], actionHistory[i]);
				actionHistory[i] = 0;
			}
		}
	}	

	/**
	 * This method simulates a game between two players using their
	 * corresponding strategyTables. Use the aiChooseAction method
	 * to choose an action for each player. Record each player's 
	 * actions in their corresponding history array. 
	 * This method doesn't print out any of the actions being taken. 
	 * Player 1 should make the first move in the game.
	 * 
	 * @param startSticks
	 *            The number of sticks to start the game with.
	 * @param player1StrategyTable
	 *            An array of actionRankings.
	 * @param player1ActionHistory
	 *            An array for recording the actions that occur.
	 * @param player2StrategyTable
	 *            An array of actionRankings.
	 * @param player2ActionHistory
	 *            An array for recording the actions that occur.
	 * @return 1 or 2 indicating which player won the game.
	 */
	static int playAiVsAi(int startSticks, int[][] player1StrategyTable, 
			int[] player1ActionHistory, int[][] player2StrategyTable, 
			int[] player2ActionHistory) {
		int sticksRemaining = startSticks; //store the number of remaining sticks
		int sticksTaken; //store number of sticks that is taken at each step
		int winner = -1; //store winner
		do {
	   sticksTaken = aiChooseAction(sticksRemaining, player1StrategyTable[sticksRemaining-1]);
	   //let computer choose how many sticks to take based on strategy table
	   player1ActionHistory[sticksRemaining-1] = sticksTaken;//track action history
	   sticksRemaining -= sticksTaken;//track 
		//remaining sticks after action
		if (isGameOver(sticksRemaining)) {//track whether human loses
			winner = 2;
		break;
		}
		
	   sticksTaken = aiChooseAction(sticksRemaining, player2StrategyTable[sticksRemaining-1]);
	   player2ActionHistory[sticksRemaining-1] = sticksTaken;
	   sticksRemaining -= sticksTaken;//track 
		//remaining sticks after action
		if (isGameOver(sticksRemaining)) {//track whether human loses
			winner = 1;
		break;
		}
		} while (sticksRemaining > 0);
		return winner;
	}

	/**
	 * This method has the computer play against itself many times. Each time 
	 * it plays it records the history of its actions and uses those actions 
	 * to improve its strategy.
	 * 
	 * Algorithm: 
	 * 1) Create a strategy table for each of 2 players with 
	 *    createAndInitializeStrategyTable. 
	 * 2) Create an action history for each player.  An action history is a 
	 *    single dimension array of int. Each index in action history 
	 *    corresponds to the number of sticks remaining where the 0 index is
	 *    1 stick remaining.
	 * 3) For each game, 
	 * 		4) Call playAiVsAi with the return value indicating the winner. 
	 * 		5) Call updateStrategyTableOnWin for the winner and 
	 * 		6) Call updateStrategyTableOnLoss for the loser. 
	 * 7) After the games are played then the strategyTable for whichever 
	 * 	  strategy won the most games is returned. When both players win the 
	 *    same number of games, return the first player's strategy table.
	 * 
	 * @param startSticks
	 *            The number of sticks to start with.
	 * @param numberOfGamesToPlay
	 *            The number of games to play and learn from.
	 * @return A strategyTable that can be used to make action choices when
	 *         playing a person. Returns null if startSticks is less than
	 *         Config.MIN_STICKS or greater than Config.MAX_STICKS. Also returns
	 *         null if numberOfGamesToPlay is less than 1.
	 */
	static int[][] trainAi(int startSticks, int numberOfGamesToPlay) {
		if (startSticks < Config.MIN_STICKS || startSticks > Config.MAX_STICKS || 
				numberOfGamesToPlay < 1) { //return null if start sticks is less than MIN_STICKS
							//or more than MAX_STICKS or number of games to train AI is less than 1
			return null;
		}
		else {
			//create strategy table and actionHistory for each player
			int[][] player1StrategyTable = createAndInitializeStrategyTable(startSticks);
			int[][] player2StrategyTable = createAndInitializeStrategyTable(startSticks);
			int[] player1ActionHistory = new int[startSticks];
			int[] player2ActionHistory = new int[startSticks];
			int winner; //store winner player
			int player1WinTimes = 0; //store how many times player1 won
			int player2WinTimes = 0; //store how many times player2 won
			for (int i = 0; i < numberOfGamesToPlay; i++) {  //loop to play defined times of games
				winner = playAiVsAi(startSticks, player1StrategyTable, 
						player1ActionHistory, player2StrategyTable, 
						player2ActionHistory); //determine winner for each game
				if (winner == 1) { //update winner and loser's strategy table and action history
					player1WinTimes++;
					updateStrategyTableOnWin(player1StrategyTable, player1ActionHistory);
					updateStrategyTableOnLoss(player2StrategyTable, player2ActionHistory);
				}
				if (winner == 2) {
					player2WinTimes++;
					updateStrategyTableOnWin(player2StrategyTable, player2ActionHistory);
					updateStrategyTableOnLoss(player1StrategyTable, player1ActionHistory);	
				}
			}
			
			if (player1WinTimes >= player2WinTimes) { //return winner's strategy table
				return player1StrategyTable;
			}
			else {
				return player2StrategyTable;
			}
		}
	}
}
