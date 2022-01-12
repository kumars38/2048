/** 
 * @file UserInterface.java
 * @author Samarth Kumar (kumars38)
 * @brief Contains a class for the view of a 2048 game
 * @date Apr. 12th, 2021
 */

/**
 * @brief A user interface (view) responsible for printing values obtained
 * from the GameBoard class through the Controller, such as the score and 
 * visual representation of the board in the output console.
 */
public class UserInterface {
	
	/**
	 * @brief Prints the scores for the game to the screen
	 * @param score Integer representing the score of the current game
	 * @param highScore Integer representing the highest score achieved across
	 * all games
	 */
	public static void printScore(int score, int highScore) {
		// Clear* output
		for (int i=0; i<20; i++) {
			System.out.println();
		}
		System.out.println("High Score: "+highScore);
		System.out.println("Current Score: "+score);
		System.out.println("======================");
	}

	/**
	 * @brief Prints the game board to the screen
	 * @param b 2D array of integers representing the values at each position
	 * of the game board
	 */	
	public static void printBoard(int[][] b) {
		int numSpaces = 5;
		for (int[] row : b) {
			for (int cell : row) {
				String s = Integer.toString(cell);
				System.out.print(s);
				for (int i=s.length(); i<numSpaces; i++) {
					System.out.print(" ");
				}
			}
			System.out.println("\n");
		}
		System.out.println();
	}
	
	/**
	 * @brief Prints an introductory message to the screen
	 */	
	public static void printStartingMessage() {
		System.out.println("\nWelcome to 2048, the game.\n");
	}
	
	/**
	 * @brief Prompts the user for input to determine which game mode to play
	 * @details The user input is handled by the Controller
	 */	
	public static void printGameModePrompt() {
		System.out.println("Type 1 to play standard 2048 (4x4 board).");
		System.out.println("Type 2 to play on a custom board.");
		System.out.println("Type 'exit' to quit.");
	}
	
	/**
	 * @brief Prompts the user for input to determine the direction to move all tiles
	 * @details The user input is handled by the Controller
	 */	
	public static void printMovePrompt() {
		System.out.print("Enter move (l, r, u, or d): ");
	}
	
	/**
	 * @brief Prints a message to the screen once the game has finished
	 * @details Prints a congratulatory or game over message depending on
	 * if the user won. The score and high score are also printed. The user
	 * is asked whether they want to play again.
	 * @param won Boolean which is true if the user won (reached 2048), false otherwise
	 * @param score Integer representing the final score for the game
	 * @param highScore Integer representing the high score across all games 
	 */	
	public static void printEndingMessage(boolean won, int score, int highScore) {
		if (won) {
			System.out.println("Congratulations, you reached 2048.");
		}
		else {
			System.out.println("Game over!");
		}
		System.out.println("You finished with a score of "+score+".");
		System.out.println("Your high score is "+highScore+".\n");
		System.out.print("Would you like to play again (y/n): ");
	}
	
}