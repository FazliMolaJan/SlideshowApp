package com.example.shika.slidishow.Code.ui;

import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.shika.slidishow.Code.adapters.SlideShowAdapter;
import com.example.shika.slidishow.Code.adapters.SlideshowEditorAdapter;
import com.example.shika.slidishow.Code.utils.MediaItem;
import com.example.shika.slidishow.Code.utils.SlideShowInfo;
import com.example.shika.slidishow.R;
import com.example.shika.slidishow.Code.fragments.FragmentEditor;


public class SlidingShowEditor extends ListActivity {

    SlideshowEditorAdapter slideshowEditorAdapter;
    SlideShowInfo slideShowInfo;
    public static final int PICTURE_ID = 1;

    public static final int Music_ID = 2;
    public static final int TAKE_ID=3;

    public static final int VIDEO_ID=4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_sliding_show_editor);
        String name=getIntent().getStringExtra(SlideShowAdapter.EXTER_NAME);

        slideShowInfo=MainActivity.getSlideinfo(name);



        Button done= (Button) findViewById(R.id.done);
        done.setOnClickListener(doneListener);


        Button add_pic= (Button) findViewById(R.id.add_pic);
        add_pic.setOnClickListener(addpicListener);


        Button addmic= (Button) findViewById(R.id.add_music);
        addmic.setOnClickListener(addmicListener);


        Button play= (Button) findViewById(R.id.play);
        play.setOnClickListener(playListener);


        Button Take= (Button) findViewById(R.id.tackpic);
        Take.setOnClickListener(TakeImage);

        Button add_Video= (Button) findViewById(R.id.addvid);
        add_Video.setOnClickListener(addVideo);

        slideshowEditorAdapter=new SlideshowEditorAdapter(SlidingShowEditor.this , slideShowInfo.getImageList());

        setListAdapter(slideshowEditorAdapter);

    }




    View.OnClickListener doneListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            finish();
        }
    };



    View.OnClickListener addpicListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Intent picture=new Intent(Intent.ACTION_GET_CONTENT);
            picture.setType("image/*");
            startActivityForResult(Intent.createChooser(picture, "choose your picture"), PICTURE_ID);

        }
    };


    View.OnClickListener addmicListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Intent music=new Intent(Intent.ACTION_GET_CONTENT);
            music.setType("audio/*");
            startActivityForResult(Intent.createChooser(music, "choose slide music"), Music_ID);
        }
    };

    View.OnClickListener playListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Intent playSlideshow=new Intent(SlidingShowEditor.this , VewingSliding.class);

            playSlideshow.putExtra(SlideShowAdapter.EXTER_NAME , slideShowInfo.getName());
            startActivity(playSlideshow);
        }
    };


    View.OnClickListener TakeImage=new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Intent take=new Intent(SlidingShowEditor.this ,PictureTracker.class);

            startActivityForResult(take, TAKE_ID);

        }
    };


    View.OnClickListener addVideo=new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Intent video=new Intent(Intent.ACTION_GET_CONTENT);
            video.setType("video/*");
            startActivityForResult(Intent.createChooser(video, "choose your video"), VIDEO_ID);

        }
    };
    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sliding_show_editor, menu);
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


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode==RESULT_OK) {
            Uri uri = data.getData();
            Toast.makeText(SlidingShowEditor.this, uri.toString(), Toast.LENGTH_LONG).show();
            if (requestCode == PICTURE_ID ||requestCode==TAKE_ID||requestCode==VIDEO_ID) {

                MediaItem.MediaType type =(requestCode==VIDEO_ID ? MediaItem.MediaType.VIDEO: MediaItem.MediaType.IMAGGE);

                        slideShowInfo.addPhoto(type,uri.toString());
                Toast.makeText(SlidingShowEditor.this, slideShowInfo.size() + "", Toast.LENGTH_LONG).show();
                slideshowEditorAdapter.notifyDataSetChanged();

            } else if (requestCode == Music_ID) {
                slideShowInfo.setMusicPath(uri.toString());
            }
        }




    }

}
