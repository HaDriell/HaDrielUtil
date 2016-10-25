package fr.hadriel.serialization.hzip;

import fr.hadriel.serialization.Serial;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

/**
 * Created by HaDriel on 21/10/2016.
 */
public class HZipHeader {
    public static final byte[] PREFIX = "HZIP".getBytes();
    public static final byte MEMSIZE = (byte) (PREFIX.length + 8);

    public long mapPointer = MEMSIZE;

    public void read(DataInput in) throws IOException {
        byte[] prefix = new byte[4];
        Serial.readBytes(in, prefix);
        if(!Arrays.equals(prefix, PREFIX))
            throw new IOException("Bad Prefix");
        mapPointer = Serial.readLong(in);
        System.out.println("MapPointer: " + mapPointer);
    }

    public void write(DataOutput out) throws IOException {
        Serial.write(out, PREFIX);
        Serial.write(out, mapPointer);
    }
}