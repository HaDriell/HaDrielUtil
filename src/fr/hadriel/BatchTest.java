package fr.hadriel;

import fr.hadriel.hgl.g2d.BatchGraphics;
import fr.hadriel.hgl.g2d.BatchRenderer;
import fr.hadriel.hgl.g2d.QuadraticBezierCurve;
import fr.hadriel.hgl.g2d.Transform2D;
import fr.hadriel.hgl.glfw.GLFWWindow;
import fr.hadriel.hgl.opengl.Texture2D;
import fr.hadriel.hgl.opengl.TextureRegion;
import fr.hadriel.math.Matrix4f;
import fr.hadriel.math.Vec2;
import fr.hadriel.math.Vec4;
import fr.hadriel.time.Timer;

import java.io.IOException;

/**
 * Created by HaDriel on 08/12/2016.
 */
public class BatchTest {
    public static void main(String[] args) {
        new GLFWWindow() {

            private boolean hit = false;
            private Transform2D transform;

            private Timer t = new Timer();
            private Texture2D a, b;
            private TextureRegion regionA, regionb;
            private BatchRenderer batch;
            private BatchGraphics g;
            public void onInit() {
                try {
                    a = new Texture2D("Teron Fielsang.png");
                    b = new Texture2D("illuminati.png");
                    regionA = a.getRectangle(0, 0, a.width, a.height);
                    regionb = b.getRectangle(0, 0, b.width, b.height);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                batch = new BatchRenderer(0, 800, 0, 450);
                g = new BatchGraphics(batch);
                transform = new Transform2D();
                transform.rotate(20);
                transform.translate(100, 100);
            }

            private Vec2 mouse = new Vec2();
            private Vec2 control = new Vec2();

            public void onRender(long window) {
                g.begin();
                g.setColor(0, 0, 0, 1);
                g.fillRect(0, 0, 800, 450);
                g.setColor(1, 1, 1, 1);
                g.drawBezierCurve(800, 0, control.x, control.y, mouse.x, mouse.y);
                g.end();
            }

            public void onKey(long window, int key, int scancode, int action, int mods) {}
            public void onDestroy() {}
            public void onMouseButton(long window, int button, int action, int mods) {
                control.set(mouse);
            }

            public void onMousePos(long window, double xpos, double ypos) {
                mouse.set((float) xpos, (float) ypos);
            }
            public void onCursorEnter(long window, boolean entered) {}
            public void onWindowFocus(long window, boolean focus) {}
            public void onWindowClose(long window) {}
            public void onWindowSize(long window, int width, int height) {}
        };
    }
}