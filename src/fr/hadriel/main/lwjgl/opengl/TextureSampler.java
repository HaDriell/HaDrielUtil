package fr.hadriel.main.lwjgl.opengl;


import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL13.*;

/**
 * Created by glathuiliere on 05/12/2016.
 */
public class TextureSampler {

    private List<Texture> textures;

    public TextureSampler() {
        textures = new ArrayList<>();
    }

    public int load(Texture texture) {
        int index = textures.indexOf(texture);
        if(index == -1) {
            index = textures.size();
            textures.add(texture);
        }
        return index;
    }

    public void reset() {
        textures.clear();
    }

    public int getTextureCount() {
        return textures.size();
    }

    public void bindTextures() {
        for(int i = 0; i < textures.size(); i++) {
            glActiveTexture(GL_TEXTURE0 + i);
            textures.get(i).bind();
        }
    }

    public int[] getSamplerTextureUnitsIndices() {
        int[] units = new int[textures.size()];
        for(int i = 0; i < textures.size(); i++) {
            units[i] = i;
        }
        return units;
    }
}