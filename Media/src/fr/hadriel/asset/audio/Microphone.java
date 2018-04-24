package fr.hadriel.asset.audio;

import java.nio.ByteBuffer;

import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.openal.ALC11.*;

public class Microphone {

    public final long device;

    public Microphone() {
        this(null, 22050, 11025);
    }

    public Microphone(String device, int frequency, int bufferSize) {
        if(device != null)
            this.device = alcCaptureOpenDevice(device, frequency, AL_FORMAT_MONO16, bufferSize);
        else
            this.device = alcCaptureOpenDevice((ByteBuffer) null, frequency, AL_FORMAT_MONO16, bufferSize);
    }

    public void start() {
        alcCaptureStart(device);
    }

    public void stop() {
        alcCaptureStop(device);
    }
}