/*
 * Decompiled with CFR 0_118.
 */
package x1.Studio.Core;

public class VideoDataBuf {
    byte[] buf;
    int time;
    int jumpFlag;
    int size;
    int resolution;

    public VideoDataBuf(byte[] buf, int time, int size) {
        this.buf = buf;
        this.time = time;
        this.size = size;
    }

    public VideoDataBuf(byte[] buf, int time, int size, int resolution) {
        this.buf = buf;
        this.time = time;
        this.size = size;
        this.resolution = resolution;
    }

    public byte[] getBuf() {
        return this.buf;
    }

    public void setBuf(byte[] buf) {
        this.buf = buf;
    }

    public int getTime() {
        return this.time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public VideoDataBuf(byte[] buf, int time) {
        this.buf = buf;
        this.time = time;
    }

    public VideoDataBuf(byte[] buf) {
        this.buf = buf;
    }

    public VideoDataBuf() {
    }

    public int getJumpFlag() {
        return this.jumpFlag;
    }

    public void setJumpFlag(int jumpFlag) {
        this.jumpFlag = jumpFlag;
    }

    public int getSize() {
        return this.size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getResolution() {
        return this.resolution;
    }

    public void setResolution(int resolution) {
        this.resolution = resolution;
    }
}

