package fr.hadriel.lwjgl.opengl;

import static org.lwjgl.opengl.GL11.*;
/**
 * Created by HaDriel on 01/12/2016.
 */
public enum GLType {
    BYTE(GL_BYTE, 1),
    UBYTE(GL_UNSIGNED_BYTE, 1),
    SHORT(GL_SHORT, 2),
    USHORT(GL_UNSIGNED_SHORT, 2),
    INT(GL_INT, 4),
    UINT(GL_UNSIGNED_INT, 4),
    FLOAT(GL_FLOAT, 4),
    DOUBLE(GL_DOUBLE, 8),
    ;

    public final int name;
    public final int size;

    private GLType(int name, int size) {
        this.name = name;
        this.size = size;
    }
}