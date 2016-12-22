package com.example.yang.test.bean;

/**
 * Created by Administrator on 2016/12/7.
 */

public class MusicBean {

    String title;
    String artist;
    String data;

    public MusicBean(String title,String artist,String data){
        this.title = title;
        this.artist = artist;
        this.data = data;
    }

    public String getArtist() {
        return artist;
    }

    public String getData() {
        return data;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return "MusicBean{" +
                "artist='" + artist + '\'' +
                ", title='" + title + '\'' +
                ", data='" + data + '\'' +
                '}';
    }
}
