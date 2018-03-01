package fr.hadriel;

import fr.hadriel.graphics.glfw.WindowHint;
import fr.hadriel.gui.Window;

/**
 * Created by gauti on 28/02/2018.
 */
public class TestWindow {

    public static void main(String[] args) {
        WindowHint hint = new WindowHint();
        hint.visible = true;
        for(int i = 0; i < 10; i++) {
            hint.title = "Window " + i;
            new Window(hint);
        }
    }
}
