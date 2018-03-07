package fr.hadriel.renderers;

import fr.hadriel.graphics.font.Font;

public class FontRenderer implements IRenderer<String> {

    private Font font;
    private float size;

    public void setFont(Font font) {
        this.font = font;
    }

    public Font getFont() {
        return font;
    }

    public void setSize(float size) {
        this.size = size;
    }

    public float getSize() {
        return size;
    }

    public void draw(String text) {
        if(text == null || text.isEmpty()) return; // no text to render
        if(font == null) return; // no configured font
        //TODO : render the text using the configured font
    }
}