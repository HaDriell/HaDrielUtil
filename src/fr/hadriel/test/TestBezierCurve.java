package fr.hadriel.test;

import fr.hadriel.main.lwjgl.g2d.BatchGraphics;
import fr.hadriel.main.lwjgl.g2d.G2DWindow;
import fr.hadriel.main.lwjgl.g2d.ui.Widget;

/**
 * Created by glathuiliere on 12/04/2017.
 */
public class TestBezierCurve {

    public static void main(String[] args) {
        G2DWindow window = new G2DWindow();
        window.getRoot().add(new Widget() {
            protected void onRender(BatchGraphics g, float width, float height) {
                g.setColor(0xFFFFFFFF);
                g.drawBezierCurve(100, 100, 200, 500, 300, 100);
            }
        });
    }
}
