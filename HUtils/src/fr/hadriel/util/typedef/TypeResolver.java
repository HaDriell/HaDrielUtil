package fr.hadriel.util.typedef;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author glathuiliere
 */
public class TypeResolver {

    private final List<TypeDefinition> definitions;
    private long nextid;

    public TypeResolver() {
        this.definitions = new ArrayList<>();
        this.nextid = 0;
    }

    public boolean isDefined(Class type) {
        for(TypeDefinition typedef : definitions)
            if(typedef.type == type)
                return true;
        return false;
    }

    public void define(Class type)  throws TypeDefinitionException {
        define(type, nextid++);
    }

    public void define(Class type, long id)  throws TypeDefinitionException {
        define(type, id, false);
    }

    public void define(Class type, long id, boolean override) throws TypeDefinitionException {
        TypeDefinition definition = new TypeDefinition(type, id);

        if(override) {
            undefine(type);
            undefine(id);
        } else if (definitions.contains(definition))
            throw new TypeDefinitionException("Type or ID already defined");

        definitions.add(definition);
    }

    public void undefine(long id) {
        definitions.removeIf(typedef -> typedef.id == id);
    }

    public void undefine(Class type) {
        definitions.removeIf(typedef -> typedef.type == type);
    }

    public Class resolve(long id) throws TypeDefinitionException {
        for(TypeDefinition typedef : definitions)
            if(typedef.id == id)
                return typedef.type;
        throw new TypeDefinitionException("Type ID " + id + " is not defined");
    }

    public long typeid(Class type) throws TypeDefinitionException {
        for(TypeDefinition typedef : definitions)
            if(typedef.type == type)
                return typedef.id;
        throw new TypeDefinitionException("Type " + type.getName() + " is not defined");
    }
}