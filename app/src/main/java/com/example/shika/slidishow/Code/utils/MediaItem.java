package com.example.shika.slidishow.Code.utils;

import java.io.Serializable;

/**
 * Created by shika on 9/8/2015.
 */
public class MediaItem implements Serializable {

    private static final long SerialVerionUID=1L;
    public static enum MediaType{
        IMAGGE,
        VIDEO
    }


    MediaType Type;
    private final String Path;

    public MediaItem(MediaType mediaType , String location){

        Type=mediaType;
        Path=location;
    }

    public String getPath() {
        return Path;
    }

    public MediaType getType() {
        return Type;
    }
}
