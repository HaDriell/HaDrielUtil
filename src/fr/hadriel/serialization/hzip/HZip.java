package fr.hadriel.serialization.hzip;

/**
 * Created by glathuiliere on 27/10/2016.
 */
public final class HZip {
    public static final byte[] HEADER_PREFIX = {'H', 'Z', 'I', 'P'};
    public static final long HEADER_SIZE = 12;
    public static final byte[] EMPTY_HZIP = {
            'H','Z','I','P',0,0,0,0,0,0,0,2, // HEADER [ 'HZIP' + mapSize ]
            0,0 // empty map : [ count(0) ]
    };
}