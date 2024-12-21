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

import javax.sound.sampled.*;
import java.io.InputStream;
import java.io.IOException;

public class SoundTTT {

    public static void playSound(String soundFile) {
        try {
            // Try loading the resource from the current package
            InputStream sound = SoundTTT.class.getResourceAsStream("/" + soundFile);
            if (sound == null) {
                System.out.println("Sound file not found: " + soundFile);
                return;
            }

            // Create an AudioInputStream from the InputStream
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(sound);
            Clip clip = AudioSystem.getClip();  // Get a clip to play the sound
            clip.open(audioIn);  // Open the audio input stream
            clip.start();  // Start playing the sound
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }
}