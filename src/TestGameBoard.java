/**
 * Author: Samarth Kumar (kumars38)
 * Revised: Apr. 12th, 2021
 * 
 * Description: Test cases for Model of 2048 game
 */

import org.junit.*;
import static org.junit.Assert.*;
import java.util.Arrays;

public class TestGameBoard
{
	private int[][] e;
	
	@Before
	public void setUp()
	{
		GameBoard.init(4, 4);
		
		// use for empty board
		e = new int[4][4];
	
	}
	
	@After
	public void tearDown()
	{
		// Re-initialize game board to reset
		GameBoard.init(4, 4);
		GameBoard.resetHighScore();
	}
	
	@Test
	public void testCustomBoard()
	{
		// Try to create a board that is 5x7
		int[][] b = new int[5][7];
		GameBoard.init(b);
	}
	
	@Test
	public void testGetBoard()
	{
		int[][] b1 = {{0,2,4,4},{2,0,0,0},{0,2,4,0},{4,2,0,4}};
		GameBoard.init(b1);
		
		int[][] b2 = GameBoard.getBoard();
		
		// Assert that board arrays match
		assertTrue(Arrays.deepEquals(b1, b2));
	}
	
	@Test
	public void testInitStartTiles()
	{
		int numTiles = 0;
		int[][] b = GameBoard.getBoard();
		
		for (int[] row : b) {
			for (int cell : row) {
				if (cell != 0) {
					numTiles++;
					// Non-empty cell must be 2 or 4 at the start
					assertTrue(cell == 2 || cell == 4);
				}
			}
		}
		// Test that two tiles are now in the board after initialization
		assertTrue(numTiles == 2);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInitTooFewRows()
	{
		GameBoard.init(3,3);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInitInvalidArray()
	{
		// The second row is missing an element
		int[][] b1 = {{0,2,4,4},{2,0,0},{0,2,4,0},{4,2,0,4}};
		GameBoard.init(b1);
	}

	@Test
	public void testSetStatus()
	{
		int[][] b1 = {{0,2,4,4},{2,0,0,0},{0,2,4,0},{4,2,0,4}};
		GameBoard.init(b1);

		GameBoard.setStatus(false);
		assertFalse(GameBoard.getStatus());
	}

	@Test
	public void testGetStatus()
	{
		int[][] b1 = {{0,2,4,4},{2,0,0,0},{0,2,4,0},{4,2,0,4}};
		GameBoard.init(b1);

		assertTrue(GameBoard.getStatus());
	}

	@Test
	// Score should be 0 at the beginning of the game
	public void testGetScoreInitial()
	{
		int[][] b1 = {{0,2,4,4},{2,0,0,0},{0,2,4,0},{4,2,0,4}};
		GameBoard.init(b1);

		assertTrue(GameBoard.getScore() == 0);
	}

	@Test
	// Score should become 12 after merging (4 + 8)
	public void testGetScoreAfter()
	{
		// 0-2-0-2      0-0-0-4
		// 0-0-4-4  ->  0-0-0-8
		// 0-0-0-0      0-0-0-0
		// 0-0-0-0      0-0-0-0
		int[][] b1 = {{0,2,0,2},{0,0,4,4},{0,0,0,0},{0,0,0,0}};
		GameBoard.init(b1);

		GameBoard.shiftRight();

		assertTrue(GameBoard.getScore() == 12);
	}

	@Test
	// After re-initializing the game, high score should remain
	public void testGetHighScore()
	{
		// 0-2-0-2      0-0-0-4
		// 0-0-4-4  ->  0-0-0-8
		// 8-0-0-8      0-0-0-16
		// 0-0-0-0      0-0-0-0
		int[][] b1 = {{0,2,0,2},{0,0,4,4},{8,0,0,8},{0,0,0,0}};
		GameBoard.init(b1);
		GameBoard.shiftRight();

		assertTrue(GameBoard.getHighScore() == 28);

		// Re-init
		GameBoard.init(4,4);

		assertTrue(GameBoard.getHighScore() == 28);
	}	

	@Test
	public void testUpdateScore()
	{
		GameBoard.init(4,4);

		GameBoard.updateScore(32);
		assertTrue(GameBoard.getScore() == 32 && GameBoard.getHighScore() == 32);
		GameBoard.updateScore(8);
		assertTrue(GameBoard.getScore() == 40 && GameBoard.getHighScore() == 40);
	}	
	
	@Test
	// Shift up when the board is empty
	public void testShiftUp1()
	{
		// Initialize empty board
		GameBoard.init(e);
		GameBoard.shiftUp();
		int[][] shiftedB = GameBoard.getBoard();
		assertTrue(Arrays.deepEquals(e, shiftedB));
	}
	
	@Test
	// Shift up when board is full and has no merges
	public void testShiftUp2()
	{
		// 4-8-4 -4        4-8-4 -4
		// 8-4-2 -8    ->  8-4-2 -8
		// 4-2-32-64       4-2-32-64
		// 2-4-64-256      2-4-64-256
		
		int[][] b = {{4,8,4,4},{8,4,2,8},{4,2,32,64},{2,4,64,256}};
		GameBoard.init(b);
		GameBoard.shiftUp();
		// Expected board, no changes
		int[][] expB = b;
		int[][] shiftedB = GameBoard.getBoard();
		assertTrue(Arrays.deepEquals(expB, shiftedB));
	}
	
	@Test
	// Shift up no merges
	public void testShiftUp3()
	{
		// 0-2-4-4      2-2-4-4
		// 2-0-0-0  ->  4-8-8-2
		// 0-8-8-0      0-2-0-0
		// 4-2-0-2      0-0-0-0
		
		int[][] b = {{0,2,4,4},{2,0,0,0},{0,8,8,0},{4,2,0,2}};
		GameBoard.init(b);
		GameBoard.shiftUp();
		// Expected board
		int[][] expB = {{2,2,4,4},{4,8,8,2},{0,2,0,0},{0,0,0,0}};
		int[][] shiftedB = GameBoard.getBoard();
		assertTrue(Arrays.deepEquals(expB, shiftedB));
	}
	
	@Test
	// Shift up single merges (2-2-8-16) -> (4-8-16-0)
	public void testShiftUp4()
	{
		// 2 -4-0-16      4 -8-0-32
		// 2 -0-0-0   ->  8 -2-0-0
		// 8 -4-0-0       16-0-0-0
		// 16-2-0-16      0 -0-0-0
		
		int[][] b = {{2,4,0,16},{2,0,0,0},{8,4,0,0},{16,2,0,16}};
		GameBoard.init(b);
		GameBoard.shiftUp();
		// Expected board
		int[][] expB = {{4,8,0,32},{8,2,0,0},{16,0,0,0},{0,0,0,0}};
		int[][] shiftedB = GameBoard.getBoard();
		assertTrue(Arrays.deepEquals(expB, shiftedB));
	}
	
	@Test
	// Shift up double merges (4-4-4-4) -> (8-8-0-0)
	public void testShiftUp5()
	{
		// 8-4-8-2       16-8-8-4
		// 8-4-4-2   ->  16-8-4-16
		// 8-4-8-8       0 -0-8-0
		// 8-4-4-8       0 -0-4-0
		
		int[][] b = {{8,4,8,2},{8,4,4,2},{8,4,8,8},{8,4,4,8}};
		GameBoard.init(b);
		GameBoard.shiftUp();
		// Expected board
		int[][] expB = {{16,8,8,4},{16,8,4,16},{0,0,8,0},{0,0,4,0}};
		int[][] shiftedB = GameBoard.getBoard();
		assertTrue(Arrays.deepEquals(expB, shiftedB));
	}
	
	@Test
	// Shift up, same tile should not merge twice (4-2-2-4) -> (4-4-4-0) 
	// Not (4-2-2-4) -> (8-4-0-0) 
	public void testShiftUp6()
	{
		// 4-8 -2-0       4-16-4-4
		// 2-8 -0-2   ->  4-32-4-2
		// 2-16-2-2       4-0 -0-0
		// 4-16-4-2       0-0 -0-0
		
		int[][] b = {{4,8,2,0},{2,8,0,2},{2,16,2,2},{4,16,4,2}};
		GameBoard.init(b);
		GameBoard.shiftUp();
		// Expected board
		int[][] expB = {{4,16,4,4},{4,32,4,2},{4,0,0,0},{0,0,0,0}};
		int[][] shiftedB = GameBoard.getBoard();
		assertTrue(Arrays.deepEquals(expB, shiftedB));
	}
	
	@Test
	// Shift down no merges
	public void testShiftDown1()
	{
		// 0-2-4-4      0-0-0-0
		// 2-0-0-0  ->  0-2-0-0
		// 0-8-8-0      2-8-4-4
		// 4-2-0-2      4-2-8-2
		
		int[][] b = {{0,2,4,4},{2,0,0,0},{0,8,8,0},{4,2,0,2}};
		GameBoard.init(b);
		GameBoard.shiftDown();
		// Expected board
		int[][] expB = {{0,0,0,0},{0,2,0,0},{2,8,4,4},{4,2,8,2}};
		int[][] shiftedB = GameBoard.getBoard();
		assertTrue(Arrays.deepEquals(expB, shiftedB));
	}
	
	@Test
	// Shift left no merges
	public void testShiftLeft1()
	{
		// 0-2-8-4      2-8-4-0
		// 2-0-0-0  ->  2-0-0-0
		// 0-8-4-0      8-4-0-0
		// 4-2-0-4      4-2-4-0
		
		int[][] b = {{0,2,8,4},{2,0,0,0},{0,8,4,0},{4,2,0,4}};
		GameBoard.init(b);
		GameBoard.shiftLeft();
		// Expected board
		int[][] expB = {{2,8,4,0},{2,0,0,0},{8,4,0,0},{4,2,4,0}};
		int[][] shiftedB = GameBoard.getBoard();
		assertTrue(Arrays.deepEquals(expB, shiftedB));
	}
	
	@Test
	// Shift right no merges
	public void testShiftRight1()
	{
		// 0-2-8-4      0-2-8-4
		// 2-0-0-0  ->  0-0-0-2
		// 0-8-4-0      0-0-8-4
		// 4-2-0-4      0-4-2-4
		
		int[][] b = {{0,2,8,4},{2,0,0,0},{0,8,4,0},{4,2,0,4}};
		GameBoard.init(b);
		GameBoard.shiftRight();
		// Expected board
		int[][] expB = {{0,2,8,4},{0,0,0,2},{0,0,8,4},{0,4,2,4}};
		int[][] shiftedB = GameBoard.getBoard();
		assertTrue(Arrays.deepEquals(expB, shiftedB));
	}
	
	@Test
	// Board is not full, game is not over
	public void testGameOverBoardNotFull() 
	{
		int[][] b = {{0,2,8,4},{2,0,0,0},{0,8,4,0},{4,2,0,4}};
		GameBoard.init(b);
		GameBoard.checkGameOver();
		//status should still be true if game is not over
		boolean s = GameBoard.getStatus();
		assertTrue(s); 
	}
	
	@Test
	// Board is full with no possible merges -- game over
	public void testGameOverBoardFull1() 
	{
		// 2 -4-8-16
		// 4 -2-4-64
		// 2 -8-2-4
		// 16-4-8-2
		
		int[][] b = {{2,4,8,16},{4,2,4,64},{2,8,2,4},{16,4,8,2}};
		GameBoard.init(b);
		GameBoard.checkGameOver();
		//status should be false since game is over
		boolean s = GameBoard.getStatus();
		assertFalse(s); 
	}
	
	@Test
	// Board is full with a possible merge -- game not over
	public void testGameOverBoardFull2() 
	{
		// 2 -4-8-8
		// 4 -2-4-64
		// 2 -8-2-4
		// 16-4-8-2
		
		int[][] b = {{2,4,8,8},{4,2,4,64},{2,8,2,4},{16,4,8,2}};
		GameBoard.init(b);
		GameBoard.checkGameOver();
		//status should be true since game is not over
		boolean s = GameBoard.getStatus();
		assertTrue(s); 
	}
	
	@Test
	// A 2048 tile being created
	public void testReached2048() 
	{
		// 1024 -0-0-0
		// 1024 -0-0-0
		// 0    -0-0-0
		// 0    -0-0-0
		
		int[][] b = {{1024,0,0,0},{1024,0,0,0},{0,0,0,0},{0,0,0,0}};
		GameBoard.init(b);
		
		// board does not have 2048 initially
		assertFalse(GameBoard.has2048());
		
		// shift up to create 2048
		GameBoard.shiftUp();
		GameBoard.checkGameOver();
		
		// board should recognize that there is now a 2048 tile,
		// and game should end
		assertTrue(GameBoard.has2048() && !GameBoard.getStatus());
	}

	@Test
	// A 2048 tile being created
	public void testSwapCells() 
	{
		// 0-8-0-0      8-0-0-0
		// 0-0-0-0  ->  0-0-0-0
		// 0-0-0-0      0-0-0-0
		// 0-0-0-0      0-0-0-0
		
		int[][] b = {{0,8,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,0}};
		GameBoard.init(b);
		
		GameBoard.swapCells(0,1,0,-1);
		
		// get the new board
		int[][] gameB = GameBoard.getBoard();
		int[][] expB = {{8,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,0}};
		
		assertTrue(Arrays.deepEquals(expB, gameB));
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void testSwapCellsOutOfBounds() 
	{
		// 8-0-0-0
		// 0-0-0-0
		// 0-0-0-0
		// 0-0-0-0      

		int[][] b = {{8,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,0}};
		GameBoard.init(b);

		// Swapping the 8 to the left should not be possible:
		// causes index to be out of bounds
		GameBoard.swapCells(0,0,0,-1);
	}

	@Test
	// Add a random tile to a board
	public void testAddRandomTile() 
	{
		// Initial board
		// 0-2-8-4
		// 2-0-0-0
		// 0-8-4-0
		// 4-2-0-4

		int[][] b = {{0,2,8,4},{2,0,0,0},{0,8,4,0},{4,2,0,4}};
		GameBoard.init(b);
		
		GameBoard.addRandomTile();

		// check that exactly one empty cell became a tile with value 2 or 4
		
		// get the new board
		int[][] gameB = GameBoard.getBoard();
		int c = 0; // counter

		for (int i=0; i<4; i++) {
			for (int j=0; j<4; j++) {
				if (b[i][j] == 0 && gameB[i][j] != 0) {
					c++;
					assertTrue(gameB[i][j] == 2 || gameB[i][j] == 4);
				}
			}
		}
		// make sure only one tile changed
		assertTrue(c == 1);
	}


}
