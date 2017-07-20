package fr.hadriel.main.lwjgl.glfw3;

import fr.hadriel.main.lwjgl.core.GraphicsThread;
import fr.hadriel.main.lwjgl.core.Hook;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;

/**
 * Created by glathuiliere on 20/07/2017.
 */
public class GLFW3WindowManager extends Hook {
    private static final GLFW3WindowManager instance = new GLFW3WindowManager();

    public static void register(GLFW3Handle handle) {
        GraphicsThread.submit(() -> instance.registerHandle(handle)); // async in order to avoid comodification Exceptions
    }

    public static void unregister(GLFW3Handle handle) {
        GraphicsThread.submit(() -> instance.unregisterHandle(handle)); // async in order to avoid comodification Exceptions
    }

    //Single Definition
    private List<GLFW3Handle> handles;

    private void registerHandle(GLFW3Handle handle) {
        if(handle == null) return;
        handles.add(handle);
    }

    private void unregisterHandle(GLFW3Handle handle) {
        if(handle == null) return;
        handles.remove(handle);
    }

    private GLFW3WindowManager() {
        this.handles = new ArrayList<>();
        GraphicsThread.register(this); // register itself on first call
    }

    public boolean isIdle() {
        return handles.isEmpty();
    }

    protected void onInit() {
        glfwInit();
    }

    public void update() {
        handles.forEach(GLFW3Handle::update);
        handles.forEach(handle -> {
            if(handle.isClosing())
                handle.destroy();
        });
        handles.removeIf(GLFW3Handle::isDestroyed);
    }

    protected void onTerminate() {
        glfwTerminate();
    }
}