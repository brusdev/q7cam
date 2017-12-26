/*
 * Decompiled with CFR 0_118.
 */
package com.microembed.sccodec;

public final class AudioDecoder {
    public static final int DecoderTypeG711a = 1;
    public static final int DecoderTypeG711u = 2;
    public static final int DecoderTypeG726 = 3;
    private int _handle = -1;

    static {
        System.loadLibrary("SCCodec");
    }

    public int init(int decoderType, int bitRate, int sampleRate, int channels, int sampleSize) {
        this._handle = this.__init(decoderType, bitRate, sampleRate, channels, sampleSize);
        if (-1 == this._handle) {
            return -1;
        }
        return 0;
    }

    public void destroy() {
        this.__destory(this._handle);
        this._handle = -1;
    }

    public int decode(byte[] bitstream, int bitstreamLength, short[] samplesBuffer, int bufferSize) {
        return this.__decode(this._handle, bitstream, bitstreamLength, samplesBuffer, bufferSize);
    }

    private native int __init(int var1, int var2, int var3, int var4, int var5);

    private native int __decode(int var1, byte[] var2, int var3, short[] var4, int var5);

    private native void __destory(int var1);
}

