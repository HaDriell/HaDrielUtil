package fr.hadriel.opengl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL14.*;

public class PipelineState {

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

    public PipelineState() {
        this.capabilities = new HashMap<>();
        Arrays.stream(Capability.values()).forEach(this::disable);
        setBlendEquation(BlendEquation.GL_FUNC_ADD);
        setBlendFunction(BlendFactor.GL_ONE, BlendFactor.GL_ZERO);
        setWindingOrder(WindingOrder.COUNTER_CLOCKWISE);
        setFaceCulling(FaceCulling.BACK);
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