package fr.hadriel.graphics.opengl;

import java.util.Arrays;

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

    public void matches(GLType... glTypes) {
        for(GLType type : glTypes)
            if(this == type)
                return;
        throw new IllegalArgumentException("Invalid GLType " + this + ", expected of one of " + Arrays.toString(glTypes));
    }

    public String toString() {
        switch (name) {
            case GL_BYTE: return "GL_BYTE";
            case GL_UNSIGNED_BYTE: return "GL_UNSIGNED_BYTE";
            case GL_SHORT: return "GL_SHORT";
            case GL_UNSIGNED_SHORT: return "GL_UNSIGNED_SHORT";
            case GL_INT: return "GL_INT";
            case GL_UNSIGNED_INT: return "GL_UNSIGNED_INT";
            case GL_FLOAT: return "GL_FLOAT";
            case GL_DOUBLE: return "GL_DOUBLE";
            default:
                throw new IllegalStateException("Unknown GLType Enum");
        }
    }
}