package fr.hadriel;

import fr.hadriel.application.Application;
import fr.hadriel.asset.font.Font;
import fr.hadriel.math.Mathf;
import fr.hadriel.math.Matrix3;
import fr.hadriel.math.Vec4;
import fr.hadriel.renderers.FontRenderer;
import fr.hadriel.renderers.RenderUtil;

/**
 * Created by gauti on 28/02/2018.
 */
public class TestFont extends Application {

    FontRenderer renderer;
    Font font;
    float age;

    protected void start(String[] args) {
        System.out.println("Loading Resources");
        font = manager.loadFont("Font", "Media/res/Arial.fnt");
        renderer = new FontRenderer();
        renderer.setProjection(0, 800, 0, 450); // that's the shit bugging
        System.out.println("Loading Done !");
    }

    protected void update(float delta) {
        age += delta;
        RenderUtil.Clear();
        renderer.setWeight(0.5f);
        renderer.setEdge(0.1f);
        renderer.draw(Matrix3.Translation(100, 100), "Hello, world!", font, 20 + 10 * Mathf.cos(age), new Vec4(1, 1, 1, 1));
    }

    protected void terminate() {

    }

    public static void main(String[] args) {
        launch(new TestFont());
    }
}