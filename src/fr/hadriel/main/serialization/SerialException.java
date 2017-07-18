package fr.hadriel.main.serialization;



/**
 * Created by HaDriel on 19/10/2016.
 */
public class SerialException extends Exception {

    public SerialException() {
        super();
    }

    public SerialException(String message) {
        super(message);
    }

    public SerialException(String message, Throwable cause) {
        super(message, cause);
    }

    public SerialException(Throwable cause) {
        super(cause);
    }
}
