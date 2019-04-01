package dev.rocco.botw.randomizer.utils.rstb;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class ByteReaderWrapper {

    public static int readInt(byte[] data, int offset) {
        int size = 4;
        byte[] intBytes = Arrays.copyOfRange(data, offset, offset + size);

        return ByteBuffer.wrap(intBytes).getInt();
    }

    public static long readUnsignedInt(byte[] data, int offset) {
        int size = 4;
        byte[] intBytes = Arrays.copyOfRange(data, offset, offset + size);

        return (long) ByteBuffer.wrap(intBytes).getInt() & 0xffffffffL;
    }

    public static int getUnsignedInt(long value) {
        return (int) (value & 0xffffffffL);
    }

    public static String readString(byte[] data, int offset) {
        int size = 128;
        byte[] strBytes = Arrays.copyOfRange(data, offset, offset + size);

        return new String(strBytes);
    }

}
