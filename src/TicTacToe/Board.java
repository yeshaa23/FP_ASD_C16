/**
 * ES234317-Algorithm and Data Structures
 * Semester Ganjil, 2024/2025
 * Group Capstone Project
 * Group #16
 * 1 - 5026231038 - Nabila Shinta Luthfia
 * 2 - 5026231125 - Ayesha Hana Azkiya
 * 3 - 5026231139 - Amandea Chandiki Larasati
 */
package TicTacToe;

import TicTacToe.State;

public class Board {  // save as "Board.java"
    // Define named constants for the grid
    public static final int ROWS = 3;
    public static final int COLS = 3;

    // Define properties (package-visible)
    /** A board composes of [ROWS]x[COLS] Cell instances */
    Cell[][] cells;

    /** Constructor to initialize the game board */
    public Board() {
        initGame();
    }

    /** Initialize the board (run once) */
    public void initGame() {
        cells = new Cell[ROWS][COLS];  // allocate the array
        for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLS; ++col) {
                // Allocate element of the array
                cells[row][col] = new Cell(row, col);
            }
        }
    }

    /** Reset the contents of the game board, ready for new game. */
    public void newGame() {
        for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLS; ++col) {
                cells[row][col].newGame();  // The cells init itself
            }
        }
    }

    /**
     *  The given player makes a move on (selectedRow, selectedCol).
     *  Update cells[selectedRow][selectedCol]. Compute and return the
     *  new game state (PLAYING, DRAW, CROSS_WON, NOUGHT_WON).
     */
    public State stepGame(Seed player, int selectedRow, int selectedCol) {
        // Update game board
        cells[selectedRow][selectedCol].content = player;

        // Compute and return the new game state
        if (checkWin(player, selectedRow, selectedCol)) {
            return (player == Seed.CROSS) ? State.CROSS_WON : State.NOUGHT_WON;
        } else {
            // Nobody wins. Check for DRAW (all cells occupied) or PLAYING.
            for (int row = 0; row < ROWS; ++row) {
                for (int col = 0; col < COLS; ++col) {
                    if (cells[row][col].content == Seed.NO_SEED) {
                        return State.PLAYING; // still have empty cells
                    }
                }
            }
            return State.DRAW; // no empty cell, it's a draw
        }
    }

    /** Helper method to check if the current move results in a win */
    private boolean checkWin(Seed player, int selectedRow, int selectedCol) {
        // Check row, column, and both diagonals
        return checkLine(player, selectedRow, 0, 0, 1) // Check row
                || checkLine(player, 0, selectedCol, 1, 0) // Check column
                || checkLine(player, 0, 0, 1, 1)           // Check diagonal
                || checkLine(player, 0, COLS - 1, 1, -1);  // Check anti-diagonal
    }

    /** Helper method to check a line for a win */
    private boolean checkLine(Seed player, int startRow, int startCol, int deltaRow, int deltaCol) {
        int count = 0;
        for (int i = 0; i < Math.max(ROWS, COLS); i++) {
            int row = startRow + i * deltaRow;
            int col = startCol + i * deltaCol;
            if (row >= 0 && row < ROWS && col >= 0 && col < COLS && cells[row][col].content == player) {
                count++;
                if (count == 3) { // Win condition (adjustable for other grid sizes)
                    return true;
                }
            } else {
                count = 0; // Reset count if sequence breaks
            }
        }
        return false;
    }

    /** AI move using Minimax algorithm */
    public int[] getBestMove(Seed aiPlayer) {
        int bestScore = Integer.MIN_VALUE;
        int[] bestMove = {-1, -1};

        for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLS; ++col) {
                if (cells[row][col].content == Seed.NO_SEED) {
                    // Try the move
                    cells[row][col].content = aiPlayer;
                    int score = minimax(0, false, aiPlayer);
                    cells[row][col].content = Seed.NO_SEED; // Undo the move

                    if (score > bestScore) {
                        bestScore = score;
                        bestMove = new int[]{row, col};
                    }
                }
            }
        }
        return bestMove;
    }

    /** Minimax algorithm */
    private int minimax(int depth, boolean isMaximizing, Seed aiPlayer) {
        State state = evaluateState(aiPlayer);
        if (state == State.CROSS_WON) return (aiPlayer == Seed.CROSS ? 10 - depth : depth - 10);
        if (state == State.NOUGHT_WON) return (aiPlayer == Seed.NOUGHT ? 10 - depth : depth - 10);
        if (state == State.DRAW) return 0;

        int bestScore = isMaximizing ? Integer.MIN_VALUE : Integer.MAX_VALUE;

        for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLS; ++col) {
                if (cells[row][col].content == Seed.NO_SEED) {
                    cells[row][col].content = isMaximizing ? aiPlayer : (aiPlayer == Seed.CROSS ? Seed.NOUGHT : Seed.CROSS);
                    int score = minimax(depth + 1, !isMaximizing, aiPlayer);
                    cells[row][col].content = Seed.NO_SEED;

                    bestScore = isMaximizing ? Math.max(bestScore, score) : Math.min(bestScore, score);
                }
            }
        }
        return bestScore;
    }

    /** Evaluate the current state of the board */
    private State evaluateState(Seed aiPlayer) {
        for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLS; ++col) {
                if (cells[row][col].content != Seed.NO_SEED) {
                    if (checkWin(cells[row][col].content, row, col)) {
                        return (cells[row][col].content == Seed.CROSS) ? State.CROSS_WON : State.NOUGHT_WON;
                    }
                }
            }
        }
        for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLS; ++col) {
                if (cells[row][col].content == Seed.NO_SEED) {
                    return State.PLAYING;
                }
            }
        }
        return State.DRAW;
    }

    /** Return the board's current state as a Seed[][] array */
    public Seed[][] getBoard() {
        Seed[][] boardState = new Seed[ROWS][COLS];
        for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLS; ++col) {
                boardState[row][col] = cells[row][col].content;
            }
        }
        return boardState;
    }

    /** The board paints itself */
    public void paint() {
        for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLS; ++col) {
                System.out.print(" ");
                cells[row][col].paint();   // each cell paints itself
                System.out.print(" ");
                if (col < COLS - 1) System.out.print("|");  // column separator
            }
            System.out.println();
            if (row < ROWS - 1) {
                System.out.println("-----------");  // row separator
            }
        }
        System.out.println();
    }
}
