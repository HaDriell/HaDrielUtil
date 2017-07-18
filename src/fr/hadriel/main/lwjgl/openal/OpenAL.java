package fr.hadriel.main.lwjgl.openal;

import fr.hadriel.main.math.Vec3;
import org.lwjgl.openal.*;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.openal.ALC10.*;

/**
 * Created by glathuiliere on 03/01/2017.
 */
public class OpenAL {

    private static long device;
    private static long context;
    private static ALCapabilities capabilities;

    public static void create() {
        if(capabilities != null) return;
        device = alcOpenDevice((ByteBuffer) null);
        ALCCapabilities alcCaps = ALC.createCapabilities(device);
        context = alcCreateContext(device, (IntBuffer) null);
        alcMakeContextCurrent(context);
        capabilities = AL.createCapabilities(alcCaps);
        alListener3f(AL_POSITION, 0, 0, 0);
        alDistanceModel(AL_INVERSE_DISTANCE_CLAMPED);
    }

    public static void destroy() {
        if(capabilities == null) return;
        alcDestroyContext(context);
        alcCloseDevice(device);
        context = 0;
        device = 0;
        capabilities = null;
    }

    public static void setMasterGain(float gain) {
        alListenerf(AL_GAIN, gain);
    }

    public static void setListenerPosition(float x, float y, float z) {
        alListener3f(AL_POSITION, x, y, z);
    }

    public static void setListenerOrientation(Vec3 at, Vec3 up) {
        alListenerfv(AL_ORIENTATION, new float[]{at.x, at.y, at.z, up.x, up.y, up.z});
    }

    public static int getALFormat(int channels, int samples) {
        if(samples == 16) return channels > 1 ? AL_FORMAT_STEREO16 : AL_FORMAT_MONO16;
        if(samples == 8) return channels > 1 ? AL_FORMAT_STEREO8 : AL_FORMAT_MONO8;
        return -1;
    }

    public static void printALFormat(int alFormat) {
        switch (alFormat) {
            case AL_FORMAT_STEREO16: System.out.println("AL_FORMAT_STEREO16"); break;
            case AL_FORMAT_STEREO8: System.out.println("AL_FORMAT_STEREO8"); break;
            case AL_FORMAT_MONO16: System.out.println("AL_FORMAT_MONO16"); break;
            case AL_FORMAT_MONO8: System.out.println("AL_FORMAT_MONO8"); break;
            default: System.out.println("Unkown Format");
        }
    }

    public static void checkALError() {
        switch (alGetError()) {
            case AL_OUT_OF_MEMORY: System.err.println("AL_OUT_OF_MEMORY"); break;
            case AL_INVALID_VALUE: System.err.println("AL_INVALID_VALUE"); break;
            case AL_INVALID_ENUM: System.err.println("AL_INVALID_ENUM"); break;
            case AL_INVALID_NAME: System.err.println("AL_INVALID_NAME"); break;
            case AL_INVALID_OPERATION: System.err.println("AL_INVALID_OPERATION"); break;
        }
    }
}
