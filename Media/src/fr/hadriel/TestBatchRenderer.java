package fr.hadriel;

import fr.hadriel.application.Application;
import fr.hadriel.asset.graphics.font.Font;
import fr.hadriel.asset.graphics.image.Image;
import fr.hadriel.math.Mathf;
import fr.hadriel.math.Matrix3;
import fr.hadriel.math.Vec4;
import fr.hadriel.renderer.BatchRenderer;
import fr.hadriel.g2d.RenderUtil;
import fr.hadriel.util.Timer;

public class TestBatchRenderer extends Application {

    BatchRenderer renderer;
    Image teron;
    Font diablo;

    Timer time;

    protected void start(String[] args) {
        renderer = new BatchRenderer();
        renderer.setProjection(0, 800, 0, 450);
        teron = manager.load("Media/res/Teron Fielsang.png", Image.class);
        diablo = manager.load("Media/res/Diablo2.fnt", Font.class);
        time = new Timer();
        time.reset();
    }

    protected void update(float delta) {
        float size = 20f + 5f * Mathf.sin(time.elapsed() * 0.3f);

        RenderUtil.Clear();
        renderer.begin();
        renderer.draw(Matrix3.Identity, 0, 0, teron.width(), teron.height(), teron.getRegion(), new Vec4(1, 1, 1, 1));
        renderer.draw(Matrix3.Identity, 0, 100, "Hello world", diablo, 10f, new Vec4(1, 1, 1, 1));
        renderer.end();
    }

    protected void terminate() {
        manager.unload(diablo);
        manager.unload(teron);
    }

    public static void main(String... args) {
        launch(new TestBatchRenderer());
    }
}
