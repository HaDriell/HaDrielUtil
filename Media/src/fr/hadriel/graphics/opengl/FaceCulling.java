package fr.hadriel.graphics.opengl;

import org.lwjgl.opengl.GL11;

/**
 * TODO comment
 *
 * @author glathuiliere
 */
public class FaceCulling {

    public static final int GL_CW = GL11.GL_CW;
    public static final int GL_CCW = GL11.GL_CCW;

    public static final int GL_FRONT = GL11.GL_FRONT;
    public static final int GL_BACK = GL11.GL_BACK;
    public static final int GL_FRONT_AND_BACK = GL11.GL_FRONT_AND_BACK;

    public int front = GL_CCW;
    public int culling = GL_BACK;

    public void enable() {
        GL11.glFrontFace(front);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glCullFace(culling);
    }

    public void disable() {
        GL11.glDisable(GL11.GL_CULL_FACE);
    }
}