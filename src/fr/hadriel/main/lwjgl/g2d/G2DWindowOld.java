package fr.hadriel.main.lwjgl.g2d;

import fr.hadriel.main.lwjgl.g2d.event.*;
import fr.hadriel.main.lwjgl.g2d.ui.Scene;
import fr.hadriel.main.lwjgl.glfw.GLFWWindow;
import fr.hadriel.main.lwjgl.glfw.GLFWWindowHint;
import fr.hadriel.main.math.Vec2;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

/**
 * Created by glathuiliere on 08/02/2017.
 */
public class G2DWindowOld extends GLFWWindow {

    private GLFWWindowHint properties;
    private BatchRenderer renderer;
    private BatchGraphics g;
    private Scene scene;

    private Vec2 mouse;

    public G2DWindowOld(GLFWWindowHint hint) {
        super(hint);
        this.properties = hint;
        this.mouse = new Vec2();
        this.scene = new Scene();
    }

    public G2DWindowOld() {
        this(new GLFWWindowHint()); //default hints
    }

    public int getWidth() {
        return properties.width;
    }

    public int getHeight() {
        return properties.height;
    }

    public Scene getScene() {
        return scene;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    public void onInit() {
        renderer = new BatchRenderer(0, properties.width, 0, properties.height);
        g = new BatchGraphics(renderer);
    }

    public void onDestroy() {
        //Sadly not supported yet
    }

    public void onKey(long window, int key, int scancode, int action, int mods) {
        if(action == GLFW.GLFW_RELEASE) scene.onEvent(new KeyReleasedEvent(key, mods));
        if(action == GLFW.GLFW_PRESS) scene.onEvent(new KeyPressedEvent(key, mods));
    }

    public void onMouseButton(long window, int button, int action, int mods) {
        if(action == GLFW.GLFW_RELEASE) scene.onEvent(new MouseReleasedEvent(mouse.x, mouse.y, button));
        if(action == GLFW.GLFW_PRESS) scene.onEvent(new MousePressedEvent(mouse.x, mouse.y, button));
    }

    public void onMousePos(long window, double xpos, double ypos) {
        mouse = new Vec2((float) xpos, (float) ypos);
        scene.onEvent(new MouseMovedEvent(mouse, false));
    }

    public void onCursorEnter(long window, boolean entered) {
        if(entered)
            scene.onEvent(new MouseEnterEvent());
        else
            scene.onEvent(new MouseExitEvent());
    }

    public void onWindowFocus(long window, boolean focus) {
        if(focus)
            scene.onEvent(new FocusGainEvent());
        else
            scene.onEvent(new FocusLostEvent());
    }

    public void onRender(long window) {
        g.begin();
        g.setColor(0, 0, 0, 1);
        g.fillRect(0, 0, properties.width, properties.height);
        if(scene != null) {
            scene.render(g);
        }
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