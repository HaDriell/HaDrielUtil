package fr.hadriel.audio.ogg;

import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import static org.lwjgl.stb.STBVorbis.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.libc.Stdlib.*;

public class VorbisFile implements AutoCloseable {

    public final int frequency;
    public final int channels;
    public final ShortBuffer samples;

    public VorbisFile(String filename) {

        try (MemoryStack stack = stackPush()) {
            IntBuffer channelsBuffer = stack.mallocInt(1);
            IntBuffer frequencyBuffer = stack.mallocInt(1);
            this.samples = stb_vorbis_decode_filename(filename, channelsBuffer, frequencyBuffer);
            this.channels = channelsBuffer.get(0);
            this.frequency = frequencyBuffer.get(0);
        }
    }

    public void close() {
        free(samples);
    }
}