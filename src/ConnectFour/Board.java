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

import java.awt.*;
/**
 * The Board class models the ROWS-by-COLS game board.
 */
public class Board {
    // Define named constants
    public static final int ROWS = 6;   // 6 baris untuk Connect4
    public static final int COLS = 7;   // 7 kolom untuk Connect4
    public Cell[][] cells;// Papan permainan
    private Seed[][] board;


    // Define named constants for drawing
    public static final int CANVAS_WIDTH = Cell.SIZE * COLS;  // the drawing canvas
    public static final int CANVAS_HEIGHT = Cell.SIZE * ROWS;
    public static final int GRID_WIDTH = 8;  // Grid-line's width
    public static final int GRID_WIDTH_HALF = GRID_WIDTH / 2; // Grid-line's half-width
    public static final Color COLOR_GRID = Color.LIGHT_GRAY;  // grid lines
    public static final int Y_OFFSET = 1;  // Fine tune for better display

    // Define properties (package-visible)
    /** Composes of 2D array of ROWS-by-COLS Cell instances */


    /** Constructor to initialize the game board */
    public Board() {
        initGame();
    }

    public boolean isValidMove(int col) {
        return col >= 0 && col < COLS && board[0][col] == Seed.NO_SEED;
    }

    public void init() {
        initGame();
    }


    public void makeMove(int col, Seed seed) {
        for (int row = ROWS - 1; row >= 0; row--) {
            if (board[row][col] == Seed.NO_SEED) {
                board[row][col] = seed;
                break;
            }
        }
    }

    public boolean hasWon(Seed seed) {
        // Check for 4-in-a-row logic here (horizontal, vertical, diagonal)
        // Simplified example: return false for now
        return false;
    }


    /** Initialize the game objects (run once) */
    public void initGame() {
        cells = new Cell[ROWS][COLS];
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                cells[row][col] = new Cell(row, col);
            }
        }
    }
    /**
     *  The given player makes a move on (selectedRow, selectedCol).
     *  Update cells[selectedRow][selectedCol]. Compute and return the
     *  new game state (PLAYING, DRAW, CROSS_WON, NOUGHT_WON).
     */
    public State stepGame(Seed seed, int row, int col) {
        cells[row][col].content = seed; // Isi sel dengan simbol pemain

        // Tambahkan poin jika ada empat simbol berjajar
        int points = checkFourInARow(seed, row, col);
        if (points > 0) {
            if (seed == Seed.CROSS) {
                GameMain.player1.addScore(points); // Tambah skor ke pemain 1
                if (GameMain.player1.hasWon()) return State.CROSS_WON;
            } else if (seed == Seed.NOUGHT) {
                GameMain.player2.addScore(points); // Tambah skor ke pemain 2
                if (GameMain.player2.hasWon()) return State.NOUGHT_WON;
            }
        }

        // Periksa jika papan penuh (draw)
        if (isDraw()) {
            return State.DRAW;
        }

        return State.PLAYING; // Jika tidak ada yang menang, lanjutkan permainan
    }

    public int checkFourInARow(Seed seed, int row, int col) {
        int points = 0;

        // Periksa semua arah (horizontal, vertikal, diagonal, anti-diagonal)
        if (countConsecutive(seed, row, col, 0, 1) >= 4) points++; // Horizontal
        if (countConsecutive(seed, row, col, 1, 0) >= 4) points++; // Vertikal
        if (countConsecutive(seed, row, col, 1, 1) >= 4) points++; // Diagonal
        if (countConsecutive(seed, row, col, 1, -1) >= 4) points++; // Anti-diagonal

        return points;
    }

    private int countConsecutive(Seed seed, int row, int col, int deltaRow, int deltaCol) {
        int count = 0;
        int r = row, c = col;

        // Hitung ke depan
        while (r >= 0 && r < ROWS && c >= 0 && c < COLS && cells[r][c].content == seed) {
            count++;
            r += deltaRow;
            c += deltaCol;
        }

        // Hitung ke belakang
        r = row - deltaRow;
        c = col - deltaCol;
        while (r >= 0 && r < ROWS && c >= 0 && c < COLS && cells[r][c].content == seed) {
            count++;
            r -= deltaRow;
            c -= deltaCol;
        }

        return count;
    }
    public boolean isDraw() {
        // Periksa apakah semua sel sudah terisi
        for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLS; ++col) {
                if (cells[row][col].content == Seed.NO_SEED) {
                    return false; // Masih ada sel kosong, belum seri
                }
            }
        }

        // Periksa apakah tidak ada pemain yang menang
        if (GameMain.player1.getScore() < 3 && GameMain.player2.getScore() < 3) {
            return true; // Semua sel terisi, tapi tidak ada yang menang
        }

        return false; // Seri tidak terjadi jika ada pemenang
    }


    public int getColFromX(int x) {
        return x / (800 / COLS); // Assuming board width is 800px
    }


    /** Paint itself on the graphics canvas, given the Graphics context */
    public void paint(Graphics g) {
        // Draw the grid-lines
        g.setColor(COLOR_GRID);
        for (int row = 1; row < ROWS; ++row) {
            g.fillRoundRect(0, Cell.SIZE * row - GRID_WIDTH_HALF,
                    CANVAS_WIDTH - 1, GRID_WIDTH,
                    GRID_WIDTH, GRID_WIDTH);
        }
        for (int col = 1; col < COLS; ++col) {
            g.fillRoundRect(Cell.SIZE * col - GRID_WIDTH_HALF, 0 + Y_OFFSET,
                    GRID_WIDTH, CANVAS_HEIGHT - 1,
                    GRID_WIDTH, GRID_WIDTH);
        }

        // Draw all the cells
        for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLS; ++col) {
                cells[row][col].paint(g);  // ask the cell to paint itself
            }
        }
    }
}