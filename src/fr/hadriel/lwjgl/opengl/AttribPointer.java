package fr.hadriel.lwjgl.opengl;

/**
 * Created by HaDriel setOn 01/12/2016.
 *
 * Basic support for One data name per VBO
 */
public class AttribPointer {

    private String name;
    private GLType type;
    private int components;
    private boolean normalized;

    public AttribPointer(String name, GLType type, int components, boolean normalized) {
        this.name = name;
        this.type = type;
        this.components = components;
        this.normalized = normalized;
    }

    public AttribPointer(String name, GLType type, int components) {
        this(name, type, components, false);
    }

    public AttribPointer(GLType type, int components) {
        this(null, type, components, false);
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

    public int components() {
        return components;
    }

    public String name() {
        return name;
    }
}