package fr.hadriel.renderers.g2d;

import fr.hadriel.opengl.RenderState;

import static org.lwjgl.opengl.GL11.*;

public final class G2D {
    private static final float ZBUFFER_QUANTUM = 0.0001f; // TODO : define a constant value for each layers (and get a maximum value of switches out of it)

    private static int zbuffer = 0;
    public static final RenderState renderState = new RenderState();

    public void Clear() {
        glClearColor(0, 0, 0, 1);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT | GL_STENCIL_BUFFER_BIT);
    }

    public void Render(Object object) {
    }
}