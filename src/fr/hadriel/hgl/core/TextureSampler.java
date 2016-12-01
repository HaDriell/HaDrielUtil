package fr.hadriel.hgl.core;


import fr.hadriel.graphics.Text;
import fr.hadriel.hgl.graphics.Texture;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL13.*;

/**
 * Created by HaDriel on 30/11/2016.
 */
public class TextureSampler {

    private final int maxTextures;
    private List<Texture> textures;

    public TextureSampler() {
        textures = new ArrayList<>();
        int[] mlt = new int[1];
        GL11.glGetIntegerv(GL_MAX_TEXTURE_UNITS, mlt);
        maxTextures = mlt[0];
    }

    public void reset() {
        textures.clear();
    }

    public int getMaxTextures() {
        return maxTextures;
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