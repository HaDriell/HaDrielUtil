package fr.hadriel;

import fr.hadriel.lwjgl.font.TrueTypeFont;
import fr.hadriel.lwjgl.g2d.BatchGraphics;
import fr.hadriel.lwjgl.g2d.G2DWindow;
import fr.hadriel.lwjgl.g2d.events.KeyPressedEvent;
import fr.hadriel.lwjgl.g2d.events.KeyReleasedEvent;
import fr.hadriel.lwjgl.g2d.ui.Widget;
import fr.hadriel.math.Matrix3f;
import fr.hadriel.math.Vec2;
import fr.hadriel.math.Vec4;
import fr.hadriel.util.Timer;
import org.lwjgl.glfw.GLFW;

import java.io.IOException;

/**
 * Created by glathuiliere on 13/02/2017.
 */
public class TestTTF {
    public static void main(String[] args) throws IOException {
        G2DWindow window = new G2DWindow();
        Widget w = new Widget() {
            private TrueTypeFont font;
            private String text = "Look at my Awesome Helloworld";
            private float fontHeight = 30f;
            private float fontHeightSpeed = 0;

            {
                addEventHandler(KeyPressedEvent.class, (event) -> {
                    if(event.key == GLFW.GLFW_KEY_UP) fontHeightSpeed = 4;
                    if(event.key == GLFW.GLFW_KEY_DOWN) fontHeightSpeed = -4;
                });
                addEventHandler(KeyReleasedEvent.class, (event) -> {
                    if(event.key == GLFW.GLFW_KEY_UP) fontHeightSpeed = 0;
                    if(event.key == GLFW.GLFW_KEY_DOWN) fontHeightSpeed = 0;
                });
            }

            private Timer t = new Timer();

            protected void onRender(BatchGraphics g) {
                fontHeight += fontHeightSpeed * t.elapsed();
                t.reset();

                if(font == null) {
                    try {
                        font = new TrueTypeFont("berylium.TTF");
                    } catch (IOException ignore) {}
                }
                String fulltext = String.format("%s in %.2f p", text, fontHeight);

                Vec2 textSize = font.getSizeOfString(fulltext, fontHeight);
                g.push(Matrix3f.Translation((window.getWidth() - textSize.x) / 2, (window.getHeight() - textSize.y) / 2));
                font.drawText(g, fulltext, fontHeight, new Vec4(1, 1, 1, 1));
                g.pop();
            }
        };
        w.requestFocus();
        window.getRoot().add(w);
    }
}
