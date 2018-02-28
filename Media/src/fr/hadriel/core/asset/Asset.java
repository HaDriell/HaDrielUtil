package fr.hadriel.core.asset;

import fr.hadriel.core.media.Medias;

public abstract class Asset {
    public static final int CREATED     = 0b00001;
    public static final int LOADING     = 0b00010;
    public static final int LOADED      = 0b00100;
    public static final int UNLOADING   = 0b01000;
    public static final int UNLOADED    = 0b10000;

    private int state;
    private boolean asyncLoading;

    protected Asset() {
        this(false);
    }

    protected Asset(boolean asyncLoading) {
        this.state = CREATED;
        this.asyncLoading = asyncLoading;
    }

    public boolean isCreated() {
        return state == CREATED;
    }

    public boolean isLoading() {
        return state == LOADING;
    }

    public boolean isLoaded() {
        return state == LOADED;
    }

    public boolean isUnloading() {
        return state == UNLOADING;
    }

    public boolean isUnloaded() {
        return state == UNLOADED;
    }

    //package private
    void load(AssetManager manager) {
        if(state != CREATED) return; // ignore bad call
        if(asyncLoading)
            Medias.execute(() -> _load(manager));
        else
            _load(manager);
    }

    //package private
    void unload(AssetManager manager) {
        if(state != LOADED) return; // ignore bad call
        if(asyncLoading)
            Medias.execute(() -> _unload(manager));
        else
            _unload(manager);
    }

    private void _load(AssetManager manager) {
        state = LOADING;
        onLoad(manager);
        state = LOADED;
    }

    private void _unload(AssetManager manager) {
        state = UNLOADING;
        onUnload(manager);
        state = UNLOADED;
    }

    protected abstract void onLoad(AssetManager manager);
    protected abstract void onUnload(AssetManager manager);
}