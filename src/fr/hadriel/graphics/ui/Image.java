package fr.hadriel.graphics.ui;

import fr.hadriel.graphics.HLGraphics;
import fr.hadriel.graphics.Texture;

/**
 * Created by glathuiliere on 11/08/2016.
 */
public class Image extends Widget {

    private Texture texture;

    public Image(Texture texture) {
        setTexture(texture);
    }

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
        if(texture != null) setSize(texture.getWidth(), texture.getHeight());
        else setSize(0, 0);
    }

    public void onRender(HLGraphics g) {
        if(texture != null)
            texture.render(g);
    }
}
