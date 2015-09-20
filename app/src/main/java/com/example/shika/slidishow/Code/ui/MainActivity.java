package com.example.shika.slidishow.Code.ui;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.shika.slidishow.Code.adapters.SlideShowAdapter;
import com.example.shika.slidishow.Code.utils.SlideShowInfo;
import com.example.shika.slidishow.R;
import com.example.shika.slidishow.Code.fragments.FragmentMain;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class MainActivity extends ListActivity implements SlideShowAdapter.callback {
    SlideShowAdapter adapter;
    static List<SlideShowInfo> infoList;
    File fileSlideshow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_main);

        fileSlideshow=new File(getExternalFilesDir(null).getAbsolutePath()+"/EnhancedSlideshowData.ser");





        new LoadSlideshowTask().execute((Object[])null);

    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        new SaveSlideshowTask().execute((Object[])null);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void editCallBack(View v) {
        Intent i=new Intent(MainActivity.this, SlidingShowEditor.class);
        i.putExtra(SlideShowAdapter.EXTER_NAME , ((SlideShowInfo) v.getTag()).getName() );

        startActivityForResult(i, 0);
    }

    @Override
    public void refershAfterDelete(View v) {
        infoList.remove((SlideShowInfo) v.getTag());
        new SaveSlideshowTask().execute((Object[])null);
        adapter.notifyDataSetChanged();

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.



        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.add_sliding) {

            LayoutInflater layoutInflater=(LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view=layoutInflater.inflate(R.layout.item_name, null);
            final EditText editText= (EditText) view.findViewById(R.id.slidingName);
            AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Enter Unique Name Slide Show ");
            builder.setView(view);
            builder.setPositiveButton("Set Name" , new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    String name=editText.getText().toString().trim();
                    if (name.length()>0){
                        infoList.add(new SlideShowInfo(name));
                        Intent intent=new Intent(MainActivity.this , SlidingShowEditor.class);

                        intent.putExtra(SlideShowAdapter.EXTER_NAME ,name );
                        startActivityForResult(intent, 0);

                    }else{
                        Toast.makeText(MainActivity.this, "please Enter Slide Name", Toast.LENGTH_LONG).show();

                    }
                }
            });

            builder.setNegativeButton("Cancel" , null);
            builder.show();


            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static SlideShowInfo getSlideinfo(String Name){
        for (SlideShowInfo info : infoList){
            if (info.getName().equals(Name)){
                return info;
            }
        }
        return null;
    }
    class LoadSlideshowTask extends AsyncTask<Object,Object,Object>{

        @Override
        protected Object doInBackground(Object... params) {

            if (fileSlideshow.exists()) {
                try {


                    ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(fileSlideshow));
                    infoList= (List<SlideShowInfo>) objectInputStream.readObject();


                } catch (final Exception e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, e.getMessage() ,Toast.LENGTH_LONG).show();
                        }
                    });

                }
            }

            if (infoList==null){
                infoList=new ArrayList<>();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            adapter=new SlideShowAdapter(MainActivity.this , infoList);
            setListAdapter(adapter);
            adapter.setcallbackListener(MainActivity.this);
        }
    }


    public  class  SaveSlideshowTask extends AsyncTask<Object , Object , Object>{

        @Override
        protected Object doInBackground(Object... params) {
            try {


                if (!fileSlideshow.exists()) {
                     fileSlideshow.createNewFile();
                }
                ObjectOutputStream objectOutputStream=new ObjectOutputStream(new FileOutputStream(fileSlideshow));
                objectOutputStream.writeObject(infoList);
                objectOutputStream.flush();
                objectOutputStream.close();

            }catch (final Exception e){

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this , e.getMessage() ,Toast.LENGTH_LONG).show();
                    }
                });
            }
            return null;
        }
    }


}
