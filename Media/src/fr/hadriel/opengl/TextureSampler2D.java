package fr.hadriel.opengl;


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
        for (int i = 0; i < slot; i++) {
            if (textures[i].handle == texture.handle)
                return i;
        }
        return -1;
    }

    public int activateTexture(Texture2D texture) {
        int id = getActiveTextureIndex(texture);
        if(slot < textures.length && id == -1) {
            id = slot;
            textures[slot] = texture;
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