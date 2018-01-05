package fr.hadriel.resources;

/**
 * TODO comment
 *
 * @author glathuiliere
 */
public abstract class AbstractResource {

    private boolean init = false;

    public boolean isInit() {
        return init;
    }

    public final synchronized void init() {
        if(init) return;
        init = true;
        onInit(); // FIXME use an async method
    }

    public final synchronized void destroy() {
        if(!init) return;
        init = false;
        onDestroy(); // FIXME use an async method
    }

    protected abstract void onInit();
    protected abstract void onDestroy();
}