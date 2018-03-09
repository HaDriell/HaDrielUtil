package fr.hadriel.asset;

import java.util.UUID;

public abstract class Asset {
    public static final int CREATED     = 0b00001;
    public static final int LOADED      = 0b00100;
    public static final int UNLOADED    = 0b10000;

    private int state;

    protected Asset() {
        this.state = CREATED;
    }

    public boolean isCreated() {
        return state == CREATED;
    }

    public boolean isLoaded() {
        return state == LOADED;
    }

    public boolean isUnloaded() {
        return state == UNLOADED;
    }

    //package private
    void load(AssetManager manager) {
        if(state != CREATED) return; // ignore bad call
        _load(manager);
    }

    //package private
    void unload(AssetManager manager) {
        if(state != LOADED) return; // ignore bad call
        _unload(manager);
    }

    private void _load(AssetManager manager) {
        onLoad(manager);
        state = LOADED;
    }

    private void _unload(AssetManager manager) {
        onUnload(manager);
        state = UNLOADED;
    }

    public void requireLoaded() {
        if(!isLoaded()) {
            throw new RuntimeException("Trying to use an non loaded Asset");
        }
    }

    protected abstract void onLoad(AssetManager manager);
    protected abstract void onUnload(AssetManager manager);
}