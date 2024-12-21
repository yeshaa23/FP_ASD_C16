/**
 * ES234317-Algorithm and Data Structures
 * Semester Ganjil, 2024/2025
 * Group Capstone Project
 * Group #16
 * 1 - 5026231038 - Nabila Shinta Luthfia
 * 2 - 5026231125 - Ayesha Hana Azkiya
 * 3 - 5026231139 - Amandea Chandiki Larasati
 */
package Sudoku;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class GameBoardPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private static final int MAX_HINTS = 5; // Maksimum penggunaan hint
    private int hintsUsed = 0; // Melacak jumlah hint yang telah digunakan
    private int score = 0; // Skor permainan
    private JLabel scoreLabel;
    private boolean isDark = false;

    // Konstanta untuk UI
    public static final int CELL_SIZE = 60;
    public static final int BOARD_WIDTH = CELL_SIZE * SudokuConstants.GRID_SIZE;
    public static final int BOARD_HEIGHT = CELL_SIZE * SudokuConstants.GRID_SIZE;

    // Properti
    private final Cell[][] cells = new Cell[SudokuConstants.GRID_SIZE][SudokuConstants.GRID_SIZE];
    private final Puzzle puzzle = new Puzzle();
    private boolean isPaused = false;

    public void setPaused(boolean isPaused) {
        this.isPaused = isPaused;
    }

    // Tambahkan metode untuk mendapatkan jumlah hint yang tersisa
    public int getHintsRemaining() {
        return MAX_HINTS - hintsUsed;
    }

    // Tambahkan metode untuk mendapatkan sebuah sel
    public Cell getCell(int row, int col) {
        if (row >= 0 && row < SudokuConstants.GRID_SIZE && col >= 0 && col < SudokuConstants.GRID_SIZE) {
            return cells[row][col];
        } else {
            throw new IllegalArgumentException("Invalid cell position: (" + row + ", " + col + ")");
        }
    }

    /**
     * Constructor
     */
    public GameBoardPanel() {
        super.setLayout(new BorderLayout());

        // Membuat grid untuk sel
        JPanel cellPanel = new JPanel(new GridLayout(SudokuConstants.GRID_SIZE, SudokuConstants.GRID_SIZE));
        for (int row = 0; row < SudokuConstants.GRID_SIZE; ++row) {
            for (int col = 0; col < SudokuConstants.GRID_SIZE; ++col) {
                cells[row][col] = new Cell(row, col);
                cellPanel.add(cells[row][col]);
            }
        }
        super.add(cellPanel, BorderLayout.CENTER);

        // Tambahkan listener untuk sel yang dapat diedit
        CellInputListener listener = new CellInputListener();
        for (int row = 0; row < SudokuConstants.GRID_SIZE; ++row) {
            for (int col = 0; col < SudokuConstants.GRID_SIZE; ++col) {
                if (cells[row][col].isEditable()) {
                    cells[row][col].addActionListener(listener);
                }
            }
        }

        super.setPreferredSize(new Dimension(BOARD_WIDTH, BOARD_HEIGHT));
    }

    public void setScoreLabel(JLabel scoreLabel) {
        this.scoreLabel = scoreLabel;
    }

    private void updateScore(int delta) {
        score += delta;
        if (scoreLabel != null) {
            scoreLabel.setText("Score: " + score);
            scoreLabel.revalidate();
            scoreLabel.repaint();
        }
    }

    public void newGame(String difficulty) {
        puzzle.newPuzzle(difficulty);
        for (int row = 0; row < SudokuConstants.GRID_SIZE; ++row) {
            for (int col = 0; col < SudokuConstants.GRID_SIZE; ++col) {
                cells[row][col].newGame(puzzle.numbers[row][col], puzzle.isGiven[row][col]);
            }
        }
        score = 0;
        hintsUsed = 0; // Reset jumlah hint
        updateScore(0);
    }

    // Metode untuk memberikan hint
    public String getHint() {
        if (hintsUsed >= MAX_HINTS) {
            return "You have used all 5 hints!";
        }

        for (int row = 0; row < SudokuConstants.GRID_SIZE; ++row) {
            for (int col = 0; col < SudokuConstants.GRID_SIZE; ++col) {
                Cell cell = cells[row][col];

                // Jika sel kosong dan tidak diberikan sebelumnya
                if (cell.getText().isEmpty() && !puzzle.isGiven[row][col]) {
                    int correctNumber = puzzle.numbers[row][col]; // Angka yang benar
                    cell.setForeground(Color.GRAY); // Warna angka samar
                    cell.setText(String.valueOf(correctNumber));
                    cell.setEditable(false); // Hint tidak boleh diubah

                    hintsUsed++;
                    updateScore(-2); // Kurangi skor untuk setiap hint
                    return "Hint added at (" + row + ", " + col + "). Remaining hints: " + getHintsRemaining();
                }
            }
        }

        return "No empty cells available for hint!";
    }

    public void resetGame() {
        for (int row = 0; row < SudokuConstants.GRID_SIZE; ++row) {
            for (int col = 0; col < SudokuConstants.GRID_SIZE; ++col) {
                if (!puzzle.isGiven[row][col]) {
                    cells[row][col].setText("");
                    cells[row][col].status = CellStatus.TO_GUESS;
                    cells[row][col].paint();
                }
            }
        }
        score = 0;
        updateScore(0);
    }

    public boolean isSolved() {
        for (int row = 0; row < SudokuConstants.GRID_SIZE; ++row) {
            for (int col = 0; col < SudokuConstants.GRID_SIZE; ++col) {
                if (cells[row][col].status == CellStatus.TO_GUESS || cells[row][col].status == CellStatus.WRONG_GUESS) {
                    return false;
                }
            }
        }

        // Tampilkan dialog ketika game selesai
        SwingUtilities.invokeLater(() -> showGameCompleteDialog());
        return true;
    }

    private void showGameCompleteDialog() {
        String[] options = {"Restart", "New Game", "Main Menu"};
        int choice = JOptionPane.showOptionDialog(
                this,
                "Congratulations! You have completed the puzzle. What would you like to do next?",
                "Game Completed",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[0]
        );

        switch (choice) {
            case 0: // Restart
                resetGame();
                break;
            case 1: // New Game
                String difficulty = JOptionPane.showInputDialog(
                        this,
                        "Enter difficulty (easy, medium, hard):",
                        "New Game Difficulty",
                        JOptionPane.PLAIN_MESSAGE
                );
                if (difficulty != null && !difficulty.isEmpty()) {
                    newGame(difficulty.toLowerCase());
                }
                break;
            case 2: // Main Menu
                SwingUtilities.invokeLater(() -> {
                    JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
                    topFrame.dispose(); // Tutup jendela permainan saat ini
                    new Sudoku(); // Buka kembali main menu
                });
                break;
            default:
                // Tidak ada tindakan
                break;
        }
    }

    public void applyTheme(boolean isDarkMode) {
        for (int row = 0; row < SudokuConstants.GRID_SIZE; row++) {
            for (int col = 0; col < SudokuConstants.GRID_SIZE; col++) {
                Cell cell = getCell(row, col);
                if (cell != null) {
                    cell.setDarkMode(isDarkMode); // Set dark mode for each cell
                }
            }
        }
    }

    public boolean checkAnswer(int row, int col, int numberIn) {
        return cells[row][col].number == numberIn;
    }

    private class CellInputListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Cell sourceCell = (Cell) e.getSource();
            try {
                int numberIn = Integer.parseInt(sourceCell.getText());
                System.out.println("You entered " + numberIn);

                // Cek apakah jawaban benar atau salah
                if (checkAnswer(sourceCell.row, sourceCell.col, numberIn)) {
                    sourceCell.status = CellStatus.CORRECT_GUESS;
                    updateScore(10); // Tambahkan 10 poin jika jawaban benar
                } else {
                    sourceCell.status = CellStatus.WRONG_GUESS;
                    updateScore(-5); // Kurangi 5 poin jika jawaban salah
                }

                sourceCell.paint(); // Perbarui tampilan status sel

                if (isSolved()) {
                    JOptionPane.showMessageDialog(null, "Congratulations! You have solved the puzzle!", "Puzzle Solved", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Invalid input! Please enter a number.", "Error", JOptionPane.ERROR_MESSAGE);
                sourceCell.setText("");
            }
        }
    }
}