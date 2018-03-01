package fr.hadriel.opengl;

import org.lwjgl.opengl.GL11;

/**
 * Created by HaDriel on 08/12/2016.
 */
public enum BlendFactor {
    GL_ONE(GL11.GL_ONE),
    GL_ZERO(GL11.GL_ZERO),
    GL_SRC_COLOR(GL11.GL_SRC_COLOR),
    GL_ONE_MINUS_SRC_COLOR(GL11.GL_ONE_MINUS_SRC_COLOR),
    GL_DST_COLOR(GL11.GL_DST_COLOR),
    GL_ONE_MINUS_DST_COLOR(GL11.GL_ONE_MINUS_DST_COLOR),
    GL_SRC_ALPHA(GL11.GL_SRC_ALPHA),
    GL_ONE_MINUS_SRC_ALPHA(GL11.GL_ONE_MINUS_SRC_ALPHA),
    GL_DST_ALPHA(GL11.GL_DST_ALPHA),
    GL_ONE_MINUS_DST_ALPHA(GL11.GL_ONE_MINUS_DST_ALPHA);

    public final int value;

    BlendFactor(int value) {
        this.value = value;
    }
}
