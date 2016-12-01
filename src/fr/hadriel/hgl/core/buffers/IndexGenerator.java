package fr.hadriel.hgl.core.buffers;

import org.lwjgl.BufferUtils;

import java.nio.ShortBuffer;

/**
 * Created by HaDriel on 01/12/2016.
 */
public interface IndexGenerator {
    public ShortBuffer getIndexBuffer(int elementCount);


    // Basic Indexers :
    public static final IndexGenerator TRIANGLES = (elementCount) -> {
        ShortBuffer indices = BufferUtils.createShortBuffer(elementCount * 3);
        for(short index = 0; index < elementCount * 3; index++) {
            indices.put(index);
        }
        indices.flip();
        return indices;
    };

    public static final IndexGenerator QUADS = (elementCount) -> {
        ShortBuffer indices = BufferUtils.createShortBuffer(elementCount * 6);
        short V1 = 0, V2 = 1, V3 = 2, V4 = 2, V5 = 3, V6 = 0;
        short offset = 0;
        for(short quad = 0; quad < elementCount; quad++) {
            indices.put((short) (V1 + offset));
            indices.put((short) (V2 + offset));
            indices.put((short) (V3 + offset));
            indices.put((short) (V4 + offset));
            indices.put((short) (V5 + offset));
            indices.put((short) (V6 + offset));
        }
        indices.flip();
        return indices;
    };
}