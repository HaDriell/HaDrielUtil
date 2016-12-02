package fr.hadriel.hgl.core.buffers;

import static org.lwjgl.opengl.GL15.*;

/**
 * Created by HaDriel on 02/12/2016.
 */
public enum GLMode {
    READ(GL_READ_ONLY),
    WRITE(GL_WRITE_ONLY),
    BOTH(GL_READ_WRITE)
    ;

    public final int mode;

    private GLMode(int mode) {
        this.mode = mode;
    }
}