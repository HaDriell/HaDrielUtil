package fr.hadriel;

import fr.hadriel.application.Application;
import fr.hadriel.asset.AssetManager;
import fr.hadriel.graphics.font.Font;

/**
 * Created by gauti on 28/02/2018.
 */
public class TestFont extends Application {

    protected void start(String[] args) {
        AssetManager manager = new AssetManager();
        Font font = new Font("Media/res/Arial.fnt");
        manager.load("Font", font);
    }

    protected void update(float delta) {

    }

    protected void terminate() {

    }

    public static void main(String[] args) {
        launch(new TestFont());
    }
}