package fr.hadriel.opengl;

import java.util.Objects;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL14.*;

public class RenderState {

    private boolean blending;

    private BlendEquation equation;
    private BlendFactor srcFactor;
    private BlendFactor dstFactor;

    //Winding order
    private int windingOrder;

    //Face Culling
    private boolean faceCulling;
    private int culling;

    public RenderState() {
        this.blending       = false;
        this.equation       = BlendEquation.GL_FUNC_ADD;
        this.srcFactor      = BlendFactor.GL_ONE;
        this.dstFactor      = BlendFactor.GL_ZERO;
        this.windingOrder   = GL_CCW;
        this.faceCulling    = false;
        this.culling        = GL_BACK;
    }

    public void setBlending(boolean blending) {
        this.blending = blending;
    }

    public void setFaceCulling(boolean faceCulling) {
        this.faceCulling = faceCulling;
    }

    public void setBlendEquation(BlendEquation equation) {
        this.equation = Objects.requireNonNull(equation);
    }

    public void setSrcBlendFactor(BlendFactor factor) {
        this.srcFactor = Objects.requireNonNull(factor);
    }

    public void setDstBlendFactor(BlendFactor factor) {
        this.dstFactor = Objects.requireNonNull(factor);
    }

    public void setWindingClockwise() {
        windingOrder = GL_CW;
    }

    public void setWindingCounterClockwise() {
        windingOrder = GL_CCW;
    }

    public boolean isWindingClockwise() {
        return windingOrder == GL_CW;
    }

    public boolean isWindingCounterClockwise() {
        return windingOrder == GL_CCW;
    }

    public boolean isCullingBothFaces() {
        return culling == GL_FRONT_AND_BACK;
    }

    public void setCullinBothFaces() {
        culling = GL_FRONT_AND_BACK;
    }

    public boolean isCullingFrontFace() {
        return culling == GL_FRONT;
    }

    public void setCullingFrontFace() {
        culling = GL_FRONT;
    }

    public boolean isCullingBackFace() {
        return culling == GL_BACK;
    }

    public void setCullingBackFace() {
        culling = GL_BACK;
    }

    public void apply() {
        //Winding Order
        glFrontFace(windingOrder);

        //Face Culling
        if(faceCulling) {
            glEnable(GL_CULL_FACE);
            glCullFace(culling);
        } else {
            glDisable(GL_CULL_FACE);
        }

        //Blending Mode
        if(blending) {
            glEnable(GL_BLEND);
            glBlendEquation(equation.value);
            glBlendFunc(srcFactor.value, dstFactor.value);
        } else {
            glDisable(GL_BLEND);
        }
    }
}