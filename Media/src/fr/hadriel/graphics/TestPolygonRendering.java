package fr.hadriel.graphics;

import fr.hadriel.graphics.g2d.G2DWindow;
import fr.hadriel.graphics.g2d.Graphics;
import fr.hadriel.graphics.g2d.ui.UIContext;
import fr.hadriel.graphics.g2d.ui.Widget;
import fr.hadriel.graphics.glfw3.WindowHint;
import fr.hadriel.math.geometry.Polygon;
import fr.hadriel.math.Vec2;

/**
 *
 * @author glathuiliere
 */
public class TestPolygonRendering {
    public static void main(String[] args) {
        Polygon p = new Polygon(
                new Vec2(0, 0),
                new Vec2(10, 0),
                new Vec2(10, 10),
                new Vec2(0, 10)
        );


        WindowHint hint = new WindowHint();
        hint.width = 100;
        hint.height= 100;
        G2DWindow window = new G2DWindow(hint);
        window.getScene().add(new Widget() {
            protected void onRender(Graphics g, float width, float height, UIContext context) {
//                g.draw(p);
            }
        });
    }
}