package fr.hadriel.audio;

public class SourceManager {

    private Source2D[] MUSICS;
    private Source2D[] SOUNDS;

    public SourceManager(int maxSounds, int maxMusics) {
        this.SOUNDS = new Source2D[maxSounds];
        this.MUSICS = new Source2D[maxMusics];
    }

    public Source2D playSound(Sound... sounds) {
        return play(false, sounds);
    }

    public Source2D playMusic(Sound... sounds) {
        return play(true, sounds);
    }

    private synchronized Source2D play(boolean music, Sound... sounds) {
        Source2D source = FindSlot(music ? MUSICS : SOUNDS);

        //Setup the Source playing instantly
        if(source != null) {
            source.queue(sounds);
            source.play();
        }
        return source;
    }

    private static Source2D FindSlot(Source2D[] sources) {
        int index = -1;
        for(int i = 0;  i < sources.length; i++) {
            if(sources[i] != null) { // check only not null sources
                if(sources[i].isStopped()) { // stop acts as a terminator
                    sources[i].dispose(); // notify OpenAL that the source should be deleted
                    sources[i] = new Source2D(); // create a new Source for the slot to be used
                }
            }

            //save the first slot available
            if(index == -1 && sources[i] == null) {
                index = i;
            }
        }
        if(index != -1) {
            sources[index] = new Source2D();
            return sources[index];
        }
        return null;
    }
}