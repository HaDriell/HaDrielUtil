package fr.hadriel.graphics.font;

public class FontPage {
    public int id;
    public String file;

    public String toString() {
        return String.format("page=%d file=%s", id, file);
    }
}