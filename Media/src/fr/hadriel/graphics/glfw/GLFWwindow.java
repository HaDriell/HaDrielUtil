package fr.hadriel.graphics.glfw;

import fr.hadriel.core.application.GUIApplication;
import fr.hadriel.core.application.GUIHandle;
import fr.hadriel.math.Vec2;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GLCapabilities;

import static org.lwjgl.glfw.GLFW.*;

/**
 * Created by glathuiliere on 20/07/2017.
 */
public class GLFWwindow extends GUIHandle {

    //GLFWInit loading security
    private static int handleCount;

    private static synchronized void glfwWindowCreated() {
        if(handleCount == 0)
            glfwInit();
        handleCount++;
    }

    private static synchronized void glfwWindowDestroyed() {
        handleCount--;
        if(handleCount == 0)
            glfwTerminate();
    }

    // Class Definition

    //TODO : make a "Property-like" access to the properties so it is synchronized with the actual GLFW context.
    protected WindowHint properties;

    private long window = -1;
    private GLCapabilities capabilities = null; // not sure if required for each GLFWwindow
    private GLFWWindowRenderCallbackI renderCallback;

    public GLFWwindow(WindowHint hint) {
        this.properties = hint == null ? new WindowHint() : hint;
        GUIApplication.runLater(this);
    }

    private void checkHandle() {
        if(window < 0)
            throw new RuntimeException("Unable to use an unitialized GLFWwindow");
    }

    protected void onInit() {
        glfwWindowCreated(); // ensure glfw is initialized before actually creating the window

        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        //Configure the Window properties
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_RESIZABLE, properties.fullscreen ? GLFW_FALSE : properties.resizable ? GLFW_TRUE : GLFW_FALSE);
        glfwWindowHint(GLFW_DECORATED, properties.fullscreen ? GLFW_FALSE : properties.decorated ? GLFW_TRUE : GLFW_FALSE);
        glfwWindowHint(GLFW_VISIBLE, properties.visible ? GLFW_TRUE : GLFW_FALSE);

        //Configure the Window Pos & Size
        int x = properties.x == GLFW_DONT_CARE ? (vidmode.width() - properties.width) / 2 : properties.x;
        int y = properties.y == GLFW_DONT_CARE ? (vidmode.height() - properties.height) / 2 : properties.y;
        int width = properties.fullscreen ? vidmode.width() : properties.width;
        int height = properties.fullscreen ? vidmode.height() : properties.height;
        long windowHandle = glfwCreateWindow(width, height, properties.title, 0, 0);
        glfwSetWindowPos(windowHandle, x , y);
        glfwMakeContextCurrent(windowHandle);

        this.window = windowHandle;
        this.capabilities = GL.createCapabilities();
    }

    protected boolean onUpdate(float dt) {
        checkHandle();
        glfwMakeContextCurrent(window);
        GL.setCapabilities(capabilities);
        if(renderCallback != null) renderCallback.render(window);
        glfwSwapBuffers(window);
        glfwPollEvents();
        return isClosing();
    }

    protected void onDestroy() {
        checkHandle();
        glfwDestroyWindow(window);
        window = -1;
        capabilities = null;
        glfwWindowDestroyed();
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