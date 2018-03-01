package fr.hadriel.graphics.font;

public class FontCommon {
    public int lineHeight;  // distance in pixels between two lines
    public int base;        // distance from the absolute top of the baseline
    public int scaleW;      // render texture width
    public int scaleH;      // render texture height
    public int pages;       // number of pages
    // packing is not supported in this implementation

    public String toString() {
        return String.format("lineHeight=%d base=%d scaleW=%d scaleH=%d pages=%d", lineHeight, base, scaleW, scaleH, pages);
    }
}