package com.example.shika.slidishow.Code.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.shika.slidishow.Code.adapters.SlideShowAdapter;
import com.example.shika.slidishow.Code.adapters.SlideshowEditorAdapter;
import com.example.shika.slidishow.Code.ui.MainActivity;
import com.example.shika.slidishow.Code.ui.VewingSliding;
import com.example.shika.slidishow.Code.utils.SlideShowInfo;
import com.example.shika.slidishow.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class FragmentEditor extends ListFragment {


    SlideshowEditorAdapter slideshowEditorAdapter;
    SlideShowInfo slideShowInfo;
    public static final int PICTURE_ID = 1;

    public static final int Music_ID = 2;
    public FragmentEditor() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_sliding_show_editor, container, false);

        String name=getActivity().getIntent().getStringExtra(SlideShowAdapter.EXTER_NAME);

        slideShowInfo=MainActivity.getSlideinfo(name);



        Button done= (Button) rootView.findViewById(R.id.done);
        done.setOnClickListener(doneListener);


        Button add_pic= (Button) rootView.findViewById(R.id.add_pic);
        add_pic.setOnClickListener(addpicListener);


        Button addmic= (Button) rootView.findViewById(R.id.add_music);
        addmic.setOnClickListener(addmicListener);


        Button play= (Button) rootView.findViewById(R.id.play);
        play.setOnClickListener(playListener);


        slideshowEditorAdapter=new SlideshowEditorAdapter(getActivity() , slideShowInfo.getImageList());
        slideshowEditorAdapter.setNotifyOnChange(true);
        setListAdapter(slideshowEditorAdapter);
        return rootView;
    }

    View.OnClickListener doneListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            getActivity().finish();
        }
    };



    View.OnClickListener addpicListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Intent picture=new Intent(Intent.ACTION_GET_CONTENT);
            picture.setType("image/*");
            getActivity().startActivityForResult(Intent.createChooser(picture , "choose your picture"),PICTURE_ID);

        }
    };


    View.OnClickListener addmicListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Intent music=new Intent(Intent.ACTION_GET_CONTENT);
            music.setType("audio/*");
            getActivity().startActivityForResult(Intent.createChooser(music , "choose slide music"),Music_ID);
        }
    };

    View.OnClickListener playListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Intent playSlideshow=new Intent(getActivity() , VewingSliding.class);

            playSlideshow.putExtra(SlideShowAdapter.EXTER_NAME , slideShowInfo.getName());
            getActivity().startActivity(playSlideshow);
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        Toast.makeText(getActivity() , slideShowInfo.size()+"" ,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {


            Uri uri = data.getData();
            Toast.makeText(getActivity(), uri.toString(), Toast.LENGTH_LONG).show();
            if (requestCode == PICTURE_ID) {
              //  slideShowInfo.addPhoto(uri.toString());
                Toast.makeText(getActivity(), slideShowInfo.size() + "", Toast.LENGTH_LONG).show();
                slideshowEditorAdapter.notifyDataSetChanged();

            } else if (requestCode == Music_ID) {
                slideShowInfo.setMusicPath(uri.toString());
            }






    }
}
