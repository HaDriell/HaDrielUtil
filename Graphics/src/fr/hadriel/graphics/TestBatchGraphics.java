package fr.hadriel.graphics;

import fr.hadriel.graphics.g2d.Graphics;
import fr.hadriel.graphics.g2d.G2DWindow;
import fr.hadriel.graphics.g2d.ui.UIContext;
import fr.hadriel.graphics.g2d.ui.Widget;
import fr.hadriel.graphics.opengl.Texture;

import java.io.IOException;

public class TestBatchGraphics {
    public static void main(String[] args) {
        G2DWindow window = new G2DWindow();
        window.getScene().add(new Widget() {
            Texture texture;
            protected void onRender(Graphics g, float width, float height, UIContext context) {
                if(texture == null) {
                    try {
                        texture = new Texture("illuminati.png");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                g.drawTextureRegion(0, 0, 10, 10, texture.getRegion());
            }
        });
    }
}