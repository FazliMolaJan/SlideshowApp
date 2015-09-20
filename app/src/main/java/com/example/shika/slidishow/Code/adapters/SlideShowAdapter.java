package com.example.shika.slidishow.Code.adapters;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.shika.slidishow.Code.ui.MainActivity;
import com.example.shika.slidishow.Code.ui.VewingSliding;
import com.example.shika.slidishow.Code.utils.MediaItem;
import com.example.shika.slidishow.Code.utils.SlideShowInfo;
import com.example.shika.slidishow.R;

import java.util.List;

/**
 * Created by shika on 9/6/2015.
 */
public class SlideShowAdapter extends ArrayAdapter<SlideShowInfo>{
    LayoutInflater layoutInflater;
    List<SlideShowInfo> mInfolist;
    Context con;
    callback call;

    public static final String EXTER_NAME="Name";

    public SlideShowAdapter(Context context, List<SlideShowInfo> infoList) {
        super(context, -1, infoList);
        layoutInflater=LayoutInflater.from(context);
        con=context;
        mInfolist=infoList;
    }


    public void setcallbackListener(callback mcall){
        call=mcall;
    }
    public class ViewHolder{
        public TextView name;
        public Button  play;
        public Button  edit;
        public Button  delete;
        public ImageView imageSlide;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView==null){
            convertView=layoutInflater.inflate(R.layout.slidingshow_list_item,parent,false);
            holder=new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.slidingName);
            holder.imageSlide = (ImageView) convertView.findViewById(R.id.slidingImage);
            holder.play = (Button) convertView.findViewById(R.id.play);
            holder.edit = (Button) convertView.findViewById(R.id.edit);
            holder.delete = (Button) convertView.findViewById(R.id.delete);
            convertView.setTag(holder);
        }else{
            holder= (ViewHolder) convertView.getTag();
        }
        SlideShowInfo info=mInfolist.get(position);
        holder.name.setText(info.getName());

        if (info.size() > 0){
            MediaItem item = info.getMediaIremAt(0);


            new ThumbilLoadingImage().execute(holder.imageSlide ,item.getType(), Uri.parse(item.getPath()));
        }

        holder.play.setTag(info);
        holder.play.setOnClickListener(playListener);


        holder.edit.setTag(info);
        holder.edit.setOnClickListener(editListener);


        holder.delete.setTag(info);
        holder.delete.setOnClickListener(deleteListener);

        return convertView;
    }










    public class ThumbilLoadingImage extends AsyncTask<Object ,Object ,Bitmap>{
        ImageView imageView;

        @Override
        protected Bitmap doInBackground(Object... params) {
            imageView = (ImageView) params[0];



            return SlideShowAdapter.LoadThumbil((MediaItem.MediaType) params[1],(Uri)params[2] ,
                    con.getContentResolver() , new BitmapFactory.Options());
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            imageView.setImageBitmap(bitmap);
        }
    }





    public static Bitmap LoadThumbil(MediaItem.MediaType type,Uri uri , ContentResolver contentResolver , BitmapFactory.Options options){

        Bitmap bitmap=null;
        int id = Integer.parseInt(uri.getLastPathSegment());
        if (type== MediaItem.MediaType.IMAGGE) {
            bitmap = MediaStore.Images.Thumbnails.getThumbnail(contentResolver, id, MediaStore.Images.Thumbnails.MICRO_KIND
                    , options);
        }else if (type== MediaItem.MediaType.VIDEO){

            bitmap = MediaStore.Video.Thumbnails.getThumbnail(contentResolver, id, MediaStore.Images.Thumbnails.MICRO_KIND
                    , options);
        }

        return bitmap;
    }






    View.OnClickListener playListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent=new Intent(con , VewingSliding.class);
            intent.putExtra(EXTER_NAME ,((SlideShowInfo) v.getTag()).getName() );

            con.startActivity(intent);
        }
    };



    View.OnClickListener editListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (call != null){
                call.editCallBack(v);
            }


        }
    };



    View.OnClickListener deleteListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
           // mInfolist.remove((SlideShowInfo) v.getTag());
            //notifyDataSetChanged();

            if (call != null){
                call.refershAfterDelete(v);
            }


        }

    };

    public interface callback{
        public void editCallBack(View v);
        public void refershAfterDelete(View v);
    }
}
