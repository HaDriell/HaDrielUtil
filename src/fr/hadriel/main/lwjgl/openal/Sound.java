package fr.hadriel.main.lwjgl.openal;

import fr.hadriel.main.lwjgl.data.AudioData;

import java.nio.ByteBuffer;

import static org.lwjgl.openal.AL10.*;
/**
 * Created by glathuiliere on 03/01/2017.
 */
public class Sound {

    private int source;
    private int buffer;

    public Sound(AudioData data) {
        this(data.data, data.channels, data.samplerate);
    }

    private Sound(ByteBuffer data, int channels, int sampleRate) {
        //Create Source
        source = alGenSources();
        //Create & Bind Buffer
        buffer = alGenBuffers();
        alBufferData(buffer, OpenAL.getALFormat(channels, sampleRate), data, sampleRate);
        alSourcei(source, AL_BUFFER, buffer);
        //Set default settings
        setPitch(1);
        setGain(1);
        setPosition(0, 0, 0);
        setVelocity(0, 0, 0);
        setLooping(false);
    }

    public void destroy() {
        stop(); // ensure source is stopped
        alSourcei(source, AL_BUFFER, 0);
        alDeleteBuffers(buffer);
        alDeleteSources(source);
    }

    public void play() {
        alSourcePlay(source);
    }

    public void pause() {
        alSourcePause(source);
    }

    public void stop() {
        alSourceStop(source);
    }

    public void rewind() {
        alSourceRewind(source);
    }

    public void setLooping(boolean looping) {
        alSourcei(source, AL_LOOPING, looping ? AL_TRUE : AL_FALSE);
    }

    public void setPitch(float pitch) {
        alSourcef(source, AL_PITCH, pitch);
    }

    public void setGain(float gain) {
        alSourcef(source, AL_GAIN, gain);
    }

    public void setPosition(float x, float y, float z) {
        alSource3f(source, AL_POSITION, x, y, z);
    }

    public void setVelocity(float x, float y, float z) {
        alSource3f(source, AL_VELOCITY, x, y, z);
    }

    public boolean isPlaying() {
        return alGetSourcei(source, AL_PLAYING) == AL_TRUE;
    }

    public void setAttenuationModel(float rollOffFactor, float referenceDistance, float maxDistance) {
        alSourcef(source, AL_ROLLOFF_FACTOR, rollOffFactor);
        alSourcef(source, AL_REFERENCE_DISTANCE, referenceDistance);
        alSourcef(source, AL_MAX_DISTANCE, maxDistance);
    }
}