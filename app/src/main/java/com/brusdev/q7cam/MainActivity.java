package com.brusdev.q7cam;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.nio.ByteBuffer;

import x1.Studio.Core.IVideoDataCallBack;
import x1.Studio.Core.OnlineService;

public class MainActivity extends AppCompatActivity implements SurfaceHolder.Callback,
        IVideoDataCallBack {

    private OnlineService ons;
    public static boolean isInitLan = false;
    private Bitmap VideoBit;
    private boolean VideoInited = false;
    private Rect RectOfRegion;
    private RectF RectOfScale;
    private Matrix rotator;
    private int Width;
    private int Height;
    private SurfaceView mSurfaceView;
    private SurfaceHolder mSurfaceHolder;
    private DevInfo devInfo;
    private String callID;

    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ons = OnlineService.getInstance();
        ons.setCallBackData(this);

        if (!isInitLan) {
            isInitLan = true;
            ons.initLan(); //��ʼ��������;

        }

        mSurfaceView = findViewById(R.id.surfaceView_video);
        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.addCallback(this);

        ons.regionAudioDataServer();
        ons.regionVideoDataServer();

        devInfo = new DevInfo();

        handler = new Handler();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ctrlPlayer(false);

                System.out.println(ons.refreshLan());

                Snackbar.make(view, "Refreshing lan", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void ctrlPlayer(boolean enabled) {
        if (callID != null) {
            ons.closeLanVideo(callID);
            devInfo.setHkid(0);
            callID = null;
        }

        if (enabled) {
            callID = ons.callLanVideo(devInfo.getDevid(),
                    devInfo.getHkid(), devInfo.getVideoType(),
                    devInfo.getChannal(), 0);
        }
    }

    private void initPlayer(int mFrameWidth, int mFrameHeight) {

        VideoInited = true;

        VideoBit = Bitmap.createBitmap(mFrameWidth, mFrameHeight,
                Bitmap.Config.RGB_565);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        this.Width = dm.widthPixels;
        this.Height = dm.heightPixels;

        // int Left = (this.Width - this.mFrameWidth) / 2;
        int Top = 0;
        int myHight = this.Height;
        int myWidth = (int) (mFrameWidth * (Double.valueOf(this.Height) / Double
                .valueOf(mFrameHeight)));
        int Left = (this.Width - myWidth) / 2;
        this.RectOfRegion = null;
        // this.RectOfScale = new RectF(Left, Top, Left + myWidth, myHight);
        this.RectOfScale = new RectF(0, 0, mSurfaceView.getHeight(), mSurfaceView.getWidth());
    }

    @Override
    public void OnCallbackFunForDataServer(String CallId, ByteBuffer Buf,
                                           int mFrameWidth, int mFrameHeight, int mEncode, int mTime) {

        if (!VideoInited) {
            initPlayer(mFrameWidth, mFrameHeight);

        }

        this.VideoBit.copyPixelsFromBuffer(Buf);
        try {
            Canvas canvas = mSurfaceHolder.lockCanvas(null);

            try {
                canvas.save();
                canvas.rotate(90, this.RectOfScale.width() / 2, this.RectOfScale.height() / 2);
                canvas.translate((this.RectOfScale.width() - this.RectOfScale.height()) / 2,
                        (this.RectOfScale.width() - this.RectOfScale.height()) / 2);
                canvas.drawColor(Color.BLACK);
                canvas.drawBitmap(this.VideoBit, this.RectOfRegion,
                        this.RectOfScale, null);
                canvas.restore();
            } finally {
                this.mSurfaceHolder.unlockCanvasAndPost(canvas);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void OnCallbackFunForUnDecodeDataServer(String var1, byte[] var2, int var3, int var4, int var5, int var6, int var7) {

    }

    @Override
    public void OnCallbackFunForLanDate(String devid, String videoType, int hkid, int channal, int status, String audioType) {
        if (devid.equals("302")) {
            return;
        }

        //TODO: create a list to select the cam.
        devInfo.setDevid(devid);
        devInfo.setVideoType(videoType);
        devInfo.setHkid(hkid);
        devInfo.setChannal(channal);
        devInfo.setStats(status);
        devInfo.setAudioType(audioType);
        devInfo.setType(0);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ctrlPlayer(true);
            }
        }, 500);
    }

    @Override
    public void OnCallbackFunForRegionMonServer(int iFlag) {

    }

    @Override
    public void OnCallbackFunForComData(int Type, int Result,
                                        int AttachValueBufSize, String AttachValueBuf) {

    }

    @Override
    public void OnCallbackFunForGetItem(byte[] byteArray, int result) {

    }

    @Override
    public void OnCallbackFunForIPRateData(String Ip) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }


    @Override
    protected void onDestroy() {
        if (callID != null) {
            ons.closeLanVideo(callID);
        }

        // ons.quitSysm();

        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();

        ons.setCallBackData(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        this.finish();
    }
}
