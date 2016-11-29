package fr.hadriel.hgl.core;

import fr.hadriel.hgl.WindowConfig;
import fr.hadriel.hgl.core.events.HGLContextCreated;
import org.lwjgl.opengl.GLCapabilities;

/**
 * Created by glathuiliere on 28/11/2016.
 */
public abstract class HGLContext {
    private long window;
    private GLCapabilities capabilities;

    public HGLContext() {
        this(new WindowConfig());
    }

    public HGLContext(WindowConfig config) {
        HGL.submitEvent(new HGLContextCreated(this, config));
    }

    public GLCapabilities getCapabilities() {
        return capabilities;
    }

    public long getWindow() {
        return window;
    }

    void setWindow(long window) {
        this.window = window;
    }

    void setCapabilities(GLCapabilities capabilities) {
        this.capabilities = capabilities;
    }

    public abstract void onInit();
    public abstract void onDestroy();

    public abstract void onRender();
    public abstract void onKey(int key, int scancode, int action, int mods);
    public abstract void onMouse(int button, int action, int mods);
    public abstract void onScroll(double xOffset, double yOffset);
    public abstract void onCursorPos(double xpos, double ypox);
    public abstract void onCursorEnter(boolean inside);

    public abstract void onFrameBuffer(int width, int height);
    public abstract void onWindowPos(int xpos, int ypos);
    public abstract void onWindowSize(int width, int height);
    public abstract void onWindowFocus(boolean focussed);
    public abstract void onWindowRefresh();
    public abstract void onWindowClosed();
    public abstract void onWindowIconified(boolean iconified);
}