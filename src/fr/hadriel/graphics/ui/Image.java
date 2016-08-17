package fr.hadriel.graphics.ui;

import fr.hadriel.graphics.HLGraphics;
import fr.hadriel.graphics.Texture;
import fr.hadriel.util.Property;

/**
 * Created by glathuiliere on 11/08/2016.
 */
public class Image extends Widget {

    private Property<Texture> textureProperty;

    public Image(Texture texture) {
        this.textureProperty = new Property<>(texture);

        this.textureProperty.addCallback((tex) -> {
            if(tex == null)
                setSize(0, 0);
            else
                setSize(tex.getWidth(), tex.getHeight());
        });
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
            t.render(g);
    }
}
