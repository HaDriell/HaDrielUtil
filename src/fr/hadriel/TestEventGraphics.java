package fr.hadriel;


import fr.hadriel.graphics.Window;
import fr.hadriel.graphics.ui.Group;
import fr.hadriel.graphics.ui.Label;
import fr.hadriel.math.Mathf;
import fr.hadriel.time.Timer;

import java.awt.*;
import java.io.File;


/**
 * Created by glathuiliere on 08/08/2016.
 */
public class TestEventGraphics {

    public static void main(String[] args) throws Exception {
        Font font = Font.createFont(Font.TRUETYPE_FONT, new File("res/font.ttf"));
        Group root = new Group();
        Label  label = new Label("Hello world", font, 25, Color.white);
        label.getTransform().rotate(45f);
        label.getTransform().translate(100, 100);
        root.add(label);
        root.getTransform().translate(0, 50);

        Window window = new Window();
        window.start();
        window.addRoot(root);

        float dx, dy;
        Timer timer = new Timer();
        while(!window.isDisposed()) {
            label.getTransform().rotate(1f);
//            dx = Mathf.cos(timer.elapsed() * 2) * 10;
//            dy = Mathf.sin(timer.elapsed() * 2) * 10;
//            root.getTransform().setToTranslation(dx, dy);
            Thread.sleep(16);
        }
    }
}