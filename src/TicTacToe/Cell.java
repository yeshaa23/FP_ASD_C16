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

import javax.swing.*;

public class Cell {  // save as "Cell.java"
    // Define properties (package-visible)
    /** Content of this cell (CROSS, NOUGHT, NO_SEED) */
    Seed content;
    /** Row and column of this cell, not used in this program */
    int row, col;
    private ImageIcon crossIcon, noughtIcon; // Gambar untuk CROSS dan NOUGHT

    /** Constructor to initialize this cell */
    public Cell(int row, int col) {
        this.row = row;
        this.col = col;
        this.content = Seed.NO_SEED;
        loadIcons(); // memuat gambar
    }

    private void loadIcons() {
        // Path gambar yang digunakan
        String crossPath = "cat.png";
        String noughtPath = "mouse.png";

        // Membuat ImageIcon dari path gambar
        crossIcon = new ImageIcon(getClass().getClassLoader().getResource(crossPath));
        noughtIcon = new ImageIcon(getClass().getClassLoader().getResource(noughtPath));

        // Memeriksa apakah gambar berhasil dimuat
        if (crossIcon.getIconWidth() == -1 || noughtIcon.getIconWidth() == -1) {
            System.out.println("Error loading images! Gambar tidak ditemukan.");
        } else {
            System.out.println("Gambar berhasil dimuat.");
        }
    }

    /** Reset the cell content to EMPTY, ready for a new game. */
    public void newGame() {
        this.content = Seed.NO_SEED;
    }

    /** The cell paints itself */
    public void paint() {
        // Retrieve the display icon (text) and print
        String icon = this.content.getIcon();
        System.out.print(icon);
    }

    public JLabel toJLabel() {
        JLabel label = new JLabel();

        // Mengatur ikon atau teks berdasarkan isi cell
        if (content.getImage() != null) {
            label.setIcon(new ImageIcon(content.getImage())); // Atur ikon gambar
        } else {
            label.setText(content.getDisplayName()); // Jika tidak ada gambar, gunakan teks
        }

        label.setHorizontalAlignment(SwingConstants.CENTER);
        return label;
    }

    // Add method to get the current content of the cell
    public Seed getContent() {
        return content;
    }

    // Method to set the content of the cell (CROSS or NOUGHT)
    public void setContent(Seed content) {
        this.content = content;
    }
}
