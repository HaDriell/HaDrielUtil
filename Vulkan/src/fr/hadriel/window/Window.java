package fr.hadriel.window;

import fr.hadriel.event.EventDispatcher;
import fr.hadriel.math.Vec2;
import fr.hadriel.window.event.*;
import org.lwjgl.glfw.GLFWVidMode;

import static org.lwjgl.glfw.GLFW.*;

public class Window {

    private EventDispatcher dispatcher;
    private long window;

    private Vec2 mouse = new Vec2();


    public Window() {
        this(new WindowHint());
    }

    public Window(WindowHint hint) {
        this.dispatcher = new EventDispatcher(true);

        //Configure the Window hints
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_RESIZABLE, hint.fullscreen ? GLFW_FALSE : hint.resizable ? GLFW_TRUE : GLFW_FALSE);
        glfwWindowHint(GLFW_DECORATED, hint.fullscreen ? GLFW_FALSE : hint.decorated ? GLFW_TRUE : GLFW_FALSE);
        glfwWindowHint(GLFW_VISIBLE, hint.visible ? GLFW_TRUE : GLFW_FALSE);
        glfwWindowHint(GLFW_CLIENT_API, GLFW_NO_API); // Delegate Window management to Vulkan

        //Configure the Window Size
        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        int windowWidth = hint.fullscreen ? vidmode.width() : hint.width;
        int windowHeight = hint.fullscreen ? vidmode.height() : hint.height;
        long windowHandle = glfwCreateWindow(windowWidth, windowHeight, hint.title, 0, 0);

        //Configure the Window Position
        int x = hint.x == GLFW_DONT_CARE ? (vidmode.width() - windowWidth) / 2 : hint.x;
        int y = hint.y == GLFW_DONT_CARE ? (vidmode.height() - windowHeight) / 2 : hint.y;
        glfwSetWindowPos(windowHandle, x , y);

        this.window = windowHandle;

        glfwSetWindowCloseCallback(window, (w) -> dispatcher.onEvent(new WindowClose()));
        glfwSetWindowFocusCallback(window, (w, focus) -> dispatcher.onEvent(focus ? new WindowFocusGainEvent() : new WindowFocusLostEvent()));
        glfwSetWindowSizeCallback(window, (w, width, height) -> dispatcher.onEvent(new WindowResizeEvent(width, height)));
        glfwSetMouseButtonCallback(window, (w, button, action, mods) -> {
            if(action == GLFW_RELEASE) dispatcher.onEvent(new MouseReleasedEvent(mouse.x, mouse.y, button));
            if(action == GLFW_PRESS) dispatcher.onEvent(new MousePressedEvent(mouse.x, mouse.y, button));
        });
        glfwSetCursorPosCallback(window, (w, xpos, ypos) -> {
            mouse = new Vec2(xpos, ypos);
            dispatcher.onEvent(new MouseMovedEvent(mouse));
        });
        glfwSetCursorEnterCallback(window, (w, enter) -> dispatcher.onEvent(enter ? new MouseEnterWindowEvent() : new MouseLeaveWindowEvent()));
        glfwSetKeyCallback(window, (w, key, scancode, action, mods) -> {
            if(action == GLFW_RELEASE) dispatcher.onEvent(new KeyReleasedEvent(key, mods));
            if(action == GLFW_PRESS) dispatcher.onEvent(new KeyPressedEvent(key, mods));
        });
    }

    public EventDispatcher getDispatcher() {
        return dispatcher;
    }

    public void render() {
        dispatcher.onEvent(new RenderEvent());
        glfwSwapBuffers(window);
    }

    public void show() {
        glfwShowWindow(window);
    }

    public void hide() {
        glfwHideWindow(window);
    }

    public void maximize() {
        glfwMaximizeWindow(window);
    }

    public void iconify() {
        glfwIconifyWindow(window);
    }

    public void restore() {
        glfwRestoreWindow(window);
    }

    public void close() {
        glfwSetWindowShouldClose(window, true);
    }

    public boolean shouldClose() {
        return glfwWindowShouldClose(window);
    }

    //TODO : add more direct control on the window
}