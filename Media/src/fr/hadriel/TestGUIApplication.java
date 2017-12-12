package fr.hadriel;

import fr.hadriel.application.Launcher;
import fr.hadriel.core.application.GUIApplication;
import fr.hadriel.gui.Window;

/**
 * TODO comment
 *
 * @author glathuiliere
 */
public class TestGUIApplication extends GUIApplication {

    protected void start(Window window) {
        new Thread(() -> {
            try { Thread.sleep(1000); } catch (InterruptedException ignore) {}
            window.show();
            try { Thread.sleep(1000); } catch (InterruptedException ignore) {}
            window.close();
        }).start();
    }

    public static void main(String[] args) {
        Launcher.launch(new TestGUIApplication(), args);
    }
}
