package fr.hadriel.asset.graphics;

import fr.hadriel.application.event.*;
import fr.hadriel.event.EventDispatcher;
import fr.hadriel.event.IEventListener;
import fr.hadriel.math.Vec2;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GLCapabilities;
import org.lwjgl.system.MemoryStack;

import java.nio.DoubleBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.*;

public final class Graphic2D {
    public static final int NO_WINDOW = -1;
    private Graphic2D() {}
    private static long window = NO_WINDOW;
    private static GLCapabilities capabilities;
    private static EventDispatcher dispatcher;

    private static Vec2 mouse;

    public static void create(WindowHint hint) {
        if(window != NO_WINDOW)
            return;
        glfwSetErrorCallback(GLFWErrorCallback.createPrint(System.err));

        if(!glfwInit())
            throw new RuntimeException("Unable to Initialize GLFW");
        System.out.println(glfwGetVersionString());


        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_RESIZABLE, hint.fullscreen ? GLFW_FALSE : hint.resizable ? GLFW_TRUE : GLFW_FALSE);
        glfwWindowHint(GLFW_DECORATED, hint.fullscreen ? GLFW_FALSE : hint.decorated ? GLFW_TRUE : GLFW_FALSE);
        glfwWindowHint(GLFW_VISIBLE, hint.visible ? GLFW_TRUE : GLFW_FALSE);

        int x = hint.x == GLFW_DONT_CARE ? (vidmode.width() - hint.width) / 2 : hint.x;
        int y = hint.y == GLFW_DONT_CARE ? (vidmode.height() - hint.height) / 2 : hint.y;
        int width = hint.fullscreen ? vidmode.width() : hint.width;
        int height = hint.fullscreen ? vidmode.height() : hint.height;
        window = glfwCreateWindow(width, height, hint.title, 0, 0);

        glfwSetWindowPos(window, x , y);
        glfwMakeContextCurrent(window);
        capabilities = GL.createCapabilities();

        dispatcher = new EventDispatcher();
        glfwSetWindowCloseCallback(window, window -> dispatcher.onEvent(new WindowCloseEvent()));
        glfwSetWindowFocusCallback(window, (window, focused) -> {
            if(focused)
                dispatcher.onEvent(new FocusGainedEvent());
            else
                dispatcher.onEvent(new FocusLostEvent());
        });
        glfwSetWindowRefreshCallback(window, window -> new RenderEvent());
        glfwSetWindowSizeCallback(window, (window, w, h) -> new WindowSizeEvent(w, h));
        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if(action == GLFW_RELEASE)
                dispatcher.onEvent(new KeyReleasedEvent(key, mods));
            else
                dispatcher.onEvent(new KeyPressedEvent(key, mods));
        });
        glfwSetMouseButtonCallback(window, (window, button, scancode, action) -> {
            if(action == GLFW_RELEASE)
                dispatcher.onEvent(new MouseReleasedEvent(mouse, button));
            else
                dispatcher.onEvent(new MousePressedEvent(mouse, button));
        });
        glfwSetCursorPosCallback(window, (window, xpos, ypos) -> {
            mouse = new Vec2(xpos, ypos);
            dispatcher.onEvent(new MouseMovedEvent(mouse));
        });
    }

    public static void addEventListener(IEventListener listener) {
        dispatcher.addEventListener(listener);
    }

    public static void removeEventListener(IEventListener listener) {
        dispatcher.removeEventListener(listener);
    }

    public static void terminate() {
        if(window == NO_WINDOW) return;
        glfwDestroyWindow(window);
        glfwTerminate();
        window = NO_WINDOW;
        capabilities = null;
        dispatcher = null;
    }

    public static void maximize() {
        glfwMaximizeWindow(window);
    }

    public static void show() {
        glfwShowWindow(window);
    }

    public static void hide() {
        glfwHideWindow(window);
    }

    public static void iconify() {
        glfwIconifyWindow(window);
    }

    public static void setTitle(String title) {
        glfwSetWindowTitle(window, title);
    }

    public static void setPosition(int x, int y) {
        glfwSetWindowPos(window, x, y);
    }

    public static void setSize(int width, int height) {
        glfwSetWindowSize(window, width, height);
    }

    public static boolean shouldClose() {
        return glfwWindowShouldClose(window);
    }

    public static void setVSync(boolean vsync) {
        glfwSwapInterval(vsync ? GLFW_TRUE : GLFW_FALSE);
    }

    public static void makeContextCurrent() {
        glfwMakeContextCurrent(window);
        GL.setCapabilities(capabilities);
    }

    public static void update() {
        glfwPollEvents();
        glfwSwapBuffers(window);
    }

    public static Vec2 getMouse() {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            DoubleBuffer x = stack.mallocDouble(1);
            DoubleBuffer y = stack.mallocDouble(1);
            glfwGetCursorPos(window, x, y);
            return new Vec2(x.get(0), y.get(0));
        }
    }

    public static boolean isMouseButtonDown(int button) {
        return glfwGetMouseButton(window, button) != GLFW_RELEASE;
    }

    public static Vec2 getWindowSize() {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer width = stack.mallocInt(1);
            IntBuffer height = stack.mallocInt(1);
            glfwGetWindowSize(window, width, height);
            return new Vec2(width.get(0), height.get(0));
        }
    }
}