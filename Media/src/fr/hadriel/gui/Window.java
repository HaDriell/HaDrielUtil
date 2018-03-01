package fr.hadriel.gui;

import fr.hadriel.graphics.glfw.GLFWwindow;
import fr.hadriel.graphics.glfw.WindowHint;

public final class Window {

    private GLFWwindow window;

    public Window() {
        this(new WindowHint()); //default hints
    }

    public Window(WindowHint hint) {
        this.window = new GLFWwindow(hint);

        while(!window.isExecuting()) {
            try { Thread.sleep(1); } catch (InterruptedException ignore) {}
        }

        //Input Callbacks
        window.bindKey(this::onKey);
        window.bindMouseButton(this::onMouseButton);
        window.bindCursorPos(this::onMousePos);
        window.bindCursorEnter(this::onCursorEnter);
        //Window Callbacks
        window.bindWindowClose(this::onWindowClose);
        window.bindWindowFocus(this::onWindowFocus);
        window.bindWindowSize(this::onWindowSize);
        //Render Callback
        window.bindWindowRender(this::onRender);
    }

    public void onKey(long window, int key, int scancode, int action, int mods) {
        System.out.println("Key=" + key + " action=" + action);
//        if(action == GLFW.GLFW_RELEASE) scene.onEvent(new KeyReleasedEvent(key, mods));
//        if(action == GLFW.GLFW_PRESS) scene.onEvent(new KeyPressedEvent(key, mods));
    }

    public void onMouseButton(long window, int button, int action, int mods) {
//        if(action == GLFW.GLFW_RELEASE) scene.onEvent(new MouseReleasedEvent(mouse.x, mouse.y, button));
//        if(action == GLFW.GLFW_PRESS) scene.onEvent(new MousePressedEvent(mouse.x, mouse.y, button));
    }

    public void onMousePos(long window, double xpos, double ypos) {
//        mouse = new Vec2((float) xpos, (float) ypos);
//        scene.onEvent(new MouseMovedEvent(mouse, false));
    }

    public void onCursorEnter(long window, boolean entered) {
//        if(entered)
//            scene.onEvent(new MouseEnterEvent());
//        else
//            scene.onEvent(new MouseExitEvent());
    }

    public void onWindowFocus(long window, boolean focus) {
//        if(focus)
//            scene.onEvent(new FocusGainEvent());
//        else
//            scene.onEvent(new FocusLostEvent());
    }

    public void onRender(long window) {
//        if(g == null) return; //initialization is async now :/
//        g.begin();
//        g.settings().color(0, 0, 0, 1);
//        g.fillRect(0, 0, properties.width, properties.height);
//        if(scene != null) {
//            scene.draw(g);
//        }
//        g.end();
//        g.clear();
    }

    public void onWindowClose(long window) {}

    public void onWindowSize(long window, int width, int height) {
//        properties.width = width;
//        properties.height = height;
//        GL11.glViewport(0, 0, properties.width, properties.height);
    }
}