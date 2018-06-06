package fr.hadriel.asset.audio;

import fr.hadriel.asset.Asset;
import fr.hadriel.io.VorbisFile;

import java.nio.ByteBuffer;
import java.nio.file.Path;

import static org.lwjgl.openal.AL10.*;

public class Sound extends Asset {

    //package-private
    int buffer = -1;



    protected void onLoad(Path path, ByteBuffer fileContent) {
        try (VorbisFile file = new VorbisFile(fileContent)) {
            buffer = alGenBuffers();
            alBufferData(buffer, file.channels == 1 ? AL_FORMAT_MONO16 : AL_FORMAT_STEREO16, file.samples, file.frequency);
        }
    }

    protected void onUnload() {
        if (buffer != -1) alDeleteBuffers(buffer);
        buffer = -1;
    }
}