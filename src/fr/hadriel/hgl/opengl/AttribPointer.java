package fr.hadriel.hgl.opengl;

/**
 * Created by HaDriel on 01/12/2016.
 *
 * Basic support for One data name per VBO
 */
public class AttribPointer {

    private GLType type;
    private int components;
    private boolean normalized;

    //Full control layout mod
    public AttribPointer(GLType type, int components, boolean normalized) {
        this.type = type;
        this.components = components;
        this.normalized = normalized;
    }

    public int getAttribSize() {
        return components * type.size;
    }

    public int type() {
        return type.name;
    }

    public int size() {
        return type.size;
    }

    public boolean normalized() {
        return normalized;
    }

    public int count() {
        return components;
    }
}