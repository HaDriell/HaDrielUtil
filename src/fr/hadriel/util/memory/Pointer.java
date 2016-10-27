package fr.hadriel.util.memory;

/**
 * Created by glathuiliere on 27/10/2016.
 */
public class Pointer {

    private MemoryManager manager;
    private long size;

    public Pointer(long size) {
        this(null, size);
    }

    public Pointer(MemoryManager manager, long size) {
        this.manager = manager;
        this.size = size;
    }

    public void initialize(MemoryManager manager) {
        initialize(manager, size);
    }

    public void initialize(MemoryManager manager, long size) {
        if(manager != null) throw new IllegalStateException("Pointer already used");
        this.manager = manager;
        this.size = size;
    }

    public void dispose() {
        manager = null;
    }

    void setSize(long size) {
        this.size = size;
    }

    public long address() {
        return manager.getPointerAddress(this);
    }

    public long size() {
        if(manager == null) return -1;
        return size;
    }

    public void resize(long nsize) {
        if(manager == null) return;
        manager.reallocate(this, nsize);
    }
}