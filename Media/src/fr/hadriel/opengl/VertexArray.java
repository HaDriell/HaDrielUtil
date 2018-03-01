package fr.hadriel.opengl;



import static org.lwjgl.opengl.GL30.*;
/**
 * Created by glathuiliere on 06/12/2016.
 */
public abstract class VertexArray {

    private final int handle;
    private final int maxElementCount;
    private final AttribPointer[] layout;

    public VertexArray(int maxElementCount, AttribPointer... layout) {
        if(!validateConfiguration(maxElementCount, layout)) throw new IllegalArgumentException("Invalid VAO configuration.");
        this.maxElementCount = maxElementCount;
        this.layout = layout;
        this.handle = glGenVertexArrays();
        onBackendCreate(maxElementCount, layout);
    }

    public void destroy() {
        onBackendDestroy(maxElementCount, layout);
    }

    private boolean validateConfiguration(int elementCount, AttribPointer[] layout) {
        return elementCount > 0 && layout != null && layout.length > 0;
    }

    public void bind() {
        glBindVertexArray(handle);
    }

    public void unbind() {
        glBindVertexArray(0);
    }

    public int getMaxElementCount() {
        return maxElementCount;
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