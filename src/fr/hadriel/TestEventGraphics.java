package fr.hadriel;


import fr.hadriel.graphics.*;
import fr.hadriel.graphics.ui.*;

/**
 * Created by glathuiliere on 08/08/2016.
 */
public class TestEventGraphics {

    public static void main(String[] args) throws Exception {
        Window window = new Window();
        window.start();
        Group root = new Group();

        NinePatch idlePatch = new NinePatch("res/button-idle-patch.png", 25, 25, 25, 25);
        NinePatch hoveredPatch = new NinePatch("res/button-hovered-patch.png", 25, 25, 25, 25);
        NinePatch pressedPatch = new NinePatch("res/button-pressed-patch.png", 25, 25, 25, 25);

        root.add(new Button("Fucking nice looking Button",
                idlePatch.createTexture(300, 100),
                hoveredPatch.createTexture(300, 100),
                pressedPatch.createTexture(300, 100)));
        root.getTransform().translate(50, 50);
        window.addRoot(root);
    }
}