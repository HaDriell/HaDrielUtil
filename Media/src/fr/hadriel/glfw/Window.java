package fr.hadriel.glfw;

import fr.hadriel.core.media.GFX;
import fr.hadriel.event.EventDispatcher;
import fr.hadriel.glfw.event.*;
import fr.hadriel.math.Vec2;

import java.util.concurrent.CountDownLatch;

import static org.lwjgl.glfw.GLFW.*;

public class Window {

    private final GLFWHandle handle;
    private final EventDispatcher dispatcher;
    private Vec2 mouse;

    public Window() {
        this(new WindowHint()); // use default hints
    }

    public Window(WindowHint hint) {
        this.handle = new GLFWHandle(hint);
        this.dispatcher = new EventDispatcher(true);

        //We want to have a synchronized Window initialization
        //Wait for init to be done before continuing
        final CountDownLatch initLatch = new CountDownLatch(1);
        handle.setOnBeginCallback(initLatch::countDown);
        GFX.add(handle);
        try { initLatch.await(); } catch (InterruptedException ignore) {}

        //Input Callbacks
        handle.bindKey(this::onKey);
        handle.bindMouseButton(this::onMouseButton);
        handle.bindCursorPos(this::onMousePos);
        handle.bindCursorEnter(this::onCursorEnter);
        //Window Callbacks
        handle.bindWindowClose(this::onWindowClose);
        handle.bindWindowFocus(this::onWindowFocus);
        handle.bindWindowSize(this::onWindowSize);
        //Render Callback
        handle.bindWindowRender(this::onRender);
    }

    public void show() {
        handle.show();
    }

    public void hide() {
        handle.hide();
    }

    public void maximize() {
        handle.maximize();
    }

    public void iconify() {
        handle.iconify();
    }

    public void restore() {
        handle.restore();
    }

    public void close() {
        handle.close();
    }

    private void onKey(long window, int key, int scancode, int action, int mods) {
        if(action == GLFW_RELEASE) dispatcher.onEvent(new KeyReleasedEvent(key, mods));
        if(action == GLFW_PRESS) dispatcher.onEvent(new KeyPressedEvent(key, mods));
    }

    private void onMouseButton(long window, int button, int action, int mods) {
        if(action == GLFW_RELEASE) dispatcher.onEvent(new MouseReleasedEvent(mouse.x, mouse.y, button));
        if(action == GLFW_PRESS) dispatcher.onEvent(new MousePressedEvent(mouse.x, mouse.y, button));
    }

    private void onMousePos(long window, double xpos, double ypos) {
        mouse = new Vec2((float) xpos, (float) ypos);
        dispatcher.onEvent(new MouseMovedEvent(mouse, false));
    }

    private void onCursorEnter(long window, boolean entered) {
        if(entered)
            dispatcher.onEvent(new MouseEnterEvent());
        else
            dispatcher.onEvent(new MouseLeaveEvent());
    }

    private void onWindowFocus(long window, boolean focus) {
        if(focus)
            dispatcher.onEvent(new FocusGainEvent());
        else
            dispatcher.onEvent(new FocusLostEvent());
    }

    private void onRender(long window) {
        dispatcher.onEvent(new RenderEvent());
    }

    private void onWindowClose(long window) {
        dispatcher.onEvent(new WindowClose());
    }

    private void onWindowSize(long window, int width, int height) {
        dispatcher.onEvent(new WindowResize(width, height));
    }
}