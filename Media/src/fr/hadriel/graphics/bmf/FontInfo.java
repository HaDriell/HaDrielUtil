package fr.hadriel.graphics.bmf;

public class FontInfo {
    public String face;     //name of the font
    public int size;        //size of the render
    public boolean bold;    //bold switch
    public boolean italic;  //italic switch
    public String charset;  //name of the OEM
    public boolean unicode; //unicode charset switch
    public int stretch;     //height stretch in percentage (100% = no stretch)
    public boolean smooth;  //smooth switch
    public int aa;          //supersampling level (1 = no supersampling)
    public int[] padding;   //padding for each character
    public int[] spacing;   //spacing for each character
    public int outline;     //thickness of the outline
}
