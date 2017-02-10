package fr.hadriel.inventory;

/**
 * Created by glathuiliere on 10/02/2017.
 */
public class Inventory {

    private ItemStack[] stacks;

    public Inventory(int size) {
        if(size <= 0) throw new IllegalArgumentException("Invalid Inventory Size (must be > 0");
        this.stacks = new ItemStack[size];
    }

    /**
     * @return the slot count of this Inventory
     */
    public int getSize() {
        return stacks.length;
    }

    /**
     * Places a new ItemStack at the given slot.
     * @param slot the selected slot
     * @param stack the ItemStack that will be placed. May be null
     * @return the ancient ItemStack placed on the slot. May be null
     */
    public ItemStack setStack(int slot, ItemStack stack) {
        ItemStack old = stacks[slot];
        stacks[slot] = stack;
        return old;
    }

    /**
     * Gives informations about the selected ItemStack
     * @param slot the selected slot
     * @return the ItemStack place on the slot. May be null
     */
    public ItemStack getStack(int slot) {
        return stacks[slot];
    }

    /**
     * TODO : possible upgrade, add more than one item
     * Tries to add an Item to any existing ItemStack in the Inventory. Creates a new ItemStack if needed.
     * @param itemType the item type
     * @return true if the item was successfully added to an ItemStack in the Inventory
     */
    public boolean add(ItemType itemType) {
        //Try adding without creating a stack
        for(ItemStack stack : stacks) {
            if(stack != null)
                if(stack.stacksItem(itemType))
                    if(stack.add())
                        return true;
        }

        //Try creating a new stack
        for(int i = 0;  i < stacks.length; i++) {
            if(stacks[i] == null) {
                stacks[i] = new ItemStack(itemType);
                stacks[i].add();
            }
        }
        return false;
    }

    /**
     * TODO : possible upgrade, remove more than one item
     * Tries to remove an Item to any existing ItemStack in the Inventory.
     * @param itemType the item type
     * @return true if the item was successfully removed to an ItemStack in the Inventory
     */
    public boolean remove(ItemType itemType) {
        //Try finding an Item
        for(int i = 0;  i < stacks.length; i++) {
            if(stacks[i] != null)
                if(!stacks[i].isEmpty())
                    if(stacks[i].remove()) {

                        //Clean the slot is stack becomes empty
                        if(stacks[i].isEmpty())
                            stacks[i] = null;
                        return true;
                    }
        }
        return false;
    }
}