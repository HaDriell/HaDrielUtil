package fr.hadriel.asset.graphics.font;

public class FontInfo {
    /**
     * name of the font
     */
    public String face;

    /**
     * size of the render
     */
    public int size;

    /**
     * bold switch
     */
    public boolean bold;

    /**
     * italic switch
     */
    public boolean italic;

    /**
     * name of the OEM
     */
    public String charset;

    /**
     * unicode charset switch
     */
    public boolean unicode;

    /**
     * height stretchH in percentage (100% = no stretchH)
     */
    public int stretchH;

    /**
     * smooth switch
     */
    public boolean smooth;

    /**
     * supersampling level (1 = no supersampling)
     */
    public int aa;

    /**
     * padding for each character
     */
    public int paddingUp;

    /**
     * padding for each character
     */
    public int paddingRight;

    /**
     * padding for each character
     */
    public int paddingDown;

    /**
     * padding for each character
     */
    public int paddingLeft;


    /**
     * spacing for each character
     */
    public int spacingV;
    /**
     * spacing for each character
     */
    public int spacingH;
}
