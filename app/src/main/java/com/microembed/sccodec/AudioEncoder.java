/*
 * Decompiled with CFR 0_118.
 */
package com.microembed.sccodec;

public final class AudioEncoder {
    public static final int EncoderTypeG711a = 1;
    public static final int EncoderTypeG711u = 2;
    public static final int EncoderTypeG726 = 3;
    private int _handle = -1;

    static {
        System.loadLibrary("SCCodec");
    }

    public int init(int encoderType, int bitRate, int sampleRate, int channels, int sampleSize, int frameSize) {
        this._handle = this.__init(encoderType, bitRate, sampleRate, channels, sampleSize, frameSize);
        if (-1 == this._handle) {
            return -1;
        }
        return 0;
    }

    public void destroy() {
        this.__destory(this._handle);
        this._handle = -1;
    }

    public int encode(short[] samplesBuffer, int bufferSize, byte[] bitstream, int bitstreamLength) {
        return this.__encode(this._handle, samplesBuffer, bufferSize, bitstream, bitstreamLength);
    }

    private native int __init(int var1, int var2, int var3, int var4, int var5, int var6);

    private native int __encode(int var1, short[] var2, int var3, byte[] var4, int var5);

    private native void __destory(int var1);
}

