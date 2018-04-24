package fr.hadriel;

import org.lwjgl.opengl.GL;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.glfw.GLFW.*;

public class TestOpenGL {
    public static void main(String[] args) {
        glfwInit();

        glfwDefaultWindowHints();
        long window = glfwCreateWindow(800, 450, "Test", 0, 0);

        glfwMakeContextCurrent(window);
        GL.createCapabilities();


        glfwSetWindowTitle(window, glGetString(GL_VERSION));

        while (!glfwWindowShouldClose(window)) {
            glfwPollEvents();
            glfwSwapBuffers(window);
        }

        glfwTerminate();
    }
}
