package fr.hadriel.util.memory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by glathuiliere on 27/10/2016.
 */
public class ContiguousMemoryManager extends AbstractMemoryManager {

    private List<Pointer> pointers;

    public ContiguousMemoryManager() {
        this(null);
    }

    public ContiguousMemoryManager(Collection<Pointer> pointers) {
        this.pointers = new ArrayList<>();
        initialize(pointers);
    }

    public void initialize(Collection<Pointer> pointers) {
        this.pointers.clear();
        for(Pointer p : this.pointers) {
            p.initialize(this, p.size());
        }
        if(pointers != null)
            this.pointers.addAll(pointers);
    }

    public List<Pointer> getPointers() {
        return pointers;
    }

    public long getPointerAddress(Pointer p) {
        int limit = pointers.indexOf(p);
        long address = -1;
        if(limit == -1) return address;
        for(int i = 0; i < limit; i++) {
            address += pointers.get(i).size();
        }
        return address;
    }

    public Pointer allocateImpl(long size) {
        Pointer p = new Pointer(this, size);
        return pointers.add(p) ? p : null;
    }

    public void deleteImpl(Pointer p) {
        if(pointers.remove(p)) {
            p.dispose();
        }
    }

    public void reallocateImpl(Pointer p, long nsize) {
        p.setSize(nsize);
    }
}