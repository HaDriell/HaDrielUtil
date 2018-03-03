package fr.hadriel.asset;

import fr.hadriel.asset.font.Font;
import fr.hadriel.asset.shader.Shader;
import fr.hadriel.asset.texture.Texture;
import fr.hadriel.asset.texture.TextureHint;

import java.util.HashMap;
import java.util.Map;

public class AssetManager {

    private final Map<String, Asset> assets;

    public AssetManager() {
        this.assets = new HashMap<>();
    }

    public <A extends Asset> A get(String name, Class<A> type) {
        Asset asset = assets.get(name);
        return type.isInstance(asset) ? type.cast(asset) : null;
    }

    public <A extends Asset> void load(String name, A asset) {
        if(assets.containsKey(name)) throw new RuntimeException("Unable to load asset : An asset is already loaded for name " + name);
        assets.put(name, asset);
        asset.load(this);
    }

    public <A extends Asset> void unload(String name) {
        Asset asset = assets.remove(name);
        if(asset != null)
            asset.unload(this);
    }

    public Texture loadTexture(String name, String filename) {
        return loadTexture(name, filename, new TextureHint());
    }

    public Texture loadTexture(String name, String filename, TextureHint hint) {
        Texture texture = new Texture(filename, hint);
        load(name, texture);
        return texture;
    }

    public Font loadFont(String name, String filename) {
        Font font = new Font(filename);
        load(name, font);
        return font;
    }

    public Shader loadShader(String name, String filename) {
        Shader shader = new Shader(filename);
        load(name, shader);
        return shader;
    }
}