package fr.hadriel.test;

import fr.hadriel.main.lwjgl.font.GLFont;
import fr.hadriel.main.lwjgl.g2d.BatchGraphics;
import fr.hadriel.main.lwjgl.g2d.G2DWindow;
import fr.hadriel.main.lwjgl.g2d.event.KeyPressedEvent;
import fr.hadriel.main.lwjgl.g2d.event.KeyReleasedEvent;
import fr.hadriel.main.lwjgl.g2d.ui.Widget;
import fr.hadriel.main.math.Mathf;
import fr.hadriel.main.math.Matrix3f;
import fr.hadriel.main.math.Vec2;
import fr.hadriel.main.math.Vec4;
import fr.hadriel.main.util.Timer;
import org.lwjgl.glfw.GLFW;

import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * Created by glathuiliere on 13/02/2017.
 */
public class TestGLFont {
    public static void main(String[] args) throws IOException {
        G2DWindow window = new G2DWindow();
        Widget w = new Widget() {
            private GLFont font;
            private String text = "Look at my Awesome Helloworld";
            private float fontHeight = 10f;
            private float fontHeightSpeed = 0;

            private Timer t = new Timer();

            protected void onRender(BatchGraphics g,  float width, float height) {
//                fontHeight += fontHeightSpeed * t.elapsed();
                fontHeight += Math.abs(40 * Mathf.sin(0.2f * t.elapsed()));
                t.reset();

                if(font == null) {
                    try {
                        Font input = Font.createFont(Font.TRUETYPE_FONT, new File("berylium.TTF")).deriveFont(20f);
                        font = new GLFont(input);
                    } catch (Exception ignore) {}
                }
                String fulltext = String.format("%s in %.2f p", text, fontHeight);

                Vec2 textSize = font.getSizeOfString(fulltext, fontHeight);
                g.push(Matrix3f.Translation((window.getWidth() - textSize.x) / 2, (window.getHeight() - textSize.y) / 2));
                font.drawText(g, fulltext, fontHeight, new Vec4(1, 1, 1, 1));
                g.pop();
            }
        };
        window.getRoot().add(w);
    }
}
