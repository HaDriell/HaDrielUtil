package fr.hadriel.core.asset;

import java.util.HashMap;
import java.util.Map;

public class AssetManager {

    private final Map<Class, Map<String, Asset>> assets;

    public AssetManager() {
        this.assets = new HashMap<>();
    }

    private Map<String, Asset> assetMap(Class<? extends Asset> type) {
        return assets.computeIfAbsent(type, t -> new HashMap<>());
    }

    public <A extends Asset> A get(String name, Class<A> type) {
        Asset asset = assetMap(type).get(name);
        return type.cast(asset);
    }

    public <A extends Asset> void load(String name, A asset, Class<A> type) {
        Map<String, Asset> map = assetMap(type);
        if(map.containsKey(name)) throw new RuntimeException("Unable to load asset : An asset is already loaded for name " + name);
        map.put(name, asset);
        asset.load(this);
    }

    public <A extends Asset> void unload(String name, Class<A> type) {
        Map<String, Asset> assets = assetMap(type);
        Asset asset = assets.remove(name);
        if(asset != null)
            asset.unload(this);
    }
}