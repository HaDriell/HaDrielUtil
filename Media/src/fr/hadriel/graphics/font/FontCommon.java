package fr.hadriel.graphics.font;

public class FontCommon {

    /**
     * distance in pixels between two lines
     */
    public final int lineHeight;
    /**
     * distance from the absolute top of the baseline
     */
    public final int base;
    /**
     * render texture2D width
     */
    public final int scaleW;
    /**
     * render texture2D height
     */
    public final int scaleH;
    /**
     * number of pages
     */
    public final int pages;

    public FontCommon(int lineHeight, int base, int scaleW, int scaleH, int pages) {
        this.lineHeight = lineHeight;
        this.base = base;
        this.scaleW = scaleW;
        this.scaleH = scaleH;
        this.pages = pages;
    }

    // packing is not supported in this implementation

    public String toString() {
        return String.format("lineHeight=%d base=%d scaleW=%d scaleH=%d pages=%d", lineHeight, base, scaleW, scaleH, pages);
    }
}