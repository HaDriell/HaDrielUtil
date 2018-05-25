package fr.hadriel.opengl.shader;


final class UniformDeclaration {

    public final String name;
    public final int location;
    public final GLSLType type;

    public UniformDeclaration(String name, int location, GLSLType type) {
        this.name = name;
        this.location = location;
        this.type = type;
    }

    public String toString() {
        return String.format("declaration %s %s", type, name);
    }

    public boolean equals(Object obj) {
        if (obj instanceof UniformDeclaration) {
            UniformDeclaration other = (UniformDeclaration) obj;
            return type == other.type && location == other.location; // ignore name, location is already supposedly unique and type too.
        }
        return false;
    }
}