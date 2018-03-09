package fr.hadriel.opengl;

public final class VertexAttribute {
    public final String name;
    public final GLType type;
    public final int components;
    public final boolean normalized;

    public VertexAttribute(String name, GLType type, int components) {
        this(name, type, components, false);
    }

    public VertexAttribute(String name, GLType type, int components, boolean normalized) {
        this.name = name;
        this.type = type;
        this.components = components;
        this.normalized = normalized;
    }

    public int size() {
        return type.size;
    }

    public boolean equals(Object obj) {
        if (obj instanceof VertexAttribute) {
            VertexAttribute a = (VertexAttribute) obj;
            return name.equals(a.name) && type == a.type && components == a.components && normalized == a.normalized;
        }
        return false;
    }
}