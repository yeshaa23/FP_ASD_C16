/**
 * ES234317-Algorithm and Data Structures
 * Semester Ganjil, 2024/2025
 * Group Capstone Project
 * Group #16
 * 1 - 5026231038 - Nabila Shinta Luthfia
 * 2 - 5026231125 - Ayesha Hana Azkiya
 * 3 - 5026231139 - Amandea Chandiki Larasati
 */
package ConnectFour;

/**
 * Abstract base class for AI players.
 * Provides basic properties and methods for AI players to interact with the game board.
 */
public abstract class AIPlayer {
    protected int ROWS = Board.ROWS; // Number of rows on the board
    protected int COLS = Board.COLS; // Number of columns on the board
    protected Cell[][] cells;       // Reference to the game board cells
    protected Seed mySeed;          // AI's seed (CROSS or NOUGHT)
    protected Seed oppSeed;         // Opponent's seed

    /**
     * Constructor for AIPlayer.
     * Initializes the AIPlayer with a reference to the game board.
     * @param board The game board.
     */
    public AIPlayer(Board board) {
        if (board == null) {
            throw new IllegalArgumentException("Board cannot be null");
        }
        this.cells = board.cells; // Pastikan cells berasal dari board yang valid
    }

    /**
     * Sets the seed for this AI player and determines the opponent's seed.
     * @param seed The seed for this AI player.
     */
    public void setSeed(Seed seed) {
        if (seed == null) {
            throw new IllegalArgumentException("Seed cannot be null");
        }
        this.mySeed = seed;
        oppSeed = (mySeed == Seed.CROSS) ? Seed.NOUGHT : Seed.CROSS;
    }

    /**
     * Abstract method for determining the AI's move.
     * To be implemented by subclasses.
     * @return An array of two integers {row, col} representing the move.
     */
    abstract int[] move();
}


