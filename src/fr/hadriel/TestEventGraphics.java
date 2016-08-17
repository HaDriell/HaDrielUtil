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
        root.getTransform().translate(50, 50);

        //SLIDER STUFF
        Slider slider = new Slider(300, 12);
        root.add(slider);
        //END SLIDER STUFF

        //BUTTON STUFF
        Button defaultButton = new Button("Epic Button");
        defaultButton.getTransform().translate(0, 150);
        defaultButton.setSize(150, 50);
        root.add(defaultButton);

//        NinePatch idlePatch = new NinePatch("res/button-idle-patch.png", 25, 25, 25, 25);
//        NinePatch hoveredPatch = new NinePatch("res/button-hovered-patch.png", 25, 25, 25, 25);
//        NinePatch pressedPatch = new NinePatch("res/button-pressed-patch.png", 25, 25, 25, 25);
//        Button texturedButton = new Button("Click me !");
//        texturedButton.setSize(300, 50);
//        texturedButton.setOnIdleTexture(idlePatch);
//        texturedButton.setOnHoveredTexture(hoveredPatch);
//        texturedButton.setOnPressedTexture(pressedPatch);
//        texturedButton.getTransform().translate(0, 40);
//        root.add(texturedButton);

        //END BUTTON STUFF

        //LABEL STUFF
        Label label = new Label("Texte");
        root.add(label);
        //END LABEL STUFF
        window.addRoot(root);
    }
}