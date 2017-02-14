package fr.hadriel;

import fr.hadriel.lwjgl.font.TrueTypeFont;
import fr.hadriel.lwjgl.g2d.BatchGraphics;
import fr.hadriel.lwjgl.g2d.G2DWindow;
import fr.hadriel.lwjgl.g2d.ui.Widget;
import fr.hadriel.math.Matrix3f;
import fr.hadriel.math.Vec4;
import fr.hadriel.util.Timer;

import java.io.IOException;

/**
 * Created by glathuiliere on 13/02/2017.
 */
public class TestTTF {
    public static void main(String[] args) throws IOException {
        G2DWindow window = new G2DWindow();
        Widget w = new Widget() {
            private TrueTypeFont font;
            private Timer t = new Timer();

            protected void onRender(BatchGraphics g) {
                if(font == null) {
                    try {
                        font = new TrueTypeFont("font.ttf");
                    } catch (IOException ignore) {}
                }

                g.push(Matrix3f.Translation(100, 100));
                font.drawText(g, "Look at my FUCKING HelloWorld", 30f, new Vec4(1, 1, 1, 1));
                g.pop();
            }
        };
        window.getRoot().add(w);
    }
}
