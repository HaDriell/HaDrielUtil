package fr.hadriel.serialization.hzip.old;

import fr.hadriel.serialization.Serial;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * Created by HaDriel on 13/10/2016.
 */
public class Index {
    private String name;
    private long length;

    Index(DataInput in) throws IOException {
        load(in);
    }

    Index(String name, long length) {
        name(name);
        length(length);
    }

    void name(String name) {
        this.name = new String(name.getBytes()); // deep copy, avoid future modification
    }

    public String name() {
        return name;
    }

    void length(long length) {
        this.length = length;
    }

    public long length() {
        return length;
    }

    public int memsize() {
        return 2 + name.length() + 8;
    }


    void load(DataInput in) throws IOException {
        short strlen = Serial.readShort(in);
        this.name = new String(Serial.readBytes(in, strlen));
        this.length = Serial.readInt(in);
    }

    void save(DataOutput out) throws IOException {
        Serial.write(out, (short) name.length());
        Serial.write(out, name.getBytes());
        Serial.write(out, length);
    }
}