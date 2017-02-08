package fr.hadriel.lwjgl.g2d;

import fr.hadriel.lwjgl.g2d.events.*;
import fr.hadriel.lwjgl.g2d.ui.Group;
import fr.hadriel.lwjgl.g2d.ui.Widget;
import fr.hadriel.lwjgl.glfw.GLFWWindow;
import fr.hadriel.lwjgl.glfw.GLFWWindowHint;
import fr.hadriel.math.Vec2;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

/**
 * Created by glathuiliere on 08/02/2017.
 */
public class G2DWindow extends GLFWWindow {

    private GLFWWindowHint properties;
    private BatchRenderer renderer;
    private BatchGraphics g;
    private final Group root;

    private Vec2 mouse;

    public G2DWindow(GLFWWindowHint hint) {
        super(hint);
        this.properties = hint;
        this.mouse = new Vec2();
        this.root = new Group();
    }

    public G2DWindow() {
        this(new GLFWWindowHint()); //default hints
    }

    public Group getRoot() {
        return root;
    }

    public void onInit() {
        renderer = new BatchRenderer(0, properties.width, 0, properties.height);
        g = new BatchGraphics(renderer);
    }

    public void onDestroy() {
        //Sadly not supported yet
    }

    public void onKey(long window, int key, int scancode, int action, int mods) {
        if(action == GLFW.GLFW_RELEASE) root.onEvent(new KeyReleasedEvent(key, mods));
        if(action == GLFW.GLFW_PRESS) root.onEvent(new KeyPressedEvent(key, mods));
    }

    public void onMouseButton(long window, int button, int action, int mods) {
        if(action == GLFW.GLFW_RELEASE) root.onEvent(new MouseReleasedEvent(mouse.x, mouse.y, button));
        if(action == GLFW.GLFW_PRESS) root.onEvent(new MousePressedEvent(mouse.x, mouse.y, button));
    }

    public void onMousePos(long window, double xpos, double ypos) {
        mouse.set((float) xpos, (float) ypos);
        root.onEvent(new MouseMovedEvent(mouse, false));
    }

    public void onCursorEnter(long window, boolean entered) {}
    public void onWindowFocus(long window, boolean focus) {}

    public void onRender(long window) {
        g.begin();
        g.setColor(1, 1, 1, 1);
        g.fillRect(0, 0, properties.width, properties.height);
        root.render(g);
        g.end();
        g.clear();
    }

    public void onWindowClose(long window) {}

    public void onWindowSize(long window, int width, int height) {
        properties.width = width;
        properties.height = height;
        GL11.glViewport(0, 0, properties.width, properties.height);
    }
}