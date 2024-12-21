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