package fr.hadriel.graphics;

import fr.hadriel.time.Timer;

/**
 * Created by glathuiliere on 11/08/2016.
 */
public class Animation implements HLRenderable {

    private static final float DEFAULT_FRAME_LENGTH = 1000f / 30f;

    private Texture[] textures;
    private int frame;
    private float frameLength;

    private Timer timer;
    private boolean paused;
    private boolean looped;

    public Animation(Texture... textures) {
        this.textures = textures;
        this.timer = new Timer();
        this.frameLength = DEFAULT_FRAME_LENGTH;
        this.frame = 0;
        this.paused = true;
        this.looped = false;
    }

    public void pause() {
        paused = true;
    }

    public void unpause() {
        paused = false;
    }

    public void play() {
        setFrame(0);
        unpause();
    }

    public void setLooped(boolean looped) {
        this.looped = looped;
    }

    public boolean isLooped() {
        return looped;
    }

    public void setSpeed(int fps) {
        frameLength = 1000f / fps;
    }

    public void setFrame(int frame) {
        if(frame < 0)
            frame = 0;
        this.frame = (looped ? frame % textures.length : Math.min(frame, textures.length));
    }

    public void render(HLGraphics g) {
        if(!paused) {
            int frameUpdateCount = (int) (timer.elapsed() / frameLength);
            setFrame(frameUpdateCount);
        }
        textures[frame].render(g);
    }
}
