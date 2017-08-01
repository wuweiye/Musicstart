package com.example.administrator.musicstart.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/6/10.
 */
public class Music implements Serializable{
    private int id;
    private String name;
    private String album;
    private String artist;
    private String url;
    protected int duration;
    private long size;

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getName() {

        return name;
    }

    public int getId() {
        return id;
    }

    public String getArtist() {
        return artist;
    }

    public String getAlbum() {
        return album;
    }

    public String getUrl() {
        return url;
    }

    public long getSize() {
        return size;
    }

    public int getDuration() {
        return duration;
    }
}
