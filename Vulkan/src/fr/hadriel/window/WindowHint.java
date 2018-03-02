package fr.hadriel.window;

import org.lwjgl.glfw.GLFW;

public class WindowHint {
    public int x = GLFW.GLFW_DONT_CARE;
    public int y = GLFW.GLFW_DONT_CARE;
    public int width = 800;
    public int height = width * 9/16;
    public String title = "Window";
    public boolean visible = false;
    public boolean fullscreen = false;
    public boolean resizable = true;
    public boolean decorated = true;
}