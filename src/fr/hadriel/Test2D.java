package fr.hadriel;

import fr.hadriel.lwjgl.g2d.events.MouseMovedEvent;
import fr.hadriel.lwjgl.g2d.BatchGraphics;
import fr.hadriel.lwjgl.g2d.BatchRenderer;
import fr.hadriel.event.Node;
import fr.hadriel.lwjgl.glfw.GLFWWindow;
import fr.hadriel.lwjgl.opengl.Texture2D;
import fr.hadriel.lwjgl.opengl.TextureRegion;
import fr.hadriel.math.Vec2;

import java.io.IOException;

/**
 * Created by glathuiliere on 31/01/2017.
 */
public class Test2D {

    public static void main(String[] args) {
        new GLFWWindow() {

            private Texture2D a, b;
            private TextureRegion regionA, regionb;
            private BatchRenderer batch;
            private BatchGraphics g;
            private Node root;

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
//                root = new Node();
//                Node group = new Node();
//                group.rotate(45);
//                group.translate(100, 100);
//                root.add(group);
            }

            private Vec2 mouse = new Vec2();

            public void onRender(long window) {
                g.begin();
                g.setColor(1, 1, 1, 1);
                g.fillRect(0, 0, 800, 450);
//                root.render(g);
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
