package fr.hadriel;

import fr.hadriel.graphics.NinePatch;
import fr.hadriel.graphics.Window;
import fr.hadriel.graphics.ui.*;
import fr.hadriel.graphics.ui.Button;
import fr.hadriel.graphics.ui.Image;
import fr.hadriel.graphics.ui.Label;

import java.awt.*;

/**
 * Created by glathuiliere on 18/08/2016.
 */
public class FunGUI {

    public static void main(String[] args) {
        Window window = new Window();
        int windowWidth = window.getSize().width - 16;
        int windowHeight = window.getSize().height - 38; // Windows7
        Group root = new Group();
        Image image = new Image(new NinePatch(UIDefaults.copyOf(UIDefaults.BACKGROUND, (bg) -> {
            Graphics2D g = bg.createGraphics();
            g.setColor(Color.red.darker().darker().darker());
            g.fillRect(8, 8, bg.getWidth() - 16, bg.getHeight() - 16);
            g.dispose();
        }), 8, 8, 8, 8).createTexture(windowWidth, windowHeight));
        System.out.println(window.getSize());
        System.out.println(image.getSize());
        root.add(image);

        Button applyButton = new Button("Apply Changes");
        applyButton.getTransform().translate(20, 20);
        root.add(applyButton);

        Button cancel = new Button("Cancel Changes");
        cancel.getTransform().translate(windowWidth - cancel.getWidth() - 20, 20);
        root.add(cancel);

        //Sound options
        Label masterSoundLabel = new Label("Master Sound");
        masterSoundLabel.getTransform().setMatrix(applyButton.getTransform().toMatrix());
        masterSoundLabel.getTransform().translate(0, 100);
        root.add(masterSoundLabel);
        Slider masterSoundSlider = new Slider(0.8f, 300, 20);
        masterSoundSlider.getTransform().setMatrix(masterSoundLabel.getTransform().toMatrix());
        masterSoundSlider.getTransform().translate(masterSoundLabel.getWidth() + 20, 0);
        root.add(masterSoundSlider);

        Label musicSoundLabel = new Label("Music Sound");
        musicSoundLabel.getTransform().setMatrix(masterSoundLabel.getTransform().toMatrix());
        musicSoundLabel.getTransform().translate(0, 20 + masterSoundLabel.getHeight());
        root.add(musicSoundLabel);
        Slider musicSoundSlider = new Slider(1f, 300, 20);
        musicSoundSlider.getTransform().setMatrix(musicSoundLabel.getTransform().toMatrix());
        musicSoundSlider.getTransform().translate(musicSoundLabel.getWidth() + 20, 0);
        root.add(musicSoundSlider);

        window.addRoot(root);
        window.start();
    }
}