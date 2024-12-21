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

import javax.swing.JOptionPane;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ConnectFour {

    public enum Seed {
        EMPTY(0), PLAYER1(1), PLAYER2(2);

        private final int value;

        Seed(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public static final int ROWS = 6;
    public static final int COLS = 7;
    public static final Seed AI_PLAYER = Seed.PLAYER1;
    public static final Seed HUMAN_PLAYER = Seed.PLAYER2;
    public static final Seed EMPTY = Seed.EMPTY;
    public static final int MAX_DEPTH = 5;

    private Seed[][] board;
    private Player humanPlayer;
    private Player aiPlayer;

    // Constructor
    public ConnectFour() {
        this.board = new Seed[ROWS][COLS];
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                board[row][col] = EMPTY;
            }
        }
    }

    public ConnectFour(String humanName, String aiName) {
        this.board = new Seed[ROWS][COLS];
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                board[row][col] = EMPTY;
            }
        }
        this.humanPlayer = new Player(humanName);
        this.aiPlayer = new Player(aiName);
    }

    public Seed[][] getBoard() {
        return board;
    }

    // Method to play the game
    public void playGame(String humanName, String aiName) {
        this.humanPlayer = new Player(humanName);
        this.aiPlayer = new Player(aiName);

        boolean isAITurn = false;

        while (true) {
            printBoard();

            if (isWinningMove(board, HUMAN_PLAYER)) {
                System.out.println(humanPlayer.getName() + " wins!");
                humanPlayer.addScore(1);
                break;
            } else if (isWinningMove(board, AI_PLAYER)) {
                System.out.println(aiPlayer.getName() + " wins!");
                aiPlayer.addScore(1);
                break;
            } else if (isDraw()) {
                System.out.println("It's a draw!");
                break;
            }

            if (isAITurn) {
                int bestMove = getBestMove();
                makeMove(bestMove, AI_PLAYER);
                System.out.println(aiPlayer.getName() + " chooses column: " + bestMove);
            } else {
                int col;
                do {
                    System.out.print(humanPlayer.getName() + ", enter your move (0-6): ");
                    col = new Scanner(System.in).nextInt();
                } while (!isValidMove(col));
                makeMove(col, HUMAN_PLAYER);
            }

            isAITurn = !isAITurn;
        }

        System.out.println("Scoreboard:");
        System.out.println(humanPlayer.getName() + ": " + humanPlayer.getScore());
        System.out.println(aiPlayer.getName() + ": " + aiPlayer.getScore());
    }

    private void printBoard() {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                System.out.print(board[row][col].getValue() + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    public boolean isDraw() {
        for (int col = 0; col < COLS; col++) {
            if (board[0][col] == EMPTY) {
                return false;
            }
        }
        return true;
    }

    public void makeMove(int col, Seed player) {
        for (int row = ROWS - 1; row >= 0; row--) {
            if (board[row][col] == EMPTY) {
                board[row][col] = player;
                break;
            }
        }
    }

    public boolean isValidMove(int col) {
        return col >= 0 && col < COLS && board[0][col] == EMPTY;
    }

    private List<Integer> validMoves() {
        List<Integer> moves = new ArrayList<>();
        for (int col = 0; col < COLS; col++) {
            if (isValidMove(col)) {
                moves.add(col);
            }
        }
        return moves;
    }

    // Menambahkan metode getCellSeed untuk mendapatkan status di suatu cell pada papan
    public Seed getCellSeed(int row, int col) {
        if (row >= 0 && row < ROWS && col >= 0 && col < COLS) {
            return board[row][col];
        }
        return null; // Mengembalikan null jika indeks di luar batas papan
    }

    private Seed[][] simulateMove(Seed[][] board, int col, Seed player) {
        Seed[][] newBoard = new Seed[ROWS][COLS];
        for (int row = 0; row < ROWS; row++) {
            System.arraycopy(board[row], 0, newBoard[row], 0, COLS);
        }
        for (int row = ROWS - 1; row >= 0; row--) {
            if (newBoard[row][col] == EMPTY) {
                newBoard[row][col] = player;
                break;
            }
        }
        return newBoard;
    }

    public int getBestMove() {
        int bestScore = Integer.MIN_VALUE;
        int bestMove = -1;

        for (int col : validMoves()) {
            Seed[][] newBoard = simulateMove(board, col, AI_PLAYER);
            int score = minimax(newBoard, MAX_DEPTH, false, Integer.MIN_VALUE, Integer.MAX_VALUE);
            if (score > bestScore) {
                bestScore = score;
                bestMove = col;
            }
        }
        return bestMove;
    }

    private int minimax(Seed[][] board, int depth, boolean isMaximizingPlayer, int alpha, int beta) {
        if (isWinningMove(board, AI_PLAYER)) return 1000;
        if (isWinningMove(board, HUMAN_PLAYER)) return -1000;
        if (isDraw() || depth == 0) return evaluateBoard(board);

        if (isMaximizingPlayer) {
            int maxEval = Integer.MIN_VALUE;
            for (int col : validMoves()) {
                Seed[][] newBoard = simulateMove(board, col, AI_PLAYER);
                int eval = minimax(newBoard, depth - 1, false, alpha, beta);
                maxEval = Math.max(maxEval, eval);
                alpha = Math.max(alpha, eval);
                if (beta <= alpha) break;
            }
            return maxEval;
        } else {
            int minEval = Integer.MAX_VALUE;
            for (int col : validMoves()) {
                Seed[][] newBoard = simulateMove(board, col, HUMAN_PLAYER);
                int eval = minimax(newBoard, depth - 1, true, alpha, beta);
                minEval = Math.min(minEval, eval);
                beta = Math.min(beta, eval);
                if (beta <= alpha) break;
            }
            return minEval;
        }
    }

    public boolean isWinningMove(Seed[][] board, Seed player) {
        // Check horizontal
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS - 3; col++) {
                if (board[row][col] == player &&
                        board[row][col + 1] == player &&
                        board[row][col + 2] == player &&
                        board[row][col + 3] == player) {
                    return true;
                }
            }
        }

        // Check vertical
        for (int row = 0; row < ROWS - 3; row++) {
            for (int col = 0; col < COLS; col++) {
                if (board[row][col] == player &&
                        board[row + 1][col] == player &&
                        board[row + 2][col] == player &&
                        board[row + 3][col] == player) {
                    return true;
                }
            }
        }

        // Check diagonal (positive slope)
        for (int row = 0; row < ROWS - 3; row++) {
            for (int col = 0; col < COLS - 3; col++) {
                if (board[row][col] == player &&
                        board[row + 1][col + 1] == player &&
                        board[row + 2][col + 2] == player &&
                        board[row + 3][col + 3] == player) {
                    return true;
                }
            }
        }

        // Check diagonal (negative slope)
        for (int row = 3; row < ROWS; row++) {
            for (int col = 0; col < COLS - 3; col++) {
                if (board[row][col] == player &&
                        board[row - 1][col + 1] == player &&
                        board[row - 2][col + 2] == player &&
                        board[row - 3][col + 3] == player) {
                    return true;
                }
            }
        }

        return false;
    }

    private int evaluateBoard(Seed[][] board) {
        int score = 0;

        for (int row = 0; row < ROWS; row++) {
            if (board[row][COLS / 2] == AI_PLAYER) {
                score += 3;
            } else if (board[row][COLS / 2] == HUMAN_PLAYER) {
                score -= 3;
            }
        }

        score += evaluateLines(board, AI_PLAYER);
        score -= evaluateLines(board, HUMAN_PLAYER);

        return score;
    }

    private int evaluateLines(Seed[][] board, Seed player) {
        int score = 0;

        // Evaluate rows
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS - 3; col++) {
                score += evaluateWindow(board[row][col], board[row][col + 1], board[row][col + 2], board[row][col + 3], player);
            }
        }

        // Evaluate columns
        for (int row = 0; row < ROWS - 3; row++) {
            for (int col = 0; col < COLS; col++) {
                score += evaluateWindow(board[row][col], board[row + 1][col], board[row + 2][col], board[row + 3][col], player);
            }
        }

        // Evaluate diagonals (positive slope)
        for (int row = 0; row < ROWS - 3; row++) {
            for (int col = 0; col < COLS - 3; col++) {
                score += evaluateWindow(board[row][col], board[row + 1][col + 1], board[row + 2][col + 2], board[row + 3][col + 3], player);
            }
        }

        // Evaluate diagonals (negative slope)
        for (int row = 3; row < ROWS; row++) {
            for (int col = 0; col < COLS - 3; col++) {
                score += evaluateWindow(board[row][col], board[row - 1][col + 1], board[row - 2][col + 2], board[row - 3][col + 3], player);
            }
        }

        return score;
    }

    private int evaluateWindow(Seed slot1, Seed slot2, Seed slot3, Seed slot4, Seed player) {
        int score = 0;
        Seed opponent = (player == AI_PLAYER) ? HUMAN_PLAYER : AI_PLAYER;

        int countPlayer = 0;
        int countOpponent = 0;
        int countEmpty = 0;

        Seed[] slots = {slot1, slot2, slot3, slot4};
        for (Seed slot : slots) {
            if (slot == player) countPlayer++;
            else if (slot == opponent) countOpponent++;
            else countEmpty++;
        }

        if (countPlayer == 4) score += 100;
        else if (countPlayer == 3 && countEmpty == 1) score += 5;
        else if (countPlayer == 2 && countEmpty == 2) score += 2;

        if (countOpponent == 4) score -= 100;
        else if (countOpponent == 3 && countEmpty == 1) score -= 4;
        else if (countOpponent == 2 && countEmpty == 2) score -= 1;

        return score;
    }

    public static void main(String[] args) {
        JOptionPane.showMessageDialog(null, "Welcome to Connect Four Game!\nChoose your difficulty level:");

        // Difficulty selection
        String[] difficultyLevels = {"Easy", "Medium", "Hard"};
        String selectedDifficulty = (String) JOptionPane.showInputDialog(null, "Select Difficulty",
                "Difficulty Level", JOptionPane.QUESTION_MESSAGE, null, difficultyLevels, difficultyLevels[1]);

        String humanName = JOptionPane.showInputDialog("Enter your name:");
        String aiName = "AI Player"; // AI is controlled by the system

        ConnectFour game = new ConnectFour(humanName, aiName);
        game.playGame(humanName, aiName);
    }
}
