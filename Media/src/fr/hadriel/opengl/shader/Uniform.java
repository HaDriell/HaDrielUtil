package fr.hadriel.opengl.shader;


final class Uniform {

    public final String name;
    public final int location;
    public final GLSLType type;

    public Uniform(String name, int location, GLSLType type) {
        this.name = name;
        this.location = location;
        this.type = type;
    }

    public String toString() {
        return String.format("uniform %s %s", type, name);
    }
}