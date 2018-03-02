package fr.hadriel;

import fr.hadriel.window.Window;
import fr.hadriel.window.WindowHint;
import org.lwjgl.vulkan.VkInstance;

import static org.lwjgl.glfw.GLFW.*;

public abstract class VulkanApplication {

    //GLFW Window
    private Window window;

    //Vulkan internal variables
    private VkInstance instance;

    protected abstract void start();
    protected abstract void update(float deltaTime);
    protected abstract void render();
    protected abstract void terminate();

    private void initialize(WindowHint hint) {
        glfwInit();
        window = new Window(hint);
        initializeVulkan();
        start();
    }

    private void initializeVulkan() {

    }

    private void mainloop() {
        while (!window.shouldClose()) {
            glfwPollEvents();
            window.render();
        }
        window.close();
    }

    private void uninintialize() {
        terminate();
        glfwTerminate();
    }

    public static void launch(VulkanApplication application, WindowHint hint) {
        application.initialize(hint);
        application.mainloop();
        application.uninintialize();
    }
}