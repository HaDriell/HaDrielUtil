package fr.hadriel;

import fr.hadriel.util.typedef.TypeDefinitionException;
import fr.hadriel.util.typedef.TypeResolver;


import static fr.hadriel.util.Assert.*;
/**
 *
 * @author glathuiliere
 */
public class TestTypeDefinition {
    public static final class A {}
    public static final class B {}

    public static void main(String... args) {
        TypeResolver types = new TypeResolver();
        types.define(A.class);
        types.define(B.class);
        expect(TypeDefinitionException.class, () -> types.define(A.class));
    }
}
