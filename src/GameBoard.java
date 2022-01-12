/** 
 * @file GameBoard.java
 * @author Samarth Kumar (kumars38)
 * @brief Contains a class for the model of a 2048 game
 * @date Apr. 12th, 2021
 */

import java.util.ArrayList;
import java.util.Random;

/**
 * @brief A model representing the game board for 2048
 * @details It is assumed that one of the init methods are called
 * before any other methods.
 */
public class GameBoard {
	
	// xSize and ySize correspond to the number of rows and columns, respectively
	private static int xSize, ySize;
	private static int[][] board;
	private static int score;
	private static int highScore = 0;

	// The status is true as long as the game is not over
	private static boolean status;
	private static boolean has2048;
	
	/**
	 * @brief Initializes the game board
	 * @details Initializes an empty game board with the
	 * specified dimensions and adds 2 start tiles
	 * @param rows Integer representing the number of rows in the board
	 * @param cols Integer representing the number of columns in the board
	 * @throws IllegalArgumentException If there are fewer than 4 rows or columns
	 */
	public static void init(int rows, int cols) {
		if (rows < 4 || cols < 4) {
			throw new IllegalArgumentException("The board must have at least 4 rows and columns");
		}
		
		xSize = rows;
		ySize = cols;
		board = new int[xSize][ySize];
		score = 0;
		status = true;
		has2048 = false;
		generateStartTiles();
	}
	
	/**
	 * @brief Initializes the game board
	 * @details Initializes an empty game board using the
	 * specified array. It is assumed that the input array is not 
	 * empty array. This is used for testing purposes only.
	 * @param b 2D array of integers representing entries to be used in the board
	 * @throws IllegalArgumentException If one or more of the rows of the array
	 * has a different length to the others
	 */
	public static void init(int[][] b) {
		xSize = b.length;
		ySize = b[0].length;
	
		for (int[] row : b) {
			if (row.length != ySize) {
				throw new IllegalArgumentException("One or more board columns are invalid.");
			}
		}
		
		// Initialize game board with specified values
		board = new int[xSize][ySize];
		for (int i=0; i<xSize; i++) {
			for (int j=0; j<ySize; j++) {
				board[i][j] = b[i][j];
			}
		}
		score = 0;
		status = true;
		has2048 = false;
	}
	
	private static void generateStartTiles() {
		addRandomTile();
		addRandomTile();
	}
	
	/**
	 * @brief Changes an empty cell of the board to a 2 or a 4
	 * @details It is assumed that this will never be called if there are
	 * no empty cells remaining in the board
	 */
	public static void addRandomTile() {
		Random rand = new Random();
		int[][] emptyCells = getEmptyCells();
		
		// choose a random empty cell index
		int r = (int) (rand.nextDouble()*(emptyCells.length));
		
		// get board positions from the random empty cell
		int x = emptyCells[r][0];
		int y = emptyCells[r][1];
		
		// add a tile to the board with value 2 or 4
		// (currently 10% chance to get a 4 instead of 2)
		board[x][y] = (int) (rand.nextDouble()*10) == 9 ? 4 : 2;
	}
	
	private static int[][] getEmptyCells() {
		ArrayList<int[]> a = new ArrayList<>();
		for (int i=0; i<xSize; i++) {
			for (int j=0; j<ySize; j++) {
				if (board[i][j] == 0) {
					a.add(new int[]{i,j});
				}
			}
		}
		int[][] emptyCells = new int[a.size()][2];
		int c=0;
		for (int[] cell : a) {
			emptyCells[c] = cell;
			c++;
		}
		return emptyCells;
	}
	
	/**
	 * @brief Changes the status based on the current board
	 * @details The status is used by the controller to determine when
	 * the game is over. If a cell contains the value 2048, then the 
	 * status is changed to false to indicate game over. The game is also
	 * over if there are no empty cells and no possible merges.
	 */
	public static void checkGameOver() {
		
		// first check if there is a 2048, to end the game
		for (int[] row : board) {
			for (int val : row) {
				if (val == 2048) {
					has2048 = true;
					status = false;
					return;
				}
			}
		}
		
		int[][] e = getEmptyCells();
		// no empty cells, game might be over
		if (e.length == 0) {
			boolean flag = false;
			for (int i=0; i<xSize; i++) {
				for (int j=0; j<ySize; j++) {
					//check all 4 directions if possible for duplicates
					if ((i>0 && board[i-1][j] == board[i][j]) ||
						(i<xSize-1 && board[i+1][j] == board[i][j]) ||
						(j>0 && board[i][j-1] == board[i][j]) ||
						(j<ySize-1 && board[i][j+1] == board[i][j])) {
						flag = true;
						break;
					}
				}
				if (flag)
					break;
			}
			// game over if board is full and no possible merge was found
			if (!flag) {
				status = false;
			}
		}
	}
	
	/**
	 * @brief Gets the current status of the game
	 * @details A status of true means the game is still running
	 * @return Boolean representing the game status
	 */
	public static boolean getStatus() {
		return status;
	}
	
	/**
	 * @brief Sets the current status of the game
	 * @details Can be used by the controller to manually end
	 * the game
	 * @param s Boolean representing whether status should
	 * become true or false
	 */
	public static void setStatus(boolean s) {
		status = s;
	}
	
	/**
	 * @brief Gets the current game board
	 * @return 2D array of integers, representing
	 * the positions of tiles and empty cells on the board
	 */
	public static int[][] getBoard() {
		return board;
	}
	
	/**
	 * @brief Gets the current game score
	 * @return Integer representing the game score
	 */
	public static int getScore() {
		return score;
	}
	
	/**
	 * @brief Gets the high score across all games
	 * @return Integer representing the high score
	 */
	public static int getHighScore() {
		return highScore;
	}

	/**
	 * @brief Reset the high score for testing purposes
	 */
	public static void resetHighScore() {
		highScore = 0;
	}
	
	/**
	 * @brief Updates the score and high score
	 * @details The score is incremented by the value of the merged tile,
	 * and the high score is updated if necessary
	 * @param val Integer representing the value of a tile
	 * that has just been merged
	 */
	public static void updateScore(int val) {
		score += val;
		if (score > highScore) {
			highScore = score;
		}
	}
	
	/**
	 * @brief Gets whether or not a 2048 was found in the board
	 * @return True if a 2048 was found in the board, False otherwise
	 */
	public static boolean has2048() {
		return has2048;
	}
	
	/**
	 * @brief Swaps the values of two cells in the board
	 * @param x Integer representing the row index of the first cell to be swapped
	 * @param y Integer representing the column index of the first cell to be swapped
	 * @param xDiff Integer representing the row difference from the first cell to the second cell
	 * @param yDiff Integer representing the column difference from the first cell to the second cell
	 * @throws IndexOutOfBoundsException If one or both of the specified indices is outside of the scope of
	 * the game board
	 */
	public static void swapCells(int x, int y, int xDiff, int yDiff) {
		
		if (!(0 <= x + xDiff) || !(x + xDiff < xSize) || !(0 <= y + yDiff) || !(y + yDiff < ySize)) {
			throw new IndexOutOfBoundsException("Cell index out of bounds");
		}

		int temp = board[x][y];
		int val = board[x+xDiff][y+yDiff];
		board[x][y] = val;
		board[x+xDiff][y+yDiff] = temp;
	}
	
	/**
	 * @brief Shifts all tiles upwards
	 * @details All non-empty cells with an empty cell above are swapped.
	 * If two vertically adjacent cells have the same value, they are merged
	 * upwards to form a cell with twice the value of each of them, and the value
	 * of the bottom cell becomes 0. If merges occur, the game score is updated
	 * accordingly.
	 */
	public static void shiftUp() {
		boolean[][] merged = new boolean[xSize][ySize];
		for (int col=0; col<ySize; col++) {
			for (int row=1; row<xSize; row++) {
				// only make moves if there is a tile at current position
				if (board[row][col] != 0) {
					int cR = row; // keep track of current row
					while (cR > 0) {
						// empty cell above tile
						if (board[cR-1][col] == 0) {
							swapCells(cR, col, -1, 0);
							cR--;
						}
						// non-empty cell above tile with same value,
						// merge the tiles only if that tile was not merged on same turn
						else if (board[cR-1][col] == board[cR][col]) {
							if (!merged[cR-1][col]) {
								int val = board[cR][col];
								board[cR-1][col] = 2*val;
								board[cR][col] = 0;
								merged[cR-1][col] = true;
								updateScore(2*val);
							}
							break;
						}
						// non-empty cell above with different value,
						// no shift can be made
						else {
							break;
						}
					}
				}
			}
		}
	}
	
	/**
	 * @brief Shifts all tiles downwards
	 * @details All non-empty cells with an empty cell below are swapped.
	 * If two vertically adjacent cells have the same value, they are merged
	 * downwards to form a cell with twice the value of each of them, and the value
	 * of the top cell becomes 0. If merges occur, the game score is updated
	 * accordingly.
	 */
	public static void shiftDown() {
		boolean[][] merged = new boolean[xSize][ySize];
		for (int col=0; col<ySize; col++) {
			for (int row=xSize-2; row>=0; row--) {
				// only make moves if there is a tile at current position
				if (board[row][col] != 0) {
					int cR = row; // keep track of current row
					while (cR < xSize-1) {
						// empty cell below tile
						if (board[cR+1][col] == 0) {
							swapCells(cR, col, 1, 0);
							cR++;
						}
						// non-empty cell below tile with same value,
						// merge the tiles only if that tile was not merged on same turn
						else if (board[cR+1][col] == board[cR][col]) {
							if (!merged[cR+1][col]) {
								int val = board[cR][col];
								board[cR+1][col] = 2*val;
								board[cR][col] = 0;
								merged[cR+1][col] = true;
								updateScore(2*val);
							}
							break;
						}
						// non-empty cell above with different value,
						// no shift can be made
						else {
							break;
						}
					}
				}
			}
		}
	}
	
	/**
	 * @brief Shifts all tiles towards the left
	 * @details All non-empty cells with an empty cell to the left are swapped.
	 * If two horizontally adjacent cells have the same value, they are merged
	 * to the left to form a cell with twice the value of each of them, and the value
	 * of the right cell becomes 0. If merges occur, the game score is updated
	 * accordingly.
	 */
	public static void shiftLeft() {
		boolean[][] merged = new boolean[xSize][ySize];
		for (int row=0; row<xSize; row++) {
			for (int col=1; col<ySize; col++) {
				// only make moves if there is a tile at current position
				if (board[row][col] != 0) {
					int cC = col; // keep track of current column
					while (cC > 0) {
						// empty cell left of tile
						if (board[row][cC-1] == 0) {
							swapCells(row, cC, 0, -1);
							cC--;
						}
						// non-empty cell left of tile with same value,
						// merge the tiles only if that tile was not merged on same turn
						else if (board[row][cC-1] == board[row][cC]) {
							if (!merged[row][cC-1]) {
								int val = board[row][cC];
								board[row][cC-1] = 2*val;
								board[row][cC] = 0;
								merged[row][cC-1] = true;
								updateScore(2*val);
							}
							break;
						}
						// non-empty cell above with different value,
						// no shift can be made
						else {
							break;
						}
					}
				}
			}
		}
	}
	
	/**
	 * @brief Shifts all tiles towards the right
	 * @details All non-empty cells with an empty cell to the right are swapped.
	 * If two horizontally adjacent cells have the same value, they are merged
	 * to the right to form a cell with twice the value of each of them, and the value
	 * of the left cell becomes 0. If merges occur, the game score is updated
	 * accordingly.
	 */
	public static void shiftRight() {
		boolean[][] merged = new boolean[xSize][ySize];
		for (int row=0; row<xSize; row++) {
			for (int col=ySize-2; col>=0; col--) {
				// only make moves if there is a tile at current position
				if (board[row][col] != 0) {
					int cC = col; // keep track of current column
					while (cC < ySize-1) {
						// empty cell right of tile
						if (board[row][cC+1] == 0) {
							swapCells(row, cC, 0, 1);
							cC++;
						}
						// non-empty cell right of tile with same value,
						// merge the tiles only if that tile was not merged on same turn
						else if (board[row][cC+1] == board[row][cC]) {
							if (!merged[row][cC+1]) {
								int val = board[row][cC];
								board[row][cC+1] = 2*val;
								board[row][cC] = 0;
								merged[row][cC+1] = true;
								updateScore(2*val);
							}
							break;
						}
						// non-empty cell above with different value,
						// no shift can be made
						else {
							break;
						}
					}
				}
			}
		}
	}
}
