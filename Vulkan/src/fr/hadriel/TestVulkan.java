package fr.hadriel;

import fr.hadriel.window.Window;
import org.lwjgl.glfw.GLFW;
import static org.lwjgl.vulkan.VK10.*;

public class TestVulkan {

    public static void main(String[] args) {
        GLFW.glfwInit();
        Window window = new Window();
        window.show();

        int[] count = new int[1];
        vkEnumerateInstanceExtensionProperties("", count, null);

        System.out.println(count[0] + " extensions supported");

        while (!window.shouldClose()) {
            GLFW.glfwPollEvents();
            window.render();
        }
        GLFW.glfwTerminate();
    }
}