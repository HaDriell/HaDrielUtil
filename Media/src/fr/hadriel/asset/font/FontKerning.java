package fr.hadriel.asset.font;

public class FontKerning {

    /**
     * first character
     */
    public final int first;
    /**
     * second character
     */
    public final int second;
    /**
     * x-axis in pixels adjustment
     */
    public final int amount;

    public FontKerning(int first, int second, int amount) {
        this.first = first;
        this.second = second;
        this.amount = amount;
    }

    public String toString() {
        return String.format("Kerning first=%d second=%d amount=%d", first, second, amount);
    }
}