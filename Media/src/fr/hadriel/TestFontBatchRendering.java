package fr.hadriel;

import fr.hadriel.application.Application;
import fr.hadriel.asset.graphics.font.Font;
import fr.hadriel.math.Mathf;
import fr.hadriel.math.Matrix3;
import fr.hadriel.math.Vec4;
import fr.hadriel.renderers.FontBatchRenderer;
import fr.hadriel.renderers.RenderUtil;

/**
 * Created by gauti on 28/02/2018.
 */
public class TestFontBatchRendering extends Application {

    FontBatchRenderer renderer;
    Font arial;
    Font diablo;
    float age;

    protected void start(String[] args) {
        System.out.println("Loading Resources");
        arial = manager.load("Media/res/Arial.fnt", Font.class);
        diablo = manager.load("Media/res/Diablo.fnt", Font.class);
        renderer = new FontBatchRenderer();
        renderer.setProjection(0, 800, 0, 450); // that's the shit bugging
        System.out.println("Loading Done !");
    }

    protected void update(float delta) {
        age += delta;
        RenderUtil.Clear();
        renderer.setWeight(0.5f);
        renderer.setEdge(0.1f);
        renderer.begin();
        renderer.draw(Matrix3.Translation(100, 100), "Hello, world!", arial, 20 + 10 * Mathf.cos(age), new Vec4(1, 1, 1, 1));
        renderer.draw(Matrix3.Translation(100, 100 + 20 + 10 * Mathf.cos(age)), "Diablo", diablo, 20 + 10 * Mathf.sin(age), new Vec4(1, 1, 1, 1));
        renderer.draw(Matrix3.Translation(0, 0), "Diablo", diablo, 20, new Vec4(1, 1, 1, 1));
        renderer.end();
    }

    protected void terminate() {

    }

    public static void main(String[] args) {
        launch(new TestFontBatchRendering());
    }
}