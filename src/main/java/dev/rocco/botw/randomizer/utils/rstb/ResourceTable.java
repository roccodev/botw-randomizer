package dev.rocco.botw.randomizer.utils.rstb;

import dev.rocco.filelib.sarc.io.FileReader;

import java.io.File;
import java.util.HashMap;

public class ResourceTable {

    private FileReader reader;
    private HashMap<Integer, Integer> crc32Map = new HashMap<>();
    private HashMap<String, Integer> nameMap = new HashMap<>();

    public ResourceTable(File input) {
        reader = new FileReader(input);
        reader.parse();
    }

    public void parse() {

        byte[] crc32Bytes = null;
        byte[] nameBytes = null;

        int crc32Size = reader.readInt(4);
        int crc32End = 12 + crc32Size * 8;

        int nameSize = reader.readInt(8);

        if(crc32Size > 0)
            crc32Bytes = reader.readBytes(12, crc32End - 12);

        if(nameSize > 0)
            nameBytes = reader.readBytes(crc32End, nameSize * 132);

        if(crc32Bytes != null) {
            for(int i = 0; i < crc32Bytes.length / 8; i++) {
                int crc32 = reader.readInt(8 * i);
                int size = reader.readInt(8 * i + 4);

                crc32Map.put(crc32, size);
            }
        }

        if(nameBytes != null) {
            for(int i = 0; i < nameBytes.length / 132; i++) {
                String name = reader.readString(132 * i, 128);
                int size = reader.readInt(132 * i + 128);

                nameMap.put(name, size);
            }
        }
    }

    public HashMap<Integer, Integer> getCrc32Map() {
        return crc32Map;
    }

    public HashMap<String, Integer> getNameMap() {
        return nameMap;
    }
}
