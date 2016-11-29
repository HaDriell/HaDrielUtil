package fr.hadriel.hgl.core;

import fr.hadriel.event.MultiEventListener;
import fr.hadriel.hgl.WindowConfig;
import fr.hadriel.hgl.core.events.HGLContextCreated;
import fr.hadriel.hgl.core.events.HGLContextDestroyed;
import fr.hadriel.hgl.core.events.HGLEvent;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import static org.lwjgl.glfw.GLFW.*;

/**
 * Created by glathuiliere on 28/11/2016.
 */
public class HGL {

    private static HGL instance = new HGL();

    public static void submitEvent(HGLEvent event) {
        instance.queueEvent(event);
    }

    // -------------------- ENVIRONMENT ---------------------------- //

    private List<HGLContext> contexts;

    private MultiEventListener listener;
    private Queue<HGLEvent> events;
    private Lock eventLock = new ReentrantLock();

    private Thread thread;
    private boolean running = false;

    private HGL() {
        if(!glfwInit()) {
            throw new IllegalStateException("Unable to setup GLFW Environnement");
        }
        contexts = new ArrayList<>();
        events = new LinkedList<>();
        listener = new MultiEventListener();
        listener.setHandler(HGLContextCreated.class, this::onContextCreated);
        listener.setHandler(HGLContextDestroyed.class, this::onContextDestroyed);
    }

    public void queueEvent(HGLEvent event) {
        eventLock.lock();
        events.add(event);
        eventLock.unlock();
        start(); // (re)start on event queueEvent
    }

    private synchronized void start() {
        if(running) return;
        running = true;
        thread = new Thread(this::update, "HGLThread");
        thread.start();
    }

    private synchronized void interrupt() {
        if(!running) return;
        running = false;
        thread.interrupt();
    }

    private void update() {
        do {
            eventLock.lock();
            while (events.size() > 0) {
                HGLEvent event = events.poll();
                listener.onEvent(event);
                event.consume();
            }
            eventLock.unlock();

            glfwPollEvents();
            for (HGLContext context : contexts) {
                if(glfwWindowShouldClose(context.getWindow())) {
                    queueEvent(new HGLContextDestroyed(context)); // auto destroy this context on next update
                    continue;
                }
                glfwMakeContextCurrent(context.getWindow());
                GL.setCapabilities(context.getCapabilities());
                context.onRender();
            }
        } while (contexts.size() > 0);
    }


    //Context initialization
    private boolean onContextCreated(HGLContextCreated event) {
        if(!contexts.contains(event.context)) {
            GLFWVidMode screen = glfwGetVideoMode(glfwGetPrimaryMonitor());
            glfwDefaultWindowHints();
            WindowConfig config = event.configuration;
            HGLContext context = event.context;
            int width = config.fullscreen ? screen.width() : config.width;
            int height = config.fullscreen ? screen.height() : config.height;
            boolean decorated = !config.fullscreen && config.decorated;

            glfwWindowHint(GLFW_RESIZABLE, config.visible ? GLFW_TRUE : GLFW_FALSE);
            glfwWindowHint(GLFW_VISIBLE, config.resizable ? GLFW_TRUE : GLFW_FALSE);
            glfwWindowHint(GLFW_DECORATED, decorated ? GLFW_TRUE : GLFW_FALSE);

            long window = glfwCreateWindow(width, height, config.title, 0, 0);
            glfwSetWindowPos(window, (screen.width() - width) / 2, (screen.height() -height) / 2);

            //Bind OpenGL backend to this GLFW Window
            glfwMakeContextCurrent(window);
            context.setWindow(window);
            context.setCapabilities(GL.createCapabilities());
            context.onInit();

            //Input Callbacks
            glfwSetKeyCallback(window, (w, key, scancode, action, mods) -> {
                if (w == window) context.onKey(key, scancode, action, mods);
            });
            glfwSetMouseButtonCallback(window, (w, button, action, mods) -> {
                if (w == window) context.onMouse(button, action, mods);
            });
            glfwSetScrollCallback(window, (w, xoffset, yoffset) -> {
                if (w == window) context.onScroll(xoffset, yoffset);
            });
            glfwSetCursorPosCallback(window, (w, xpos, ypos) -> {
                if (w == window) context.onCursorPos(xpos, ypos);
            });
            glfwSetCursorEnterCallback(window, (w, inside) -> {
                if (w == window) context.onCursorEnter(inside);
            });

            //Window Callbacks
            glfwSetFramebufferSizeCallback(window, (w, width1, heigh1) -> {
                if (w == window) context.onFrameBuffer(width1, heigh1);
            });
            glfwSetWindowPosCallback(window, (w, xpos, ypos) -> {
                if (w == window) context.onWindowPos(xpos, ypos);
            });
            glfwSetWindowFocusCallback(window, (w, state) -> {
                if (w == window) context.onWindowFocus(state);
            });
            glfwSetWindowSizeCallback(window, (w, width1, height1) -> {
                if (w == window) context.onWindowSize(width1, height1);
            });
            glfwSetWindowCloseCallback(window, (w) -> {
                if (w == window) context.onWindowClosed();
            });
            glfwSetWindowIconifyCallback(window, (w, iconified) -> {
                if (w == window) context.onWindowIconified(iconified);
            });
            glfwSetWindowRefreshCallback(window, (w) -> {
                if (w == window) context.onWindowRefresh();
            });
            contexts.add(event.context);
        }
        return true;
    }

    private boolean onContextDestroyed(HGLContextDestroyed event) {
        if(contexts.remove(event.context)) {
            glfwMakeContextCurrent(event.context.getWindow());
            GL.setCapabilities(event.context.getCapabilities());
            event.context.onDestroy();

            glfwDestroyWindow(event.context.getWindow());
            event.context.setWindow(0);
            event.context.setCapabilities(null);
        }
        return true;
    }
}