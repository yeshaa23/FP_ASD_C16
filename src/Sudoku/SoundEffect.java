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
import javax.sound.sampled.*;
import java.io.InputStream;

public class SoundEffect {
    private Clip clip;

    public SoundEffect(String soundFileName) {
        try {
            // Use getResourceAsStream to load files inside the src folder
            InputStream audioSrc = getClass().getClassLoader().getResourceAsStream(soundFileName);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(audioSrc);
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to start audio loop
    public void play() {
        if (clip != null) {
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    // Method to stop audio
    public void stop() {
        if (clip != null) {
            clip.stop();
            clip.close();  // Close the clip to release resources
        }
    }
}