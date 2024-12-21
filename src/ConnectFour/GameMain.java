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
import java.awt.event.*;
import javax.swing.*;

public class GameMain extends JFrame {

    private static final long serialVersionUID = 1L; // to prevent serializable warning
    private AIPlayerMinimax aiPlayer;
    private boolean isAIMode = true; // Default: human vs AI

    // Define named constants for the drawing graphics
    public static final String TITLE = "Connect Four";
    public static final Color COLOR_BG = Color.WHITE;
    public static final Color COLOR_BG_STATUS = new Color(230, 190, 255, 200); // Soft lavender background
    public static final Color COLOR_CROSS = new Color(255, 127, 80, 215); // Coral orange
    public static final Color COLOR_NOUGHT = new Color(255, 165, 0); // Golden orange
    public static final Font FONT_STATUS = new Font("Arial Rounded MT Bold", Font.BOLD, 14);

    // Define game objects
    private Board board;         // the game board
    private State currentState;  // the current state of the game
    private Seed currentPlayer;  // the current player
    private JLabel statusBar;    // for displaying status message

    // SoundEffect objects for sound effects
    private SoundEffect moveSound;
    private SoundEffect winSound;
    private SoundEffect drawSound;
    private SoundEffect moveAISound;

    public static Player player1 = new Player("Player 1");
    public static Player player2 = new Player("Player 2");
    private JLabel player1ScoreLabel;
    private JLabel player2ScoreLabel;

    /** Constructor to setup the UI and game components */
    public GameMain() {
        board = new Board();
        aiPlayer = new AIPlayerMinimax(board);
        aiPlayer.setSeed(Seed.NOUGHT); // Set AI seed

        // Initialize sound effects
        moveSound = new SoundEffect("ConnectFour/beep.wav");
        winSound = new SoundEffect("ConnectFour/mati.wav");
        drawSound = new SoundEffect("ConnectFour/meledak.wav");
        moveAISound = new SoundEffect("ConnectFour/ai_move.wav");

        // Setup UI
        Container cp = getContentPane();
        cp.setLayout(new BorderLayout());

        // Status bar for status messages
        statusBar = new JLabel(" ");
        statusBar.setFont(FONT_STATUS);
        statusBar.setHorizontalAlignment(SwingConstants.CENTER);
        statusBar.setBackground(COLOR_BG_STATUS);
        statusBar.setOpaque(true);
        cp.add(statusBar, BorderLayout.SOUTH);

        // Player scores
        JPanel scorePanel = new JPanel(new GridLayout(1, 2));
        player1ScoreLabel = new JLabel(player1.getName() + ": " + player1.getScore());
        player2ScoreLabel = new JLabel(player2.getName() + ": " + player2.getScore());
        scorePanel.add(player1ScoreLabel);
        scorePanel.add(player2ScoreLabel);
        cp.add(scorePanel, BorderLayout.NORTH);

        // Board panel
        JPanel boardPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                board.paint(g);
            }
        };
        boardPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleClick(e.getX(), e.getY());
            }
        });
        cp.add(boardPanel, BorderLayout.CENTER);

        // Set frame properties
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle(TITLE);
        setSize(800, 600);
        setResizable(false);
        setVisible(true);

        // Start a new game
        initGame();
    }

    /** Initialize or reset the game */
    public void initGame() {
        currentPlayer = Seed.CROSS; // Start with Player 1
        currentState = State.PLAYING;
        board.init();
        updateStatus();
    }

    /** Handle mouse click event */
    private void handleClick(int x, int y) {
        if (currentState == State.PLAYING) {
            int col = board.getColFromX(x);
            if (board.isValidMove(col)) {
                board.makeMove(col, currentPlayer);
                moveSound.play();
                if (board.hasWon(currentPlayer)) {
                    currentState = (currentPlayer == Seed.CROSS) ? State.CROSS_WON : State.NOUGHT_WON;
                    winSound.play();
                } else if (board.isDraw()) {
                    currentState = State.DRAW;
                    drawSound.play();
                } else {
                    currentPlayer = (currentPlayer == Seed.CROSS) ? Seed.NOUGHT : Seed.CROSS;
                    if (isAIMode && currentPlayer == Seed.NOUGHT) {
                        int[] aiMove = aiPlayer.move(); // Get [row, col] from AI
                        int columnToPlay = aiMove[1];  // Extract column
                        board.makeMove(columnToPlay, currentPlayer);
                        moveAISound.play();
                        if (board.hasWon(currentPlayer)) {
                            currentState = State.NOUGHT_WON;
                            winSound.play();
                        }
                        currentPlayer = Seed.CROSS;
                    }
                }
                updateStatus();
                repaint();
            }
        } else {
            initGame();
            repaint();
        }
    }

    /** Update status message */
    private void updateStatus() {
        switch (currentState) {
            case PLAYING -> statusBar.setText((currentPlayer == Seed.CROSS ? player1.getName() : player2.getName()) + "'s Turn");
            case CROSS_WON -> {
                statusBar.setText(player1.getName() + " Won!");
                player1.addScore(1);
                updateScores();
            }
            case NOUGHT_WON -> {
                statusBar.setText(player2.getName() + " Won!");
                player2.addScore(1);
                updateScores();
            }
            case DRAW -> statusBar.setText("It's a Draw!");
        }
    }

    /** Update player scores */
    private void updateScores() {
        player1ScoreLabel.setText(player1.getName() + ": " + player1.getScore());
        player2ScoreLabel.setText(player2.getName() + ": " + player2.getScore());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GameMain::new);
    }
}