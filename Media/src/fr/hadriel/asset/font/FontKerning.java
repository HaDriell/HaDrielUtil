package fr.hadriel.asset.font;

public class FontKerning {
    public int first;   // left character
    public int second;  // right character
    public int amount;  // x-axis in pixels adjustment

    public String toString() {
        return String.format("Kerning first=%d second=%d amount=%d", first, second, amount);
    }
}