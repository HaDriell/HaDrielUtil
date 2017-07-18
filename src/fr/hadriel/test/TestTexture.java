package fr.hadriel.test;

import fr.hadriel.main.lwjgl.g2d.BatchGraphics;
import fr.hadriel.main.lwjgl.g2d.G2DWindow;
import fr.hadriel.main.lwjgl.g2d.ui.Widget;
import fr.hadriel.main.lwjgl.opengl.Texture;
import fr.hadriel.main.lwjgl.opengl.TextureHint;
import fr.hadriel.main.math.Matrix3f;
import fr.hadriel.main.util.Timer;
import org.lwjgl.opengl.GL11;

import java.io.IOException;

/**
 * Created by glathuiliere on 10/02/2017.
 */
public class TestTexture {

    public static void main(String[] args) {
        G2DWindow window = new G2DWindow();
        window.getRoot().add(new Widget() {

            Texture normal;
            Texture mipmap;

            Timer t = new Timer();

            protected void onRender(BatchGraphics g, float w, float h) {
                if(normal == null) {
                    t.reset();
                    try {
                        TextureHint hint = new TextureHint();
                        hint.GL_TEXTURE_MIPMAP_COUNT = 8;
                        hint.GL_MIN_FILTER = GL11.GL_LINEAR_MIPMAP_NEAREST;
                        mipmap = new Texture("Teron Fielsang.png", hint);
                        normal = new Texture("Teron Fielsang.png");
                    } catch (IOException ignore) {}

                }

                float width = normal.width / (1 + t.elapsed() * 0.1f);
                float height = normal.height / (1 + t.elapsed() * 0.1f);
                g.push(Matrix3f.Translation(window.getWidth() / 4, 100));
                g.drawTextureRegion(0, 0, width, height, normal.getRegion(0, 0, normal.width / 2, normal.height));
                g.push(Matrix3f.Translation(width, 0));
                g.drawTextureRegion(0, 0, width, height, mipmap.getRegion(normal.width / 2, 0, normal.width / 2, normal.height));
            }
        });
    }
}
