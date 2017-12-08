package fr.hadriel.graphics;

import fr.hadriel.graphics.glfw3.GLFWwindow;
import fr.hadriel.graphics.glfw3.WindowHint;

/**
 * TODO comment
 *
 * @author glathuiliere
 */
public class TestGFXThreadLifespan {
    public static void main(String[] arsg) {
        new GLFWwindow(new WindowHint()).close(); // Should Terminate immediately
    }
}
