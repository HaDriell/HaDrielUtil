package fr.hadriel;

import fr.hadriel.lwjgl.openal.AudioData;
import fr.hadriel.lwjgl.openal.OpenAL;
import fr.hadriel.lwjgl.openal.Sound;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by glathuiliere on 10/02/2017.
 */
public class TestOpenALOgg {
    public static void main(String[] args) throws IOException {
        InputStream stream = new FileInputStream("music.ogg");
        AudioData data = AudioData.loadOggVorbis(stream);
        stream.close();
        OpenAL.create();
        Sound s = new Sound(data);
        s.play();
        try {
            Thread.sleep(16000);
        } catch (InterruptedException ignore) {}
        OpenAL.destroy();
    }
}
