package fr.hadriel.asset;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.*;

public abstract class Asset {
    public static final int CREATED  = 0b001;
    public static final int LOADED   = 0b010;
    public static final int UNLOADED = 0b100;

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
    void load(Path path) {
        if(state != CREATED) return; // ignore bad call
        try {
            FileChannel channel = new RandomAccessFile(path.toFile(), "r").getChannel();
            MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_ONLY, channel.position(), channel.size());
            onLoad(path, buffer);
            state = LOADED;
        } catch (IOException e) {
            throw new RuntimeException("Failed to load Asset:" + e.getMessage());
        }
    }

    //package private
    void unload() {
        if(state != LOADED) return; // ignore bad call
        onUnload();
        state = UNLOADED;
    }

    protected abstract void onLoad(Path path, ByteBuffer fileContent);
    protected abstract void onUnload();
}