package fr.hadriel.hgl.core;


import fr.hadriel.hgl.graphics.Texture;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL13.*;

/**
 * Created by HaDriel on 30/11/2016.
 */
public class TextureSampler {

    public static final int MAX_TEXTURE_COUNT = 80;
    private List<Texture> textures;

    public TextureSampler() {
        textures = new ArrayList<>();
        int[] mlt = new int[16];
    }

    public void reset() {
        textures.clear();
    }

    public int load(Texture texture) {
        int index = textures.indexOf(texture);
        if(index == -1) {
            index = textures.size(); // should be the index of texture

            //Setup the Active textures
            glActiveTexture(GL_TEXTURE0 + index);
            texture.bind();
            //add to the local list to track it
            textures.add(texture);
        }
        return index;
    }
}