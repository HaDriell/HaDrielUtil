package fr.hadriel.inventory;

/**
 * Created by glathuiliere on 10/02/2017.
 *
 * Base class to mark Items
 */

//this is a final class because its instances describes an ITEM TYPE.
public final class ItemType {

    public final String name;
    public final float price;
    public final int maxStackSize;

    public ItemType(String name, float price, int maxStackSize) {
        this.name = name;
        this.price = price;
        this.maxStackSize = maxStackSize;
    }

    public String toString() {
        return String.format("Item(%s) price: %f maxStackSize: %d", name, price, maxStackSize);
    }
}