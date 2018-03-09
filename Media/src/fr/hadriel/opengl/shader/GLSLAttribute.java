package fr.hadriel.opengl.shader;


import fr.hadriel.opengl.VertexAttribute;

public final class GLSLAttribute {
    public final String name;
    public final GLSLType type;

    public GLSLAttribute(String name, GLSLType type) {
        this.name = name;
        this.type = type;
    }

    public boolean validate(VertexAttribute attribute) {
        return name.equals(attribute.name) && type.size == attribute.type.size * attribute.components;
    }
}