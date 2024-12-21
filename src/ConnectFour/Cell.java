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

import java.awt.*;
public class Cell {
    public static final int SIZE = 100; // Ukuran satu sel
    public Seed content;               // Isi sel: CROSS, NOUGHT, atau kosong
    private int row, col;              // Posisi sel pada papan

    /** Konstruktor */
    public Cell(int row, int col) {
        this.row = row;
        this.col = col;
        this.content = Seed.NO_SEED; // Awalnya kosong
    }

    /** Menggambar isi sel di canvas */
    public void paint(Graphics g) {
        int x = col * SIZE;
        int y = row * SIZE;

        // Gambar kotak sel
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(x, y, SIZE, SIZE);

        // Gambar isi sel berdasarkan konten menggunakan gambar dari Seed
        if (content != Seed.NO_SEED) {
            Image img = content.getImage();
            if (img != null) {
                g.drawImage(img, x + 10, y + 10, SIZE - 20, SIZE - 20, null);
            } else {
                // Jika gambar tidak tersedia, gunakan representasi teks
                g.setColor(Color.BLACK);
                g.drawString(content.getDisplayName(), x + SIZE / 2 - 5, y + SIZE / 2 + 5);
            }
        }
    }

    /** Reset isi sel ke kosong */
    public void clear() {
        content = Seed.NO_SEED;
    }

    /** Getter untuk baris */
    public int getRow() {
        return row;
    }

    /** Getter untuk kolom */
    public int getCol() {
        return col;
    }

    /** Setter untuk isi sel */
    public void setContent(Seed content) {
        this.content = content;
    }
}
