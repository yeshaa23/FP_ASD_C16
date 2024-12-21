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

import Sudoku.SoundEffect;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Input name for the Human player
        System.out.print("Enter name for Human player: ");
        String humanName = scanner.nextLine();

        // Set AI's name automatically to "Computer"
        String aiName = "Computer";

        // Create a new Board instance
        Board gameBoard = new Board();

        // Create a new ConnectFour game instance with the human player, AI player, and the board
        ConnectFour game = new ConnectFour(humanName, aiName);

        Sudoku.SoundEffect soundEffect = new SoundEffect("bensound-clearday.wav");
        soundEffect.play();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> soundEffect.stop()));
        // Start the game
        //game.playGame();
    }
}