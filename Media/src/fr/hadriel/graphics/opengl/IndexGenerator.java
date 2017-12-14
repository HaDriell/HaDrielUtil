package fr.hadriel.graphics.opengl;

import org.lwjgl.BufferUtils;

import java.nio.ShortBuffer;

/**
 * Created by HaDriel on 01/12/2016.
 */
public interface IndexGenerator {
    public static void checkElementCount(int elementCount) {
        if(elementCount < 0 || elementCount > Short.MAX_VALUE) throw new IllegalArgumentException("Invalid Element components :" + elementCount);
    }


    public ShortBuffer getIndexBuffer(int elementCount);


    // Basic Indexers :
    public static final IndexGenerator TRIANGLES = (elementCount) -> {
        checkElementCount(elementCount);
        ShortBuffer indices = BufferUtils.createShortBuffer(elementCount);
        for(int i = 0; i + 3 <= elementCount; i+= 3) {
            indices.put((short) (i + 0));
            indices.put((short) (i + 1));
            indices.put((short) (i + 2));
        }
        indices.flip();
        return indices;
    };

    public static final IndexGenerator QUADS = (elementCount) -> {
        checkElementCount(elementCount);
        ShortBuffer indices = BufferUtils.createShortBuffer(elementCount);
        for(int i = 0; i + 6 <= elementCount; i += 6) {
            indices.put((short) (i + 0));
            indices.put((short) (i + 1));
            indices.put((short) (i + 2));
            indices.put((short) (i + 2));
            indices.put((short) (i + 3));
            indices.put((short) (i + 0));
        }
        indices.flip();
        return indices;
    };
}