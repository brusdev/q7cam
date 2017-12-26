/*
 * Decompiled with CFR 0_118.
 */
package x1.Studio.Core;

import java.util.LinkedList;
import java.util.NoSuchElementException;
import x1.Studio.Core.VideoDataBuf;

public class QueueOfVideoByte {
    private LinkedList<VideoDataBuf> msgList = new LinkedList();
    private int VEDIO_QUEUE_SIZE = 25;

    public void setMax(int max) {
        this.VEDIO_QUEUE_SIZE = max;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public VideoDataBuf poll() {
        QueueOfVideoByte queueOfVideoByte = this;
        synchronized (queueOfVideoByte) {
            if (this.msgList.size() > 0) {
                return this.msgList.removeFirst();
            }
            return null;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public boolean adjustex() {
        boolean V = false;
        QueueOfVideoByte queueOfVideoByte = this;
        synchronized (queueOfVideoByte) {
            if (this.msgList.size() > 6) {
                V = true;
            }
        }
        return V;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public boolean adjust() {
        QueueOfVideoByte queueOfVideoByte = this;
        synchronized (queueOfVideoByte) {
            return this.msgList.size() > this.VEDIO_QUEUE_SIZE;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void add(VideoDataBuf obj) {
        QueueOfVideoByte queueOfVideoByte = this;
        synchronized (queueOfVideoByte) {
            this.msgList.addLast(obj);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void addA(VideoDataBuf obj) {
        QueueOfVideoByte queueOfVideoByte = this;
        synchronized (queueOfVideoByte) {
            if (this.msgList.size() > this.VEDIO_QUEUE_SIZE) {
                try {
                    this.msgList.removeFirst();
                }
                catch (NoSuchElementException var3_3) {
                    // empty catch block
                }
            }
            this.msgList.addLast(obj);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void clear() {
        QueueOfVideoByte queueOfVideoByte = this;
        synchronized (queueOfVideoByte) {
            this.msgList.clear();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public int size() {
        QueueOfVideoByte queueOfVideoByte = this;
        synchronized (queueOfVideoByte) {
            return this.msgList.size();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public boolean isEmpty() {
        QueueOfVideoByte queueOfVideoByte = this;
        synchronized (queueOfVideoByte) {
            return this.msgList.isEmpty();
        }
    }
}

