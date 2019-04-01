package dev.rocco.botw.randomizer.utils.rstb;

/*
 * We can't use a TreeMap to store the entries, since Java maps
 * don't allow duplicate keys.
 *
 * The RSTB format is documented here:
 * https://zeldamods.org/wiki/ResourceSizeTable.product.rsizetable
 */
public class NameEntry implements Comparable<NameEntry> {

    private String name;
    private int size;

    public NameEntry(String name, int size) {
        this.name = name;
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public int compareTo(NameEntry otherEntry) {
        return this.name.compareTo(otherEntry.name);
    }
}
