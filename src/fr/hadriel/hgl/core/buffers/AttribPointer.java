package fr.hadriel.hgl.core.buffers;

import fr.hadriel.hgl.core.GLType;

import static org.lwjgl.opengl.GL20.*;
/**
 * Created by HaDriel on 01/12/2016.
 *
 * Basic support for One data type per VBO
 */
class AttribPointer {

    public final GLType type; // datatype & size
    public final int count;
    public final boolean normalized;

    //Full control layout mod
    public AttribPointer(GLType type, int count, boolean normalized) {
        this.type = type;
        this.count = count;
        this.normalized = normalized;
    }

    public int getAttribSize() {
        return count * type.size;
    }
}