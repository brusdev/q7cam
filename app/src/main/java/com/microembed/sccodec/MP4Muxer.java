/*
 * Decompiled with CFR 0_118.
 */
package com.microembed.sccodec;

public final class MP4Muxer {
    public static final int BITSTREAM_TYPE_H264 = 0;
    public static final int BITSTREAM_TYPE_MPEG4 = 1;
    private int _handle = -1;

    static {
        System.loadLibrary("SCCodec");
    }

    public int open(String fileName, int bsType, int width, int height, int rate) {
        this._handle = this.__open(fileName, bsType, width, height, rate);
        if (-1 == this._handle) {
            return -1;
        }
        return 0;
    }

    public int wriet(byte[] bitstream, int length, boolean isKeyFrame) {
        return this.__write(this._handle, bitstream, length, isKeyFrame);
    }

    public void close() {
        this.__close(this._handle);
    }

    private native int __open(String var1, int var2, int var3, int var4, int var5);

    private native int __write(int var1, byte[] var2, int var3, boolean var4);

    private native void __close(int var1);
}

