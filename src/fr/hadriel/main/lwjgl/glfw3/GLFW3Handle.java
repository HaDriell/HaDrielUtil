package fr.hadriel.main.lwjgl.glfw3;

import fr.hadriel.main.lwjgl.core.GraphicsThread;
import fr.hadriel.main.math.Vec2;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GLCapabilities;

import java.util.concurrent.CountDownLatch;

import static org.lwjgl.glfw.GLFW.*;

/**
 * Created by glathuiliere on 20/07/2017.
 */
public class GLFW3Handle {

    private long window = -1;
    private GLCapabilities capabilities = null; // not sure if required for each GLFW3Handle
    private GLFWWindowRenderCallbackI renderCallback;

    public GLFW3Handle(WindowHint windowHint) {
        final WindowHint hint = windowHint == null ? new WindowHint() : windowHint;
        final CountDownLatch cd = new CountDownLatch(1);

        GraphicsThread.submit(() -> {
            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            //Configure the Window properties
            glfwDefaultWindowHints();
            glfwWindowHint(GLFW_RESIZABLE, hint.fullscreen ? GLFW_FALSE : hint.resizable ? GLFW_TRUE : GLFW_FALSE);
            glfwWindowHint(GLFW_DECORATED, hint.fullscreen ? GLFW_FALSE : hint.decorated ? GLFW_TRUE : GLFW_FALSE);
            glfwWindowHint(GLFW_VISIBLE, hint.visible ? GLFW_TRUE : GLFW_FALSE);

            //Configure the Window Pos & Size
            int x = hint.x == GLFW_DONT_CARE ? (vidmode.width() - hint.width) / 2 : hint.x;
            int y = hint.y == GLFW_DONT_CARE ? (vidmode.height() - hint.height) / 2 : hint.y;
            int width = hint.fullscreen ? vidmode.width() : hint.width;
            int height = hint.fullscreen ? vidmode.height() : hint.height;
            long windowHandle = glfwCreateWindow(width, height, hint.title, 0, 0);
            glfwSetWindowPos(windowHandle, x , y);
            glfwMakeContextCurrent(windowHandle);

            this.window = windowHandle;
            this.capabilities = GL.createCapabilities();
            cd.countDown();
        });
        GLFW3WindowManager.register(this);
        try { cd.await(); } catch (InterruptedException exception) {}
    }

    private void checkHandle() {
        if(window < 0)
            throw new RuntimeException("Unable to use an unitialized GLFW3Handle");
    }

    public void update() {
        checkHandle();
        glfwMakeContextCurrent(window);
        GL.setCapabilities(capabilities);
        if(renderCallback != null) renderCallback.render(window);
        glfwSwapBuffers(window);
        glfwPollEvents();
    }

    public void destroy() {
        checkHandle();
        glfwDestroyWindow(window);
        window = -1;
        capabilities = null;
        GLFW3WindowManager.unregister(this);
    }

    public boolean isDestroyed() {
        return window == -1;
    }

    public boolean isClosing() {
        checkHandle();
        return glfwWindowShouldClose(window);
    }

    public void close() {
        checkHandle();
        glfwSetWindowShouldClose(window, true);
    }

    public void show() {
        checkHandle();
        glfwShowWindow(window);
    }

    public void hide() {
        checkHandle();
        glfwHideWindow(window);
    }

    public void maximize() {
        checkHandle();
        glfwMaximizeWindow(window);
    }

    public void restore() {
        checkHandle();
        glfwRestoreWindow(window);
    }

    public void setPosition(int x, int y) {
        checkHandle();
        glfwSetWindowPos(window, x, y);
    }

    public Vec2 getPosition() {
        checkHandle();
        int[] x = new int[1];
        int[] y = new int[1];
        glfwGetWindowPos(window, x, y);
        return new Vec2(x[0], y[0]);
    }

    public void setSize(int width, int height) {
        checkHandle();
        glfwSetWindowSize(window, width, height);
    }

    public Vec2 getSize() {
        checkHandle();
        int[] x = new int[1];
        int[] y = new int[1];
        glfwGetWindowSize(window, x, y);
        return new Vec2(x[0], y[0]);
    }

    public void setSizeLimits(int minWidth, int minHeight, int maxWidth, int maxHeight) {
        checkHandle();
        glfwSetWindowSizeLimits(window, minWidth, minHeight, maxWidth, maxHeight);
    }

    public void bindKey(GLFWKeyCallbackI callback) {
        checkHandle();
        glfwSetKeyCallback(window, callback);
    }

    public void bindWindowPos(GLFWWindowPosCallbackI callback) {
        checkHandle();
        glfwSetWindowPosCallback(window, callback);
    }

    public void bindWindowClose(GLFWWindowCloseCallbackI callback) {
        checkHandle();
        glfwSetWindowCloseCallback(window, callback);
    }

    public void bindWindowFocus(GLFWWindowFocusCallbackI callback) {
        checkHandle();
        glfwSetWindowFocusCallback(window, callback);
    }

    public void bindWindowIconify(GLFWWindowIconifyCallbackI callback) {
        checkHandle();
        glfwSetWindowIconifyCallback(window, callback);
    }

    public void bindWindowMaximize(GLFWWindowMaximizeCallbackI callback) {
        checkHandle();
        glfwSetWindowMaximizeCallback(window, callback);
    }

    public void bindWindowRefresh(GLFWWindowRefreshCallbackI callback) {
        checkHandle();
        glfwSetWindowRefreshCallback(window, callback);
    }

    public void bindWindowSize(GLFWWindowSizeCallbackI callback) {
        checkHandle();
        glfwSetWindowSizeCallback(window, callback);
    }

    public void bindWindowRender(GLFWWindowRenderCallbackI callback) {
        this.renderCallback = callback;
    }

    public void bindCursorEnter(GLFWCursorEnterCallbackI callback) {
        checkHandle();
        glfwSetCursorEnterCallback(window, callback);
    }

    public void bindCursorPos(GLFWCursorPosCallbackI callback) {
        checkHandle();
        glfwSetCursorPosCallback(window, callback);
    }

    public void bindMouseButton(GLFWMouseButtonCallbackI callback) {
        checkHandle();
        glfwSetMouseButtonCallback(window, callback);
    }
}