/*
 * Decompiled with CFR 0_118.
 */
package x1.Studio.Core;

public class VideoSleep {
    static int _lastReceiveTice = 0;
    static long _lastLocalTick = 0;

    public static void opeartion(int mTime, int queueCount) {
        int needSleep = mTime - _lastReceiveTice - (int)(System.currentTimeMillis() - _lastLocalTick);
        if (queueCount < 100 && needSleep > 0 && needSleep < 150) {
            int operationCount = queueCount;
            if (operationCount >= 3) {
                needSleep = operationCount < 5 ? (int)((double)needSleep * 0.9) : (operationCount < 10 ? (int)((double)needSleep * 0.8) : (operationCount < 15 ? (int)((double)needSleep * 0.5) : (operationCount < 25 ? (int)((double)needSleep * 0.4) : (operationCount < 30 ? (int)((double)needSleep * 0.3) : (int)((double)needSleep * 0.2)))));
            }
            try {
                Thread.sleep(needSleep);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        _lastReceiveTice = mTime;
        _lastLocalTick = System.currentTimeMillis();
    }
}

