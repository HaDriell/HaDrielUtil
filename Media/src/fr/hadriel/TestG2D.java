package fr.hadriel;

import fr.hadriel.application.Application;
import fr.hadriel.asset.graphics.image.Image;
import fr.hadriel.asset.graphics.image.Sprite;
import fr.hadriel.renderer.g2d.G2D;
import fr.hadriel.renderer.g2d.SpriteRenderer;

public class TestG2D extends Application {

    Image teron;

    protected void start(String[] args) {
        teron = manager.load("Media/res/Teron Fielsang.png", Image.class);
        SpriteRenderer renderer = new SpriteRenderer();
        renderer.setProjection(0, 800, 0, 450, 1000, 0);
        G2D.define(Sprite.class, renderer);
        G2D.setViewport(0, 0, 800, 450);
    }

    protected void update(float delta) {
        G2D.prepareFrame();
        G2D.draw(teron.getSprite(0, 0, teron.width(), teron.height()));
        G2D.presentFrame();
    }

    protected void terminate() {

    }

    public static void main(String... args) {
        launch(new TestG2D());
    }
}
