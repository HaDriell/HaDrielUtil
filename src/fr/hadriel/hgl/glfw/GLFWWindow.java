package fr.hadriel.hgl.glfw;

import org.lwjgl.opengl.GLCapabilities;

/**
 * Created by HaDriel setOn 04/12/2016.
 */
public abstract class GLFWWindow {
    Long handle = null;
    GLCapabilities capabilities;

    protected GLFWWindow() {
        GLFWThread.create(this, null);
    }

    protected GLFWWindow(GLFWWindowHint definition) {
        GLFWThread.create(this, definition);
    }

    public abstract void onInit();
    public abstract void onDestroy();
    public abstract void onKey(long window, int key, int scancode, int action, int mods);
    public abstract void onMouseButton(long window, int button, int action, int mods);
    public abstract void onMousePos(long window, double xpos, double ypos);
    public abstract void onCursorEnter(long window, boolean entered);
    public abstract void onWindowFocus(long window, boolean focus);
    public abstract void onRender(long window);
    public abstract void onWindowClose(long window);
    public abstract void onWindowSize(long window, int width, int height);
}