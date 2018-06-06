package fr.hadriel.application;

import fr.hadriel.math.Vec2;
import fr.hadriel.math.Vec3;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALCCapabilities;
import org.lwjgl.openal.ALCapabilities;

import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.openal.ALC10.*;

public final class Audio2D {
    private Audio2D() {}

    private static long device;
    private static long context;

    private static ALCCapabilities alcCapabilities;
    private static ALCapabilities alCapabilities;

    //Params
    private static Vec3 position = Vec3.ZERO;

    public static void initialize() {
        device = alcOpenDevice(alcGetString(0, ALC_DEFAULT_DEVICE_SPECIFIER));
        context = alcCreateContext(device, new int[] {0});
        alcMakeContextCurrent(context);
        alcCapabilities = ALC.createCapabilities(device);
        alCapabilities = AL.createCapabilities(alcCapabilities);

        //Init Listener in 2D XY space (not in the center but shifted on the Z axis)
        setScreenDistance(-1f);
    }

    public static void setScreenDistance(float distance) {
        position = new Vec3(position.x, position.y, distance);
        alListener3f(AL_POSITION, position.x, position.y, position.z);
    }

    public static void setListenerPosition(float x, float y) {
        position = new Vec3(x, y, position.z);
        alListener3f(AL_POSITION, position.x, position.y, position.z);
    }

    public static void setListenerPosition(Vec2 v) {
        setListenerPosition(v.x, v.y);
    }

    public static void terminate() {
        alcDestroyContext(context);
        alcCloseDevice(device);
    }
}