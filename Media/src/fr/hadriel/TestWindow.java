package fr.hadriel;

import fr.hadriel.application.WindowHint;
import fr.hadriel.glfw.Window;

public class TestWindow {

    public static void main(String[] args) {
        WindowHint hint = new WindowHint();
        hint.visible = true;
        Window a = new Window(hint);
    }
}
