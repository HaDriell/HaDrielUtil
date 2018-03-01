package fr.hadriel.graphics.font;

public class FontChar {
    public int id;
    public int page;
    public int x;
    public int y;
    public int width;
    public int height;
    public int xoffset;
    public int yoffset;
    public int xadvance;
    //channels aren't supporetd


    public String toString() {
        return String.format("Char(%d) page=%d x=%d y=%d width=%d height=%d xoffset=%d yoffset=%d xadvance=%d", id, page, x, y, width, height, xoffset, yoffset, xadvance);
    }
}