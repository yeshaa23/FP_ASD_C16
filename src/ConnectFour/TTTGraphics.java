package ConnectFour;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class TTTGraphics extends JFrame {
    private static final int ROWS = 6;
    private static final int COLS = 7;
    private static final int CELL_SIZE = 100;
    private static final int GRID_WIDTH = 8;
    private static final int GRID_WIDTH_HALF = GRID_WIDTH / 2;
    private static final int CANVAS_WIDTH = CELL_SIZE * COLS;
    private static final int CANVAS_HEIGHT = CELL_SIZE * ROWS;

    private ConnectFour game; // Reference to ConnectFour game
    private final JPanel gamePanel;
    private final JLabel statusBar;

    private Seed currentPlayer; // Current player
    private State currentState; // Current game state
    private String difficultyLevel; // Difficulty level selected by the user
    private String currentTheme;  // Current selected theme

    private Image backgroundImage; // Background image
    private Image humanPion; // Image for human player's pion
    private Image aiPion; // Image for AI player's pion

    public TTTGraphics() {
        // Load background and pion images from resources
        try {
            backgroundImage = new ImageIcon(getClass().getResource("/background.jpg")).getImage();
            humanPion = new ImageIcon(getClass().getResource("/bibble.png")).getImage();
            aiPion = new ImageIcon(getClass().getResource("/Dizzle.png")).getImage();
        } catch (Exception e) {
            System.err.println("Error loading images: " + e.getMessage());
        }

        // Show a welcome pop-up and select difficulty level
        JOptionPane.showMessageDialog(null, "Welcome to Connect Four Game!\nChoose your difficulty level:");

        String[] difficultyLevels = {"Easy", "Medium", "Hard"};
        difficultyLevel = (String) JOptionPane.showInputDialog(null, "Select Difficulty",
                "Difficulty Level", JOptionPane.QUESTION_MESSAGE, null, difficultyLevels, difficultyLevels[1]);

        // Select theme
        String[] themes = {"Classic", "Neon", "Retro", "Cartoon"};
        currentTheme = (String) JOptionPane.showInputDialog(null, "Select Theme",
                "Game Theme", JOptionPane.QUESTION_MESSAGE, null, themes, themes[0]);

        // Initialize ConnectFour game with human and AI names
        game = new ConnectFour("Player 1", "AI (" + difficultyLevel + ")");

        gamePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                // Draw background image
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                }

                // Draw grid
                g.setColor(Color.LIGHT_GRAY);
                for (int row = 1; row < ROWS; row++) {
                    g.fillRoundRect(0, CELL_SIZE * row - GRID_WIDTH_HALF, CANVAS_WIDTH, GRID_WIDTH, GRID_WIDTH, GRID_WIDTH);
                }
                for (int col = 1; col < COLS; col++) {
                    g.fillRoundRect(CELL_SIZE * col - GRID_WIDTH_HALF, 0, GRID_WIDTH, CANVAS_HEIGHT, GRID_WIDTH, GRID_WIDTH);
                }

                // Draw pieces
                for (int row = 0; row < ROWS; row++) {
                    for (int col = 0; col < COLS; col++) {
                        int x1 = col * CELL_SIZE + GRID_WIDTH_HALF;
                        int y1 = row * CELL_SIZE + GRID_WIDTH_HALF;
                        if (game.getBoard()[row][col] == ConnectFour.HUMAN_PLAYER) {
                            if (humanPion != null) {
                                g.drawImage(humanPion, x1, y1, CELL_SIZE - GRID_WIDTH, CELL_SIZE - GRID_WIDTH, this);
                            } else {
                                g.setColor(Color.RED);
                                g.fillOval(x1, y1, CELL_SIZE - GRID_WIDTH, CELL_SIZE - GRID_WIDTH);
                            }
                        } else if (game.getBoard()[row][col] == ConnectFour.AI_PLAYER) {
                            if (aiPion != null) {
                                g.drawImage(aiPion, x1, y1, CELL_SIZE - GRID_WIDTH, CELL_SIZE - GRID_WIDTH, this);
                            } else {
                                g.setColor(Color.BLUE);
                                g.fillOval(x1, y1, CELL_SIZE - GRID_WIDTH, CELL_SIZE - GRID_WIDTH);
                            }
                        }
                    }
                }
            }
        };

        gamePanel.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
        gamePanel.setOpaque(true); // Ensure that the background color is painted
        gamePanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int col = e.getX() / CELL_SIZE;

                if (currentState == State.PLAYING && currentPlayer == Seed.PLAYER1) {
                    if (game.isValidMove(col)) {
                        game.makeMove(col, ConnectFour.HUMAN_PLAYER);
                        currentState = checkGameState();
                        currentPlayer = Seed.PLAYER2;

                        // AI's turn
                        if (currentState == State.PLAYING) {
                            int bestMove = game.getBestMove();
                            game.makeMove(bestMove, ConnectFour.AI_PLAYER);
                            currentState = checkGameState();
                            currentPlayer = Seed.PLAYER1;
                        }
                    }
                    repaint();
                }
            }
        });

        statusBar = new JLabel(" ");
        statusBar.setFont(new Font(Font.DIALOG_INPUT, Font.BOLD, 14));
        statusBar.setBorder(BorderFactory.createEmptyBorder(2, 5, 4, 5));

        setLayout(new BorderLayout());
        add(gamePanel, BorderLayout.CENTER);
        add(statusBar, BorderLayout.PAGE_END);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack(); // Ensure layout is packed after adding components
        setTitle("Connect Four");
        setVisible(true);

        resetGame();
    }

    private void resetGame() {
        game = new ConnectFour("Player 1", "AI (" + difficultyLevel + ")");
        currentPlayer = Seed.PLAYER1;
        currentState = State.PLAYING;
        statusBar.setText("Player 1's Turn");
    }

    private State checkGameState() {
        if (game.isWinningMove(game.getBoard(), ConnectFour.HUMAN_PLAYER)) {
            statusBar.setText("Player 1 Wins!");
            // Show pop-up for winning
            showGameOverDialog("Player 1 Wins!");
            return State.WIN;
        } else if (game.isWinningMove(game.getBoard(), ConnectFour.AI_PLAYER)) {
            statusBar.setText("Computer Wins!");
            // Show pop-up for winning
            showGameOverDialog("Computer Wins!");
            return State.WIN;
        } else if (game.isDraw()) {
            statusBar.setText("It's a Draw!");
            // Show pop-up for draw
            showGameOverDialog("It's a Draw!");
            return State.DRAW;
        } else {
            statusBar.setText(currentPlayer == Seed.PLAYER1 ? "Player 1's Turn" : "Computer's Turn");
            return State.PLAYING;
        }
    }

    private void showGameOverDialog(String message) {
        String[] options = {"Restart Game", "Back to Main Menu"};
        int choice = JOptionPane.showOptionDialog(
                this,
                message,
                "Game Over",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[0]
        );

        if (choice == 0) {
            resetGame();
            repaint();
        } else if (choice == 1) {
            showMainMenu();
        }
    }

    private void showMainMenu() {
        // Close the current game window
        dispose();

        // Show the main menu dialog again
        JOptionPane.showMessageDialog(null, "Welcome to Connect Four Game!\nChoose your difficulty level:");

        String[] difficultyLevels = {"Easy", "Medium", "Hard"};
        difficultyLevel = (String) JOptionPane.showInputDialog(null, "Select Difficulty",
                "Difficulty Level", JOptionPane.QUESTION_MESSAGE, null, difficultyLevels, difficultyLevels[1]);

        // Start a new game with the selected difficulty
        game = new ConnectFour("Player 1", "AI (" + difficultyLevel + ")");
        new TTTGraphics(); // Create a new instance of the game with the new difficulty level
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(TTTGraphics::new);
    }
}
