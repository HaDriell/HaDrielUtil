package fr.hadriel;

import fr.hadriel.application.Application;
import fr.hadriel.asset.graphics.image.Image;
import fr.hadriel.math.Mathf;
import fr.hadriel.math.Matrix3;
import fr.hadriel.math.Vec4;
import fr.hadriel.renderers.RenderUtil;
import fr.hadriel.renderers.SpriteBatchRenderer;

import java.util.Random;

public class TestSpriteBatchRendering extends Application {

    SpriteBatchRenderer renderer;

    Image arial;
    Image illuminati;
    Image teron;

    Matrix3[] illuminatiMatrices;
    Matrix3[] teronMatrices;

    protected void start(String[] args) {

        renderer = new SpriteBatchRenderer();
        illuminati = manager.load("Media/res/illuminati.png", Image.class);
        teron = manager.load("Media/res/Teron Fielsang.png", Image.class);
        arial = manager.load("Media/res/Arial.png", Image.class);

        renderer.setProjection(0, 800, 0, 450);
        Random random = new Random(0);

        teronMatrices = new Matrix3[100];
        for(int i = 0; i < teronMatrices.length; i++) {
            Matrix3 transform = new Matrix3();
            transform = transform.multiply(Matrix3.Translation(random.nextFloat() * 800, random.nextFloat() * 450));
            transform = transform.multiply(Matrix3.Scale(Mathf.lerp(0.5f, 1f, random.nextFloat()), Mathf.lerp(0.5f, 1f, random.nextFloat())));
            transform = transform.multiply(Matrix3.Rotation(random.nextFloat() * 360));
            teronMatrices[i] = transform;
        }

        illuminatiMatrices = new Matrix3[100];
        for(int i = 0; i < illuminatiMatrices.length; i++) {
            Matrix3 transform = new Matrix3();
            transform = transform.multiply(Matrix3.Translation(random.nextFloat() * 800, random.nextFloat() * 450));
            transform = transform.multiply(Matrix3.Scale(random.nextFloat(), random.nextFloat()));
            transform = transform.multiply(Matrix3.Rotation(random.nextFloat() * 360));
            illuminatiMatrices[i] = transform;
        }
    }

    protected void update(float delta) {
        RenderUtil.Clear();
        renderer.begin();
        for (int i = 0; i < teronMatrices.length; i++) {
            renderer.draw(teronMatrices[i], 32, 32, teron.getRegion(), new Vec4(1,1,1,1));
        }

        for (int i = 0; i < illuminatiMatrices.length; i++) {
            renderer.draw(illuminatiMatrices[i], 32, 32, illuminati.getRegion(), new Vec4(1,1,1,1));
        }
        renderer.draw(Matrix3.Identity, 32, 32, teron.getRegion(), new Vec4(1,1,1,1));
        renderer.draw(Matrix3.Translation(32, 0), 32, 32, arial.getRegion(), new Vec4(1,1,1,1));
        renderer.draw(Matrix3.Translation(64, 0), 32, 32, illuminati.getRegion(), new Vec4(1,1,1,1));
        renderer.end();
    }

    protected void terminate() { }

    public static void main(String[] args) {
        launch(new TestSpriteBatchRendering());
    }
}