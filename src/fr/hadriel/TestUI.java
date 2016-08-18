package fr.hadriel;


import fr.hadriel.graphics.*;
import fr.hadriel.graphics.ui.*;
import fr.hadriel.math.Mathf;
import fr.hadriel.time.Timer;

/**
 * Created by glathuiliere on 08/08/2016.
 */
public class TestUI {

    public static void main(String[] args) throws Exception {
        Window window = new Window();
        window.start();
        Group root = new Group();
        window.addRoot(root);

        //PROGRESSBAR STUFF
        ProgressBar progressBar = new ProgressBar(150, 20);
        progressBar.getTransform().translate(200, 0);
        root.add(progressBar);
        //END PROGRESSBAR STUFF

        //SLIDER STUFF
        Slider slider = new Slider(150, 20);
        root.add(slider);
        //END SLIDER STUFF

        //BUTTON STUFF
        Button defaultButton = new Button("Epic Button");
        defaultButton.getTransform().translate(0, 50);
        defaultButton.setSize(150, 50);
        root.add(defaultButton);
        //END BUTTON STUFF

        //LABEL STUFF
        Label label = new Label("Texte");
        root.add(label);
        //END LABEL STUFF

        //Endless loop
        Timer timer = new Timer();
        while(!window.isDisposed()) {
            float value = Mathf.abs(Mathf.cos(timer.elapsed() / 2));
            progressBar.setValue(value);
            try {
                Thread.sleep(16);
            } catch (InterruptedException ignore) {}
        }
    }
}