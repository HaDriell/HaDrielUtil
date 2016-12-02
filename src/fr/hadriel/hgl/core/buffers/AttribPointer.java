package fr.hadriel.hgl.core.buffers;

/**
 * Created by HaDriel on 01/12/2016.
 *
 * Basic support for One data name per VBO
 */
class AttribPointer {

    public final GLType type;
    public final int components;
    public final boolean normalized;

    //Full control layout mod
    public AttribPointer(GLType type, int components, boolean normalized) {
        this.type = type;
        this.components = components;
        this.normalized = normalized;
    }

    public int getLayoutSize() {
        return components * type.size;
    }
}