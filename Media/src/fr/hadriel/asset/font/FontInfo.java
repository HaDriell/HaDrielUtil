package fr.hadriel.asset.font;

public class FontInfo {
    public String face;     //name of the font
    public int size;        //size of the render
    public boolean bold;    //bold switch
    public boolean italic;  //italic switch
    public String charset;  //name of the OEM
    public boolean unicode; //unicode charset switch
    public int stretchH;     //height stretchH in percentage (100% = no stretchH)
    public boolean smooth;  //smooth switch
    public int aa;          //supersampling level (1 = no supersampling)
    public int[] padding;   //padding for each character
    public int[] spacing;   //spacing for each character
    public int outline;     //thickness of the outline

    public String toString() {
        return String.format("face=%s size=%d bold=%b italic=%b charset=%s unicode=%b stretchH=%d smooth=%b aa=%d padding=(%d,%d,%d,%d) spacing=(%d,%d) outline=%d",
                face, size, bold, italic, charset, unicode, stretchH, smooth, aa, padding[0], padding[1], padding[2], padding[3], spacing[0], spacing[1], outline);
    }
}
