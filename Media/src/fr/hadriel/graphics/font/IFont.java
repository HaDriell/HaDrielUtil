package fr.hadriel.graphics.font;

import fr.hadriel.gui.Graphics;
import fr.hadriel.math.Vec4;

/**
 * @author glathuiliere
 */
public interface IFont {
    public void drawText(Graphics g, String string, float size, Vec4 color);
}
