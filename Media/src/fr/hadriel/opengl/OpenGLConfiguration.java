package fr.hadriel.opengl;

import java.util.Objects;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL14.*;

public class OpenGLConfiguration {

    private int x, y, width, height;

    private boolean depthTesting;

    private boolean blending;
    private BlendEquation equation;
    private BlendFactor srcFactor;
    private BlendFactor dstFactor;

    //Winding order
    private WindingOrder windingOrder;

    //Face Culling
    private FaceCulling faceCulling;

    public OpenGLConfiguration() {
        this.depthTesting = false;
        this.blending     = false;
        this.equation     = BlendEquation.GL_FUNC_ADD;
        this.srcFactor    = BlendFactor.GL_ONE;
        this.dstFactor    = BlendFactor.GL_ZERO;
        this.windingOrder = WindingOrder.COUNTER_CLOCKWISE;
        this.faceCulling  = FaceCulling.NONE;
    }

    public void setViewport(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void setDepthTesting(boolean depthTesting) {
        this.depthTesting = depthTesting;
    }

    public boolean isDepthTesting() {
        return depthTesting;
    }

    public void setBlending(boolean blending) {
        this.blending = blending;
    }

    public void setBlendEquation(BlendEquation equation) {
        this.equation = Objects.requireNonNull(equation);
    }

    public void setBlendFunction(BlendFactor source, BlendFactor destination) {
        this.srcFactor = Objects.requireNonNull(source);
        this.dstFactor = Objects.requireNonNull(destination);
    }

    public void setWindingOrder(WindingOrder windingOrder) {
        this.windingOrder = Objects.requireNonNull(windingOrder);
    }

    public void setFaceCulling(FaceCulling faceCulling) {
        this.faceCulling = Objects.requireNonNull(faceCulling);
    }

    public void apply() {
        //Viewport
        glViewport(x, y, width, height);

        //FaceCulling
        switch (faceCulling) {
            case NONE:
                glDisable(GL_CULL_FACE);
                break;
            case FRONT:
                glEnable(GL_CULL_FACE);
                glCullFace(GL_FRONT);
                break;
            case BACK:
                glEnable(GL_CULL_FACE);
                glCullFace(GL_BACK);
                break;
        }

        //Winding order
        glFrontFace(windingOrder == WindingOrder.CLOCKWISE ? GL_CW : GL_CCW);

        //Blending Mode
        if (blending) {
            glEnable(GL_BLEND);
            glBlendEquation(equation.value);
            glBlendFunc(srcFactor.value, dstFactor.value);
        } else {
            glDisable(GL_BLEND);
        }

        //Depth Testing
        if (depthTesting) {
            glEnable(GL_DEPTH_TEST);
        } else {
            glDisable(GL_DEPTH_TEST);
        }
    }
}