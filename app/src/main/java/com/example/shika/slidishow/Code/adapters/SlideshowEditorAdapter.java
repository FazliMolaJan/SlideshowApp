package com.example.shika.slidishow.Code.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;

import com.example.shika.slidishow.Code.utils.MediaItem;
import com.example.shika.slidishow.Code.utils.SlideShowInfo;
import com.example.shika.slidishow.R;

import java.util.List;

/**
 * Created by shika on 9/6/2015.
 */
public class SlideshowEditorAdapter extends ArrayAdapter<MediaItem> {
    List<MediaItem> imageUri;
    LayoutInflater layoutInflater;
    Context con;

    public SlideshowEditorAdapter(Context context,List<MediaItem> uris) {
        super(context, -1, uris);

        imageUri=uris;
        con=context;
        layoutInflater=LayoutInflater.from(context);

    }



    class ViewHolder{
        ImageView Image;
        Button DeleteButton;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView==null) {
            convertView = layoutInflater.inflate(R.layout.image_list_editor, parent, false);
            holder = new ViewHolder();

            holder.Image=(ImageView) convertView.findViewById(R.id.editor_image_item);
            holder.DeleteButton= (Button)  convertView.findViewById(R.id.delete);

            convertView.setTag(holder);
        }else{
            holder=(ViewHolder) convertView.getTag();
        }

        MediaItem uri=imageUri.get(position);
        new ThumbilLoadingImage().execute(holder.Image ,uri.getType() ,Uri.parse(uri.getPath()));

        holder.DeleteButton.setTag(uri);
        holder.DeleteButton.setOnClickListener(deleteListener);

        return convertView;
    }

    public class ThumbilLoadingImage extends AsyncTask<Object ,Object ,Bitmap> {
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

    View.OnClickListener deleteListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            imageUri.remove(v.getTag());
            remove((MediaItem) v.getTag());
            notifyDataSetChanged();
        }

    };
}
