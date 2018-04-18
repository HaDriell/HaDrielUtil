package fr.hadriel;

import fr.hadriel.application.Application;
import fr.hadriel.audio.Audio2D;
import fr.hadriel.audio.Sound;
import fr.hadriel.audio.Source2D;
import fr.hadriel.math.Mathf;
import fr.hadriel.math.Vec2;
import fr.hadriel.util.Timer;

public class TestAudio2D extends Application {


    Timer timer;

    Sound sound;
    Source2D source;
    float age;

    protected void start(String[] args) {
        sound = manager.load("Media/res/wilhelm.ogg", Sound.class);
        Audio2D.setScreenDistance(2);
        source = new Source2D();
        timer = new Timer();
        timer.reset();
    }

    protected void update(float delta) {
        age += delta;
        if (timer.elapsed() > 1f) {
            timer.reset();

            source.unqueue(sound);
            source.queue(sound);
            source.play();
        }
        float x = 10 * Mathf.cos(age / 4);
        float y = 10 * Mathf.sin(age / 4);
        source.setPosition(x, y);
    }

    protected void terminate() {

    }

    public static void main(String... args) {
        launch(new TestAudio2D());
    }
}