/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  android.annotation.SuppressLint
 *  android.graphics.Bitmap
 *  android.graphics.Bitmap$CompressFormat
 *  android.graphics.Bitmap$Config
 *  android.media.AudioManager
 *  android.media.AudioRecord
 *  android.media.AudioTrack
 *  android.media.MediaPlayer
 *  android.net.wifi.WifiManager
 *  android.os.AsyncTask
 *  android.util.Log
 */
package x1.Studio.Core;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaPlayer;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.util.Log;
import com.microembed.sccodec.AudioDecoder;
import com.microembed.sccodec.AudioEncoder;
import com.microembed.sccodec.VideoDecoder;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.List;
import x1.Studio.Core.IVideoDataCallBack;
import x1.Studio.Core.QueueOfVideoByte;
import x1.Studio.Core.VideoDataBuf;
import x1.Studio.Core.VideoSleep;

public class OnlineService {
    public static String loglog = "OnlineService";
    public String HK_VIDEO_MPEG4 = "MPEG4";
    public String HK_VIDEO_H264 = "H264";
    public String HK_VIDEO_MJPEG = "MJPEG";
    public String HK_VIDEO_DVR = "hkdvr";
    private static VideoDecoder _decoders = new VideoDecoder();
    private IVideoDataCallBack videoDataOfCallBack;
    private int mFrameWidth;
    private int mFrameHeight;
    private int mEncode;
    private int mTime;
    private int mFream;
    private int decoderType;
    private AudioTrack audioTrack;
    private Thread CaptureThumbnailThread;
    private String tag = "player";
    private boolean VideoInited = false;
    private int mPixelSize;
    private byte[] DataBuf = null;
    private byte[] mPixel;
    private ByteBuffer buff;
    private int mIfream;
    private int mRecordIfream;
    private boolean IsLostForVideoQueue;
    private QueueOfVideoByte VideoQueue = new QueueOfVideoByte();
    private List devList = new ArrayList();
    private int AudioVolume;
    private String AudioDecodeType;
    private String mCallId;
    private String mAudioCallId;
    private String mSayCallId;
    private static OnlineService ons;
    private boolean isVideoStop = false;
    private boolean isCaptureThumbnail = true;
    private boolean isStartVideo = false;
    private boolean isStartAudio = false;
    private static boolean isDecode_;
    private QueueOfVideoByte recordDataQueue = new QueueOfVideoByte();
    private MediaPlayer camear;
    private String devid;
    private int hkid;
    private Bitmap VideoBit;
    private int talkMode = 0;
    private int talkFlag = 0;
    private byte[] iFrameData;
    private boolean isPlayAudio = true;
    private WifiManager wm;
    private boolean isInitAudioEncoder = false;
    private AudioEncoder audioEncoder = new AudioEncoder();
    private boolean AudioIsEncode = false;
    private Thread AudioEncoderThread = null;
    private boolean isInitAudioDecoder = false;
    AudioDecoder audioDecoder = new AudioDecoder();
    int bufferSize;
    short[] samplesBuffer;

    static {
        isDecode_ = true;
        System.loadLibrary("chinalink");
        System.loadLibrary("system");
        System.loadLibrary("captetown1");
    }

    public void setCallBackData(IVideoDataCallBack listener) {
        if (listener != null) {
            this.videoDataOfCallBack = listener;
        } else {
            Log.e((String)"IVideoDataCallCack", (String)"is null");
        }
    }

    public static OnlineService getInstance() {
        if (ons == null) {
            ons = new OnlineService();
        }
        return ons;
    }

    public void setDecode(boolean isDecode) {
        isDecode_ = isDecode;
    }

    private native int RegionMonServer();

    private native int sccLogin(String var1, String var2, String var3);

    private native int RegionDataServer();

    private native int GetItem(int var1);

    private native int GetItemEX(int var1);

    private native int QuitSysm();

    private native String DoMonDowSdData(String var1, String var2, int var3);

    private native String DoLanDowSdData(int var1, String var2, int var3);

    private native String DoWanAudioInvite(String var1, String var2, int var3, int var4);

    private native String DoWanAudioSayInvite(String var1, String var2, int var3, int var4);

    private native int DoMonCloseDialog(String var1, String var2);

    private native int SetPTZ(String var1, int var2, int var3);

    private native int AutoControl(String var1, int var2, int var3);

    private native int Preset(String var1, int var2, int var3);

    private native int Aperture(String var1, int var2, int var3);

    private native int LanSetPTZ(int var1, int var2, int var3);

    private native int LanAutoControl(int var1, int var2, int var3);

    private native int LanPreset(int var1, int var2, int var3);

    private native int LanAperture(int var1, int var2, int var3);

    private native int QuitLan();

    private native String GetLanSysInfo(int var1, String var2);

    private native int SetLanDevIP(int var1, int var2, String var3);

    private native int DoLocalAlarmSensitivity(String var1, int var2, int var3, int var4);

    private native int DoMonAlarmSensitivity(String var1, int var2, int var3);

    private native int DoLanGetWifiSid(int var1, String var2, int var3);

    private native int SetLanWifi(int var1, int var2, int var3, String var4);

    private native int GetLanSysDevInfo(int var1, String var2, int var3);

    private native int SetLanSysInfo(int var1, int var2, String var3, int var4);

    private native int GetWanSysDevInfo(String var1, int var2);

    private native String GetWanSysInfo(int var1, String var2);

    private native int SetWanSysInfo(int var1, String var2, String var3, int var4);

    private native int DoWanAddDev(String var1, String var2, String var3, int var4);

    private native int SetLanSdParam(int var1, String var2);

    private native int DoRegistrationUser(String var1, String var2, String var3, String var4);

    private native int DoWanVideoReversal(String var1, short var2, int var3);

    private native int DoLocalVideoReversal(int var1, short var2, int var3);

    private native int SysUpdatePasswd(String var1);

    private native int DoSetAccessPswd(String var1, String var2, int var3, int var4);

    private native int DoWanDeleteDev(String var1, int var2);

    public native int DoWanUpdateDev(String var1, byte[] var2, int var3, int var4);

    private native int DoLanReadSdData(int var1, int var2, int var3, int var4);

    private native int DoMonSDReadData(String var1, int var2, int var3, int var4);

    private native int DoLocalMonUpdateResolution(String var1, int var2, int var3, int var4, int var5);

    private native int DoMonUpdateResolution(String var1, int var2, int var3, int var4);

    private native int DoLocalMonSetAlarmEmail(int var1, String var2, int var3);

    private native int DoMonSetAlarmEmail(String var1, String var2, int var3);

    private native int DoLocalMonGetAlarmEmail(int var1, int var2);

    private native int DoMonGetAlarmEmail(String var1, int var2);

    private native int DoLocalMonFormatSD(int var1, int var2);

    private native int DoMonFormatSD(String var1, int var2);

    private native int sccGetLANInfo(String var1, int var2, int var3, int var4, int var5);

    private native int sccSetLANInfo(String var1, int var2, int var3, String var4, int var5, int var6);

    private native int DoLocalMonDeletePhoto(int var1, int var2, String var3);

    private native int DoMonDeletePhoto(String var1, int var2, String var3);

    private native int RegionSdDataServer();

    private native int sccYuv2Rgb(byte[] var1, byte[] var2, int var3, int var4);

    private native int DoLanMonSetLevel(int var1, String var2, int var3, int var4, int var5);

    private native int DoMonSetLevel(int var1, String var2, int var3, int var4);

    private native int InitAviInfo(int var1, int var2, int var3, int var4, int var5, String var6, String var7);

    private native int sccAviRecordVideo(byte[] var1, int var2);

    private native int sccAviRecordAudio(byte[] var1, int var2);

    private native int sccRecordClose();

    private native int LanInit();

    private native int LanPresent();

    private native int LanPresentEX(int var1);

    private native String DoInvite(String var1, String var2, int var3, int var4);

    private native String DoLanInvite(int var1, String var2, int var3, int var4);

    private native String sccWANDevidCalling(int var1, String var2, String var3, String var4, int var5);

    private native int RegionAudioServer();

    private native int AudioData(byte[] var1, int var2, int var3);

    private native String DoLanAudioInvite(int var1, String var2, int var3, int var4);

    private native String DoLanAudioSayInvite(int var1, String var2, int var3, int var4);

    private native int AudioData(byte[] var1, int var2, int var3, String var4);

    private native int DoLanClose(String var1);

    private native int sccGetDevStatus(String var1);

    private native int sccGetDevStatusEX(String var1, int var2);

    private native int sccCheckAccessPwd(String var1, String var2);

    private native int DoMonAppCheck(String var1, int var2);

    private native int DoLocalAppCheck(int var1, int var2);

    private native int sccDoInitCmd();

    private native int sccConnectDev(String var1, int var2);

    private native int sccCloseConnectDev();

    public native int sccInitIPRate();

    public native int sccAddTestSrvIP(String var1);

    public native int sccStartCheckRate(String var1);

    public native int DoSetAccessPswdEX(String var1, String var2, String var3, int var4, int var5);

    private native int DoSetUserPassword(String var1, String var2, String var3);

    private native int sccWanSendData(String var1, String var2, int var3);

    private native int sccLanSendData(int var1, String var2, int var3);

    private native int DoWlsqAddPushUser(String var1, String var2, String var3, String var4);

    private native int DoWlsqDelPushUser(String var1, String var2, String var3);

    private native int DoMonDevIoAram(String var1, int var2, int var3);

    private native int DoLocalMonDevIoAram(int var1, int var2, int var3);

    private native int sccCheckAPStatus(int var1);

    private native int sccAddBcastAddr(String var1);

    public int doBindUser(String jsToke, String jsUserid, String jsDevid, String jsExid) {
        return this.DoWlsqAddPushUser(jsToke, jsUserid, jsDevid, jsExid);
    }

    public int doUnbindUser(String jsUserid, String jsDevid, String jsExid) {
        return this.DoWlsqDelPushUser(jsUserid, jsDevid, jsExid);
    }

    public void CallbackFunForRegionMonServer(int iFlag) {
        this.videoDataOfCallBack.OnCallbackFunForRegionMonServer(iFlag);
    }

    public void CallbackFunForComData(int Type, int Result, int AttachValueBufSize, String AttachValueBuf) {
        Log.e((String)"CallbackFunForComData", (String)("\u6536\u5230\u56de\u8c03\uff1a" + Type));
        this.videoDataOfCallBack.OnCallbackFunForComData(Type, Result, AttachValueBufSize, AttachValueBuf);
    }

    public void CallbackFunForLanDate(String devid, String devType, int hkid, int channal, int stats, String auioType) {
        boolean Exist = false;
        if (devid.equals("301") || devid.equals("210") || devid.equals("302") || devid.equals("304") || devid.equals("305") || devid.equals("307") || devid.equals("308") || devid.equals("312") || devid.equals("200")) {
            this.videoDataOfCallBack.OnCallbackFunForLanDate(devid, devType, hkid, channal, stats, auioType);
            return;
        }
        int i = 0;
        while (i < this.devList.size()) {
            if (this.devList.get(i).equals(devid)) {
                Exist = true;
                break;
            }
            ++i;
        }
        if (!Exist) {
            this.devList.add(devid);
            this.videoDataOfCallBack.OnCallbackFunForLanDate(devid, devType, hkid, channal, stats, auioType);
        }
    }

    public void CallbackFunForGetItem(byte[] byteArray, int result) {
        this.videoDataOfCallBack.OnCallbackFunForGetItem(byteArray, result);
    }

    public void CallbackFunForIPRateData(String Ip, int arg1, int arg2) {
        this.videoDataOfCallBack.OnCallbackFunForIPRateData(Ip);
    }

    public void CallbackFunForSdData(byte[] Buf, int Size, int Encode, int Resolution, int IFrame, int TurnType) {
        if (IFrame == 2) {
            this.mIfream = 1;
        }
        if (this.mIfream != 1) {
            return;
        }
        if (this.isStartVideo) {
            if (IFrame == 2) {
                this.mRecordIfream = 1;
            }
            if (this.mRecordIfream == 1) {
                this.recordDataQueue.add(new VideoDataBuf(Buf));
            }
        }
        this.VideoQueue.add(new VideoDataBuf(Buf, TurnType, Size));
        if (!this.VideoInited) {
            this.InitPlayer(null, Encode, Resolution, TurnType, 0);
        }
        System.gc();
    }

    public void CallbackFunForDataServer(String CallId, byte[] Buf, int Size, int Encode, int Resolution, int IFrame, int TurnType, int Time) {
        if (this.isVideoStop) {
            return;
        }
        if (IFrame == 2) {
            this.mIfream = 1;
            this.iFrameData = Buf;
        }
        if (this.mIfream != 1) {
            return;
        }
        if (this.isStartVideo) {
            if (IFrame == 2) {
                this.mRecordIfream = 1;
            }
            if (this.mRecordIfream == 1) {
                this.recordDataQueue.add(new VideoDataBuf(Buf));
            }
        }
        this.VideoQueue.add(new VideoDataBuf(Buf, Time, Size));
        if (!this.VideoInited) {
            this.InitPlayer(CallId, Encode, Resolution, IFrame, Time);
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public void CallbackFunForAudioData(byte[] AudioData, int Size, int ncid) {
        if (!this.isPlayAudio) {
            return;
        }
        if (Size == 80) {
            if (this.talkMode == 0) {
                this.StartPlayAudio(AudioData);
                return;
            } else {
                if (this.talkFlag != 0) return;
                this.StartPlayAudio(AudioData);
            }
            return;
        } else {
            if (Size % 320 != 0) return;
            int result = Size / 320;
            int j = 0;
            while (j < result) {
                int data = 320 * j;
                byte[] databuf = new byte[320];
                System.arraycopy(AudioData, data, databuf, 0, 320);
                if (this.talkMode == 0) {
                    this.StartPlayAudio(databuf);
                } else if (this.talkFlag == 0) {
                    this.StartPlayAudio(databuf);
                }
                ++j;
            }
        }
    }

    private void InitPlayer(String CallId, int Encode, int Resolution, int IFrame, int Time) {
        switch (Resolution) {
            case 1: {
                this.mFrameWidth = 320;
                this.mFrameHeight = 180;
                break;
            }
            case 2: {
                this.mFrameWidth = 352;
                this.mFrameHeight = 288;
                break;
            }
            case 3: {
                this.mFrameWidth = 320;
                this.mFrameHeight = 240;
                break;
            }
            case 4: {
                this.mFrameWidth = 640;
                this.mFrameHeight = 360;
                break;
            }
            case 5: {
                this.mFrameWidth = 640;
                this.mFrameHeight = 480;
                break;
            }
            case 6: {
                this.mFrameWidth = 704;
                this.mFrameHeight = 480;
                break;
            }
            case 7: {
                this.mFrameWidth = 704;
                this.mFrameHeight = 576;
                break;
            }
            case 8: {
                this.mFrameWidth = 1024;
                this.mFrameHeight = 768;
                break;
            }
            case 9: {
                this.mFrameWidth = 1280;
                this.mFrameHeight = 720;
                break;
            }
            case 10: {
                this.mFrameWidth = 1280;
                this.mFrameHeight = 1024;
                break;
            }
            default: {
                if (Encode == 5 || Encode == 4) {
                    this.mFrameWidth = 1280;
                    this.mFrameHeight = 720;
                    break;
                }
                this.mFrameWidth = 640;
                this.mFrameHeight = 480;
            }
        }
        this.decoderType = Encode == 5 || Encode == 4 ? 2 : (Encode == 1 ? 3 : (Encode == 2 ? 4 : 2));
        this.mEncode = Encode;
        this.mTime = Time;
        this.mCallId = CallId;
        this.mFream = IFrame;
        if (isDecode_) {
            _decoders.init(this.decoderType, 4, this.mFrameWidth, this.mFrameHeight);
        }
        Log.i((String)this.tag, (String)("initPlay/////" + this.mFrameWidth + "X" + this.mFrameHeight));
        if (this.mFrameWidth > -1 && this.mFrameHeight > -1) {
            this.VideoInited = true;
            this.mPixelSize = this.mFrameWidth * this.mFrameHeight * 3;
            this.mPixel = new byte[this.mPixelSize];
            int i = this.mPixel.length;
            i = 0;
            while (i < this.mPixel.length) {
                this.mPixel[i] = 0;
                ++i;
            }
            this.buff = ByteBuffer.wrap(this.mPixel);
            new sendDataThread().start();
        }
    }

    private void StartSendAudio() {
        Log.v((String)this.tag, (String)("InitializedToSayAudio...!" + this.AudioDecodeType));
        if (this.AudioEncoderThread != null && this.AudioEncoderThread.isAlive()) {
            try {
                this.AudioEncoderThread.join();
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        this.AudioIsEncode = true;
        this.AudioEncoderThread = new Thread(new Runnable(){

            /*
             * Enabled force condition propagation
             * Lifted jumps to return sites
             */
            @Override
            public void run() {
                AudioEncoder audioEncoder = null;
                if (OnlineService.this.AudioDecodeType.equals("G726")) {
                    audioEncoder = new AudioEncoder();
                    if (-1 == audioEncoder.init(3, 16000, 8000, 1, 16, 640)) {
                        Log.v((String)OnlineService.this.tag, (String)"init audio encoder fail.");
                        return;
                    } else {
                        int bufferSize = AudioRecord.getMinBufferSize((int)8000, (int)2, (int)2);
                        AudioRecord audioRecord = new AudioRecord(1, 8000, 2, 2, bufferSize * 10);
                        audioRecord.startRecording();
                        int samplesBufferSize = 640;
                        short[] samplesBuffer = new short[samplesBufferSize / 2];
                        int bitstreamLength = samplesBufferSize / 8;
                        byte[] bitstream = new byte[bitstreamLength];
                        int sbufSize = bufferSize == samplesBufferSize ? samplesBufferSize : (bufferSize < samplesBufferSize ? (samplesBufferSize + bufferSize - 1) / bufferSize * bufferSize : bufferSize * 2 - samplesBufferSize);
                        ShortBuffer sbuf = ShortBuffer.allocate(sbufSize);
                        try {
                            try {
                                int subi = 0;
                                while (OnlineService.this.AudioIsEncode) {
                                    int readSizeInShort = audioRecord.read(samplesBuffer, 0, samplesBufferSize / 2);
                                    sbuf.put(samplesBuffer, 0, readSizeInShort);
                                    if (samplesBufferSize / 2 > sbuf.position()) continue;
                                    sbuf.flip();
                                    sbuf.get(samplesBuffer, 0, samplesBufferSize / 2);
                                    sbuf.compact();
                                    if (-1 == audioEncoder.encode(samplesBuffer, samplesBufferSize, bitstream, bitstreamLength)) {
                                        Log.v((String)OnlineService.this.tag, (String)"audio encode fail.");
                                        continue;
                                    }
                                    if (OnlineService.this.talkMode == 0) {
                                        Log.v((String)OnlineService.this.tag, (String)(" SendSayAudio...!" + OnlineService.this.AudioDecodeType));
                                        subi = OnlineService.this.AudioData(bitstream, bitstreamLength, 0, "G726");
                                        continue;
                                    }
                                    if (OnlineService.this.talkFlag != 1) continue;
                                    subi = OnlineService.this.AudioData(bitstream, bitstreamLength, 0, "G726");
                                }
                                return;
                            }
                            catch (Exception ioe) {
                                ioe.printStackTrace();
                                audioRecord.stop();
                                audioRecord.release();
                                audioEncoder.destroy();
                            }
                            return;
                        }
                        finally {
                            audioRecord.stop();
                            audioRecord.release();
                            audioEncoder.destroy();
                        }
                    }
                }
                if (OnlineService.this.AudioDecodeType.equals("G711")) {
                    audioEncoder = new AudioEncoder();
                    if (-1 == audioEncoder.init(1, 8000, 8000, 1, 16, 320)) {
                        Log.v((String)OnlineService.this.tag, (String)"init audio encoder fail.");
                        return;
                    }
                    int bufferSize = AudioRecord.getMinBufferSize((int)8000, (int)2, (int)2);
                    AudioRecord audioRecord = new AudioRecord(1, 8000, 2, 2, bufferSize * 10);
                    audioRecord.startRecording();
                    int samplesBufferSize = 1280;
                    short[] samplesBuffer = new short[samplesBufferSize / 2];
                    int bitstreamLength = samplesBufferSize / 2;
                    byte[] bitstream = new byte[bitstreamLength];
                    int sbufSize = bufferSize == samplesBufferSize ? samplesBufferSize : (bufferSize < samplesBufferSize ? (samplesBufferSize + bufferSize - 1) / bufferSize * bufferSize : bufferSize * 2 - samplesBufferSize);
                    ShortBuffer sbuf = ShortBuffer.allocate(sbufSize);
                    InputStream in = null;
                    try {
                        try {
                            int subi = 0;
                            while (OnlineService.this.AudioIsEncode) {
                                int readSizeInShort = audioRecord.read(samplesBuffer, 0, samplesBufferSize / 2);
                                sbuf.put(samplesBuffer, 0, readSizeInShort);
                                if (samplesBufferSize / 2 > sbuf.position()) continue;
                                sbuf.flip();
                                sbuf.get(samplesBuffer, 0, samplesBufferSize / 2);
                                sbuf.compact();
                                if (-1 == audioEncoder.encode(samplesBuffer, samplesBufferSize, bitstream, bitstreamLength)) {
                                    Log.v((String)OnlineService.this.tag, (String)"audio encode fail.");
                                    continue;
                                }
                                if (OnlineService.this.talkMode == 0) {
                                    subi = OnlineService.this.AudioData(bitstream, bitstreamLength, 0, "G711");
                                    continue;
                                }
                                if (OnlineService.this.talkFlag != 1) continue;
                                subi = OnlineService.this.AudioData(bitstream, bitstreamLength, 0, "G711");
                            }
                            return;
                        }
                        catch (Exception ioe) {
                            ioe.printStackTrace();
                            audioRecord.stop();
                            audioRecord.release();
                            audioEncoder.destroy();
                            if (in == null) return;
                            try {
                                in.close();
                                return;
                            }
                            catch (IOException var14_33) {}
                        }
                        return;
                    }
                    finally {
                        audioRecord.stop();
                        audioRecord.release();
                        audioEncoder.destroy();
                        if (in != null) {
                            try {
                                in.close();
                            }
                            catch (IOException var14_35) {}
                        }
                    }
                }
                if (OnlineService.this.AudioDecodeType.equals("PCM")) {
                    int bufferSize = AudioRecord.getMinBufferSize((int)8000, (int)2, (int)2);
                    Log.v((String)OnlineService.this.tag, (String)("AudioRecord, min buffer size: " + bufferSize));
                    AudioRecord audioRecord = new AudioRecord(1, 8000, 2, 2, bufferSize * 10);
                    audioRecord.startRecording();
                    try {
                        try {
                            byte[] bitstream = new byte[bufferSize];
                            int subi = 0;
                            while (OnlineService.this.AudioIsEncode) {
                                int readSize = audioRecord.read(bitstream, 0, bufferSize);
                                if (readSize > 0) {
                                    if (OnlineService.this.talkMode == 0) {
                                        subi = OnlineService.this.AudioData(bitstream, readSize, 0, "PCM");
                                        continue;
                                    }
                                    if (OnlineService.this.talkFlag != 1) continue;
                                    subi = OnlineService.this.AudioData(bitstream, readSize, 0, "PCM");
                                    continue;
                                }
                                Thread.sleep(500);
                            }
                            Log.v((String)OnlineService.this.tag, (String)"AudioEncoderThread, finally...");
                            return;
                        }
                        catch (Exception ioe) {
                            ioe.printStackTrace();
                            audioRecord.stop();
                            audioRecord.release();
                        }
                        return;
                    }
                    finally {
                        audioRecord.stop();
                        audioRecord.release();
                    }
                } else {
                    Log.v((String)OnlineService.this.tag, (String)"init audio encode fail...audiotype is undefined");
                }
            }
        });
        this.AudioEncoderThread.start();
    }

    private void StartPlayAudio(byte[] audioData) {
        if (this.isStartAudio) {
            this.sccAviRecordAudio(audioData, audioData.length);
        }
        if (this.AudioDecodeType.equals("G711")) {
            if (!this.isInitAudioDecoder) {
                this.audioDecoder.init(1, 32000, 8000, 1, 16);
                this.bufferSize = AudioTrack.getMinBufferSize((int)8000, (int)2, (int)2);
                this.audioTrack = new AudioTrack(1, 8000, 2, 2, this.bufferSize * 2, 1);
                this.audioTrack.setStereoVolume((float)this.AudioVolume, (float)this.AudioVolume);
                this.audioTrack.play();
                this.samplesBuffer = new short[this.bufferSize / 2];
                this.isInitAudioDecoder = true;
                Log.v((String)this.tag, (String)"init audio decoder fail.");
            } else if (-1 == this.audioDecoder.decode(audioData, audioData.length, this.samplesBuffer, this.bufferSize)) {
                Log.v((String)this.tag, (String)"711 audio decode fail.");
            } else {
                if (this.audioTrack.getPlayState() == 2) {
                    this.audioTrack.play();
                }
                this.audioTrack.write(this.samplesBuffer, 0, audioData.length);
            }
        }
        if (this.AudioDecodeType.equals("G726")) {
            if (!this.isInitAudioDecoder) {
                this.audioDecoder.init(3, 16000, 8000, 1, 16);
                this.bufferSize = 640;
                this.audioTrack = new AudioTrack(1, 8000, 2, 2, this.bufferSize * 2, 1);
                this.audioTrack.setStereoVolume((float)this.AudioVolume, (float)this.AudioVolume);
                this.audioTrack.play();
                this.samplesBuffer = new short[this.bufferSize / 2];
                this.isInitAudioDecoder = true;
                Log.v((String)this.tag, (String)"init audio decoder fail.");
            } else if (-1 == this.audioDecoder.decode(audioData, audioData.length, this.samplesBuffer, this.bufferSize)) {
                Log.v((String)this.tag, (String)"726 audio decode fail.");
            } else {
                if (this.audioTrack.getPlayState() == 2) {
                    this.audioTrack.play();
                }
                this.audioTrack.write(this.samplesBuffer, 0, 320);
            }
        }
        if (this.AudioDecodeType.equals("PCM")) {
            if (!this.isInitAudioDecoder) {
                int bufferSize = AudioTrack.getMinBufferSize((int)8000, (int)2, (int)2);
                this.audioTrack = new AudioTrack(1, 8000, 2, 2, bufferSize * 2, 1);
                this.audioTrack.setStereoVolume((float)this.AudioVolume, (float)this.AudioVolume);
                this.audioTrack.play();
                this.isInitAudioDecoder = true;
            } else {
                this.audioTrack.write(audioData, 0, audioData.length);
            }
        }
    }

    public void VolumeChanged(AudioManager am) {
        if (am != null) {
            this.AudioVolume = am.getStreamVolume(1);
            Log.i((String)this.tag, (String)("\u95c1\u7334\u62f7\u93b3\u64b9\u3050\u6fb6\u612d\ue5ca\u95b9\u60f0\u68c4\u5a05\ufffd" + this.AudioVolume));
            if (this.audioTrack != null) {
                this.audioTrack.setStereoVolume((float)this.AudioVolume, (float)this.AudioVolume);
            }
        }
    }

    public int initLan() {
        return this.LanInit();
    }

    public int refreshLan() {
        this.devList = new ArrayList();
        return this.LanPresent();
    }

    public int refreshLanEX(int type) {
        this.devList = new ArrayList();
        return this.LanPresentEX(1);
    }

    public int regionVideoDataServer() {
        return this.RegionDataServer();
    }

    public int regionAudioDataServer() {
        return this.RegionAudioServer();
    }

    public String callLanVideo(String devid, int hkid, String DevType, int Channel2, int StreamType) {
        this.VideoQueue.clear();
        this.isVideoStop = false;
        this.devid = devid;
        this.hkid = hkid;
        this.VideoInited = false;
        this.isCaptureThumbnail = true;
        return this.DoLanInvite(hkid, DevType, Channel2, StreamType);
    }

    public String callWanVideo(String Devid, String DevType, int Channel2, int StreamType) {
        this.VideoQueue.clear();
        this.devid = Devid;
        this.VideoInited = false;
        this.isVideoStop = false;
        this.isCaptureThumbnail = true;
        return this.DoInvite(Devid, DevType, Channel2, StreamType);
    }

    public String callWanVideoEx(String jsDevid, String SAccCode, String jsDevType, int nStreamType) {
        this.VideoInited = false;
        this.devid = jsDevid;
        this.isVideoStop = false;
        this.isCaptureThumbnail = true;
        return this.sccWANDevidCalling(nStreamType, jsDevid, SAccCode, jsDevType, 0);
    }

    public int doCheckAccessPwd(String localPwd, String wanPwd) {
        return this.sccCheckAccessPwd(localPwd, wanPwd);
    }

    public String callLanAudioListen(int hkid, AudioManager am, String audioType) {
        this.AudioDecodeType = audioType;
        this.isPlayAudio = true;
        this.AudioVolume = am.getStreamVolume(1);
        return this.DoLanAudioInvite(hkid, "", 0, 0);
    }

    public String callLanAudioSay(int hkid, String audioType) {
        this.AudioDecodeType = audioType;
        this.StartSendAudio();
        String V = this.DoLanAudioSayInvite(hkid, "", 0, 0);
        return V;
    }

    public int closeLanVideo(String callId) {
        this.mIfream = 0;
        this.isVideoStop = true;
        this.mRecordIfream = 0;
        int i = this.DoLanClose(callId);
        this.VideoInited = false;
        this.VideoQueue.clear();
        this.isCaptureThumbnail = false;
        return i;
    }

    public int closeLanAudio(String callId) {
        this.isPlayAudio = false;
        return this.DoLanClose(callId);
    }

    public void closeLanAudioSay(String CallId) {
        this.AudioIsEncode = false;
        this.DoLanClose(CallId);
        this.StopSayAudio();
    }

    public int login(String userName, String password, String host) {
        return this.sccLogin(userName, password, host);
    }

    public int getWanListItem() {
        return this.GetItem(0);
    }

    public int getWanListItemEX() {
        return this.GetItemEX(0);
    }

    public int quitSysm() {
        return this.QuitSysm();
    }

    public String callWanSdData(String Devid, String fileName, int Channel2) {
        this.VideoInited = false;
        this.isVideoStop = false;
        return this.DoMonDowSdData(Devid, fileName, Channel2);
    }

    public String callLanSdData(int hkid, String jsFileName, int Channel2) {
        this.VideoInited = false;
        this.isVideoStop = false;
        return this.DoLanDowSdData(hkid, jsFileName, Channel2);
    }

    public String callWanAudioListen(String Devid, AudioManager am, String audioType) {
        this.AudioDecodeType = audioType;
        this.AudioVolume = am.getStreamVolume(1);
        return this.DoWanAudioInvite(Devid, "", 0, 0);
    }

    public String callWanAudioSay(String Devid, String audioType) {
        this.AudioDecodeType = audioType;
        String V = "";
        this.StartSendAudio();
        V = this.DoWanAudioSayInvite(Devid, "", 0, 0);
        return V;
    }

    public int closeWanVideo(String Devid, String CallId) {
        int i = -1;
        if (Devid != null && CallId != null && Devid.trim().length() > 0 && CallId.length() > 0) {
            this.isVideoStop = true;
            i = this.DoMonCloseDialog(Devid, CallId);
            this.mIfream = 0;
            this.mRecordIfream = 0;
            this.VideoQueue.clear();
            this.isCaptureThumbnail = false;
            this.VideoInited = false;
        }
        return i;
    }

    public int closeWanAudio(String Devid, String CallId) {
        int i = -1;
        if (Devid != null && CallId != null && Devid.trim().length() > 0 && CallId.length() > 0) {
            this.DoMonCloseDialog(Devid, CallId);
            i = 0;
        }
        return i;
    }

    public void closeWanAudioSay(String Devid, String CallID) {
        this.AudioIsEncode = false;
        this.DoMonCloseDialog(Devid, CallID);
        this.StopSayAudio();
    }

    public int setWanPTZ(String Devid, int Direction, int Channel2) {
        return this.SetPTZ(Devid, Direction, Channel2);
    }

    public int setWanPTZAutoControl(String Devid, int Direction, int Channel2) {
        return this.AutoControl(Devid, Direction, Channel2);
    }

    public int setWanPreset(String Devid, int Preset, int CallOrSet) {
        return this.Preset(Devid, Preset, CallOrSet);
    }

    public int setLanPTZ(int hkid, int Direction, int Channel2) {
        return this.LanSetPTZ(hkid, Direction, Channel2);
    }

    public int setLanPTZAutoControl(int hkid, int Direction, int Channel2) {
        return this.LanAutoControl(hkid, Direction, Channel2);
    }

    public int setLanPreset(int hkid, int Direction, int Preset) {
        return this.LanPreset(hkid, Direction, Preset);
    }

    public int exitLan() {
        return this.QuitLan();
    }

    public int setLanIP(int flag, String cIPInfo) {
        return this.SetLanDevIP(flag, 0, cIPInfo);
    }

    public int setLanAlarmSensitivity(String devid, int hkid, int values, int nChannel) {
        return this.DoLocalAlarmSensitivity(devid, hkid, values, nChannel);
    }

    public int setWanAlarmSensitivity(String devid, int values, int nChannel) {
        return this.DoMonAlarmSensitivity(devid, values, nChannel);
    }

    public int doForgotPassword(String username, String email, String host) {
        return this.DoSetUserPassword(username, email, host);
    }

    public int getLanGetWifiSid(int hkid, String cMac, int ulParam) {
        return this.DoLanGetWifiSid(hkid, cMac, ulParam);
    }

    public int setLanWifi(int flag, int isopen, String cWifi) {
        return this.SetLanWifi(flag, isopen, 0, cWifi);
    }

    public String getLanSysInfo(int nType, String devid) {
        return this.GetLanSysInfo(nType, devid);
    }

    public int getLanSysDevInfo(int ihkid, String devid) {
        return this.GetLanSysDevInfo(ihkid, devid, 0);
    }

    public int setLanSysInfo(int ihkid, int nType, String cBufInfo) {
        return this.SetLanSysInfo(ihkid, nType, cBufInfo, 0);
    }

    public int getWanSysDevInfo(String devid, int nType) {
        return this.GetWanSysDevInfo(devid, nType);
    }

    public String getWanSysInfo(int nType, String devid) {
        return this.GetWanSysInfo(nType, devid);
    }

    public int setWanSysInfo(String devid, int nType, String cBufInfo) {
        return this.SetWanSysInfo(nType, devid, cBufInfo, 0);
    }

    public int doWanAddDev(String devid, String cAlias, String cAccessPwd) {
        return this.DoWanAddDev(devid, cAlias, cAccessPwd, 200);
    }

    public int setLanSdParam(int hkid, String cParam) {
        return this.SetLanSdParam(hkid, cParam);
    }

    public int doRegistrationUser(String name, String pwd, String emali, String host) {
        return this.DoRegistrationUser(name, pwd, emali, host);
    }

    public int SetAccessPwdEX(String devid, String accPswd, String oldPwd) {
        int _V = -1;
        _V = this.DoSetAccessPswdEX(devid, accPswd, oldPwd, 3, 0);
        return _V;
    }

    public int doWanVideoReversal(String devid, short iFlag) {
        return this.DoWanVideoReversal(devid, iFlag, 0);
    }

    public int doLanVideoReversal(int hkid, short iFlag) {
        return this.DoLocalVideoReversal(hkid, iFlag, 0);
    }

    public int sysUpdatePasswd(String newPwd) {
        return this.SysUpdatePasswd(newPwd);
    }

    public int setAccessPswd(String devid, String accPswd) {
        return this.DoSetAccessPswd(devid, accPswd, 3, 0);
    }

    public int doWanDeleteDev(String devid) {
        return this.DoWanDeleteDev(devid, 203);
    }

    public int doWanUpdateDev(String devid, byte[] devName, int size) {
        return this.DoWanUpdateDev(devid, devName, size, 0);
    }

    public int sendWanData(String devid, String data) {
        data = "data=" + data + ";";
        return this.sccWanSendData(devid, data, 0);
    }

    public int sendWanDataNoFormat(String devid, String data) {
        return this.sccWanSendData(devid, data, 0);
    }

    public int sendLanData(int hkid, String data) {
        data = "data=" + data + ";";
        return this.sccLanSendData(hkid, data, 0);
    }

    public int sendLanDataNoFormat(int hkid, String data) {
        return this.sccLanSendData(hkid, data, 0);
    }

    public int doLanReadSdData(int hkid, int currentPage, int pageSize) {
        return this.DoLanReadSdData(hkid, currentPage, pageSize, 0);
    }

    public int doWanSDReadData(String devid, int currentPage, int pageSize) {
        return this.DoMonSDReadData(devid, currentPage, pageSize, 0);
    }

    public int setLanResolution(String Devid, int hkid, int nResolu, int iStream, int iRate) {
        return this.DoLocalMonUpdateResolution(Devid, hkid, nResolu, iStream, iRate);
    }

    public int setWanResolution(String devid, int nResolu, int iStream, int iRate) {
        return this.DoMonUpdateResolution(devid, nResolu, iStream, iRate);
    }

    public int setLanAlarmEmail(int hkid, String mailInfo) {
        return this.DoLocalMonSetAlarmEmail(hkid, mailInfo, 0);
    }

    public int setWanAlarmEmail(String devid, String mailInfo) {
        return this.DoMonSetAlarmEmail(devid, mailInfo, 0);
    }

    public int getLanAlarmEmail(int hkid) {
        return this.DoLocalMonGetAlarmEmail(hkid, 0);
    }

    public int getWanAlarmEmail(String devid) {
        return this.DoMonGetAlarmEmail(devid, 0);
    }

    public int doLanFormatSD(int hkid) {
        return this.DoLocalMonFormatSD(hkid, 0);
    }

    public int doLanDeleteSdFile(int hkid, String jsFileName) {
        return this.DoLocalMonDeletePhoto(hkid, 0, jsFileName);
    }

    public int doWanDeleteSdFile(String devId, String jsFileName) {
        return this.DoMonDeletePhoto(devId, 0, jsFileName);
    }

    public int regionSdDataServer() {
        return this.RegionSdDataServer();
    }

    public void doCheckIP() {
        new CheckIPTask().execute();
    }

    private void checkIPAdd() {
        this.sccInitIPRate();
        String[] ip = new String[]{"hk1.uipcam.net", "hk2.uipcam.net", "hk3.uipcam.net", "hk4.uipcam.net", "hk5.uipcam.net", "hk6.uipcam.net", "hk7.uipcam.net", "hk8.uipcam.net"};
        int i = 0;
        while (i < ip.length) {
            this.sccAddTestSrvIP(ip[i]);
            ++i;
        }
        this.sccStartCheckRate("");
    }

    public void capture(String path, String fileName) {
        new CaptureAsyncTask(path, fileName).execute();
    }

    public int SetLanLevel(int iFlag, String devid, int hkid, int level) {
        return this.DoLanMonSetLevel(iFlag, devid, hkid, level, 0);
    }

    public int setWanLevel(int iFlag, String jsDevid, int level) {
        return this.DoMonSetLevel(iFlag, jsDevid, level, 0);
    }

    public int initAviInfo(int videoType, int iRate, int audioType, String RecPath, String FileName) {
        return this.InitAviInfo(videoType, iRate, this.mFrameWidth, this.mFrameHeight, audioType, RecPath, FileName);
    }

    public void aviRecordVideo() {
        this.DoMonDevIoAram(this.devid, 100, 1);
        this.DoLocalMonDevIoAram(this.hkid, 100, 1);
        this.isStartVideo = true;
        this.recordDataQueue.add(new VideoDataBuf(this.iFrameData));
        new RecordDataThread().start();
    }

    public void aviRecordAudio() {
        this.isStartAudio = true;
    }

    public int stopRecordClose() {
        this.mIfream = 0;
        this.mRecordIfream = 0;
        this.isStartAudio = false;
        this.isStartVideo = false;
        this.recordDataQueue.clear();
        return this.sccRecordClose();
    }

    public int doRegWanService() {
        return this.RegionMonServer();
    }

    private void StopSayAudio() {
        this.AudioIsEncode = false;
        if (this.AudioEncoderThread != null && this.AudioEncoderThread.isAlive()) {
            try {
                this.AudioEncoderThread.join();
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public int getState(String devid) {
        return this.sccGetDevStatus(devid);
    }

    public int getDevStatusEx(String devid, int count) {
        return this.sccGetDevStatusEX(devid, count);
    }

    public int chongDian(int hkid, int Flag, int Channel2) {
        return this.LanAperture(hkid, Flag, Channel2);
    }

    public int chongDianWan(String Devid, int Flag, int Channel2) {
        return this.Aperture(Devid, Flag, Channel2);
    }

    public int getQueueSize() {
        return this.VideoQueue.size();
    }

    public void setQuene(int Max) {
        this.VideoQueue.setMax(Max);
    }

    public int lanAppCheck(int hkid) {
        return this.DoLocalAppCheck(hkid, 0);
    }

    public int wanAppCheck(String devid) {
        return this.DoMonAppCheck(devid, 0);
    }

    private void CaptureThumbnail() {
        if (this.CaptureThumbnailThread == null) {
            this.CaptureThumbnailThread = new Thread(){

                @Override
                public void run() {
                    Log.i((String)OnlineService.this.tag, (String)"CaptureThumbnail Thread Is Start");
                    try {
                        try {
                            while (OnlineService.this.isCaptureThumbnail) {
                                Thread.sleep(5000);
                                byte[] rebuff = new byte[OnlineService.this.mFrameWidth * OnlineService.this.mFrameHeight * 3];
                                OnlineService.this.sccYuv2Rgb(OnlineService.this.buff.array(), rebuff, OnlineService.this.mFrameWidth, OnlineService.this.mFrameHeight);
                                Bitmap VideoBit = Bitmap.createBitmap((int)OnlineService.this.mFrameWidth, (int)OnlineService.this.mFrameHeight, (Bitmap.Config)Bitmap.Config.RGB_565);
                                VideoBit.copyPixelsFromBuffer((Buffer)ByteBuffer.wrap(rebuff));
                                Bitmap VideoBitmap = Bitmap.createBitmap((Bitmap)VideoBit);
                                System.out.println(String.valueOf(VideoBitmap.getWidth()) + "..");
                                OnlineService.this.CaptureThumbnail(VideoBitmap, OnlineService.this.devid);
                                if (VideoBitmap != null) {
                                    if (!VideoBitmap.isRecycled()) {
                                        VideoBitmap.recycle();
                                    }
                                    VideoBitmap = null;
                                }
                                if (!this.isInterrupted()) {
                                    continue;
                                }
                                break;
                            }
                        }
                        catch (InterruptedException e) {
                            Log.e((String)OnlineService.this.tag, (String)"CaptureThumbnail Thread Is Error");
                            System.gc();
                        }
                    }
                    finally {
                        System.gc();
                    }
                }
            };
            this.CaptureThumbnailThread.start();
        }
    }

    private void CaptureThumbnail(Bitmap SourceBitmap, String Id) {
    }

    public void setTalkMode(String devid, AudioManager am, String audioType) {
        this.regionAudioDataServer();
        this.talkMode = 1;
        this.AudioDecodeType = audioType;
        this.AudioVolume = am.getStreamVolume(1);
        this.devid = devid;
        this.StartSendAudio();
        this.mSayCallId = this.DoWanAudioSayInvite(devid, "", 0, 0);
        this.mAudioCallId = this.DoWanAudioInvite(devid, "", 0, 0);
    }

    public void closeWanAudio() {
        this.DoMonCloseDialog(this.devid, this.mAudioCallId);
        this.DoMonCloseDialog(this.devid, this.mSayCallId);
        this.AudioIsEncode = false;
        this.talkFlag = 0;
        this.talkMode = 0;
    }

    public void setTalkFlag(int talkFlag) {
        this.talkFlag = talkFlag;
    }

    public int addBroadcastAddr(String getway) {
        return this.sccAddBcastAddr(getway);
    }

    public int doWanFormatSD(String devid) {
        return this.DoMonFormatSD(devid, 0);
    }

    public int doSetFtpInfo(String devid, int hkid, String buf) {
        return this.sccSetLANInfo(devid, hkid, 201, buf, 0, 0);
    }

    public int doGetFtpInfo(String devid, int hkid) {
        Log.e((String)"doGetFtpInfo", (String)(String.valueOf(devid) + ".." + hkid));
        return this.sccGetLANInfo(devid, hkid, 201, 0, 0);
    }

    public int checkAPState(int hkid) {
        return this.sccCheckAPStatus(hkid);
    }

    public int doLanAutoCleaning(int hkid) {
        return this.LanAutoControl(hkid, 3, 0);
    }

    public int doWanAutoCleaning(String devid) {
        return this.AutoControl(devid, 3, 0);
    }

    public int doLanCharging(int hkid) {
        return this.LanAperture(hkid, 0, 0);
    }

    public int doWanCharging(String devid) {
        return this.Aperture(devid, 0, 0);
    }

    public int doLanIoAram(int hkid, int flag) {
        return this.DoLocalMonDevIoAram(hkid, flag, 0);
    }

    public int doWanIoAram(String devid, int flag) {
        return this.DoMonDevIoAram(devid, flag, 0);
    }

    static /* synthetic */ void access$12(OnlineService onlineService, int n) {
        onlineService.mFrameWidth = n;
    }

    static /* synthetic */ void access$13(OnlineService onlineService, int n) {
        onlineService.mFrameHeight = n;
    }

    private class CaptureAsyncTask
    extends AsyncTask<Integer, Integer, Integer> {
        String path;
        String fileName;

        public CaptureAsyncTask(String path, String fileName) {
            this.path = path;
            this.fileName = fileName;
        }

        @SuppressLint(value={"SimpleDateFormat"})
        protected /* varargs */ Integer doInBackground(Integer ... params) {
            int result = 0;
            try {
                FileOutputStream fos;
                byte[] rebuff = new byte[OnlineService.this.mFrameWidth * OnlineService.this.mFrameHeight * 3];
                Bitmap VideoBit = Bitmap.createBitmap((int)OnlineService.this.mFrameWidth, (int)OnlineService.this.mFrameHeight, (Bitmap.Config)Bitmap.Config.RGB_565);
                VideoBit.copyPixelsFromBuffer((Buffer)ByteBuffer.wrap(OnlineService.this.buff.array()));
                Bitmap VideoBitmap = Bitmap.createBitmap((Bitmap)VideoBit);
                File f = new File(this.path);
                if (!f.exists()) {
                    f.mkdirs();
                }
                if (VideoBitmap.compress(Bitmap.CompressFormat.PNG, 100, (OutputStream)(fos = new FileOutputStream(String.valueOf(this.path) + "/" + this.fileName)))) {
                    fos.flush();
                    fos.close();
                }
            }
            catch (Exception e) {
                result = -1;
                e.printStackTrace();
            }
            return result;
        }

        protected void onPostExecute(Integer result) {
            OnlineService.this.videoDataOfCallBack.OnCallbackFunForRegionMonServer(result);
        }
    }

    class CheckIPTask
    extends AsyncTask<Void, Void, Void> {
        CheckIPTask() {
        }

        protected /* varargs */ Void doInBackground(Void ... params) {
            OnlineService.this.checkIPAdd();
            return null;
        }
    }

    class RecordDataThread
    extends Thread {
        RecordDataThread() {
        }

        @Override
        public void run() {
            try {
                while (OnlineService.this.isStartVideo) {
                    byte[] buf;
                    VideoDataBuf videoBuf = OnlineService.this.recordDataQueue.poll();
                    if (videoBuf == null || (buf = videoBuf.getBuf()) == null) continue;
                    OnlineService.this.sccAviRecordVideo(buf, buf.length);
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class sendDataThread
    extends Thread {
        sendDataThread() {
        }

        @Override
        public void run() {
            try {
                while (!OnlineService.this.isVideoStop) {
                    if (OnlineService.this.VideoQueue.isEmpty()) continue;
                    VideoDataBuf videoBuf = OnlineService.this.VideoQueue.poll();
                    int time = videoBuf.getTime();
                    int size = videoBuf.getSize();
                    byte[] DataBuf = videoBuf.getBuf();
                    if (DataBuf == null || DataBuf.length <= 0) continue;
                    if (isDecode_) {
                        int len = _decoders.decode(DataBuf, DataBuf.length, OnlineService.this.mPixel, OnlineService.this.mPixelSize);
                        if (len == -1) {
                            String Info = "decode fail";
                            Log.i((String)OnlineService.this.tag, (String)Info);
                            continue;
                        }
                        if (OnlineService.this.mFrameWidth != _decoders.frameWidth() || OnlineService.this.mFrameHeight != _decoders.frameHeight()) {
                            OnlineService.access$12(OnlineService.this, _decoders.frameWidth());
                            OnlineService.access$13(OnlineService.this, _decoders.frameHeight());
                        }
                        OnlineService.this.buff.position(0);
                        int queueSize = OnlineService.this.getQueueSize();
                        OnlineService.this.videoDataOfCallBack.OnCallbackFunForDataServer(OnlineService.this.mCallId, OnlineService.this.buff, OnlineService.this.mFrameWidth, OnlineService.this.mFrameHeight, size, time);
                        VideoSleep.opeartion(time, queueSize);
                        continue;
                    }
                    OnlineService.this.videoDataOfCallBack.OnCallbackFunForUnDecodeDataServer(OnlineService.this.mCallId, DataBuf, OnlineService.this.mFrameWidth, OnlineService.this.mFrameHeight, size, time, OnlineService.this.mFream);
                }
            }
            catch (Exception videoBuf) {
                // empty catch block
            }
        }
    }

}

