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

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CountdownTimer {
    private int timeLeft;
    private Timer timer;
    private JLabel countdownLabel;

    public CountdownTimer(JLabel countdownLabel, Sudoku sudoku) {
        this.countdownLabel = countdownLabel;

        // Timer that updates every second
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (timeLeft > 0) {
                    timeLeft--;
                    updateCountdownLabel(); // Update the label each second
                } else {
                    timer.stop();
                    JOptionPane.showMessageDialog(sudoku, "Time's up!", "Game Over", JOptionPane.INFORMATION_MESSAGE);
                    sudoku.disableGameBoard(true); // Disable game when time is up
                }
            }
        });
    }

    /**
     * Sets the countdown timer based on the difficulty.
     *
     * @param difficulty The difficulty level, which is either "easy", "medium", or "hard".
     */
    public void setTime(String difficulty) {
        switch (difficulty) {
            case "easy":
                timeLeft = 2 * 60; // Easy = 2 minutes = 120 seconds
                break;
            case "medium":
                timeLeft = 3 * 60; // Medium = 3 minutes = 180 seconds
                break;
            case "hard":
                timeLeft = 4 * 60; // Hard = 4 minutes = 240 seconds
                break;
            default:
                timeLeft = 2 * 60; // Default to easy if unknown difficulty
        }
        updateCountdownLabel(); // Update the countdown label immediately with the new time
    }

    public void startTimer() {
        if (timeLeft > 0 && !timer.isRunning()) {
            timer.start();
        }
    }

    public void stopTimer() {
        timer.stop();
    }

    private void updateCountdownLabel() {
        int minutes = timeLeft / 60;
        int seconds = timeLeft % 60;
        countdownLabel.setText(String.format("Time Left: %02d:%02d", minutes, seconds)); // Update the label format
    }
}
