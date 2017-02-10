package fr.hadriel;

import fr.hadriel.lwjgl.g2d.G2DWindow;
import fr.hadriel.lwjgl.g2d.ui.Group;
import fr.hadriel.lwjgl.g2d.ui.Rectangle;

/**
 * Created by glathuiliere on 09/02/2017.
 */
public class Test2DFocus {

    public static void main(String[] args) {
        G2DWindow window = new G2DWindow();
        Group g = new Group();
        int COL = 25;
        int ROW = 25;
        int RECT_WIDTH = window.getWidth() / COL;
        int RECT_HEIGHT = window.getHeight() / ROW;
        for (int x = 0; x < COL; x++) {
            for (int y = 0; y < ROW; y++) {
                Rectangle r = new Rectangle(RECT_WIDTH, RECT_HEIGHT);
                r.translate(x * RECT_WIDTH, y * RECT_HEIGHT);
                g.add(r);
            }
        }
        window.getRoot().add(g);
    }
}
