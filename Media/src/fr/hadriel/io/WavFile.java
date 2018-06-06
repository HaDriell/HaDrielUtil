package fr.hadriel.io;

import fr.hadriel.util.IOUtils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;


//TODO : explode this file into multiple classes to have a better support of the WAV file format
public class WavFile {

    public final int bitsPerSample;
    public final int channels;
    public final int frequency;
    public final ByteBuffer samples;


    public WavFile(String filename) {
        try {
            byte[] buffer = IOUtils.readFile(filename);
            WavHeaderInfo header = new WavHeaderInfo(buffer, 0, buffer.length);
            if(header.mFmtChunk.AudioFormat != 1)
                throw new RuntimeException("Unsupported audio format: requires PCM");


            this.bitsPerSample = header.mFmtChunk.BitsPerSample;
            this.channels = header.mFmtChunk.NumChannels;
            this.frequency = header.mFmtChunk.SampleRate;

            int headerSize = header.getHeaderSize();
            samples = ByteBuffer.wrap(buffer, headerSize, buffer.length - headerSize);
        } catch (IOException e) {
            throw new RuntimeException("Unable to read " + filename, e);
        }
    }

    public void close() throws Exception {}

    /**
     * Author: landerlyoung
     * Date:   2014-10-11
     * Time:   14:38
     * Life with passion. Code with creativity!
     * wav file format parser.
     * wav header information: <a href="https://ccrma.stanford.edu/courses/422/projects/WaveFormat/" >here</a>
     * So this class support only too sub-chunk information: "fmt " and "data"
     */
    public class WavHeaderInfo {
        private RIFF_Chunk mRiffChunk;
        private FMT_Chunk mFmtChunk;
        private DATA_Chunk mDataChunk;
        private ArrayList<Unknown_Chunk> mUnknownChunkList;

        private WavHeaderInfo(byte[] data, int offset, int len) {
            mUnknownChunkList = new ArrayList<>();
            if (BaseChunk.isChunkIDEuqal(data, offset, RIFF_Chunk.CONST_CHUNK_ID, 0)) {
                mRiffChunk = RIFF_Chunk.create(data, offset, len);
                offset += 12;
            } else {
                throw new RuntimeException("no RIFF chunk");
            }

            while (true) {
                if (BaseChunk.isChunkIDEuqal(data, offset,
                        FMT_Chunk.CONST_CHUNK_ID, 0)) {
                    mFmtChunk = FMT_Chunk.create(data, offset, len - offset);
                    offset += mFmtChunk.getChunkSize();
                } else if (BaseChunk.isChunkIDEuqal(
                        data, offset, DATA_Chunk.CONST_CHUNK_ID, 0)) {
                    mDataChunk = DATA_Chunk.create(data, offset, len - offset);
                    break;
                } else {
                    Unknown_Chunk un = Unknown_Chunk.create(data, offset, len - offset);
                    offset += un.getChunkSize();
                    mUnknownChunkList.add(un);
                }
            }
            if (mFmtChunk == null) {
                throw new RuntimeException("no FMT chunk");
            }
            if (mDataChunk == null) {
                throw new RuntimeException("no Data chunk");
            }
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            if (mRiffChunk != null) {
                sb.append(mRiffChunk.toString());
                sb.append("\n");
            }
            if (mFmtChunk != null) {
                sb.append(mFmtChunk.toString());
                sb.append("\n");
            }
            for (Unknown_Chunk u : mUnknownChunkList) {
                sb.append(u.toString());
                sb.append("\n");

            }
            if (mDataChunk != null) {
                sb.append(mDataChunk);
                sb.append("\n");
            }
            return sb.toString();
        }

        /**
         * @return Bit Rate of this wav file
         * unit in bps
         */
        public int getBitRate() {
            return mFmtChunk.ByteRate * 8;
        }

        /**
         * @return Duration of this wav file
         * unit in millisecond
         */
        public long getDuration() {
            return (getDataSize() * 8L * 1000L) / getBitRate();
        }

        /**
         * For why plus 8, refer to {@link RIFF_Chunk#ChunkSize}.
         *
         * @return total file size
         */
        public int getFileSize() {
            return mRiffChunk.ChunkSize + 8;
        }

        public int getHeaderSize() {
            int unknown_chunk_size = 0;
            for (Unknown_Chunk uc : mUnknownChunkList) {
                unknown_chunk_size += uc.getChunkSize();
            }
            return /*riff chunk size*/ 12 +
                    mFmtChunk.getChunkSize() +
                    unknown_chunk_size +
                /*data chunk head size*/ 8;
        }

        public int getDataSize() {
            return getFileSize() - getHeaderSize();
        }
    }

    private static class BaseChunk {
        /**
         * This is the sub-chunk id for RIFF.
         * A string of 4 char.
         */
        public byte[] ChunkID = new byte[4];

        /**
         * This is the size of the rest of the chunk
         * following this number.  This is the size of the
         * entire file in bytes minus 8 bytes for the
         * two fields not included in this count:
         * ChunkID and ChunkSize.
         */
        public int ChunkSize;

        protected BaseChunk() {

        }

        public BaseChunk doCreate(byte[] data, int offset, int len) {
            if (offset < 0 || data.length < offset + len) {
                throw new IllegalArgumentException("array length:" + data.length +
                        " offset:" + offset + " len:" + len);
            }
            System.arraycopy(data, offset, ChunkID, 0, ChunkID.length);
            offset += 4;
            ChunkSize = peekInt(data, offset, ByteOrder.LITTLE_ENDIAN);
            return this;
        }

        /**
         * @return total size of this chunk. <em>NOT {@link #ChunkSize}!</em>
         */
        public int getChunkSize() {
            return 8 + ChunkSize;
        }

        public static boolean isChunkIDEuqal(byte[] one, int oneOffset,
                                             byte[] another, int anotherOffset) {
            if (one != null && another != null
                    && one.length >= 4 + oneOffset
                    && another.length >= 4 + anotherOffset) {
                for (int i = 0; i < 4; i++) {
                    if (one[i + oneOffset] != another[i + anotherOffset]) {
                        return false;
                    }
                }
                return true;
            }
            return false;
        }

        @Override
        public String toString() {
            return "{ ChunkID:" + new String(ChunkID) + ", ChunkSize:" + ChunkSize;
        }
    }

    public static class RIFF_Chunk extends BaseChunk {
        /**
         * ChunkID ie: string of "RIFF"
         */
        public static final byte[] CONST_CHUNK_ID = {
                'R', 'I', 'F', 'F',
        };

        public byte[] Format = new byte[4];

        public static final byte[] CONST_FORMAT = {
                'W', 'A', 'V', 'E',
        };

        private RIFF_Chunk() {

        }

        @Override
        public BaseChunk doCreate(byte[] data, int offset, int len) {
            super.doCreate(data, offset, len);
            offset += 8;
            System.arraycopy(data, offset, Format, 0, 4);
            return this;
        }

        @Override
        public String toString() {
            return super.toString() + ", Format:" + new String(Format) + " }";
        }

        public static RIFF_Chunk create(byte[] data, int offset, int len) {
            RIFF_Chunk r = new RIFF_Chunk();
            r.doCreate(data, offset, len);
            return r;
        }
    }

    public static class FMT_Chunk extends BaseChunk {
        public static final byte[] CONST_CHUNK_ID = {
                'f', 'm', 't', ' '//a space
        };

        /**
         * 16 for PCM.  This is the size of the
         * rest of the Subchunk which follows this number.
         */
        //public int ChunkSize; //existed in super class

        /**
         * AudioFormat PCM = 1 (i.e. Linear quantization)
         * <br/>
         * Values other than 1 indicate some
         * form of compression.
         */
        public short AudioFormat;

        /**
         * NumChannels  Mono = 1, Stereo = 2, etc.
         *
         * @see #NumChannels_Mono
         * @see #NumChannels_Stereo
         */
        public short NumChannels;

        /**
         * SampleRate 8000, 44100, etc.
         */
        public int SampleRate;

        /**
         * ByteRate == SampleRate * NumChannels * BitsPerSample/8
         */
        public int ByteRate;

        /**
         * BlockAlign == NumChannels * BitsPerSample/8
         * <br/>
         * The number of bytes for one sample including
         * all channels. I wonder what happens when
         * this number isn't an integer?
         */
        public short BlockAlign;

        /**
         * BitsPerSample 8 bits = 8, 16 bits = 16, etc.
         */
        public short BitsPerSample;

        private FMT_Chunk() {

        }

        @Override
        public BaseChunk doCreate(byte[] data, int offset, int len) {
            super.doCreate(data, offset, len);
            offset += 8;
            AudioFormat = peekShort(data, offset, ByteOrder.LITTLE_ENDIAN);
            offset += SHORT;
            NumChannels = peekShort(data, offset, ByteOrder.LITTLE_ENDIAN);
            offset += SHORT;
            SampleRate = peekInt(data, offset, ByteOrder.LITTLE_ENDIAN);
            offset += INT;
            ByteRate = peekInt(data, offset, ByteOrder.LITTLE_ENDIAN);
            offset += INT;
            BlockAlign = peekShort(data, offset, ByteOrder.LITTLE_ENDIAN);
            offset += SHORT;
            BitsPerSample = peekShort(data, offset, ByteOrder.LITTLE_ENDIAN);
            return this;
        }

        public static FMT_Chunk create(byte[] data, int offset, int len) {
            FMT_Chunk fmt = new FMT_Chunk();
            fmt.doCreate(data, offset, len);
            return fmt;
        }

        @Override
        public String toString() {
            return super.toString() +
                    ", AudioFormat:" + AudioFormat +
                    ", NumChannels:" + NumChannels +
                    ", SampleRate:" + SampleRate +
                    ", ByteRate:" + ByteRate +
                    ", BlockAlign:" + BlockAlign +
                    ", BitsPerSample:" + BitsPerSample +
                    " }";
        }

        //============constance of field==================
        /**
         * constance of {@link #AudioFormat}
         */
        public static final short AudioFormat_PCM = 1;

        /**
         * constance of {@link #NumChannels}
         *
         * @see #NumChannels
         * @see #NumChannels_Stereo
         */
        public static final short NumChannels_Mono = 1;
        /**
         * constance of {@link #NumChannels}
         *
         * @see #NumChannels
         * @see #NumChannels_Mono
         */
        public static final short NumChannels_Stereo = 2;
        //=================================================
    }

    public static class DATA_Chunk extends BaseChunk {
        public static final byte[] CONST_CHUNK_ID = {
                'd', 'a', 't', 'a',
        };

        /**
         * Subchunk2Size == NumSamples * NumChannels * BitsPerSample/8
         * <br/>
         * This is the number of bytes in the data.
         * You can also think of this as the size
         * of the read of the subchunk following this
         * number.
         */

        /**
         * Won't fill this file.<em>ALWAYS NULL!</em>
         */
        public byte[] Data;

        private DATA_Chunk() {

        }

        @Override
        public BaseChunk doCreate(byte[] data, int offset, int len) {
            return super.doCreate(data, offset, len);
        }

        public static DATA_Chunk create(byte[] data, int offset, int len) {
            DATA_Chunk data_chunk = new DATA_Chunk();
            data_chunk.doCreate(data, offset, len);
            return data_chunk;
        }

        @Override
        public String toString() {
            return super.toString() +
                    ", Data: not read }";
        }
    }

    public static class Unknown_Chunk extends BaseChunk {
        private Unknown_Chunk() {

        }

        public static Unknown_Chunk create(byte[] data, int offset, int len) {
            Unknown_Chunk u = new Unknown_Chunk();
            u.doCreate(data, offset, len);
            return u;
        }

        @Override
        public String toString() {
            return super.toString() + ", (unknown chunk) ... }";
        }
    }

    private static final int BYTE = 1;
    private static final int SHORT = 2;
    private static final int INT = 4;
    private static final int LONG = 8;
    private static final int FLOAT = 4;
    private static final int DOUBLE = 8;

    private static final int OBJECT = -1;

    public static int sizeof(Object o) {
        if (o instanceof byte[]) {
            return BYTE * ((byte[]) o).length;
        } else if (o instanceof short[]) {
            return SHORT * ((short[]) o).length;
        } else if (o instanceof int[]) {
            return INT * ((int[]) o).length;
        } else if (o instanceof long[]) {
            return LONG * ((long[]) o).length;
        } else if (o instanceof float[]) {
            return FLOAT * ((float[]) o).length;
        } else if (o instanceof double[]) {
            return DOUBLE * ((double[]) o).length;
        } else {
            return OBJECT;
        }
    }

    public static int sizeof(byte b) {
        return BYTE;
    }

    public static int sizeof(short i) {
        return SHORT;

    }

    public static int sizeof(int i) {
        return INT;
    }

    public static int sizeof(long i) {
        return LONG;
    }

    public static int sizeof(float i) {
        return FLOAT;

    }

    public static int sizeof(double i) {
        return DOUBLE;
    }

    public static int peekInt(byte[] src, int offset, ByteOrder order) {
        if (order == ByteOrder.BIG_ENDIAN) {
            return (((src[offset++] & 0xff) << 24) |
                    ((src[offset++] & 0xff) << 16) |
                    ((src[offset++] & 0xff) << 8) |
                    ((src[offset] & 0xff) << 0));
        } else {
            return (((src[offset++] & 0xff) << 0) |
                    ((src[offset++] & 0xff) << 8) |
                    ((src[offset++] & 0xff) << 16) |
                    ((src[offset] & 0xff) << 24));
        }
    }

    public static long peekLong(byte[] src, int offset, ByteOrder order) {
        if (order == ByteOrder.BIG_ENDIAN) {
            int h = ((src[offset++] & 0xff) << 24) |
                    ((src[offset++] & 0xff) << 16) |
                    ((src[offset++] & 0xff) << 8) |
                    ((src[offset++] & 0xff) << 0);
            int l = ((src[offset++] & 0xff) << 24) |
                    ((src[offset++] & 0xff) << 16) |
                    ((src[offset++] & 0xff) << 8) |
                    ((src[offset] & 0xff) << 0);
            return (((long) h) << 32L) | ((long) l) & 0xffffffffL;
        } else {
            int l = ((src[offset++] & 0xff) << 0) |
                    ((src[offset++] & 0xff) << 8) |
                    ((src[offset++] & 0xff) << 16) |
                    ((src[offset++] & 0xff) << 24);
            int h = ((src[offset++] & 0xff) << 0) |
                    ((src[offset++] & 0xff) << 8) |
                    ((src[offset++] & 0xff) << 16) |
                    ((src[offset] & 0xff) << 24);
            return (((long) h) << 32L) | ((long) l) & 0xffffffffL;
        }
    }

    public static short peekShort(byte[] src, int offset, ByteOrder order) {
        if (order == ByteOrder.BIG_ENDIAN) {
            return (short) ((src[offset] << 8) | (src[offset + 1] & 0xff));
        } else {
            return (short) ((src[offset + 1] << 8) | (src[offset] & 0xff));
        }
    }

    public static void pokeInt(byte[] dst, int offset, int value, ByteOrder order) {
        if (order == ByteOrder.BIG_ENDIAN) {
            dst[offset++] = (byte) ((value >> 24) & 0xff);
            dst[offset++] = (byte) ((value >> 16) & 0xff);
            dst[offset++] = (byte) ((value >> 8) & 0xff);
            dst[offset] = (byte) ((value >> 0) & 0xff);
        } else {
            dst[offset++] = (byte) ((value >> 0) & 0xff);
            dst[offset++] = (byte) ((value >> 8) & 0xff);
            dst[offset++] = (byte) ((value >> 16) & 0xff);
            dst[offset] = (byte) ((value >> 24) & 0xff);
        }
    }

    public static void pokeLong(byte[] dst, int offset, long value, ByteOrder order) {
        if (order == ByteOrder.BIG_ENDIAN) {
            int i = (int) (value >> 32);
            dst[offset++] = (byte) ((i >> 24) & 0xff);
            dst[offset++] = (byte) ((i >> 16) & 0xff);
            dst[offset++] = (byte) ((i >> 8) & 0xff);
            dst[offset++] = (byte) ((i >> 0) & 0xff);
            i = (int) value;
            dst[offset++] = (byte) ((i >> 24) & 0xff);
            dst[offset++] = (byte) ((i >> 16) & 0xff);
            dst[offset++] = (byte) ((i >> 8) & 0xff);
            dst[offset] = (byte) ((i >> 0) & 0xff);
        } else {
            int i = (int) value;
            dst[offset++] = (byte) ((i >> 0) & 0xff);
            dst[offset++] = (byte) ((i >> 8) & 0xff);
            dst[offset++] = (byte) ((i >> 16) & 0xff);
            dst[offset++] = (byte) ((i >> 24) & 0xff);
            i = (int) (value >> 32);
            dst[offset++] = (byte) ((i >> 0) & 0xff);
            dst[offset++] = (byte) ((i >> 8) & 0xff);
            dst[offset++] = (byte) ((i >> 16) & 0xff);
            dst[offset] = (byte) ((i >> 24) & 0xff);
        }
    }

    public static void pokeShort(byte[] dst, int offset, short value, ByteOrder order) {
        if (order == ByteOrder.BIG_ENDIAN) {
            dst[offset++] = (byte) ((value >> 8) & 0xff);
            dst[offset] = (byte) ((value >> 0) & 0xff);
        } else {
            dst[offset++] = (byte) ((value >> 0) & 0xff);
            dst[offset] = (byte) ((value >> 8) & 0xff);
        }
    }
}
