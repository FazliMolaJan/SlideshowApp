package com.example.shika.slidishow.Code.ui;


import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.hardware.Camera;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;

import com.example.shika.slidishow.R;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;


public class PictureTracker extends ActionBarActivity {

    public static final String TAG="PICTURE";
    SurfaceView surfaceView;
    SurfaceHolder surfaceHolder;
    boolean isViewing;
    Camera camera;
    List<String> effects;
    List<Camera.Size> sizes;
    String effect=Camera.Parameters.EFFECT_NONE;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_tracker);
        surfaceView= (SurfaceView) findViewById(R.id.cameraSurfaceView);
        surfaceView.setOnTouchListener(touchListener);

        surfaceHolder=surfaceView.getHolder();
        surfaceHolder.addCallback(callback);

        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.


        super.onCreateOptionsMenu(menu);

        if (effects!=null) {
            for (String effect : effects) {
                menu.add(effect);
            }
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        Camera.Parameters p=camera.getParameters();

        p.setColorEffect(item.getTitle().toString());
        camera.setParameters(p);
        return super.onOptionsItemSelected(item);
    }
    SurfaceHolder.Callback callback=new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {

            camera=Camera.open();
            effects=camera.getParameters().getSupportedColorEffects();
            sizes=camera.getParameters().getSupportedPictureSizes();
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            if (isViewing) {
                camera.stopPreview();
            }
            Camera.Parameters p=camera.getParameters();
            p.setPreviewSize(sizes.get(0).width,sizes.get(0).height);
            p.setColorEffect(effect);
            camera.setParameters(p);


            try {
                camera.setPreviewDisplay(holder);
            } catch (IOException e) {
                Log.d(TAG , e.getMessage());
            }


        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {

            camera.stopPreview();
            isViewing=false;
            camera.release();
        }
    };
    Camera.PictureCallback pictureCallback=new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {

            String filname="slideshow_"+System.currentTimeMillis();

            ContentValues contentValues=new ContentValues();
            contentValues.put(MediaStore.Images.Media.TITLE, filname);
            contentValues.put(MediaStore.Images.Media.DATE_ADDED,System.currentTimeMillis());
            contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/jpg");

            Uri uri=getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,contentValues);


            try {
                OutputStream outputStream=getContentResolver().openOutputStream(uri);


                outputStream.write(data);
                outputStream.flush();
                outputStream.close();
                Intent intent=new Intent();
                intent.setData(uri);
                setResult(RESULT_OK , intent);
                Toast.makeText(PictureTracker.this , "Saved " +uri.toString(),Toast.LENGTH_LONG).show();
                finish();

            }  catch (IOException e) {
                setResult(RESULT_CANCELED);
               Log.d(TAG ,e.getMessage());
            }

        }
    };

    View.OnTouchListener touchListener=new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            camera.takePicture(null, null,pictureCallback);
            return false;
        }
    };
}
