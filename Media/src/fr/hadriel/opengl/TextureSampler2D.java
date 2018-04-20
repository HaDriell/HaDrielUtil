package fr.hadriel.opengl;

import com.sun.istack.internal.NotNull;

import static org.lwjgl.opengl.GL13.*;

public class TextureSampler2D {

    private final int[] uniformValue;
    private final Texture2D[] textures;
    private int slot;

    public TextureSampler2D(int size) {
        textures = new Texture2D[size];
        //init the uniform array
        uniformValue = new int[size];
        for (int i = 0; i < uniformValue.length; i++) {
            uniformValue[i] = i;
        }
    }

    public void clear() {
        slot = 0;
    }

    public boolean isFull() {
        return slot == textures.length;
    }

    public int getActiveTextureIndex(Texture2D texture) {
        int i = 0;
        while (textures[i] != null) {
            if (texture.handle == textures[i].handle)
                return i;
            ++i;
        }
        return -1;
    }

    public int activateTexture(@NotNull Texture2D texture) {
        int id = getActiveTextureIndex(texture);
        if(id == -1) {
            id = slot;
            textures[id] = texture;
            slot++;
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
        return uniformValue;
    }
}