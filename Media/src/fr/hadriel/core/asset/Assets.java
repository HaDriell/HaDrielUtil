package fr.hadriel.core.asset;

public final class Assets {
    private Assets() { }

    private static AssetManager assets;

    public static <A extends Asset> A get(String name, Class<A> type) {
        return assets.get(name, type);
    }

    public static <A extends Asset> void load(String name, A asset, Class<A> type) {
        assets.load(name, asset, type);
    }

    public static <A extends Asset> void unload(String name, Class<A> type) {
        assets.unload(name, type);
    }
}