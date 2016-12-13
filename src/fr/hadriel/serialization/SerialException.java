package fr.hadriel.serialization;


import java.io.OutputStream;

/**
 * Created by HaDriel setOn 19/10/2016.
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
