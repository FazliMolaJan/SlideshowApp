package com.example.shika.slidishow.Code.fragments;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import com.example.shika.slidishow.Code.adapters.SlideShowAdapter;
import com.example.shika.slidishow.Code.ui.MainActivity;
import com.example.shika.slidishow.Code.utils.MediaItem;
import com.example.shika.slidishow.Code.utils.SlideShowInfo;
import com.example.shika.slidishow.R;

import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * A placeholder fragment containing a simple view.
 */
public class FragmentViewing extends Fragment {
    static final String MEDIA_TIME="MEDIA_TIME";
    static final String IMAGE_INDEX="IMAGE_INDEX";
    static final String SLIDESHOW_NAME="SLIDESHOW_NAME";
    static final int DURATION=5000;
    ImageView imageView;
    String SlideshowName;
    SlideShowInfo slideShowInfo;
    BitmapFactory.Options options;

    VideoView videoView;
    MediaPlayer mediaPlayer;

    android.os.Handler handler;
    int mediaTime;
    int nextImage;
    public FragmentViewing() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_vewing_sliding, container, false);


        if (savedInstanceState == null) {
            SlideshowName = getActivity().getIntent().getStringExtra(SlideShowAdapter.EXTER_NAME);
            mediaTime = 0;
            nextImage = 0;

        } else {

            mediaTime = savedInstanceState.getInt(MEDIA_TIME);
            nextImage = savedInstanceState.getInt(IMAGE_INDEX);
            SlideshowName = savedInstanceState.getString(SLIDESHOW_NAME);

        }
        imageView=(ImageView) rootView.findViewById(R.id.image);

        videoView= (VideoView) rootView.findViewById(R.id.videoView);


        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                handler.post(updateSlideshow);
            }
        });
        slideShowInfo = MainActivity.getSlideinfo(SlideshowName);
        options = new BitmapFactory.Options();
        options.inSampleSize = 4;
        if (slideShowInfo.getMusicPath() != null)  {

            try {
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setDataSource(getActivity(), Uri.parse(slideShowInfo.getMusicPath()));

                mediaPlayer.prepare();
                mediaPlayer.setLooping(true);
                mediaPlayer.seekTo(mediaTime);

            } catch (Exception e) {

                Log.d("Viewing" , e.getMessage());
            }
        }
        handler=new android.os.Handler();

            return rootView;
        }

    @Override
    public void onStart() {
        super.onStart();
        handler.post(updateSlideshow);
    }

    @Override
    public void onStop() {
        super.onStop();
        handler.removeCallbacks(updateSlideshow);

    }

    @Override
    public void onPause() {
        super.onPause();
        if (mediaPlayer!=null){
            mediaPlayer.pause();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mediaPlayer!=null){
            mediaPlayer.start();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer!=null){

            mediaPlayer.release();
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (mediaPlayer!=null){
            outState.putInt(MEDIA_TIME , mediaPlayer.getCurrentPosition());
        }


        outState.putInt(IMAGE_INDEX , nextImage-1);
        outState.putString(SLIDESHOW_NAME,SlideshowName);

    }

    private Runnable updateSlideshow =new Runnable() {
        @Override
        public void run() {

            if (nextImage>=slideShowInfo.size()){
                if (mediaPlayer!=null&&mediaPlayer.isPlaying()){
                    mediaPlayer.reset();
                }
                getActivity().finish();
            }else{
                MediaItem item=slideShowInfo.getMediaIremAt(nextImage);
                if (item.getType()== MediaItem.MediaType.IMAGGE) {
                    imageView.setVisibility(View.VISIBLE);

                    videoView.setVisibility(View.INVISIBLE);
                    new LoadImageTask().execute(Uri.parse(item.getPath()));

                }else{
                    imageView.setVisibility(View.INVISIBLE);

                    videoView.setVisibility(View.VISIBLE);
                    playVideo(Uri.parse(item.getPath()));
                }

                ++nextImage;

            }
        }
    };


    class LoadImageTask extends AsyncTask<Uri , Object , Bitmap>{

        @Override
        protected Bitmap doInBackground(Uri... params) {
            return getBitmap(params[0] ,getActivity().getContentResolver() ,options);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            BitmapDrawable next=new BitmapDrawable(bitmap);
            next.setGravity(Gravity.CENTER);
            Drawable previous=imageView.getDrawable();

            if (previous instanceof TransitionDrawable){

                previous=((TransitionDrawable)previous).getDrawable(1);
            }

            if (previous==null){
                imageView.setImageDrawable(next);
            }else{
                Drawable[] drawables={previous , next};
                TransitionDrawable transitionDrawable=new TransitionDrawable(drawables);
                imageView.setImageDrawable(transitionDrawable);
                transitionDrawable.startTransition(1000);
            }





            handler.postDelayed(updateSlideshow,DURATION);
        }
    }

    Bitmap getBitmap(Uri uri,ContentResolver cr ,BitmapFactory.Options option){

        Bitmap bitmap=null;
        try {
            InputStream inputStream = cr.openInputStream(uri);
            bitmap=BitmapFactory.decodeStream(inputStream);
        }catch (FileNotFoundException e){
            Log.d("Viewing",e.getMessage());
        }

        return bitmap;
    }

    void playVideo(Uri videoUri){
        videoView.setVideoURI(videoUri);
        videoView.setMediaController(new MediaController(getActivity()));
        videoView.start();
    }
}
