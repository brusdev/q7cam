/*
 * Decompiled with CFR 0_118.
 */
package x1.Studio.Core;

import java.nio.ByteBuffer;

public interface IVideoDataCallBack {
    public void OnCallbackFunForDataServer(String var1, ByteBuffer var2, int var3, int var4, int var5, int var6);

    public void OnCallbackFunForUnDecodeDataServer(String var1, byte[] var2, int var3, int var4, int var5, int var6, int var7);

    public void OnCallbackFunForLanDate(String var1, String var2, int var3, int var4, int var5, String var6);

    public void OnCallbackFunForRegionMonServer(int var1);

    public void OnCallbackFunForComData(int var1, int var2, int var3, String var4);

    public void OnCallbackFunForGetItem(byte[] var1, int var2);

    public void OnCallbackFunForIPRateData(String var1);
}

