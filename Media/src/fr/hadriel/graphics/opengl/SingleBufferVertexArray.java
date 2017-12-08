package fr.hadriel.graphics.opengl;

import static org.lwjgl.opengl.GL20.*;
/**
 * Created by glathuiliere on 06/12/2016.
 */
public class SingleBufferVertexArray extends VertexArray {

    private VertexBuffer vbo;

    public SingleBufferVertexArray(int maxElementCount, AttribPointer... layout) {
        super(maxElementCount, layout);
    }

    public VertexBuffer getBuffer(int layoutIndex) { //not used
        return vbo;
    }

    protected void onBackendCreate(int elementCount, AttribPointer[] layout) {
        bind();
        int stride = 0; // will represent the size of all contiguous attributes of angle vertex
        for(AttribPointer pointer : layout) stride += pointer.getAttribSize();

        vbo = new VertexBuffer(elementCount * stride);
        vbo.bind();
        int offset = 0;
        for(int i = 0; i < layout.length; i++) {
            AttribPointer pointer = layout[i];
            glEnableVertexAttribArray(i);
            glVertexAttribPointer(i,
                    pointer.components(),
                    pointer.type(),
                    pointer.normalized(),
                    stride,
                    offset);
            offset += pointer.getAttribSize();
        }
        vbo.unbind();
        unbind();
    }

    protected void onBackendDestroy(int elementCount, AttribPointer[] layout) {
        bind();
        for(int i = 0; i < layout.length; i++) glDisableVertexAttribArray(i);
        unbind();
        vbo.destroy();
    }
}