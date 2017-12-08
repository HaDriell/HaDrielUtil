package fr.hadriel.graphics.opengl;


import static org.lwjgl.opengl.GL20.*;
/**
 * Created by HaDriel on 03/12/2016.
 */
public class MultiBufferVertexArray extends VertexArray {

    private VertexBuffer[] vbos;

    public MultiBufferVertexArray(int elementCount, AttribPointer... attribPointers) {
        super(elementCount, attribPointers);
    }

    protected void onBackendCreate(int elementCount, AttribPointer[] layout) {
        bind();
        vbos = new VertexBuffer[layout.length];
        for(int i = 0; i < layout.length; i++) {
            AttribPointer pointer = layout[i];
            VertexBuffer vbo = new VertexBuffer(pointer.getAttribSize() * elementCount);
            vbo.bind();
            glEnableVertexAttribArray(i);
            glVertexAttribPointer(i,
                    pointer.components(),
                    pointer.type(),
                    pointer.normalized(),
                    0,
                    0);
            vbos[i] = vbo;
        }
        unbind();
    }

    protected void onBackendDestroy(int elementCount, AttribPointer[] layout) {
        bind();
        for(int i = 0; i < layout.length; i++) {
            glDisableVertexAttribArray(i); // hope it's enough lol
            vbos[i].destroy();
        }
        unbind();
    }

    public VertexBuffer getBuffer(int layoutIndex) {
        return vbos[layoutIndex];
    }
}