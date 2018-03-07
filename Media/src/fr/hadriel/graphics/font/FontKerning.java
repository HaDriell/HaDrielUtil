package fr.hadriel.graphics.font;

public class FontKerning {
    public final int first;   // left character
    public final int second;  // right character
    public final int amount;  // x-axis in pixels adjustment

    public FontKerning(int first, int second, int amount) {
        this.first = first;
        this.second = second;
        this.amount = amount;
    }

    public String toString() {
        return String.format("Kerning first=%d second=%d amount=%d", first, second, amount);
    }
}