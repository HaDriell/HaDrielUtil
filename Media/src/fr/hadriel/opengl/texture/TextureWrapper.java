package fr.hadriel.opengl.texture;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL14;

/**
 * Created by glathuiliere on 10/02/2017.
 */
public enum TextureWrapper {

    REPEAT(GL11.GL_REPEAT),
    CLAMP_TO_EDGE(GL12.GL_CLAMP_TO_EDGE),
    CLAMP_TO_BORDER(GL13.GL_CLAMP_TO_BORDER),
    MIRRORED_REPEAT(GL14.GL_MIRRORED_REPEAT);

    public final int value;
    private TextureWrapper(int value) {
        this.value = value;
    }
}