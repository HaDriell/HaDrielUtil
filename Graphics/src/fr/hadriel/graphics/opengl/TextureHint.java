package fr.hadriel.graphics.opengl;

import org.lwjgl.opengl.GL11;

/**
 * Created by glathuiliere on 10/02/2017.
 */
public class TextureHint {
    //TODO : I should make prefab Quality TextureHints (like the best Quality / best performance)

    /**
     * may take these values:
     * <br>GL_NEAREST
     * <br>GL_LINEAR
     * <br>GL_NEAREST_MIPMAP_NEAREST
     * <br>GL_LINEAR_MIPMAP_NEAREST
     * <br>GL_NEAREST_MIPMAP_LINEAR
     * <br>GL_LINEAR_MIPMAP_LINEAR
     */
    public int GL_MIN_FILTER = GL11.GL_NEAREST;

    /**
     * may take these values:
     * <br>GL_NEAREST
     * <br>GL_LINEAR
     */
    public int GL_MAG_FILTER = GL11.GL_NEAREST;

    /**
     * Defines the maximum number of mipmaps generated
     */
    public int GL_TEXTURE_MIPMAP_COUNT = 1000;

    /**
     * may take these values:
     * <br>GL_CLAMP_TO_EDGE
     * <br>GL_CLAMP_TO_BORDER
     * <br>GL_MIRRORED_REPEAT
     * <br>GL_REPEAT
     *
     */
    public int GL_TEXTURE_WRAP_S = GL11.GL_REPEAT;

    /**
     * may take these values:
     * <br>GL_CLAMP_TO_EDGE
     * <br>GL_CLAMP_TO_BORDER (unsupported)
     * <br>GL_MIRRORED_REPEAT
     * <br>GL_REPEAT
     *
     */
    public int GL_TEXTURE_WRAP_T = GL11.GL_REPEAT;

    /**
     * may take these values:
     * <br>GL_CLAMP_TO_EDGE
     * <br>GL_CLAMP_TO_BORDER (unsupported)
     * <br>GL_MIRRORED_REPEAT
     * <br>GL_REPEAT
     *
     */
    public int GL_TEXTURE_WRAP_R = GL11.GL_REPEAT;
}