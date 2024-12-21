package Sudoku;

import java.awt.*;
import javax.swing.*;

public class Sudoku extends JFrame {
    private static final long serialVersionUID = 1L;
    private boolean isPaused = false;
    private boolean isDarkMode = false;
    private CountdownTimer countdownTimer; // Timer countdown
    private JLabel countdownTimerLabel;
    private GameBoardPanel board;
    private JLabel timerLabel;
    private JLabel scoreLabel;
    private JButton darkModeButton;

    public Sudoku() {
        // Tampilkan dialog selamat datang
        showWelcomeDialog();

        // Dialog tingkat kesulitan hanya sekali setelah welcome dialog
        String difficulty = showDifficultyDialog();

        // Setup papan permainan dengan tingkat kesulitan
        setupGameBoard(difficulty);
    }

    private void setupGameBoard(String difficulty) {
        // Inisialisasi papan permainan
        Container cp = getContentPane();
        cp.setLayout(new BorderLayout());

        board = new GameBoardPanel(); // Membuat papan permainan
        board.newGame(difficulty); // Mulai permainan dengan tingkat kesulitan yang dipilih
        cp.add(board, BorderLayout.CENTER);

        JButton btnNewGame = new JButton("New Game");
        btnNewGame.addActionListener(e -> resetGame());
        cp.add(btnNewGame, BorderLayout.SOUTH);

        JPanel sidePanel = new JPanel();
        sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));
        JButton hintButton = new JButton("Hint");
        JButton pauseButton = new JButton("Pause");
        sidePanel.add(hintButton);
        sidePanel.add(pauseButton);

        darkModeButton = new JButton("Dark Mode");
        darkModeButton.addActionListener(e -> toggleDarkMode());
        sidePanel.add(darkModeButton);

        scoreLabel = new JLabel("Score: 0");
        sidePanel.add(scoreLabel);

        cp.add(sidePanel, BorderLayout.EAST);

        timerLabel = new JLabel("Time: 0", JLabel.CENTER);
        timerLabel.setFont(new Font("Arial", Font.BOLD, 20));
        cp.add(timerLabel, BorderLayout.NORTH);

        countdownTimerLabel = new JLabel("Time Left: 0", JLabel.CENTER);
        countdownTimerLabel.setFont(new Font("Arial", Font.BOLD, 20));
        cp.add(countdownTimerLabel, BorderLayout.NORTH);

        countdownTimer = new CountdownTimer(countdownTimerLabel, this);
        countdownTimer.setTime(difficulty);
        countdownTimer.startTimer();

        board.setScoreLabel(scoreLabel);

        hintButton.addActionListener(e -> {
            if (!isPaused) {
                String hintMessage = board.getHint();
                JOptionPane.showMessageDialog(this, hintMessage, "Hint", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Game is paused!", "Hint", JOptionPane.WARNING_MESSAGE);
            }
        });

        pauseButton.addActionListener(e -> togglePause(pauseButton));

        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Sudoku");
        setVisible(true);
    }

    private void togglePause(JButton pauseButton) {
        if (!isPaused) {
            countdownTimer.stopTimer();
            disableGameBoard(true);
            board.setPaused(true);
            pauseButton.setText("Resume");
            isPaused = true;
        } else {
            countdownTimer.startTimer();
            disableGameBoard(false);
            board.setPaused(false);
            pauseButton.setText("Pause");
            isPaused = false;
        }
    }

    private void toggleDarkMode() {
        isDarkMode = !isDarkMode;
        applyTheme(getContentPane());
        board.applyTheme(isDarkMode);
        darkModeButton.setText(isDarkMode ? "Light Mode" : "Dark Mode");
        repaint();
    }

    private void applyTheme(Component component) {
        Color background;
        Color foreground;

        if (isDarkMode) {
            background = Color.BLACK;
            foreground = Color.WHITE;
        } else {
            background = new Color(238, 196, 255);
            foreground = Color.BLACK;
        }

        component.setBackground(background);
        component.setForeground(foreground);

        if (component instanceof Container) {
            for (Component child : ((Container) component).getComponents()) {
                applyTheme(child);
            }
        }
    }

    private void showWelcomeDialog() {
        JDialog welcomeDialog = new JDialog(this, "Welcome to Sudoku!", true);
        welcomeDialog.setLayout(new BorderLayout());
        welcomeDialog.setSize(400, 200);
        welcomeDialog.setLocationRelativeTo(null);

        JLabel message = new JLabel("Welcome to Sudoku! Get ready to play!", JLabel.CENTER);
        JButton btnStart = new JButton("Start Game");
        btnStart.addActionListener(e -> welcomeDialog.dispose());

        welcomeDialog.add(message, BorderLayout.CENTER);
        welcomeDialog.add(btnStart, BorderLayout.SOUTH);
        welcomeDialog.setVisible(true);
    }

    public String showDifficultyDialog() {
        String[] options = {"Easy", "Medium", "Hard"};
        int choice = JOptionPane.showOptionDialog(
                this,
                "Select Difficulty Level:",
                "New Game Difficulty",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
        );

        if (choice >= 0) {
            return options[choice].toLowerCase();
        } else {
            return "easy";
        }
    }

    private void resetGame() {
        String difficulty = showDifficultyDialog();
        board.newGame(difficulty);
        countdownTimer.setTime(difficulty);
        countdownTimer.startTimer();
    }

    void disableGameBoard(boolean disable) {
        for (int row = 0; row < SudokuConstants.GRID_SIZE; row++) {
            for (int col = 0; col < SudokuConstants.GRID_SIZE; col++) {
                Cell cell = board.getCell(row, col);
                if (cell != null) {
                    cell.setEnabled(!disable);
                }
            }
        }
    }
}
