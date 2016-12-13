package fr.hadriel;

import fr.hadriel.events.MouseMovedEvent;
import fr.hadriel.hgl.g2d.BatchGraphics;
import fr.hadriel.hgl.g2d.BatchRenderer;
import fr.hadriel.hgl.g2d.ui.Group;
import fr.hadriel.hgl.g2d.ui.Node;
import fr.hadriel.hgl.g2d.ui.Transform;
import fr.hadriel.hgl.glfw.GLFWWindow;
import fr.hadriel.hgl.opengl.Texture2D;
import fr.hadriel.hgl.opengl.TextureRegion;
import fr.hadriel.math.Vec2;

import java.io.IOException;

/**
 * Created by HaDriel setOn 08/12/2016.
 */
public class BatchTest {
    public static void main(String[] args) {
        new GLFWWindow() {

            private Texture2D a, b;
            private TextureRegion regionA, regionb;
            private BatchRenderer batch;
            private BatchGraphics g;
            private Group root = new Group();

            public void onInit() {
                try {
                    a = new Texture2D("Teron Fielsang.png");
                    b = new Texture2D("illuminati.png");
                    regionA = a.getRegion(0, 0, a.width, a.height);
                    regionb = b.getRegion(0, 0, b.width, b.height);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                batch = new BatchRenderer(0, 800, 0, 450);
                g = new BatchGraphics(batch);

                // Scene Graph
                Transform transform = new Transform();
                transform.translate(100, 100);
                transform.add(new Node() {
                    boolean hit;

                    {
                        setSize(100, 100);
                        setOn(MouseMovedEvent.class, (event) -> {
                            hit = isHit(event.x, event.y);
                            return hit;
                        });
                    }

                    public void onRender(BatchGraphics g) {
                        if(!hit) g.setColor(1, 0, 0, 1);
                        else g.setColor(0, 0, 1, 1);
                        g.fillRect(0, 0, getWidth(), getHeight());
                    }
                });

                root.add(transform);
            }

            private Vec2 mouse = new Vec2();

            public void onRender(long window) {
                g.begin();
                root.onRender(g);
                g.end();
            }

            public void onKey(long window, int key, int scancode, int action, int mods) {}
            public void onDestroy() {}
            public void onMouseButton(long window, int button, int action, int mods) {
            }

            public void onMousePos(long window, double xpos, double ypos) {
                mouse.set((float) xpos, (float) ypos);
                root.onEvent(new MouseMovedEvent(mouse, false));
            }

            public void onCursorEnter(long window, boolean entered) {}
            public void onWindowFocus(long window, boolean focus) {}
            public void onWindowClose(long window) {}
            public void onWindowSize(long window, int width, int height) {}
        };
    }
}