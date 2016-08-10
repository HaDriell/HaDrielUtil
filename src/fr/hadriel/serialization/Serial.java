package fr.hadriel.serialization;

/**
 * Created by glathuiliere on 09/08/2016.
 */
public class Serial {

    public static final byte FALSE = 0x0;
    public static final byte TRUE = 0x1;

    public static int write(byte[] buffer, int pointer, byte data) {
        buffer[pointer++] = data;
        return pointer;
    }

    public static int write(byte[] buffer, int pointer, boolean data) {
        buffer[pointer++] = data ? TRUE : FALSE;
        return pointer;
    }

    public static int write(byte[] buffer, int pointer, short data) {
        buffer[pointer++] = (byte) ((data >> 8) & 0xFF);
        buffer[pointer++] = (byte) ((data >> 0) & 0xFF);
        return pointer;
    }

    public static int write(byte[] buffer, int pointer, char data) {
        buffer[pointer++] = (byte) ((data >> 8) & 0xFF);
        buffer[pointer++] = (byte) ((data >> 0) & 0xFF);
        return pointer;
    }

    public static int write(byte[] buffer, int pointer, int data) {
        buffer[pointer++] = (byte) ((data >> 24) & 0xFF);
        buffer[pointer++] = (byte) ((data >> 16) & 0xFF);
        buffer[pointer++] = (byte) ((data >> 8) & 0xFF);
        buffer[pointer++] = (byte) ((data >> 0) & 0xFF);
        return pointer;
    }

    public static int write(byte[] buffer, int pointer, long data) {
        buffer[pointer++] = (byte) ((data >> 56) & 0xFF);
        buffer[pointer++] = (byte) ((data >> 48) & 0xFF);
        buffer[pointer++] = (byte) ((data >> 40) & 0xFF);
        buffer[pointer++] = (byte) ((data >> 32) & 0xFF);
        buffer[pointer++] = (byte) ((data >> 24) & 0xFF);
        buffer[pointer++] = (byte) ((data >> 16) & 0xFF);
        buffer[pointer++] = (byte) ((data >> 8) & 0xFF);
        buffer[pointer++] = (byte) ((data >> 0) & 0xFF);
        return pointer;
    }

    public static int write(byte[] buffer, int pointer, float data) {
        int bits = Float.floatToIntBits(data);
        return write(buffer, pointer, bits);
    }

    public static int write(byte[] buffer, int pointer, double data) {
        long bits = Double.doubleToRawLongBits(data);
        return write(buffer, pointer, bits);
    }

    public static int write(byte[] buffer, int pointer, boolean[] array) {
        for(int i = 0; i < array.length; i++) {
            pointer = write(buffer, pointer, array[i]);
        }
        return pointer;
    }

    public static int write(byte[] buffer, int pointer, byte[] array) {
        for(int i = 0; i < array.length; i++) {
            pointer = write(buffer, pointer, array[i]);
        }
        return pointer;
    }

    public static int write(byte[] buffer, int pointer, short[] array) {
        for(int i = 0; i < array.length; i++) {
            pointer = write(buffer, pointer, array[i]);
        }
        return pointer;
    }

    public static int write(byte[] buffer, int pointer, char[] array) {
        for(int i = 0; i < array.length; i++) {
            pointer = write(buffer, pointer, array[i]);
        }
        return pointer;
    }

    public static int write(byte[] buffer, int pointer, int[] array) {
        for(int i = 0; i < array.length; i++) {
            pointer = write(buffer, pointer, array[i]);
        }
        return pointer;
    }

    public static int write(byte[] buffer, int pointer, long[] array) {
        for(int i = 0; i < array.length; i++) {
            pointer = write(buffer, pointer, array[i]);
        }
        return pointer;
    }

    public static int write(byte[] buffer, int pointer, float[] array) {
        for(int i = 0; i < array.length; i++) {
            pointer = write(buffer, pointer, array[i]);
        }
        return pointer;
    }

    public static int write(byte[] buffer, int pointer, double[] array) {
        for(int i = 0; i < array.length; i++) {
            pointer = write(buffer, pointer, array[i]);
        }
        return pointer;
    }

    public static boolean readBoolean(byte[] buffer, int pointer) {
        return buffer[pointer] != FALSE;
    }

    public static byte readByte(byte[] buffer, int pointer) {
        return buffer[pointer];
    }

    public static short readShort(byte[] buffer, int pointer) {
        return (short) (((buffer[pointer + 0] & 0xFF) << 8)
                | ((buffer[pointer + 1] & 0xFF) << 0)
        );
    }

    public static char readChar(byte[] buffer, int pointer) {
        return (char) (((buffer[pointer + 0] & 0xFF) << 8)
                | ((buffer[pointer + 1] & 0xFF) << 0)
        );
    }

    public static int readInt(byte[] buffer, int pointer) {
        return (int) (((buffer[pointer + 0] & 0xFF) << 24)
                | ((buffer[pointer + 1] & 0xFF) << 16)
                | ((buffer[pointer + 2] & 0xFF) << 8)
                | ((buffer[pointer + 3] & 0xFF) << 0)
        );
    }

    public static long readLong(byte[] buffer, int pointer) {
        return (long) (((buffer[pointer + 0] & 0xFFL) << 56)
                | ((buffer[pointer + 1] & 0xFFL) << 48)
                | ((buffer[pointer + 2] & 0xFFL) << 40)
                | ((buffer[pointer + 3] & 0xFFL) << 32)
                | ((buffer[pointer + 4] & 0xFFL) << 24)
                | ((buffer[pointer + 5] & 0xFFL) << 16)
                | ((buffer[pointer + 6] & 0xFFL) << 8)
                | ((buffer[pointer + 7] & 0xFFL) << 0));
    }

    public static float readFloat(byte[] buffer, int pointer) {
        int bits = readInt(buffer, pointer);
        return Float.intBitsToFloat(bits);
    }

    public static double readDouble(byte[] buffer, int pointer) {
        long bits = readLong(buffer, pointer);
        return Double.longBitsToDouble(bits);
    }

    public static int readByteArray(byte[] buffer, int pointer, byte[] array, int length) {
        System.arraycopy(buffer, pointer, array, 0, length);
        return length;
    }

    public static int readBooleanArray(byte[] buffer, int pointer, boolean[] array, int length) {
        for(int i = 0; i < length; i++) {
            array[i] = Serial.readBoolean(buffer, pointer);
            pointer += 1;
        }
        return pointer;
    }

    public static int readShortArray(byte[] buffer, int pointer, short[] array, int length) {
        for(int i = 0; i < length; i++) {
            array[i] = Serial.readShort(buffer, pointer);
            pointer += 2;
        }
        return pointer;
    }

    public static int readCharArray(byte[] buffer, int pointer, char[] array, int length) {
        for(int i = 0; i < length; i++) {
            array[i] = Serial.readChar(buffer, pointer);
            pointer += 2;
        }
        return pointer;
    }

    public static int readIntArray(byte[] buffer, int pointer, int[] array, int length) {
        for(int i = 0; i < length; i++) {
            array[i] = Serial.readInt(buffer, pointer);
            pointer += 4;
        }
        return pointer;
    }

    public static int readFloatArray(byte[] buffer, int pointer, float[] array, int length) {
        for(int i = 0; i < length; i++) {
            array[i] = Serial.readFloat(buffer, pointer);
            pointer += 4;
        }
        return pointer;
    }

    public static int readLongArray(byte[] buffer, int pointer, long[] array, int length) {
        for(int i = 0; i < length; i++) {
            array[i] = Serial.readLong(buffer, pointer);
            pointer += 8;
        }
        return pointer;
    }

    public static int readDoubleArray(byte[] buffer, int pointer, double[] array, int length) {
        for(int i = 0; i < length; i++) {
            array[i] = Serial.readDouble(buffer, pointer);
            pointer += 8;
        }
        return pointer;
    }
}