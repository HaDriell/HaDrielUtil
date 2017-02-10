package fr.hadriel.lwjgl.stb;

import fr.hadriel.lwjgl.openal.OpenAL;
import org.lwjgl.BufferUtils;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Created by glathuiliere on 03/01/2017.
 */
public class AudioFile {

    public final ByteBuffer pcm;
    public final int format;
    public final int sampleRate;

    public AudioFile(String filename) throws IOException {
        try {
            AudioInputStream in = AudioSystem.getAudioInputStream(new File(filename));
            AudioFormat af = in.getFormat();
            if (af.isBigEndian()) throw new UnsupportedAudioFileException("Big Endian file"); // ?

            format = OpenAL.getALFormat(af.getChannels(), af.getSampleSizeInBits());
            sampleRate = (int) af.getSampleRate();
            byte[] buffer = new byte[in.available()];
            in.read(buffer);
            in.close();
            pcm = BufferUtils.createByteBuffer(buffer.length);
            pcm.put(buffer).flip();
        } catch (Exception e) {
            throw new IOException(e);
        }
    }
}