package fr.hadriel;

import fr.hadriel.application.Application;
import fr.hadriel.asset.graphics.font.Font;
import fr.hadriel.asset.graphics.image.Image;
import fr.hadriel.math.Matrix3;
import fr.hadriel.math.Vec4;
import fr.hadriel.renderers.BatchRenderer;
import fr.hadriel.renderers.RenderUtil;
import fr.hadriel.util.Timer;

public class TestBatchRenderer extends Application {

    Timer age;
    BatchRenderer renderer;
    Image teron;
    Font arial;

    protected void start(String[] args) {
        renderer = new BatchRenderer();
        renderer.setProjection(0, 800, 0, 450);
        teron = manager.load("Media/res/Teron Fielsang.png", Image.class);
        arial = manager.load("Media/res/Arial.fnt", Font.class);
        age = new Timer();
    }

    protected void update(float delta) {
        float size = 12f;
        RenderUtil.Clear();
        renderer.setFontConfig(0.3f, 0.1f);
        renderer.begin();
        renderer.draw(Matrix3.Identity, 0, 0, teron.width(), teron.height(), teron.getRegion(), new Vec4(1, 1, 1, 1));
        renderer.draw(Matrix3.Identity, 0, 100, "Mon petit chéri d'amour", arial, size, new Vec4(1, 1, 1, 1));
        renderer.draw(Matrix3.Identity, 0, 120, "Aujourd'hui j'ai terminé d'unifier mes deux", arial, size, new Vec4(1, 1, 1, 1));
        renderer.draw(Matrix3.Identity, 0, 140, "moteurs de rendus (Image et Texte). C'est", arial, size, new Vec4(1, 1, 1, 1));
        renderer.draw(Matrix3.Identity, 0, 160, "trop cool :3", arial, size, new Vec4(1, 1, 1, 1));
        renderer.end();
    }

    protected void terminate() {

    }

    public static void main(String... args) {
        launch(new TestBatchRenderer());
    }
}
