/*
 * Decompiled with CFR 0_118.
 */
package x1.Studio.Core;

import java.util.LinkedList;

public class QueueOfAudioByte {
    private LinkedList<byte[]> msgList = new LinkedList();

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public byte[] poll() {
        QueueOfAudioByte queueOfAudioByte = this;
        synchronized (queueOfAudioByte) {
            if (this.msgList.size() > 0) {
                return this.msgList.removeFirst();
            }
            return null;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void add(byte[] obj) {
        QueueOfAudioByte queueOfAudioByte = this;
        synchronized (queueOfAudioByte) {
            this.msgList.addLast(obj);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void clear() {
        QueueOfAudioByte queueOfAudioByte = this;
        synchronized (queueOfAudioByte) {
            this.msgList.clear();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public int size() {
        QueueOfAudioByte queueOfAudioByte = this;
        synchronized (queueOfAudioByte) {
            return this.msgList.size();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public boolean isEmpty() {
        QueueOfAudioByte queueOfAudioByte = this;
        synchronized (queueOfAudioByte) {
            return this.msgList.isEmpty();
        }
    }
}

