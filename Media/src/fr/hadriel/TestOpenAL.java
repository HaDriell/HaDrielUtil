package fr.hadriel;

import fr.hadriel.audio.ogg.VorbisFile;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALCCapabilities;
import org.lwjgl.openal.ALCapabilities;


import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.openal.ALC10.*;

public class TestOpenAL {
    public static void main(String[] args) {

        //OpenAL setup
        String defaultDeviceName = alcGetString(0, ALC_DEFAULT_DEVICE_SPECIFIER);
        long device = alcOpenDevice(defaultDeviceName);
        int[] attributes = {0};
        long context = alcCreateContext(device, attributes);
        alcMakeContextCurrent(context);
        ALCCapabilities alcCapabilities = ALC.createCapabilities(device);
        ALCapabilities alCapabilities = AL.createCapabilities(alcCapabilities);
        if(!alCapabilities.OpenAL10) {
            System.err.println("Unable to find OpenAL 1.0 support");
            System.exit(1);
        }



        int sound = alGenBuffers();
        try (VorbisFile file = new VorbisFile("Media/res/wilhelm.ogg")) {
            int format = file.channels == 1 ? AL_FORMAT_MONO16 : AL_FORMAT_STEREO16;
            alBufferData(sound, format, file.samples, file.frequency);
        }
        int source = alGenSources();
        alSourcei(source, AL_BUFFER, sound);
        alSourcePlay(source);

        while(alGetSourcei(source, AL_SOURCE_STATE) == AL_PLAYING) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException ignore) {}
        }


        alDeleteSources(source);
        alDeleteBuffers(sound);

        //OpenAL cleanup
        alcDestroyContext(context);
        alcCloseDevice(device);
    }
}