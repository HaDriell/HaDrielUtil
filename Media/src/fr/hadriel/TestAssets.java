package fr.hadriel;

import fr.hadriel.asset.Asset;
import fr.hadriel.asset.AssetManager;

import java.nio.ByteBuffer;
import java.nio.file.Path;

public class TestAssets {
    public static class FakeFont extends Asset {
        protected void onLoad(AssetManager manager, Path path, ByteBuffer fileContent) {
            manager.load("Media/res/Arial.png", FakeTexture.class);
        }
        protected void onUnload(AssetManager manager) { }
    }

    public static class FakeTexture extends Asset {
        protected void onLoad(AssetManager manager, Path path, ByteBuffer fileContent) { }
        protected void onUnload(AssetManager manager) { }
    }

    public static void main(String[] args) {
        AssetManager manager = new AssetManager();
        manager.load("Media/res/Arial.png", FakeTexture.class);
        manager.load("Media/res/Animation.fba", FakeTexture.class);
        manager.load("Media/res/Arial.fnt", FakeFont.class); // checking Load skip
    }
}
