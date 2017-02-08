package fr.hadriel;

import fr.hadriel.lwjgl.openal.OpenAL;
import fr.hadriel.lwjgl.openal.Sound;
import fr.hadriel.lwjgl.resources.AudioFile;
import fr.hadriel.math.Mathf;
import fr.hadriel.util.Timer;

import java.io.IOException;

/**
 * Created by glathuiliere on 02/01/2017.
 */
public class TestSound {

    public static void main(String[] args) throws IOException {
        OpenAL.create();
        AudioFile file = new AudioFile("music.wav");
        Sound s = new Sound(file);
//        s.setAttenuationModel(1, 1, 1);
        s.play();
        Timer t = new Timer();
        t.reset();
        OpenAL.setListenerPosition(0, 0, 0);
        while (!Thread.interrupted()) {
            float sin = Mathf.sin(t.elapsed());
            System.out.println(sin);
            try {Thread.sleep(10);} catch (InterruptedException ignore) {}
        }
        OpenAL.destroy();
    }
}
