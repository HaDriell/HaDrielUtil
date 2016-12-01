package fr.hadriel.hgl.core;

import java.nio.ShortBuffer;

/**
 * Created by HaDriel on 01/12/2016.
 */
public interface IndexGenerator {
    public void setData(int elementCount, ShortBuffer indices);
}