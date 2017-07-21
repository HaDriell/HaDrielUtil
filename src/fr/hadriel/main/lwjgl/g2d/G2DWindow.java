package fr.hadriel.main.lwjgl.g2d;

import fr.hadriel.main.lwjgl.core.GraphicsThread;
import fr.hadriel.main.lwjgl.g2d.event.*;
import fr.hadriel.main.lwjgl.g2d.ui.Scene;
import fr.hadriel.main.lwjgl.glfw3.GLFW3Handle;
import fr.hadriel.main.lwjgl.glfw3.WindowHint;
import fr.hadriel.main.math.Vec2;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

/**
 * Created by glathuiliere on 08/02/2017.
 *
 * TODO : not an inheritance. way too permissive for the Handle to be corrupt
 */
public class G2DWindow extends GLFW3Handle {

    private BatchRenderer renderer;
    private BatchGraphics g;
    private Scene scene;

    private Vec2 mouse;
    private WindowHint properties;

    public G2DWindow(WindowHint hint) {
        super(hint);
        this.properties = hint;
        this.scene = new Scene();
        this.mouse = new Vec2();
        //Make sure the OpenGL context is initialized in the GLFW Thread
        GraphicsThread.submit(() -> {
                renderer = new BatchRenderer(0, hint.width, 0, hint.height);
                g = new BatchGraphics(renderer);
        });
        //Input Callbacks
        bindKey(this::onKey);
        bindMouseButton(this::onMouseButton);
        bindCursorPos(this::onMousePos);
        bindCursorEnter(this::onCursorEnter);
        //Window Callbacks
        bindWindowClose(this::onWindowClose);
        bindWindowFocus(this::onWindowFocus);
        bindWindowSize(this::onWindowSize);
        //Render Callback
        bindWindowRender(this::onRender);
    }

    public G2DWindow() {
        this(new WindowHint()); //default hints
    }

    public Scene getScene() {
        return scene;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
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
        if(g == null) return; //initialization is async now :/
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