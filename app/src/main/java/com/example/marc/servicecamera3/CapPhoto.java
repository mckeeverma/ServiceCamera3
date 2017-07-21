package com.example.marc.servicecamera3;
import android.app.Service;
import android.content.Intent;
import android.graphics.ImageFormat;
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

import junit.runner.Version;

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
    public String TAG = "marclog";
    // constant
    public static final long NOTIFY_INTERVAL = 30000; // milliseconds
    // run on another Thread to avoid crash
    private Handler mHandler = new Handler();
    // timer handling
    private Timer mTimer = null;
    String flashMode;
    int jpegQuality;
    int displayOrientation;
    Camera.Size camera_size;
    @Override
    public void onCreate() {
        super.onCreate();
        mCamera = Camera.open();
        //---------------------------------------------------------------------------------------------------
        flashMode = mCamera.getParameters().getFlashMode();
        Log.d("______________marclog: ", "flashMode = " + flashMode);
        jpegQuality = mCamera.getParameters().getJpegQuality();
        Log.d("______________marclog: ", "jpegQuality = " + jpegQuality);
        //jpegQuality = mCamera.getDis;
        //Log.d("______________marclog: ", "displayOrientation = " + displayOrientation);
        camera_size = mCamera.getParameters().getPictureSize();
        Log.d("______________marclog: ", "camera_size = " + camera_size.width + "x" + camera_size.height);
        //---------------------------------------------------------------------------------------------------
        Log.d("______________marclog: ", "will call mCamera.getParameters now");
        try {
            parameters = mCamera.getParameters();
            Log.d("______________marclog: ", "okay on c.getParameters");
        } catch (Exception e) {
            Log.d("______________marclog: ", "error on c.getParameters__" + e.getMessage());
        }
        //---------------------------------------------------------------------------------------------------
        Log.d("______________marclog: ", "will call parameters flash-mode now");
        try {
            parameters.setFlashMode("torch");
            Log.d("______________marclog: ", "okay on parameters flash-mode");
        } catch (Exception e) {
            Log.d("______________marclog: ", "error on parameters flash-mode" + e.getMessage());
        }
        //---------------------------------------------------------------------------------------------------
        Log.d("______________marclog: ", "will call parameters jpeg-quality now");
        try {
            parameters.setJpegQuality(99);
            Log.d("______________marclog: ", "okay on parameters jpeg-quality");
        } catch (Exception e) {
            Log.d("______________marclog: ", "error on parameters jpeg-quality" + e.getMessage());
        }
        //---------------------------------------------------------------------------------------------------
        Log.d("______________marclog: ", "will call parameters setPictureSize now");
        try {
            parameters.setPictureSize(320, 240);
            Log.d("______________marclog: ", "okay on parameters setPictureSize");
        } catch (Exception e) {
            Log.d("______________marclog: ", "error on parameters setPictureSize" + e.getMessage());
        }
        //---------------------------------------------------------------------------------------------------
        Log.d("______________marclog: ", "will call camera setParameters now");
        try {
            mCamera.setParameters(parameters);
            Log.d("______________marclog: ", "okay on camera setParameters");
        } catch (Exception e) {
            Log.d("______________marclog: ", "error on camera setParameters__" + e.getMessage());
        }
        //---------------------------------------------------------------------------------------------------
        flashMode = mCamera.getParameters().getFlashMode();
        Log.d("______________marclog: ", "flashMode = " + flashMode + " ___after change");
        jpegQuality = mCamera.getParameters().getJpegQuality();
        Log.d("______________marclog: ", "jpegQuality = " + jpegQuality + " ___after change");
        camera_size = mCamera.getParameters().getPictureSize();
        Log.d("______________marclog: ", "camera_size = " + camera_size.width + "x" + camera_size.height + " ___after change");
        //---------------------------------------------------------------------------------------------------
        //params.setPreviewSize(1280, 960);
        //---------------------------------------------------------------------------------------------------
        try {
            mCamera.setPreviewTexture(new SurfaceTexture(10));
        } catch (IOException e1) {
            Log.e(TAG, e1.getMessage());
        }
        //---------------------------------------------------------------------------------------------------
        flashMode = mCamera.getParameters().getFlashMode();
        Log.d("______________marclog: ", "flashMode = " + flashMode + " ___after change 2");
        try {
            Thread.sleep(5000);
        } catch (Exception e) {
            Log.d("______________marclog: ", "Error on Thread.sleep");
        }
        Log.d("______________marclog: ", "done sleeping");
        //---------------------------------------------------------------------------------------------------
        //SurfaceTexture st = new SurfaceTexture(10);
        //try {
        //    mCamera.setPreviewTexture(st);
        //} catch (Exception e) {
        //    Log.d("______________marclog: ", "mCamera.setPreviewTexture(st)__" + e.getMessage());
        //}
        //---------------------------------------------------------------------------------------------------
        //mCamera.setDisplayOrientation(270);
        //---------------------------------------------------------------------------------------------------
        mCamera.startPreview();
        //---------------------------------------------------------------------------------------------------
        flashMode = mCamera.getParameters().getFlashMode();
        Log.d("______________marclog: ", "flashMode = " + flashMode + " ___after change 2");
        mCamera.takePicture(null, null, null, new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                flashMode = mCamera.getParameters().getFlashMode();
                Log.d("______________marclog: ", "flashMode = " + flashMode + " ___after change 2");
                Log.i(TAG, "picture-taken___");
                flashMode = mCamera.getParameters().getFlashMode();
                Log.d("______________marclog: ", "flashMode = " + flashMode + " ___after change 2");
                Log.d(TAG, "CapPhoto 016");
                FileOutputStream outStream = null;
                try {
                    Log.d(TAG, "CapPhoto 017");
                    File sd = new File(Environment.getExternalStorageDirectory(), "A");
                    Log.d(TAG, "CapPhoto 018");
                    if (!sd.exists()) {
                        Log.d(TAG, "CapPhoto 019");
                        sd.mkdirs();
                        Log.d(TAG, "CapPhoto 020");
                        Log.i("FO", "folder" + Environment.getExternalStorageDirectory());
                    }
                    Log.d(TAG, "CapPhoto 021");
                    Calendar cal = Calendar.getInstance();
                    Log.d(TAG, "CapPhoto 022");
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
                    Log.d(TAG, "CapPhoto 023");
                    String tar = (sdf.format(cal.getTime()));
                    Log.d(TAG, "CapPhoto 024");
                    outStream = new FileOutputStream(sd + "/" + tar + ".jpg");
                    Log.d(TAG, "CapPhoto 025");
                    outStream.write(data);
                    Log.d(TAG, "CapPhoto 026");
                    outStream.close();
                    Log.d(TAG, "CapPhoto 027");
                    Log.i("CAM", data.length + " byte written to:" + sd + "/" + tar + ".jpg");
                    Log.d(TAG, "CapPhoto 028");
                } catch (FileNotFoundException e) {
                    Log.d(TAG, "CapPhoto 030");
                    Log.d("CAM", e.getMessage());
                } catch (IOException e) {
                    Log.d(TAG, "CapPhoto 031");
                    Log.d("CAM", e.getMessage());
                }
            }
        });
    }
    @Override
    public void onStart(Intent intent, int startId) {

        super.onStart(intent, startId);
        Log.d("______________marclog: ", "start of onStart method");

    }
    @Override
    public IBinder onBind(Intent intent) {
        return null;
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