package fr.hadriel.g2d;

import fr.hadriel.opengl.VertexArray;
import fr.hadriel.opengl.VertexBuffer;

import java.util.List;

public final class G2DMesh {

    private VertexArray vertexArray;

    public void setData(List<G2DVertex> vertices) {
        if (vertexArray != null) {
            vertexArray.destroy();
            vertexArray = null;
        }

        vertexArray = new VertexArray(vertices.size(), G2DVertex.VERTEX_LAYOUT);
        VertexBuffer vertexBuffer = vertexArray.getBuffer();
        vertexBuffer.bind();
        vertexBuffer.map();
        vertices.forEach(vertex -> vertexBuffer.write(vertex.position).write(vertex.color).write(vertex.uv));
        vertexBuffer.unmap();
        vertexBuffer.unbind();
    }

    public VertexArray getVertexArray() {
        return vertexArray;
    }
}