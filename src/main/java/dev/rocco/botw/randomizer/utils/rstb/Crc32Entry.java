package dev.rocco.botw.randomizer.utils.rstb;

/*
 * We can't use a TreeMap to store the entries, since Java maps
 * don't allow duplicate keys.
 *
 * The RSTB format is documented here:
 * https://zeldamods.org/wiki/ResourceSizeTable.product.rsizetable
 */
public class Crc32Entry implements Comparable<Crc32Entry> {

    private long crc32;
    private int size;

    public Crc32Entry(long crc32, int size) {
        this.crc32 = crc32;
        this.size = size;
    }

    public long getCrc32() {
        return crc32;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public int compareTo(Crc32Entry crc32Entry) {
        return Long.compare(crc32, crc32Entry.crc32);
    }
}
