package fr.hadriel.application;

import fr.hadriel.asset.AssetManager;
import fr.hadriel.audio.Audio2D;
import fr.hadriel.util.Timer;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.openal.ALCCapabilities;
import org.lwjgl.openal.ALCapabilities;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GLCapabilities;

import static org.lwjgl.glfw.GLFW.*;

public abstract class Application {

    private static final int LAUNCHING = 0x1;
    private static final int UPDATING = 0x2;
    private static final int TERMINATING = 0x3;

    //OpenAL Context


    //OpenGL/GLFW Context
    private long window;
    private GLCapabilities capabilities;

    private long applicationState;

    protected final AssetManager manager;

    protected Application() {
        this.applicationState = LAUNCHING;
        this.manager = new AssetManager();
        this.window = -1;
        this.capabilities = null;
    }

    public void close() {
        if(applicationState == UPDATING)
            applicationState = TERMINATING;
    }

    public boolean isLaunching() {
        return applicationState == LAUNCHING;
    }

    public boolean isUpdating() {
        return applicationState == UPDATING;
    }

    public boolean isTerminating() {
        return applicationState == TERMINATING;
    }

    private void _init(WindowHint hint, String[] args) {

        // OpenGL INIT
        glfwInit();
        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        //Configure the Window hint
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_RESIZABLE, hint.fullscreen ? GLFW_FALSE : hint.resizable ? GLFW_TRUE : GLFW_FALSE);
        glfwWindowHint(GLFW_DECORATED, hint.fullscreen ? GLFW_FALSE : hint.decorated ? GLFW_TRUE : GLFW_FALSE);
        glfwWindowHint(GLFW_VISIBLE, hint.visible ? GLFW_TRUE : GLFW_FALSE);

        //Configure the Window Pos & Size
        int x = hint.x == GLFW_DONT_CARE ? (vidmode.width() - hint.width) / 2 : hint.x;
        int y = hint.y == GLFW_DONT_CARE ? (vidmode.height() - hint.height) / 2 : hint.y;
        int width = hint.fullscreen ? vidmode.width() : hint.width;
        int height = hint.fullscreen ? vidmode.height() : hint.height;
        window = glfwCreateWindow(width, height, hint.title, 0, 0);
        glfwSetWindowPos(window, x , y);
        glfwMakeContextCurrent(window);
        capabilities = GL.createCapabilities();

        // OpenAL INIT
        Audio2D.initialize();
        applicationState = UPDATING;

        // End-User INIT
        start(args);
    }

    private void _update(float delta) {
        glfwPollEvents();
        if(glfwWindowShouldClose(window)) {
            close();
            return;
        }
        glfwMakeContextCurrent(window);
        GL.setCapabilities(capabilities);
        update(delta);
        glfwSwapBuffers(window);
    }

    private void _terminate() {
        terminate();
        Audio2D.terminate();
        glfwTerminate();
    }

    protected abstract void start(String[] args);
    protected abstract void update(float delta);
    protected abstract void terminate();


    // Launchers

    public static void launch(Application application) {
        launch(application, new WindowHint());
    }

    public static void launch(Application application, WindowHint hint) {
        launch(application, hint, new String[] {});
    }

    public static void launch(Application application, WindowHint hint, String[] args) {
        application._init(hint, args);
        Timer timer = new Timer();
        while (!application.isTerminating()) {
            float delta = timer.elapsed();
            timer.reset();
            application._update(delta);
        }
        application._terminate();
    }
}