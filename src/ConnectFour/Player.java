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

public class Player {
    private String name;
    private int score;

    // Constructor
    public Player(String name) {
        this.name = name;
        this.score = 0;
    }

    // Getter for name
    public String getName() {
        return name;
    }

    // Getter for score
    public int getScore() {
        return score;
    }

    // Add points to the player's score
    public void addScore(int points) {
        this.score += points;
    }

    // Check if the player has won
    public boolean hasWon() {
        return score >= 3;
    }
}
