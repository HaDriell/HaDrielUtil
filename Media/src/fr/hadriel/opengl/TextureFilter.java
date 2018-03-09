package fr.hadriel.opengl;

import org.lwjgl.opengl.GL11;

/**
 * Created by glathuiliere on 10/02/2017.
 */
public enum TextureFilter {
    NEAREST(GL11.GL_NEAREST),
    LINEAR(GL11.GL_LINEAR),
    NEAREST_MIPMAP_NEAREST(GL11.GL_NEAREST_MIPMAP_NEAREST),
    LINEAR_MIPMAP_NEAREST(GL11.GL_LINEAR_MIPMAP_NEAREST),
    NEAREST_MIPMAP_LINEAR(GL11.GL_NEAREST_MIPMAP_LINEAR),
    LINEAR_MIPMAP_LINEAR(GL11.GL_LINEAR_MIPMAP_LINEAR);

    public final int value;

    private TextureFilter(int value) {
        this.value = value;
    }
}