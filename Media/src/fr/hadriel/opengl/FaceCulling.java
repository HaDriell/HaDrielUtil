package fr.hadriel.opengl;

import org.lwjgl.opengl.GL11;

public enum FaceCulling {
    FRONT_AND_BACK(GL11.GL_FRONT_AND_BACK),
    FRONT(GL11.GL_FRONT),
    BACK(GL11.GL_BACK);

    public final int value;

    private FaceCulling(int value) {
        this.value = value;
    }
}