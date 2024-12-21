/**
 * ES234317-Algorithm and Data Structures
 * Semester Ganjil, 2024/2025
 * Group Capstone Project
 * Group #16
 * 1 - 5026231038 - Nabila Shinta Luthfia
 * 2 - 5026231125 - Ayesha Hana Azkiya
 * 3 - 5026231139 - Amandea Chandiki Larasati
 */
import Sudoku.Sudoku;
import TicTacToe.TTTGraphic;
import ConnectFour.TTTGraphics; // Memastikan package ConnectFour ada dan bisa digunakan

import javax.swing.*;
import java.awt.*;
import java.io.Serial;

public class MainMenu extends JFrame {
    @Serial
    private static final long serialVersionUID = 1L;

    public MainMenu() {
        setTitle("Main Menu");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Menempatkan window di tengah

        // Container untuk layout
        Container cp = getContentPane();
        cp.setLayout(new BorderLayout());

        // Label Title
        JLabel titleLabel = new JLabel("Choose Your Game", JLabel.CENTER);
        titleLabel.setFont(new Font("Calibre", Font.BOLD, 18));
        cp.add(titleLabel, BorderLayout.NORTH);

        // Panel dengan tombol untuk setiap game
        JPanel gamePanel = new JPanel();
        gamePanel.setLayout(new GridLayout(3, 1, 10, 10)); // Grid 3x1 untuk game

        // Tombol untuk setiap game
        JButton btnSudoku = new JButton("Sudoku");
        JButton btnTicTacToe = new JButton("Tic Tac Toe");
        JButton btnConnectFour = new JButton("Connect Four");

        // Menambahkan tombol ke panel
        gamePanel.add(btnSudoku);
        gamePanel.add(btnTicTacToe);
        gamePanel.add(btnConnectFour);

        cp.add(gamePanel, BorderLayout.CENTER);

        // Action listeners untuk tombol Sudoku
        btnSudoku.addActionListener(e -> {
            JFrame frame = new JFrame("Sudoku");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setContentPane(new Sudoku()); // Pastikan Sudoku adalah JPanel
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            dispose(); // Tutup menu utama
        });

        // Action listeners untuk tombol Tic Tac Toe
        btnTicTacToe.addActionListener(e -> {
            JFrame frame = new JFrame("Tic Tac Toe");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setContentPane(new TTTGraphic()); // Ubah ke TTTGraphicPanel, bukan TTTGraphic (yang mungkin JFrame)
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            dispose(); // Tutup menu utama
        });

        // Action listeners untuk tombol Connect Four
        btnConnectFour.addActionListener(e -> {
            JFrame frame = new JFrame("Connect Four");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setContentPane(new TTTGraphics()); // Pastikan Connect Four menggunakan kelas yang benar
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            dispose(); // Tutup menu utama
        });

        // Tombol Exit
        JButton exitButton = new JButton("Exit");
        exitButton.addActionListener(e -> System.exit(0)); // Keluar dari aplikasi
        cp.add(exitButton, BorderLayout.SOUTH);

        // Pengaturan Frame
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainMenu::new); // Menjalankan Main Menu
        SoundEffect soundEffect = new SoundEffect("bensound-clearday.wav");
        soundEffect.play();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> soundEffect.stop()));
    }
}
