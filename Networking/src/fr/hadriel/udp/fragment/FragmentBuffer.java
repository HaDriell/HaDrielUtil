package fr.hadriel.udp.fragment;

/**
 *
 * @author glathuiliere
 */
public class FragmentBuffer {
    public final int count;
    public final byte[][] fragments;

    public FragmentBuffer(byte[]... fragments) {
        this.count = fragments.length;
        this.fragments = fragments;
    }
}