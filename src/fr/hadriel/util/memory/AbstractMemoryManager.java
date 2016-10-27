package fr.hadriel.util.memory;

import fr.hadriel.util.Callback;

/**
 * Created by glathuiliere on 27/10/2016.
 */
public abstract class AbstractMemoryManager implements MemoryManager {

    public static final class ReallocateParameters {
        public final Pointer pointer;
        public final long nsize;
        public ReallocateParameters(Pointer pointer, long nsize) { this.pointer = pointer; this.nsize = nsize; }
    }

    private Callback<Long> beforeAllocate;
    private Callback<ReallocateParameters> beforeReallocate;
    private Callback<Pointer> beforeDelete;


    public void setBeforeAllocate(Callback<Long> callback) {
        beforeAllocate = callback;
    }

    public void setBeforeReallocate(Callback<ReallocateParameters> callback) {
        beforeReallocate = callback;
    }

    public void setBeforeDelete(Callback<Pointer> callback) {
        beforeDelete = callback;
    }

    public Pointer allocate(long size) {
        if(beforeAllocate != null) beforeAllocate.execute(size);
        return allocateImpl(size);
    }

    public void reallocate(Pointer p, long nsize) {
        if(beforeReallocate != null) beforeReallocate.execute(new ReallocateParameters(p, nsize));
        reallocateImpl(p, nsize);
    }

    public void delete(Pointer p) {
        if(beforeDelete != null) beforeDelete.execute(p);
        deleteImpl(p);
    }

    protected abstract Pointer allocateImpl(long size);
    protected abstract void reallocateImpl(Pointer p, long nsize);
    protected abstract void deleteImpl(Pointer p);
}