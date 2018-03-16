package fr.hadriel;

import fr.hadriel.application.Application;
import fr.hadriel.asset.AssetManager;
import fr.hadriel.graphics.font.Font;
import fr.hadriel.math.Matrix3;
import fr.hadriel.math.Vec2;
import fr.hadriel.math.Vec4;
import fr.hadriel.renderers.FontRenderer;
import fr.hadriel.renderers.RenderUtil;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

/**
 * Created by gauti on 28/02/2018.
 */
public class TestFont extends Application {

    FontRenderer renderer;
    Font font;

    protected void start(String[] args) {
        System.out.println("Loading Resources");
        font = new Font("Media/res/Arial.fnt");
        manager.load("Font", font);
        renderer = new FontRenderer();
        renderer.setProjection(0, 800, 0, 450);
        System.out.println("Loading Done !");
    }

    float angle = 0;

    protected void update(float delta) {
        RenderUtil.Clear();
        Matrix3 transform = Matrix3.Transform(1, 1, angle, 100, 20);
        angle += delta * -10;

        renderer.draw(transform, "The Quick Fox Jumps over the Sloppy Dog", font, 12, new Vec4(1, 1, 1, 1));
    }

    protected void terminate() {

    }

    public static void main(String[] args) {
        launch(new TestFont());
    }
}