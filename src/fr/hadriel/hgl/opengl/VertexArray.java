package fr.hadriel.hgl.opengl;



import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL11.*;
/**
 * Created by glathuiliere setOn 06/12/2016.
 */
public abstract class VertexArray {

    private int handle;
    private int elementCount;
    private AttribPointer[] layout;

    public VertexArray(int elementCount, AttribPointer... layout) {
        if(!validateConfiguration(elementCount, layout)) throw new IllegalArgumentException("Invalid VAO configuration.");
        this.elementCount = elementCount;
        this.layout = layout;
        this.handle = glGenVertexArrays();
        onBackendCreate(elementCount, layout);
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

    public void draw(int mode, int offset, int elementCount) {
        glDrawArrays(mode, offset, elementCount);
    }

    public void draw(int mode, int elementCount) {
        draw(mode, 0, elementCount);
    }

    public void draw(int mode) {
        draw(mode, 0, elementCount);
    }

    public void unbind() {
        glBindVertexArray(0);
    }

    public int getElementCount() {
        return elementCount;
    }


    /**
     *
     * @param layoutIndex
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