package fr.hadriel.asset.graphics.font;

public class FontInfo {
    /**
     * name of the font
     */
    public final String face;

    /**
     * size of the render
     */
    public final int size;

    /**
     * bold switch
     */
    public final boolean bold;

    /**
     * italic switch
     */
    public final boolean italic;

    /**
     * name of the OEM
     */
    public final String charset;

    /**
     * unicode charset switch
     */
    public final boolean unicode;

    /**
     * height stretchH in percentage (100% = no stretchH)
     */
    public final int stretchH;

    /**
     * smooth switch
     */
    public final boolean smooth;

    /**
     * supersampling level (1 = no supersampling)
     */
    public final int aa;

    /**
     * padding for each character
     */
    public final int[] padding;


    /**
     * spacing for each character
     */
    public final int[] spacing;

    /**
     * thickness of the outline
     */
    public final int outline;

    public FontInfo(String face, int size, boolean bold, boolean italic, String charset, boolean unicode, int stretchH, boolean smooth, int aa, int[] padding, int[] spacing, int outline) {
        this.face = face;
        this.size = size;
        this.bold = bold;
        this.italic = italic;
        this.charset = charset;
        this.unicode = unicode;
        this.stretchH = stretchH;
        this.smooth = smooth;
        this.aa = aa;
        this.padding = padding;
        this.spacing = spacing;
        this.outline = outline;
    }

    public String toString() {
        return String.format("face=%s size=%d bold=%b italic=%b charset=%s unicode=%b stretchH=%d smooth=%b aa=%d padding=(%d,%d,%d,%d) spacing=(%d,%d) outline=%d",
                face, size, bold, italic, charset, unicode, stretchH, smooth, aa, padding[0], padding[1], padding[2], padding[3], spacing[0], spacing[1], outline);
    }
}
