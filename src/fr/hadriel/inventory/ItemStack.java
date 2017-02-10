package fr.hadriel.inventory;

/**
 * Created by glathuiliere on 10/02/2017.
 */
public class ItemStack {

    private final ItemType itemType;
    private int count;

    public ItemStack(ItemType itemType) {
        if(itemType == null) throw new NullPointerException();
        this.itemType = itemType;
        this.count = 0;
    }

    public void set(int count) {
        this.count = count;
    }

    /**
     * Adds a new Item to the stack if possible
     * @return true if an item could be added
     */
    public boolean add() {
        if(!isFull()) {
            count++;
            return true;
        }
        return false;
    }

    /**
     * Removes a new Item to the stack if possible
     * @return true if an item could be removed
     */
    public boolean remove() {
        if(!isEmpty()) {
            count--;
            return true;
        }
        return false;
    }

    public boolean isFull() {
        return count == itemType.maxStackSize;
    }

    public boolean isEmpty() {
        return count == 0;
    }

    public boolean stacksItem(ItemType itemType) {
        return this.itemType == itemType;
    }
}