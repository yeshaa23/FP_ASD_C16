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

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/** Tic-Tac-Toe game with AI */
public class TTTGraphic extends JPanel {

    private static final int SIZE = 3; // Board size
    private static final int CELL_SIZE = 100; // Cell size in pixels
    private static final int WIDTH = SIZE * CELL_SIZE; // Window width
    private static final int HEIGHT = SIZE * CELL_SIZE + 60; // Window height (extra for status)
    private static final int STATUS_HEIGHT = 30; // Height for status bar

    private final Board board; // Logical board
    private Seed currentPlayer;
    private State currentState;
    private final boolean isAIPlayerTurn;
    private final AIPlayerMinimax aiPlayer;

    private final JPanel gamePanel;
    private final JPanel controlPanel;
    private final JButton resetButton;
    private final JLabel statusLabel; // Status label for messages

    private boolean vsAI; // true = melawan AI, false = dua pemain

    public TTTGraphic() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setLayout(new BorderLayout());

        // Tampilkan dialog sambutan
        showWelcomeMenu();

        // Inisialisasi permainan
        board = new Board();
        currentPlayer = Seed.CROSS;
        currentState = State.PLAYING;
        isAIPlayerTurn = false;

        // Inisialisasi AI jika mode AI dipilih
        aiPlayer = new AIPlayerMinimax(board);
        aiPlayer.setSeed(Seed.NOUGHT);

        // Setup panel permainan
        gamePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawBoard(g);
            }
        };
        gamePanel.setPreferredSize(new Dimension(WIDTH, HEIGHT - STATUS_HEIGHT));
        gamePanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (currentState == State.PLAYING) {
                    int mouseX = e.getX();
                    int mouseY = e.getY();
                    int row = mouseY / CELL_SIZE;
                    int col = mouseX / CELL_SIZE;

                    if (row >= 0 && row < SIZE && col >= 0 && col < SIZE
                            && board.cells[row][col].content == Seed.NO_SEED) {
                        currentState = board.stepGame(currentPlayer, row, col);
                        currentPlayer = (currentPlayer == Seed.CROSS) ? Seed.NOUGHT : Seed.CROSS;
                        updateStatus();
                        gamePanel.repaint();

                        // Jika mode AI, giliran AI
                        if (vsAI && currentState == State.PLAYING && currentPlayer == Seed.NOUGHT) {
                            aiMove();
                        }
                    }
                } else {
                    showGameOverMenu();
                }
            }
        });

        // Setup panel kontrol dan label status
        controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        resetButton = new JButton("Reset Game");
        resetButton.addActionListener(e -> newGame());
        controlPanel.add(new JLabel("Tic Tac Toe"));
        controlPanel.add(resetButton);

        statusLabel = new JLabel("Player X's Turn");
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 14));
        statusLabel.setOpaque(true);
        statusLabel.setBackground(Color.LIGHT_GRAY);
        statusLabel.setPreferredSize(new Dimension(WIDTH, STATUS_HEIGHT));

        add(gamePanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.NORTH);
        add(statusLabel, BorderLayout.SOUTH);
    }

    private void aiMove() {
        if (currentState == State.PLAYING) {
            int[] move = aiPlayer.move();
            if (move != null) {
                currentState = board.stepGame(Seed.NOUGHT, move[0], move[1]);
                currentPlayer = Seed.CROSS;
                updateStatus();
                gamePanel.repaint();
            }
        }
    }

    private void newGame() {
        board.newGame();
        currentPlayer = Seed.CROSS;
        currentState = State.PLAYING;
        updateStatus();
        gamePanel.repaint();
    }

    private void showWelcomeMenu() {
        int choice = JOptionPane.showOptionDialog(
                null,
                "Selamat datang di Tic Tac Toe!\nPilih mode permainan:",
                "Welcome",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                new String[] {"1 Pemain", "2 Pemain"},
                "1 Pemain"
        );
        vsAI = (choice == JOptionPane.YES_OPTION); // true jika "Lawan AI" dipilih
    }

    private void showGameOverMenu() {
        int choice = JOptionPane.showOptionDialog(
                null,
                "Permainan selesai!\nApa yang ingin Anda lakukan?",
                "Game Over",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                new String[] {"Back", "Restart Game", "Keluar"},
                "Back"
        );

        if (choice == JOptionPane.YES_OPTION) {
            removeAll(); // Remove current components
            revalidate();
            repaint();
            showWelcomeMenu();
            add(gamePanel, BorderLayout.CENTER);
            add(controlPanel, BorderLayout.NORTH);
            add(statusLabel, BorderLayout.SOUTH);
            newGame();
        } else if (choice == JOptionPane.NO_OPTION) {
            newGame();
        } else {
            System.exit(0);
        }
    }

    private void drawBoard(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setStroke(new BasicStroke(5)); // Thicker lines for grid

        // Draw grid
        g2d.setColor(Color.GRAY);
        for (int i = 1; i < SIZE; i++) {
            g2d.drawLine(i * CELL_SIZE, 0, i * CELL_SIZE, HEIGHT - STATUS_HEIGHT);
            g2d.drawLine(0, i * CELL_SIZE, WIDTH, i * CELL_SIZE);
        }

        // Draw images or text from the board's cells
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                Cell cell = board.cells[row][col];
                JLabel label = cell.toJLabel();

                // Convert JLabel to an image and draw it
                ImageIcon icon = (ImageIcon) label.getIcon();
                if (icon != null) {
                    g2d.drawImage(icon.getImage(), col * CELL_SIZE, row * CELL_SIZE, CELL_SIZE, CELL_SIZE, null);
                }
            }
        }
    }

    private void updateStatus() {
        if (currentState == State.PLAYING) {
            statusLabel.setText(currentPlayer == Seed.CROSS ? "Player X's Turn" : "Player O's Turn");
        } else if (currentState == State.CROSS_WON) {
            statusLabel.setText("Player X Wins! Click to play again.");
            showGameOverMenu();
        } else if (currentState == State.NOUGHT_WON) {
            statusLabel.setText("Player O Wins! Click to play again.");
            showGameOverMenu();
        } else if (currentState == State.DRAW) {
            statusLabel.setText("It's a Draw! Click to play again.");
            showGameOverMenu();
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Tic Tac Toe");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(new TTTGraphic());
        frame.setSize(WIDTH, HEIGHT + 30);
        frame.setResizable(false);
        frame.setVisible(true);
    }
}
