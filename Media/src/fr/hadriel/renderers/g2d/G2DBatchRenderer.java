package fr.hadriel.renderers.g2d;

import fr.hadriel.math.Matrix3;
import fr.hadriel.opengl.*;
import fr.hadriel.opengl.shader.Shader;
import fr.hadriel.renderers.RenderUtil;

public abstract class G2DBatchRenderer<Primitive> implements G2DRenderer<Primitive> {

    //Batch info
    protected final Shader shader;
    protected final RenderState renderState;
    protected final VertexArray vertexArray;

    //Batch state
    private VertexBuffer vertexBuffer;
    private int elementCount = 0;

    protected G2DBatchRenderer(Shader shader, VertexAttribute[] layout, int maxVertexCapacity) {
        this.shader = shader;
        this.renderState = new RenderState();
        this.vertexArray = new VertexArray(maxVertexCapacity, layout);
    }

    public void setDepthTesting(boolean enable) {
        renderState.setDepthTesting(enable);
    }

    public void setViewport(int x, int y, int width, int height) {
        renderState.setViewport(x, y , width, height);
    }

    public void setBlending(boolean enable) {
        renderState.setBlending(enable);
    }

    public void setBlendFunction(BlendFactor source, BlendFactor destination) {
        renderState.setSrcBlendFactor(source);
        renderState.setDstBlendFactor(destination);
    }

    public void setBlendEquation(BlendEquation equation) {
        renderState.setBlendEquation(equation);
    }

    public void begin() {
        vertexBuffer = vertexArray.getBuffer();
        vertexBuffer.bind().map();
        elementCount = 0;
        onBegin();
    }

    public void draw(Matrix3 transform, float depth, Primitive primitive) {
        if (shouldFlush(primitive, vertexArray.getMaxElementCount() - elementCount)) {
            end();
            begin();
        }
        elementCount += onDraw(vertexBuffer, transform, depth, primitive);
    }

    public void end() {
        vertexBuffer.bind().unmap();
        vertexBuffer = null;
        onEnd();
        RenderUtil.DrawTriangles(shader, vertexArray, renderState, elementCount);
    }

    /**
     * Checks if the Batch requires a Flush before drawing a new Primitive
     * @param primitive the Primitive
     * @param remainingVertices the remaining Vertices available for that Primitive
     * @return true if the Batch should Flush
     */
    protected abstract boolean shouldFlush(Primitive primitive, int remainingVertices);

    protected abstract void onBegin();
    protected abstract int onDraw(VertexBuffer vertexBuffer, Matrix3 transform, float depth, Primitive primitive);
    protected abstract void onEnd();
}