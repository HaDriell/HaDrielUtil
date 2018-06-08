package fr.hadriel.g2d;

import fr.hadriel.opengl.RenderState;
import fr.hadriel.opengl.VertexArray;

public final class G2DRenderer {
    public static final int DEFAULT_VERTEX_CAPACITY = 60_000; // 10K quads
    public static final int DEFAULT_VERTEX_ARRAY_COUNT = 2; // 2 VAOs to avoid locks

    private RenderState renderState;

    private VertexArray[] vertexArrays;
    private int activeVAO;


    protected G2DRenderer(int vertexArrayCapacity, int vertexArrayCount) {
        this.renderState = new RenderState();
        this.vertexArrays = new VertexArray[vertexArrayCount];
        for (int i = 0; i < vertexArrays.length; i++) {
            vertexArrays[i] = new VertexArray(vertexArrayCapacity, G2DVertex.VERTEX_LAYOUT);
        }
        this.activeVAO = 0;
    }

    private VertexArray getVertexArray() {
        activeVAO = (activeVAO + 1) % vertexArrays.length; // next VAO in the queue
        return vertexArrays[activeVAO];
    }

    private void begin() {

    }

    private void submit(G2DMesh mesh) {

    }

    private void end() {

    }
}