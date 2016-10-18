package fr.hadriel.serialization;

/**
 * Created by glathuiliere on 18/10/2016.
 */
public interface Primitive {

    public byte asByte();

    public boolean asBoolean();

    public short asShort();

    public char asChar();

    public int asInt();

    public long asLong();

    public float asFloat();

    public double asDouble();

    public String asString();
}