package fr.hadriel;

import fr.hadriel.application.Launcher;
import fr.hadriel.core.application.GUIApplication;
import fr.hadriel.gui.Graphics;
import fr.hadriel.graphics.glfw.GLFWwindow;
import fr.hadriel.graphics.glfw.WindowHint;
import fr.hadriel.graphics.opengl.Texture;
import fr.hadriel.graphics.renderers.BatchRenderer2D;
import fr.hadriel.gui.Window;
import fr.hadriel.math.Vec2;
import java.io.IOException;

/**
 *
 * @author glathuiliere
 */
public class TestBatchRendering {

    public static Vec2 cursor = new Vec2(100, 100);

    public static class App extends GUIApplication {
        protected void start(Window window) {

            Vec2 size = window.getSize();

            GUIApplication.runLater(() -> {
                try {
                    final BatchRenderer2D batch = new BatchRenderer2D(0, size.x, 0, size.y);
                    final Graphics g = new Graphics(batch);
                    final Texture texture = new Texture("Media/Teron Fielsang.png");

                    window.bindCursorPos((w, xpos, ypos) -> cursor = new Vec2(xpos, ypos));
                    window.bindWindowRender(w -> {
                        g.begin();
                        g.setColor(0, 0, 0, 1);
                        g.fillRect(0, 0, size.x, size.y);
                        g.setColor(1, 0, 0, 1);
                        g.drawRect(cursor.x, cursor.y, -10, -10); // empty rect test
                        g.fillRect(cursor.x, cursor.y, -10, 10); // filled rect test
                        g.drawLine(0, 0, cursor.x, cursor.y); // Line test
                        g.drawBezierCurve(0, 0, 0, cursor.y, cursor.x, cursor.y); // QuadraticBezierCurve test
                        g.drawBezierCurve(0, 0, cursor.x, 0, 0, cursor.y, cursor.x, cursor.y); // CubicBezierCurve test
                        g.drawTextureRegion(cursor.x, cursor.y, 32, 32, texture.getRegion()); // texture region test
                        g.end();
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }



    public static void main(String[] args) {
        Launcher.launch(App.class);
    }
}