package fr.hadriel.main.asset;

/**
 * Created by glathuiliere on 20/02/2017.
 */
public class Assets {
    private static final AssetManager manager = new AssetManager();

    public static <T> T get(Class<T> type, int id) {
        return manager.get(type, id);
    }

    public static <T> int load(Class<T> type, T instance) {
        return manager.load(type, instance);
    }

    public static <T> void unload(Class<T> type, int id) {
        manager.unload(type, id);
    }
}
