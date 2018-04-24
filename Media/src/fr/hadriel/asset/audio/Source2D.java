package fr.hadriel.asset.audio;

import fr.hadriel.math.Vec2;

import static org.lwjgl.openal.AL10.*;

public class Source2D {

    private int source;

    public Source2D() {
        source = alGenSources();
    }

    public void dispose() {
        source = -1;
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

    public void queue(Sound... sounds) {
        int[] buffers = new int[sounds.length];
        for(int i = 0; i < sounds.length; i++) {
            buffers[i] = sounds[i].buffer;
        }
        alSourceQueueBuffers(source, buffers);
    }

    public void unqueue(Sound... sounds) {
        int[] buffers = new int[sounds.length];
        for(int i = 0; i < sounds.length; i++) {
            buffers[i] = sounds[i].buffer;
        }
        alSourceUnqueueBuffers(source, buffers);
    }

    public boolean isPlaying() {
        return alGetSourcei(source, AL_PLAYING) == AL_TRUE;
    }

    public boolean isPaused() {
        return alGetSourcei(source, AL_PAUSED) == AL_TRUE;
    }

    public boolean isStopped() {
        return alGetSourcei(source, AL_STOPPED) == AL_TRUE;
    }

    public void setPosition(float x, float y) {
        alSource3f(source, AL_POSITION, x, y, 0);
    }

    public void setDirection(float x, float y) {
        alSource3f(source, AL_DIRECTION, x, y, 0);
    }

    public void setGain(float gain) {
        alSourcef(source, AL_GAIN, gain);
    }

    public void setPitch(float pitch) {
        alSourcef(source, AL_GAIN, pitch);
    }

    public void setLooping(boolean looping) {
        alSourcei(source, AL_LOOPING, looping ? AL_TRUE : AL_FALSE);
    }

    public void setPosition(Vec2 v) {
        setPosition(v.x, v.y);
    }

    public void setDirection(Vec2 v) {
        setDirection(v.x, v.y);
    }
}