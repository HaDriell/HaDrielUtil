package fr.hadriel;

import fr.hadriel.application.Application;
import fr.hadriel.asset.graphics.Graphic2D;
import fr.hadriel.asset.graphics.font.Font;
import fr.hadriel.math.Mathf;
import fr.hadriel.math.Vec2;
import fr.hadriel.renderers.BatchGraphics;
import fr.hadriel.renderers.RenderUtil;
import org.lwjgl.glfw.GLFW;

public class TestFontSpreadQuality extends Application {

    float size = 24;
    float buffer = 0.5f;
    float gamma = 0.1f;

    Font arial10;
    Font arial15;
    Font arial20;
    BatchGraphics g;

    protected void start(String[] args) {
        Vec2 size = Graphic2D.getWindowSize();

        g = new BatchGraphics();
        g.setProjection(0, size.x, 0, size.y);
        arial10 = manager.load("Media/res/Arial-spread-10.fnt", Font.class);
        arial15 = manager.load("Media/res/Arial-spread-15.fnt", Font.class);
        arial20 = manager.load("Media/res/Arial-spread-20.fnt", Font.class);
    }

    protected void update(float delta) {
        Vec2 mouse = Graphic2D.getMouse();
        Vec2 window_size = Graphic2D.getWindowSize();
        Graphic2D.setTitle(String.format("Size:%.2f    Buffer: %.2f    Gamma: %.2f", size, buffer, gamma));

        if (Graphic2D.isKeyPressed(GLFW.GLFW_KEY_S)) size = mouse.y;
        if (Graphic2D.isKeyPressed(GLFW.GLFW_KEY_B)) buffer = mouse.y / window_size.y;
        if (Graphic2D.isKeyPressed(GLFW.GLFW_KEY_G)) gamma = mouse.y / window_size.y;


        RenderUtil.Clear();
        g.setSDFSettings(buffer, gamma);
        g.begin();
        g.draw(0, 0 * size, "Arial-spread-10", arial10, size);
        g.draw(0, 1 * size, "Arial-spread-15", arial15, size);
        g.draw(0, 2 * size, "Arial-spread-20", arial20, size);
        g.end();
    }

    protected void terminate() {

    }

    public static void main(String... args) {
        launch(new TestFontSpreadQuality());
    }
}
