package fr.hadriel.serialization.io;

/**
 * Created by glathuiliere on 25/10/2016.
 */
public interface SerialAccess extends SerialInput, SerialOutput {

    public void seek(long pointer);

    public long getPosition();

    public long remaining();

    public long length();
}