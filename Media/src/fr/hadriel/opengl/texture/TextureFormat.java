package fr.hadriel.opengl.texture;


import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;

public enum TextureFormat {
    /* Common Formats */
    RGB8(GL11.GL_RGB8, GL11.GL_RGB),
    RGBA8(GL11.GL_RGBA8, GL11.GL_RGBA),

    /* Depth Formats */
    DEPTH16(GL14.GL_DEPTH_COMPONENT16, GL11.GL_DEPTH_COMPONENT),
    DEPTH24(GL14.GL_DEPTH_COMPONENT24, GL11.GL_DEPTH_COMPONENT),
    DEPTH32(GL14.GL_DEPTH_COMPONENT32, GL11.GL_DEPTH_COMPONENT),
    ;

    public final int internalFormat;
    public final int format;

    private TextureFormat(int internalFormat, int format) {
        this.internalFormat = internalFormat;
        this.format = format;
    }
}