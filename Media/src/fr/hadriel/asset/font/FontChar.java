package fr.hadriel.asset.font;

public class FontChar {
    public final int id;
    public final int page;
    public final int x;
    public final int y;
    public final int width;
    public final int height;
    public final int xoffset;
    public final int yoffset;
    public final int xadvance;
    //channfinal els aren't supporetd

    public FontChar(int id, int page, int x, int y, int width, int height, int xoffset, int yoffset, int xadvance) {
        this.id = id;
        this.page = page;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.xoffset = xoffset;
        this.yoffset = yoffset;
        this.xadvance = xadvance;
    }


    public String toString() {
        return String.format("Char(%d) page=%d x=%d y=%d width=%d height=%d xoffset=%d yoffset=%d xadvance=%d", id, page, x, y, width, height, xoffset, yoffset, xadvance);
    }
}