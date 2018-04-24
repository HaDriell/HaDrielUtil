package fr.hadriel;

import fr.hadriel.application.Application;
import fr.hadriel.asset.graphics.font.Font;
import fr.hadriel.asset.graphics.image.Image;
import fr.hadriel.math.Matrix3;
import fr.hadriel.math.Vec4;
import fr.hadriel.renderers.BatchRenderer;
import fr.hadriel.renderers.RenderUtil;

public class TestBatchRenderer extends Application {

    BatchRenderer renderer;
    Image teron;
    Font diablo;

    protected void start(String[] args) {
        renderer = new BatchRenderer();
        renderer.setProjection(0, 800, 0, 450);
        teron = manager.load("Media/res/Teron Fielsang.png", Image.class);
        diablo = manager.load("Media/res/Diablo.fnt", Font.class);
    }

    protected void update(float delta) {
        RenderUtil.Clear();
        renderer.begin();
        renderer.draw(Matrix3.Identity, 0, 0, teron.width(), teron.height(), teron.getRegion(), new Vec4(1, 1, 1, 1));
        renderer.draw(Matrix3.Identity, 0, 0, "Hello world", diablo, 20f, new Vec4(1, 1, 1, 1));
        renderer.end();
    }

    protected void terminate() {

    }

    public static void main(String... args) {
        launch(new TestBatchRenderer());
    }
}
