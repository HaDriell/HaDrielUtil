package fr.hadriel.opengl;

import static org.lwjgl.opengl.GL13.*;

public class TextureSampler2D {

    private final Texture2D[] textures;
    private int slot;

    public TextureSampler2D(int size) {
        textures = new Texture2D[size];
    }

    public void clear() {
        slot = 0;
    }

    public boolean isFull() {
        return slot == textures.length;
    }

    public int getActiveTextureID(Texture2D texture) {
        for(int i = 0; i < slot; i++)
            if(texture.handle == textures[i].handle)
                return i;
        return -1;
    }

    public int activateTexture(Texture2D texture) {
        if(texture == null)
            return -1;
        int id = getActiveTextureID(texture);
        if(id == -1) {
            id = slot;
            slot++;
            textures[id] = texture;
        }
        return id;
    }

    public void bindTextures() {
        for (int i = 0; i < slot; i++) {
            glActiveTexture(GL_TEXTURE0 + i);
            textures[i].bind();
        }
    }

    public int[] getUniformValue() {
        int[] ids = new int[textures.length];
        for(int i = 0; i < ids.length; i++)
            ids[i] = i;
        return ids;
    }
}