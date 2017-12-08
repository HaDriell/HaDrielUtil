package fr.hadriel;

import fr.hadriel.application.Launcher;
import fr.hadriel.core.application.GUIApplication;
import fr.hadriel.graphics.g2d.G2DWindow;

/**
 * TODO comment
 *
 * @author glathuiliere
 */
public class TestGUIApplication extends GUIApplication {

    protected void start(G2DWindow window) {
        window.hide();
    }

    public static void main(String[] args) {
        Launcher.launch(new TestGUIApplication(), args);
    }
}
