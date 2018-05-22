package fr.hadriel;

import fr.hadriel.application.Application;
import fr.hadriel.asset.graphics.Graphic2D;
import fr.hadriel.asset.graphics.image.Image;
import fr.hadriel.math.Matrix3;
import fr.hadriel.math.Vec2;
import fr.hadriel.renderers.g2d.G2DSpriteBatchRenderer;

public class TestG2D extends Application {

    Image teron;
    G2DSpriteBatchRenderer renderer;

    protected void start(String[] args) {
        teron = manager.load("Media/res/Teron Fielsang.png", Image.class);
        renderer = new G2DSpriteBatchRenderer();

        //Configure the projection
        Vec2 size = Graphic2D.getWindowSize();
        renderer.setViewport(0, 0, (int) size.x, (int) size.y);
        renderer.setProjection(0, 800, 0, 450, 1000, 0);
    }

    protected void update(float delta) {
        renderer.begin();
        renderer.draw(Matrix3.Identity, 0, teron.getRegion());
        renderer.end();
    }

    protected void terminate() {

    }

    public static void main(String... args) {
        launch(new TestG2D());
    }
}
