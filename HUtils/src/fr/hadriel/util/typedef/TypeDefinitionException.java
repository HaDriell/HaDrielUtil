package fr.hadriel.util.typedef;

/**
 *
 * @author glathuiliere
 */
public class TypeDefinitionException extends RuntimeException {

    public TypeDefinitionException() {
        super();
    }

    public TypeDefinitionException(String message) {
        super(message);
    }

    public TypeDefinitionException(String message, Throwable cause) {
        super(message, cause);
    }

    public TypeDefinitionException(Throwable cause) {
        super(cause);
    }

    protected TypeDefinitionException(String message, Throwable cause,
                               boolean enableSuppression,
                               boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
