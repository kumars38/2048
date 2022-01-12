/** 
 * @file Controller.java
 * @author Samarth Kumar (kumars38)
 * @brief Contains a class for the controller of a 2048 game
 * @date Apr. 12th, 2021
 */

import java.util.Arrays;
import java.util.Scanner;

/**
 * @brief A controller responsible for combining the game state (model) and
 * view to handle all user input and print the updated game state back to the user
 */
public class Controller {

	// changes based on whether the user wants to keep playing or not
	static boolean playing;

	// a scanner for reading user input
	static Scanner sc;
	static int rows, cols;
	
	// normal is true for regular 2048 board, false for custom board
	// (determined in setup method)
	static boolean normal;
	
	/**
	 * @brief Initializes the Controller
	 */
	public static void init() {
		setup();
		gameLoop();
	}
	
	/**
	 * @brief Loops as long as the user wants to keep playing
	 * @details Launches the game, then once it terminates, prints the final
	 * board to the user along with their score and high score. This is done by
	 * passing information from the model into the view.
	 */
	public static void gameLoop() {
		while(playing) {
			launch();
			UserInterface.printBoard(GameBoard.getBoard());
			UserInterface.printEndingMessage(GameBoard.has2048(), GameBoard.getScore(), GameBoard.getHighScore());
			while (true) {
				// See if user wants to play again, if not, set playing to false
				String choice = sc.next();
				System.out.println();
				if (choice.equals("n")) {
					playing = false;
				}
				else if (!choice.equals("y")) {
					System.out.print("Invalid choice, try again: ");
					continue;
				}
				break;
			}
		}
	}
	
	/**
	 * @brief First time setup for the game (run only once)
	 * @details Uses the view to print introductory information and prompts
	 * the user for game mode selection.
	 */
	public static void setup() {
		sc = new Scanner(System.in);
		playing = true;
		normal = true;
		rows = 4; // Default size
		cols = 4;
		
		UserInterface.printStartingMessage();
		UserInterface.printGameModePrompt();
		
		while (true) {
			String choice = sc.next();
			System.out.println();
			if (choice.equals("1")) {
				normal = true;
			}
			else if (choice.equals("2")) {
				normal = false;
				System.out.print("Enter number of rows in custom board: ");
				rows = sc.nextInt();
				System.out.println();
				System.out.print("Enter number of columns in custom board: ");
				cols = sc.nextInt();
				System.out.println();
			}
			else if (choice.equals("exit")) {
				playing = false;
			}
			else {
				System.out.print("Invalid choice, try again: ");
				continue;
			}
			break;
		}
	}
	
	/**
	 * @brief Launches a single game of 2048
	 * @details The game board is initialized to the appropriate dimensions,
	 * and while the game is running, the score, high score, and board, are 
	 * continually printed to the user. After which, the user is prompted 
	 * for a move direction, and the game board is checked for possible game 
	 * over, and random tiles are added if applicable.
	 */
	public static void launch() {
		GameBoard.init(rows, cols);
		while (GameBoard.getStatus()) {
			
			// Print the board, score and high score first using View module
			int[][] initialB = new int[rows][cols];
			int[][] b = GameBoard.getBoard();
			
			for (int i=0; i<rows; i++) {
				for (int j=0; j<cols; j++) {
					initialB[i][j] = b[i][j];
				}
			}
			
			UserInterface.printScore(GameBoard.getScore(), GameBoard.getHighScore());
			UserInterface.printBoard(b);
			
			// Get user input to make a move
			UserInterface.printMovePrompt();

			while (true) {
				String move = sc.next();
				System.out.println();
				if (move.equals("u"))
					GameBoard.shiftUp();
				else if (move.equals("d"))
					GameBoard.shiftDown();
				else if (move.equals("l"))
					GameBoard.shiftLeft();
				else if (move.equals("r"))
					GameBoard.shiftRight();
				else {
					System.out.print("Invalid choice, try again: ");
					continue;
				}
				break;
			}
			
			// Check for 2048 tile
			GameBoard.checkGameOver();
			
			// After the move, if no 2048 tile and if the board changed, 
			// add tile and update status
			if (GameBoard.getStatus() && !Arrays.deepEquals(initialB, b)) {
				GameBoard.addRandomTile();
				GameBoard.checkGameOver();
			}
		}
	}

}
