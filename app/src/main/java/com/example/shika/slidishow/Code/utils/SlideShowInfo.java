package com.example.shika.slidishow.Code.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by shika on 9/5/2015.
 */
public class SlideShowInfo implements Serializable{


    public SlideShowInfo(String name){
        this.Name=name;
        imageList=new ArrayList<>();
        musicPath=null;
    }

    public List<MediaItem> getImageList() {
        return imageList;
    }

    public void setImageList(List<MediaItem> imageList) {
        this.imageList = imageList;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getMusicPath() {
        return musicPath;
    }


    public MediaItem getMediaIremAt(int index){
        if (index >=0 && index < imageList.size()){
            return imageList.get(index);
        }else{
            return null;
        }
    }


    public void setMusicPath(String musicPath) {
        this.musicPath = musicPath;
    }

    public void addPhoto(MediaItem.MediaType mediaType , String media){
        imageList.add(new MediaItem(mediaType , media));
    }

    public int size(){
        return imageList.size();
    }

    private List<MediaItem> imageList;
    private String Name;
    private String musicPath;


    private static final long SerialVerionUID=1L;
}
