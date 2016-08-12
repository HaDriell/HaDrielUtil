package fr.hadriel;


import fr.hadriel.graphics.*;
import fr.hadriel.graphics.ui.*;
import fr.hadriel.math.Mathf;
import fr.hadriel.time.Timer;

/**
 * Created by glathuiliere on 08/08/2016.
 */
public class TestEventGraphics {

    public static void main(String[] args) throws Exception {
        Window window = new Window();
        window.start();
        Group root = new Group();
        root.getTransform().translate(50, 50);

        //SLIDER STUFF
        Slider slider = new Slider(0f);
        slider.setSize(500, 10);
        root.add(slider);

        //END SLIDER STUFF

        //BUTTON STUFF
//        NinePatch idlePatch = new NinePatch("res/button-idle-patch.png", 25, 25, 25, 25);
//        NinePatch hoveredPatch = new NinePatch("res/button-hovered-patch.png", 25, 25, 25, 25);
//        NinePatch pressedPatch = new NinePatch("res/button-pressed-patch.png", 25, 25, 25, 25);
//
//        root.add(new Button("Fucking nice looking Button",
//                idlePatch.createTexture(300, 100),
//                hoveredPatch.createTexture(300, 100),
//                pressedPatch.createTexture(300, 100)));
//        root.add(button);
        //END BUTTON STUFF
        window.addRoot(root);
    }
}