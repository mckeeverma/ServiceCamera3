package com.example.marc.servicecamera3;
import android.app.Service;
import android.content.Intent;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.StrictMode;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
@SuppressWarnings("deprecation")
public class CapPhoto extends Service {
    public SurfaceHolder sHolder;
    public Camera mCamera;
    public Camera.Parameters parameters;
    // constant
    public static final long NOTIFY_INTERVAL = 30000; // milliseconds
    // run on another Thread to avoid crash
    private Handler mHandler = new Handler();
    // timer handling
    private Timer mTimer = null;
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("CAM", "start");
        //----------------------------------------------------------------------
        // timer code - begin
        //----------------------------------------------------------------------
        // cancel if already existed
        if (mTimer != null) {
            mTimer.cancel();
        } else {
            // recreate new
            mTimer = new Timer();
        }
        // schedule task
        mTimer.scheduleAtFixedRate(new TimeDisplayTimerTask(), 0, NOTIFY_INTERVAL);
        //----------------------------------------------------------------------
        // timer code - end
        //----------------------------------------------------------------------
        Log.d("______________marclog: ", "in CapPhoto now");
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        Thread myThread = null;
    }
    @Override
    public void onStart(Intent intent, int startId) {
        Log.d("______________marclog: ", "CapPhoto 001____");
        super.onStart(intent, startId);
        Log.d("______________marclog: ", "CapPhoto 002");
        if (Camera.getNumberOfCameras() >= 2) {
            Log.d("______________marclog: ", "num of cameras: " + String.valueOf(Camera.getNumberOfCameras()));
            //mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
            Log.d("______________marclog: ", "will call getCameraInstance now");
            mCamera = getCameraInstance();
            Log.d("______________marclog: ", "call to getCameraInstance completed");
        }
        Log.d("______________marclog: ", "CapPhoto 004");
        //------------------------------------------------------------------------------------------------
        SurfaceView sv = new SurfaceView(getApplicationContext());
        //------------------------------------------------------------------------------------------------
        Log.d("______________marclog: ", "will now call mCamera.setDisplayOrientation");
        try {
            mCamera.setDisplayOrientation(90);
            Log.d("______________marclog: ", "okay: mCamera.setDisplayOrientation");
        } catch (Exception e) {
            Log.d("______________marclog: ", "Error on mCamera.setDisplayOrientation");
        }
        //------------------------------------------------------------------------------------------------
        Log.d("______________marclog: ", "will now call mCamera.setPreviewDisplay(sv.getHolder())");
        try {
            mCamera.setPreviewDisplay(sv.getHolder());
            Log.d("______________marclog: ", "okay: mCamera.setPreviewDisplay(sv.getHolder())");
        } catch (Exception e) {
            Log.d("______________marclog: ", "Error on mCamera.setPreviewDisplay(sv.getHolder())__" + e.getMessage());
        }
        //------------------------------------------------------------------------------------------------
        SurfaceTexture st = new SurfaceTexture(MODE_PRIVATE);
        try {
            Log.d("______________marclog: ", "CapPhoto 007");
            Log.d("______________marclog: ", "CapPhoto 008b");
            mCamera.setPreviewTexture(st);
            Log.d("______________marclog: ", "CapPhoto 008c");
            try {
                mCamera.startPreview();
                Log.d("______________marclog: ", "CapPhoto 008d");
            } catch (Exception e) {
                Log.d("______________marclog: ", "CapPhoto 009_error: " + e);
            }
            //
            //CameraPreview mPreview = new CameraPreview(this, mCamera);
            //Log.d(TAG, "0000   main 002");
            //FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
            //Log.d(TAG, "0000   main 003");
            //preview.addView(mPreview);
            //Log.d(TAG, "0000   main 004");
            //
            Log.d("______________marclog: ", "CapPhoto 009");
            mCamera.takePicture(null, null, mCall);
            Log.d("______________marclog: ", "CapPhoto 010");
        } catch (IOException e) {
            Log.d("______________marclog: ", "CapPhoto 011");
            e.printStackTrace();
            Log.d("______________marclog: ", "CapPhoto 012");
        }
        Log.d("______________marclog: ", "CapPhoto 013");
        sHolder = sv.getHolder();
        Log.d("______________marclog: ", "CapPhoto 014");
        sHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        Log.d("______________marclog: ", "CapPhoto 015");
    }
    Camera.PictureCallback mCall = new Camera.PictureCallback() {
        public void onPictureTaken(final byte[] data, Camera camera) {
            Log.d("______________marclog: ", "CapPhoto 016");
            FileOutputStream outStream = null;
            try {
                Log.d("______________marclog: ", "CapPhoto 017");
                File sd = new File(Environment.getExternalStorageDirectory(), "A");
                Log.d("______________marclog: ", "CapPhoto 018");
                if (!sd.exists()) {
                    Log.d("______________marclog: ", "CapPhoto 019");
                    sd.mkdirs();
                    Log.d("______________marclog: ", "CapPhoto 020");
                    Log.i("FO", "folder" + Environment.getExternalStorageDirectory());
                }
                Log.d("______________marclog: ", "CapPhoto 021");
                Calendar cal = Calendar.getInstance();
                Log.d("______________marclog: ", "CapPhoto 022");
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
                Log.d("______________marclog: ", "CapPhoto 023");
                String tar = (sdf.format(cal.getTime()));
                Log.d("______________marclog: ", "CapPhoto 024");
                outStream = new FileOutputStream(sd + "/" + tar + ".jpg");
                Log.d("______________marclog: ", "CapPhoto 025");
                outStream.write(data);
                Log.d("______________marclog: ", "CapPhoto 026");
                outStream.close();
                Log.d("______________marclog: ", "CapPhoto 027");
                Log.i("CAM", data.length + " byte written to:" + sd + "/" + tar + ".jpg");
                Log.d("______________marclog: ", "CapPhoto 028");
                camkapa(sHolder);
                Log.d("______________marclog: ", "CapPhoto 029");
            } catch (FileNotFoundException e) {
                Log.d("______________marclog: ", "CapPhoto 030");
                Log.d("CAM", e.getMessage());
            } catch (IOException e) {
                Log.d("______________marclog: ", "CapPhoto 031");
                Log.d("CAM", e.getMessage());
            }
        }
    };
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    public void camkapa(SurfaceHolder sHolder) {
        if (null == mCamera) {
            Log.d("______________marclog: ", "CapPhoto 032");
            return;
        }
        Log.d("______________marclog: ", "CapPhoto 033");
        mCamera.stopPreview();
        Log.d("______________marclog: ", "CapPhoto 034");
        mCamera.release();
        Log.d("______________marclog: ", "CapPhoto 035");
        mCamera = null;
        Log.i("CAM", " closed");
    }
    public Camera getCameraInstance() {
        Camera c = null;
        Log.d("______________marclog: ", "will call Camera.open now");
        try {
            c = Camera.open(); // attempt to get a Camera instance
            Log.d("______________marclog: ", "okay on Camera.open");
        } catch (Exception e) {
            Log.d("______________marclog: ", "error on Camera.open");
        }
        Log.d("______________marclog: ", "will call mCamera.getParameters now");
        try {
            parameters = c.getParameters();
            Log.d("______________marclog: ", "okay on c.getParameters");
        } catch (Exception e) {
            Log.d("______________marclog: ", "error on c.getParameters__" + e.getMessage());
        }
        Log.d("______________marclog: ", "will call parameters.FLASH_MODE_ON now");
        try {
            parameters.setFlashMode(parameters.FLASH_MODE_ON);
            Log.d("______________marclog: ", "okay on parameters.FLASH_MODE_ON");
        } catch (Exception e) {
            Log.d("______________marclog: ", "error on parameters.FLASH_MODE_ON__" + e.getMessage());
        }
        Log.d("______________marclog: ", "will call camera setParameters now");
        try {
            c.setParameters(parameters);
            Log.d("______________marclog: ", "okay on camera setParameters");
        } catch (Exception e) {
            Log.d("______________marclog: ", "error on camera setParameters__" + e.getMessage());
        }
        Log.d("______________marclog: ", "__end of getCameraInstance");
        return c; // returns null if camera is unavailable
    }
    class TimeDisplayTimerTask extends TimerTask {
        @Override
        public void run() {
            // run on another thread
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), getDateTime(), Toast.LENGTH_LONG).show();
                    Log.i("toast", "timer here !!!");
                }
            });
        }
        private String getDateTime() {
            // get date time in custom format
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd - HH:mm:ss");
            return sdf.format(new Date());
        }
    }
}