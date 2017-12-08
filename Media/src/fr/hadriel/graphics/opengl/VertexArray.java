package fr.hadriel.graphics.opengl;



import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL11.*;
/**
 * Created by glathuiliere on 06/12/2016.
 */
public abstract class VertexArray {

    private int handle;
    private int elementCount;
    private AttribPointer[] layout;

    public VertexArray(int maxElementCount, AttribPointer... layout) {
        if(!validateConfiguration(maxElementCount, layout)) throw new IllegalArgumentException("Invalid VAO configuration.");
        this.elementCount = maxElementCount;
        this.layout = layout;
        this.handle = glGenVertexArrays();
        onBackendCreate(maxElementCount, layout);
    }

    public void destroy() {
        onBackendDestroy(elementCount, layout);
    }

    public void configure(int nelementCount, AttribPointer... nlayout) {
        if(!validateConfiguration(nelementCount, nlayout)) throw new IllegalArgumentException("Invalid VAO configuration.");
        onBackendDestroy(elementCount, layout);
        onBackendCreate(nelementCount, nlayout);
        this.elementCount = nelementCount;
        this.layout = nlayout;
    }

    private boolean validateConfiguration(int elementCount, AttribPointer[] layout) {
        return elementCount > 0 && layout != null && layout.length > 0;
    }

    public void bind() {
        glBindVertexArray(handle);
    }

    public void drawElements(int mode, int count, int type) {
        glDrawElements(mode, count, type, 0);
    }

    public void drawArrays(int mode, int offset, int elementCount) {
        glDrawArrays(mode, offset, elementCount);
    }

    public void drawArrays(int mode, int elementCount) {
        drawArrays(mode, 0, elementCount);
    }

    public void drawArrays(int mode) {
        drawArrays(mode, 0, elementCount);
    }

    public void unbind() {
        glBindVertexArray(0);
    }

    public int getElementCount() {
        return elementCount;
    }


    /**
     *
     * @param layoutIndex the index of the tree VBO in this VAO
     * @return the VertexBuffer associated with the AttribPointer at layoutIndex
     */
    public abstract VertexBuffer getBuffer(int layoutIndex);

    /**
     * Called at VertexArrayObject instanciation & modification when size and/or layout is changed<br/>
     * Only creates & setup the new backend of this VAO
     */
    protected abstract void onBackendCreate(int elementCount, AttribPointer[] layout);


    /**
     * Called at VertexArrayObject instanciation & modification when size and/or layout is changed<br/>
     * Only clean-up & destroy the old backend of this VAO
     */
    protected abstract void onBackendDestroy(int elementCount, AttribPointer[] layout);
}