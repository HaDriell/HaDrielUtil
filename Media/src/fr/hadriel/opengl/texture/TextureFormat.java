package fr.hadriel.opengl.texture;


import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;

public enum TextureFormat {
    /* Common Formats */
    RGBA8(GL11.GL_RGBA8),
    RGB8(GL11.GL_RGB8),

    /* Depth Formats */
    DEPTH16(GL14.GL_DEPTH_COMPONENT16),
    DEPTH24(GL14.GL_DEPTH_COMPONENT24),
    DEPTH32(GL14.GL_DEPTH_COMPONENT32)
    ;

    public final int value;

    private TextureFormat(int value) {
        this.value = value;
    }
}