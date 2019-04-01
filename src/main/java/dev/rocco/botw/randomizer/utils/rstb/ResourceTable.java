// Licensed under GPLv2+
// Original: https://github.com/zeldamods/rstb/blob/master/rstb/rstb.py
// Ported to Java by RoccoDev
// Copyright 2018 leoetlino <leo@leolam.fr>

package dev.rocco.botw.randomizer.utils.rstb;

import com.arbiter34.file.io.BinaryAccessFile;
import com.arbiter34.yaz0.Yaz0Decoder;
import com.arbiter34.yaz0.Yaz0Encoder;
import dev.rocco.botw.randomizer.io.InputManager;
import dev.rocco.botw.randomizer.io.OutputManager;
import dev.rocco.filelib.sarc.io.FileReader;
import dev.rocco.filelib.sarc.io.FileWriter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class ResourceTable {

    private FileReader reader;
    private ArrayList<Crc32Entry> crc32Map = new ArrayList<>();
    private ArrayList<NameEntry> nameMap = new ArrayList<>();

    private FileWriter writer;
    private File outputFile;

    public ResourceTable() throws IOException {
        File input = new File(InputManager.getContentsFolder().getAbsolutePath()
                + "/System/Resource/ResourceSizeTable.product.srsizetable");

        BinaryAccessFile binaryInput = new BinaryAccessFile(input, "r");
        BinaryAccessFile decompressed = Yaz0Decoder.decode(binaryInput);

        reader = new FileReader(new File(decompressed.getPath()));
        reader.parse();

        decompressed.clean();

        outputFile = new File("FileCache/RST_ToCompress");
        if(!outputFile.exists()) outputFile.createNewFile();
        writer = new FileWriter(outputFile);
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
                long crc32 = ByteReaderWrapper.readUnsignedInt(crc32Bytes, 8 * i);
                int size = ByteReaderWrapper.readInt(crc32Bytes, 8 * i + 4);

                crc32Map.add(new Crc32Entry(crc32, size));
            }
        }

        if(nameBytes != null) {
            for(int i = 0; i < nameBytes.length / 132; i++) {
                String name = ByteReaderWrapper.readString(nameBytes, 132 * i);
                int size = ByteReaderWrapper.readInt(nameBytes, 132 * i + 128);

                nameMap.add(new NameEntry(name, size));
            }
        }
    }

    public void write() throws IOException {
        writer.writeString(0, "RSTB");
        writer.writeInt(4, crc32Map.size());
        writer.writeInt(8, nameMap.size());

        int currentOffset = 8;

        Collections.sort(crc32Map);
        Collections.sort(nameMap);

        for(Crc32Entry entry : crc32Map) {
            writer.writeInt(currentOffset += 4, ByteReaderWrapper.getUnsignedInt(entry.getCrc32()));
            writer.writeInt(currentOffset += 4, entry.getSize());
        }

        int newOffset = currentOffset - 128;

        for(NameEntry entry : nameMap) {
            writer.writeString(newOffset += 128, entry.getName());
            writer.writeInt(newOffset += 4, entry.getSize());
        }

        writer.writeToFile();

        BinaryAccessFile toCompress = new BinaryAccessFile(outputFile, "r");
        Yaz0Encoder.encode(toCompress,
                OutputManager.addToOutputUniversal("System/Resource/ResourceSizeTable.product.srsizetable")
                        .getAbsolutePath());

        toCompress.clean();
    }

    public ArrayList<Crc32Entry> getCrc32Map() {
        return crc32Map;
    }

    public ArrayList<NameEntry> getNameMap() {
        return nameMap;
    }
}
