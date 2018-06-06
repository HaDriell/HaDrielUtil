package fr.hadriel.opengl;

import static org.lwjgl.opengl.GL11.*;

public enum Capability {
    BLENDING(GL_BLEND),
    FACE_CULLING(GL_CULL_FACE),
    DEPTH_TEST(GL_DEPTH_TEST),
    STENCIL_TEST(GL_STENCIL_TEST)
    ;

    public final int value;

    private Capability(int value) {
        this.value = value;
    }
}