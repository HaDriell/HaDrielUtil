package fr.hadriel.graphics;

import fr.hadriel.graphics.g2d.Graphics;
import fr.hadriel.graphics.g2d.G2DWindow;
import fr.hadriel.graphics.g2d.ui.UIContext;
import fr.hadriel.graphics.g2d.ui.Widget;
import fr.hadriel.graphics.opengl.Texture;
import fr.hadriel.math.*;

import java.io.IOException;

public class TestBatchGraphics {

    public static void main(String[] args) {
        G2DWindow window = new G2DWindow();
        window.getScene().add(new Widget() {
            Texture texture;
            Vec2 mouse = Vec2.ZERO;
            CubicBezierCurve curve = new CubicBezierCurve();

            protected void onRender(Graphics g, float width, float height, UIContext context) {
                if(texture == null) {
                    try {
                        texture = new Texture("illuminati.png");
                        window.bindCursorPos((window1, xpos, ypos) -> mouse = new Vec2((float) xpos, (float) ypos));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                float ax = 0, ay = 0;
                float c1x = 0, c1y = window.getSize().y;
                float c2x = mouse.x, c2y = mouse.y;
                float bx = 20, by = 0;
                curve.setCurve(ax, ay, c1x, c1y, c2x, c2y, bx, by);

                g.setColor(new Vec4(1, 0, 0, 1));
                g.drawBezierCurve(curve);
            }
        });
    }
}