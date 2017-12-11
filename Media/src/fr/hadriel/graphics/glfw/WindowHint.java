package fr.hadriel.graphics.glfw;

/**
 * Created by glathuiliere on 22/07/2016.
 */

//TODO : extend the WindowHint support to the majority of GLFW Window Hints creation + make some tests about that
public class WindowHint {
    public int x = -1;
    public int y = -1;
    public int width = 800;
    public int height = width * 9/16;
    public String title = "";
    public boolean visible = true;
    public boolean fullscreen = false;
    public boolean resizable = true;
    public boolean decorated = true;
}