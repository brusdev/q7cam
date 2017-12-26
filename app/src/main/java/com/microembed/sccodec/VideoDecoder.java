/*
 * Decompiled with CFR 0_118.
 */
package com.microembed.sccodec;

public final class VideoDecoder {
    public static final int DecoderTypeOpenCoreH264 = 1;
    public static final int DecoderTypeH264 = 2;
    public static final int DecoderTypeMPEG4 = 3;
    public static final int DecoderTypeMJPEG = 4;
    public static final int DecoderTypeMJPEGB = 5;
    public static final int OutputFormatYUV420P = 1;
    public static final int OutputFormatARGB32 = 2;
    public static final int OutputFormatRGB24 = 3;
    public static final int OutputFormatRGB565 = 4;
    private int _handle = -1;

    static {
        System.loadLibrary("SCCodec");
    }

    public long getSystemCurrentTimeMillis() {
        return System.currentTimeMillis();
    }

    public int init(int decoderType, int outputFormat, int frameWidth, int frameHeight) {
        this._handle = this.__init(decoderType, outputFormat, frameWidth, frameHeight);
        if (-1 == this._handle) {
            return -1;
        }
        return 0;
    }

    public void destroy() {
        this.__destory(this._handle);
        this._handle = -1;
    }

    public int decode(byte[] bitstream, int bitstreamLength, byte[] frameBuffer, int bufferSize) {
        return this.__decode(this._handle, bitstream, bitstreamLength, frameBuffer, bufferSize);
    }

    public int frameWidth() {
        return this.__frameWidth(this._handle);
    }

    public int frameHeight() {
        return this.__frameHeight(this._handle);
    }

    private native int __init(int var1, int var2, int var3, int var4);

    private native int __decode(int var1, byte[] var2, int var3, byte[] var4, int var5);

    private native int __frameWidth(int var1);

    private native int __frameHeight(int var1);

    private native void __destory(int var1);
}

