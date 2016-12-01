package fr.hadriel.hgl.core.gl3;

import fr.hadriel.hgl.core.GLType;

import static org.lwjgl.opengl.GL20.*;
/**
 * Created by HaDriel on 01/12/2016.
 *
 * Basic support for One data type per VBO
 */
class AttribInfo {

    public final GLType type; // datatype & size
    public final int count;
    public final boolean normalized;

    //Full control layout mod
    public AttribInfo(GLType type, int count, boolean normalized) {
        this.type = type;
        this.count = count;
        this.normalized = normalized;
    }

    public int getElementSize() {
        return count * type.size;
    }
}