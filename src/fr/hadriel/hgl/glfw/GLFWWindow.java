package fr.hadriel.hgl.glfw;

import fr.hadriel.event.MultiEventListener;
import fr.hadriel.events.KeyPressedEvent;
import fr.hadriel.events.KeyReleasedEvent;
import fr.hadriel.events.MousePressedEvent;
import fr.hadriel.events.MouseReleasedEvent;
import org.lwjgl.opengl.GLCapabilities;

import static org.lwjgl.glfw.GLFW.*;

/**
 * Created by HaDriel on 04/12/2016.
 */
public class GLFWWindow {
    public Long handle = null;
    public GLCapabilities capabilities;
    public MultiEventListener listener = new MultiEventListener();

    public GLFWWindow() {
        GLFWThread.create(this, null);
    }

    public GLFWWindow(WindowDefinition definition) {
        GLFWThread.create(this, definition);
    }

    public void onKey(long window, int key, int scancode, int action, int mods) {
        switch (action) {
            case GLFW_PRESS: listener.onEvent(new KeyPressedEvent(key));
            case GLFW_RELEASE: listener.onEvent(new KeyReleasedEvent(key));
        }
        System.out.println(window + " : " + key);
    }

    public void onMouseButton(long window, int button, int action, int mods) {
        switch (action) {
            case GLFW_PRESS: listener.onEvent(new MousePressedEvent(0, 0, button));
            case GLFW_RELEASE: listener.onEvent(new MouseReleasedEvent(0, 0, button));
        }
    }

    public void onMousePos(long window, double xpos, double ypos) {

    }

    public void onCursorEnter(long window, boolean entered) {

    }

    public void onFocus(long window, boolean focus) {

    }

    public void onRefresh(long window) {

    }

    public void onClose(long window) {

    }

    public void onClose(long window, int width, int height) {

    }

    public void onWindowPos(long window, int xpos, int ypos) {

    }

    public void onIconify(long window, boolean iconified) {

    }

    public void onMaximize(long window, boolean maximized) {

    }
}