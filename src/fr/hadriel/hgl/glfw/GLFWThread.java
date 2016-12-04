package fr.hadriel.hgl.glfw;

import fr.hadriel.threading.Loop;
import fr.hadriel.util.ArrayMap;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import java.util.ArrayList;
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
    public static void create(GLFWWindow handle, WindowDefinition definition) {
        instance.cacheCreationRequest(handle, definition);
    }

    private Map<GLFWWindow, WindowDefinition> creationCache;
    private Lock cacheLock = new ReentrantLock();

    private List<GLFWWindow> windows;
    private List<GLFWWindow> closed;

    private GLFWVidMode vidmode;

    private GLFWThread() {
        creationCache = new ArrayMap<>();
        windows = new ArrayList<>();
        closed = new ArrayList<>();
    }

    protected void onStart() {
        if(!glfwInit()) interrupt();
    }

    protected void onLoop() {
        createWindows();
        for(GLFWWindow window : windows) {
            if(glfwWindowShouldClose(window.handle)) { //Handle handle destruction
                glfwDestroyWindow(window.handle);
                window.handle = null;
            }
            if(window.handle == null) { //Handle cleanup
                closed.add(window);
                continue;
            }
            glfwMakeContextCurrent(window.handle);

            try { GL.getCapabilities(); }
            catch (Exception noCapabilities) { GL.createCapabilities(); }

            window.onRefresh(window.handle);
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

    public void cacheCreationRequest(GLFWWindow handle, WindowDefinition definition) {
        if(handle == null) return;
        cacheLock.lock();
        creationCache.put(handle, definition == null ? new WindowDefinition() : definition);
        cacheLock.unlock();
        //start GLFWThread if stoped
        if(!isRunning()) start();
    }

    private void createWindows() {
        cacheLock.lock();
        for(Map.Entry<GLFWWindow, WindowDefinition> e : creationCache.entrySet()) {
            GLFWWindow handle = e.getKey();
            WindowDefinition def = e.getValue();
            if(handle.handle != null) continue;
            if(vidmode == null) vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
            glfwDefaultWindowHints();
            glfwWindowHint(GLFW_RESIZABLE, def.fullscreen ? GLFW_FALSE : def.resizable ? GLFW_TRUE : GLFW_FALSE);
            glfwWindowHint(GLFW_DECORATED, def.fullscreen ? GLFW_FALSE : def.decorated ? GLFW_TRUE : GLFW_FALSE);
            glfwWindowHint(GLFW_VISIBLE, def.visible ? GLFW_TRUE : GLFW_FALSE);
            handle.handle = glfwCreateWindow(
                    def.fullscreen ? vidmode.width() : def.width,
                    def.fullscreen ? vidmode.height() : def.height,
                    def.title,
                    0,
                    0);
            windows.add(handle);
        }
        creationCache.clear();
        cacheLock.unlock();
    }
}