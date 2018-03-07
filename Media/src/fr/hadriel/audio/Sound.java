package fr.hadriel.audio;

import fr.hadriel.asset.Asset;
import fr.hadriel.asset.AssetManager;
import fr.hadriel.audio.ogg.VorbisFile;

import static org.lwjgl.openal.AL10.*;

public class Sound extends Asset {

    private final String filename;
    public int buffer;

    public Sound(String filename) {
        this.filename = filename;
        this.buffer = -1;
    }

    protected void onLoad(AssetManager manager) {
        try (VorbisFile file = new VorbisFile(filename)) {
            buffer = alGenBuffers();
            alBufferData(buffer, file.channels == 1 ? AL_FORMAT_MONO16 : AL_FORMAT_STEREO16, file.samples, file.frequency);
        }
    }

    protected void onUnload(AssetManager manager) {
        alDeleteBuffers(buffer);
        buffer = -1;
    }
}