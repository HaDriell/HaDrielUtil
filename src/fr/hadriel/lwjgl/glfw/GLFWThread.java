package fr.hadriel.lwjgl.glfw;

import fr.hadriel.threading.Loop;
import fr.hadriel.util.ArrayMap;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static org.lwjgl.glfw.GLFW.*;
/**
 * Created by HaDriel on 04/12/2016.
 *
 * Should never work on iOS because it's not the main thread (fuck you bastards)
 */
public class GLFWThread extends Loop {
    private static GLFWThread instance = new GLFWThread();

    public static void create(GLFWWindow handle, GLFWWindowHint definition) {
        instance.cacheCreationRequest(handle, definition);
    }

    private Map<GLFWWindow, GLFWWindowHint> creationCache;
    private Lock cacheLock = new ReentrantLock();

    private List<GLFWWindow> windows;
    private List<GLFWWindow> closed;

    private GLFWVidMode vidmode;

    private GLFWThread() {
        creationCache = new HashMap<>();
        windows = new ArrayList<>();
        closed = new ArrayList<>();
    }

    protected void onStart() {
        if(!glfwInit()) interrupt();
        glfwSwapInterval(GLFW_TRUE);
    }

    protected void onLoop() {
        createWindows();
        for(GLFWWindow window : windows) {
            if(glfwWindowShouldClose(window.handle)) { //Handle handle destruction
                window.onDestroy();
                glfwDestroyWindow(window.handle);
                window.handle = null;
            }
            if(window.handle == null) { //Handle cleanup
                closed.add(window);
                continue;
            }
            glfwMakeContextCurrent(window.handle);
            GL.setCapabilities(window.capabilities);

            try { GL.getCapabilities(); }
            catch (Exception noCapabilities) { GL.createCapabilities(); }

            window.onRender(window.handle);
            glfwSwapBuffers(window.handle);
            glfwPollEvents(); // not sure if best way to do this, but i'm safe for each handle here
        }
        windows.removeAll(closed);
        closed.clear();
        if(windows.size() == 0) setRunning(false); // should close gracefully
    }

    protected void onStop() {
        glfwTerminate();
    }

    public void cacheCreationRequest(GLFWWindow handle, GLFWWindowHint definition) {
        if(handle == null) return;
        cacheLock.lock();
        creationCache.put(handle, definition == null ? new GLFWWindowHint() : definition);
        cacheLock.unlock();
        //start GLFWThread if stoped
        if(!isRunning()) start();
    }

    private void createWindows() {
        cacheLock.lock();
        for(Map.Entry<GLFWWindow, GLFWWindowHint> e : creationCache.entrySet()) {
            GLFWWindow window = e.getKey();
            GLFWWindowHint def = e.getValue();
            if(window.handle != null) continue;
            if(vidmode == null) vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
            glfwDefaultWindowHints();
            glfwWindowHint(GLFW_RESIZABLE, def.fullscreen ? GLFW_FALSE : def.resizable ? GLFW_TRUE : GLFW_FALSE);
            glfwWindowHint(GLFW_DECORATED, def.fullscreen ? GLFW_FALSE : def.decorated ? GLFW_TRUE : GLFW_FALSE);
            glfwWindowHint(GLFW_VISIBLE, def.visible ? GLFW_TRUE : GLFW_FALSE);
            window.handle = glfwCreateWindow(
                    def.fullscreen ? vidmode.width() : def.width,
                    def.fullscreen ? vidmode.height() : def.height,
                    def.title,
                    0,
                    0);
            glfwSetWindowPos(window.handle,
                    (vidmode.width() - def.width) / 2,
                    (vidmode.height() - def.height) / 2);
            glfwSetKeyCallback(window.handle, window::onKey);
            glfwSetMouseButtonCallback(window.handle, window::onMouseButton);
            glfwSetCursorPosCallback(window.handle, window::onMousePos);
            glfwSetCursorEnterCallback(window.handle, window::onCursorEnter);

            glfwSetWindowFocusCallback(window.handle, window::onWindowFocus);
            glfwSetWindowCloseCallback(window.handle, window::onWindowClose);
            glfwSetWindowSizeCallback(window.handle, window::onWindowSize);
            glfwMakeContextCurrent(window.handle);
            window.capabilities = GL.createCapabilities();
            window.onInit();
            windows.add(window);
        }
        creationCache.clear();
        cacheLock.unlock();
    }
}