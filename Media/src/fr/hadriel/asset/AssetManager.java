package fr.hadriel.asset;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class AssetManager {

    private final Map<Path, Asset> assets;
    private final Map<String, Asset> namedAssets;

    public AssetManager() {
        this.assets = new HashMap<>();
        this.namedAssets = new HashMap<>();
    }

    public <A extends Asset> A getByName(String name, Class<A> type) {
        Asset asset = namedAssets.get(name);
        return type.isInstance(asset) ? type.cast(asset) : null;
    }

    public <A extends Asset> A getByPath(Path path, Class<A> type) {
        Asset asset = assets.get(path);
        return type.isInstance(asset) ? type.cast(asset) : null;
    }

    public <A extends Asset> A load(String path, Class<A> type) { return load(Paths.get(path), null, type); }
    public <A extends Asset> A load(String path, String name, Class<A> type) { return load(Paths.get(path), name, type); }
    public <A extends Asset> A load(Path path, Class<A> type) { return load(path, null, type); }
    public <A extends Asset> A load(Path path, String name, Class<A> type) {
        A asset;
        if (isAssetLoaded(path)) {
            asset = getByPath(path, type);
            if (asset == null)
                throw new RuntimeException(String.format("Invalid Asset type %s for Asset %s", type.getSimpleName(), path.toString()));
            System.out.println(String.format("Asset Manager: Asset %s of type %s already loaded. Skipping Load", path.toString(), type.getSimpleName()));
        } else {
            try {
                asset = type.newInstance(); // instanciate asset
                asset.load(this, path); // load asset
                assets.put(path, asset); // reference only on load success
            } catch (InstantiationException e) {
                throw new RuntimeException("Unable to instanciate Asset (Missing argument-less Constructor ?).", e);
            } catch (Exception e) {
                throw new RuntimeException("Error while loading Asset", e);
            }
        }

        //Add a reference (if not already defined) to the asset map
        // TODO : log naming colisions
        if (name != null) {
            namedAssets.putIfAbsent(name, asset);
        }
        return asset;
    }

    public void unload(Asset asset) {
        if (asset == null || !asset.isLoaded())
            return;
    }

    public boolean isAssetLoaded(Path path) {
        return assets.get(path) != null;
    }
}