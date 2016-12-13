package fr.hadriel.hgl.opengl;


import fr.hadriel.graphics.Text;
import fr.hadriel.graphics.Texture;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL11.*;
/**
 * Created by glathuiliere on 05/12/2016.
 */
public class Texture2DSampler {

    private List<Texture2D> textures;

    public Texture2DSampler() {
        textures = new ArrayList<>();
    }

    public int load(Texture2D texture) {
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