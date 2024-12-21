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
public class SudokuMain {
    public static void main(String[] args) {
        // Initialize and start sound effect
        SoundEffect soundEffect = new SoundEffect("bensound-clearday.wav");
        soundEffect.play();

        // Main game Sudoku
        System.out.println("Sudoku");
        Sudoku game = new Sudoku();

        // Add mechanism to stop sound when Sudoku game is closed
        Runtime.getRuntime().addShutdownHook(new Thread(() -> soundEffect.stop()));
    }
}