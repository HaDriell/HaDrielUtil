package fr.hadriel.opengl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL14.*;

public class RenderState {

    //GL Capabilities states
    private Map<Capability, Boolean> capabilities;

    //Blending parameters
    private BlendEquation equation;
    private BlendFactor srcFactor;
    private BlendFactor dstFactor;

    //Winding order
    private WindingOrder windingOrder;

    //Face Culling
    private FaceCulling faceCulling;

    //Viewport
    // these viewport values are invalid to ensure that any viewport will be pushed for the first call
    private int
            x = 0,
            y = 0,
            width = -1,
            height = -1;

    public RenderState() {
        this.capabilities = new HashMap<>();
        Arrays.stream(Capability.values()).forEach(this::disable);
        setBlendEquation(BlendEquation.GL_FUNC_ADD);
        setBlendFunction(BlendFactor.GL_ONE, BlendFactor.GL_ZERO);
        setWindingOrder(WindingOrder.COUNTER_CLOCKWISE);
        setFaceCulling(FaceCulling.BACK);
    }

    public void setViewport(int x, int y, int width, int height) {
        if (this.x != x || this.y != y || this.width != width || this.height != height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            glViewport(0, 0,  width, height);
        }
    }

    public void enable(Capability capability) {
        Boolean state = capabilities.put(capability, true);
        if (state != null && !state)
            glEnable(capability.value);
    }

    public void disable(Capability capability) {
        Boolean state = capabilities.put(capability, false);
        if (state != null && state)
            glDisable(capability.value);
    }

    public void setBlendEquation(BlendEquation equation) {
        if (this.equation != equation) {
            this.equation = Objects.requireNonNull(equation);
            glBlendEquation(equation.value);
        }
    }

    public void setBlendFunction(BlendFactor source, BlendFactor destination) {
        if (this.srcFactor != source || this.dstFactor != destination) {
            this.srcFactor = Objects.requireNonNull(source);
            this.dstFactor = Objects.requireNonNull(destination);
            glBlendFunc(srcFactor.value, dstFactor.value);
        }
    }

    public void setWindingOrder(WindingOrder windingOrder) {
        if (this.windingOrder != windingOrder) {
            this.windingOrder = Objects.requireNonNull(windingOrder);
            glFrontFace(windingOrder.value);
        }
    }

    public void setFaceCulling(FaceCulling faceCulling) {
        if (this.faceCulling != faceCulling) {
            this.faceCulling = Objects.requireNonNull(faceCulling);
            glCullFace(faceCulling.value);
        }
    }
}