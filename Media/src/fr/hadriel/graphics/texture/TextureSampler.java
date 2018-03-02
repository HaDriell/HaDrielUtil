package fr.hadriel.graphics.texture;


import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL13.*;

/**
 * Created by glathuiliere on 05/12/2016.
 */
public class TextureSampler {

    private List<Texture2D> texture2DS;

    public TextureSampler() {
        texture2DS = new ArrayList<>();
    }

    public int load(Texture2D texture2D) {
        int index = texture2DS.indexOf(texture2D);
        if(index == -1) {
            index = texture2DS.size();
            texture2DS.add(texture2D);
        }
        return index;
    }

    public void reset() {
        texture2DS.clear();
    }

    public int getTextureCount() {
        return texture2DS.size();
    }

    public void bindTextures() {
        for(int i = 0; i < texture2DS.size(); i++) {
            glActiveTexture(GL_TEXTURE0 + i);
            texture2DS.get(i).bind();
        }
    }

    public int[] getSamplerTextureUnitsIndices() {
        int[] units = new int[texture2DS.size()];
        for(int i = 0; i < texture2DS.size(); i++) {
            units[i] = i;
        }
        return units;
    }
}