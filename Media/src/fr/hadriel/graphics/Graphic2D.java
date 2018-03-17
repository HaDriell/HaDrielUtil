package fr.hadriel.graphics;

import fr.hadriel.application.event.*;
import fr.hadriel.event.EventDispatcher;
import fr.hadriel.event.IEventListener;
import fr.hadriel.math.Vec2;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GLCapabilities;

import static org.lwjgl.glfw.GLFW.*;

public final class Graphic2D {
    public static final int NO_WINDOW = -1;
    private Graphic2D() {}
    private static long handle = NO_WINDOW;
    private static GLCapabilities capabilities;
    private static EventDispatcher dispatcher;

    private static Vec2 mouse;

    public static void create(WindowHint hint) {
        if(handle != NO_WINDOW) return;
        glfwInit();
        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_RESIZABLE, hint.fullscreen ? GLFW_FALSE : hint.resizable ? GLFW_TRUE : GLFW_FALSE);
        glfwWindowHint(GLFW_DECORATED, hint.fullscreen ? GLFW_FALSE : hint.decorated ? GLFW_TRUE : GLFW_FALSE);
        glfwWindowHint(GLFW_VISIBLE, hint.visible ? GLFW_TRUE : GLFW_FALSE);

        int width = hint.fullscreen ? vidmode.width() : hint.width;
        int height = hint.fullscreen ? vidmode.height() : hint.height;
        int x = hint.x == GLFW_DONT_CARE ? (vidmode.width() - width) / 2 : hint.x;
        int y = hint.y == GLFW_DONT_CARE ? (vidmode.height() - height) / 2 : hint.y;
        handle = glfwCreateWindow(width, height, hint.title, 0, 0);
        glfwSetWindowPos(handle, x , y);
        glfwMakeContextCurrent(handle);
        capabilities = GL.createCapabilities();

        dispatcher = new EventDispatcher();
        glfwSetWindowCloseCallback(handle, window -> dispatcher.onEvent(new WindowCloseEvent()));
        glfwSetWindowFocusCallback(handle, (window, focused) -> {
            if(focused)
                dispatcher.onEvent(new FocusGainEvent());
            else
                dispatcher.onEvent(new FocusLostEvent());
        });
        glfwSetWindowRefreshCallback(handle, window -> new RenderEvent());
        glfwSetWindowSizeCallback(handle, (window, w, h) -> new WindowSizeEvent(w, h));
        glfwSetKeyCallback(handle, (window, key, scancode, action, mods) -> {
            if(action == GLFW_RELEASE)
                dispatcher.onEvent(new KeyReleasedEvent(key, mods));
            else
                dispatcher.onEvent(new KeyPressedEvent(key, mods));
        });
        glfwSetMouseButtonCallback(handle, (window, button, scancode, action) -> {
            if(action == GLFW_RELEASE)
                dispatcher.onEvent(new MouseReleasedEvent(mouse, button));
            else
                dispatcher.onEvent(new MousePressedEvent(mouse, button));
        });
        glfwSetCursorPosCallback(handle, (window, xpos, ypos) -> {
            mouse = new Vec2(xpos, ypos);
            dispatcher.onEvent(new MouseMovedEvent(mouse));
        });
    }

    public void addEventListener(IEventListener listener) {
        dispatcher.addEventListener(listener);
    }

    public void removeEventListener(IEventListener listener) {
        dispatcher.removeEventListener(listener);
    }

    public static void destroy() {
        if(handle == NO_WINDOW) return;
        glfwDestroyWindow(handle);
        glfwTerminate();
        handle = NO_WINDOW;
        capabilities = null;
        dispatcher = null;
    }

    public static void setTitle(String title) {
        glfwSetWindowTitle(handle, title);
    }

    public static void setPosition(int x, int y) {
        glfwSetWindowPos(handle, x, y);
    }

    public static void setSize(int width, int height) {
        glfwSetWindowSize(handle, width, height);
    }

    public static boolean shouldClose() {
        return glfwWindowShouldClose(handle);
    }

    public static void setVSync(boolean vsync) {
        glfwSwapInterval(vsync ? 1 : 0);
    }

    public static void update() {
        glfwMakeContextCurrent(handle);
        GL.setCapabilities(capabilities);
        glfwPollEvents();
        glfwSwapBuffers(handle);
    }
}