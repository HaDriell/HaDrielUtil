package fr.hadriel.util.memory;

/**
 * Created by glathuiliere on 27/10/2016.
 */
public interface MemoryManager {

    //Memory allocation operations
    public long getPointerAddress(Pointer p);
    public Pointer allocate(long size);
    public void reallocate(Pointer p, long nsize);
    public void delete(Pointer p);
}