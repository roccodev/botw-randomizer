package dev.rocco.botw.randomizer.utils.rstb;

import java.util.zip.CRC32;

public class Crc32HashCalc {

    public static long calc(String input) {
        CRC32 crc = new CRC32();
        byte[] data = input.getBytes();

        crc.update(data, 0, data.length);

        return crc.getValue();
    }
}
