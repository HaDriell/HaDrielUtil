package fr.hadriel.lwjgl.data;

import fr.hadriel.lwjgl.openal.OggDecoder;
import fr.hadriel.lwjgl.openal.WavFile;
import org.lwjgl.BufferUtils;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

/**
 * Created by glathuiliere on 09/02/2017.
 */
public class AudioData {
    private static final int BUFFER_SIZE = 2048;

    public final ByteBuffer data;
    public final int samplerate;
    public final int channels;

    public AudioData(ByteBuffer data, int samplerate, int channels) {
        this.data = data;
        this.samplerate = samplerate;
        this.channels = channels;
    }

    public AudioData(byte[] data, int samplerate, int channels) {
        this(BufferUtils.createByteBuffer(data.length), samplerate, channels);
        this.data.put(data);
        this.data.rewind();
        System.out.println("data extract: " + new String(data, 0, 512));
    }

    public static AudioData loadWave(InputStream stream) throws IOException {
        WavFile wavFile = WavFile.readWavStream(stream);
        return null;
    }

    public static AudioData loadJavaSound(InputStream stream) throws IOException {
        ByteBuffer data;
        int samplerate;
        int channels;
        try {
            AudioInputStream in = AudioSystem.getAudioInputStream(stream);
            AudioFormat audioFormat = in.getFormat();
            if (audioFormat.isBigEndian()) throw new UnsupportedAudioFileException("Big Endian file"); // ?
            channels = audioFormat.getChannels();
            samplerate = (int) audioFormat.getSampleRate();
            byte[] buffer = new byte[in.available()];
            in.read(buffer);
            in.close();
            data = BufferUtils.createByteBuffer(buffer.length);
            data.put(buffer).flip();
        } catch (Exception e) {
            throw new IOException(e);
        }
        return new AudioData(data, samplerate, channels);
    }

    public static AudioData loadOggVorbis(InputStream stream) throws IOException {
        OggDecoder decoder = new OggDecoder();
        AudioData data = decoder.getData(stream);
        return data;
    }
}