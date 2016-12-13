package fr.hadriel.graphics.ui;

import fr.hadriel.graphics.HLGraphics;
import fr.hadriel.graphics.Texture;
import fr.hadriel.util.Property;

/**
 * Created by glathuiliere setOn 11/08/2016.
 */
public class Image extends Widget {

    private Property<Texture> textureProperty;

    public Image(Texture texture, int width, int height) {
        this.textureProperty = new Property<>(texture);
        setSize(width, height);
    }

    public Image(Texture texture) {
        this(texture, texture.getWidth(), texture.getHeight());
    }

    public Texture getTexture() {
        return textureProperty.get();
    }

    public void setTexture(Texture texture) {
        textureProperty.set(texture);
    }

    public void onRender(HLGraphics g) {
        Texture t = textureProperty.get();
        if(t != null)
            t.renderStretched(g, sizeProperty.get());
    }
}