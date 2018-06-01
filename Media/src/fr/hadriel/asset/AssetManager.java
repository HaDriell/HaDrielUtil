package fr.hadriel.asset;

import fr.hadriel.util.logging.Log;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class AssetManager {
    private static final Logger logger = Log.getLogger("IResource ResourceManager");

    private final Map<String, Asset> assets;

    public AssetManager() {
        this.assets = new HashMap<>();
    }

    public <A extends Asset> A get(Class<A> type, String id) {
        return type.cast(assets.get(id)); // checks removed. The dev has to master his Assets typing for god's sake !
    }

    //TODO : include loadInternal that handles packed resources loading. (and a specific path-to-id generation)
    //Load and aliases
    public <A extends Asset> A load(Class<A> type, String path) { return load(type, Paths.get(path), null); }
    public <A extends Asset> A load(Class<A> type, String path, String id) { return load(type, Paths.get(path), id); }
    public <A extends Asset> A load(Class<A> type, Path path) { return load(type, path, null); }
    public <A extends Asset> A load(Class<A> type, Path path, String id) {
        //Custom path to ID convertion.
        if (id == null) {
            id = path.toString().replace('\\', '/').replace('/','.');
        }
        //Check reference colisions
        if (assets.containsKey(id)) {
            logger.info(String.format("Reference collision: Asset %s is already loaded !", id));
        }

        try {
            A asset = type.newInstance();
            asset.load(path);
            assets.put(id, asset);
            logger.info(String.format("%s %s loaded from '%s'", type.getSimpleName(), id, path.toString()));
            return asset;
        } catch (InstantiationException | IllegalAccessException e) {
            logger.severe(String.format("Failed to load %s from %s", type.getSimpleName(), path.toString()));
            throw new RuntimeException("Default Constructor is required !", e);
        }
    }

    public void unload(Asset asset) {
        asset.unload();
    }

    public boolean isAssetLoaded(Path path) {
        return assets.get(path) != null;
    }
}