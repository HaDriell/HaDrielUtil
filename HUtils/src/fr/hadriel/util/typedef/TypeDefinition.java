package fr.hadriel.util.typedef;

import java.util.Objects;

/**
 *
 * @author glathuiliere
 */
public final class TypeDefinition {
    public final Class type;
    public final long id;

    public TypeDefinition(Class type, long id) {
        this.type = Objects.requireNonNull(type);
        this.id = id;
    }

    public int hashCode() {
        return type.hashCode();
    }

    public boolean equals(Object obj) {
        if(obj == this) return true;
        if(obj instanceof TypeDefinition) {
            TypeDefinition typedef = (TypeDefinition) obj;
            return typedef.id == this.id || typedef.type == this.type; // true if either type or id matches.
        }
        return false;
    }
}