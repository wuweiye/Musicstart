package com.example.administrator.musicstart.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/6/10.
 */
public class Music implements Serializable{
    // [本地歌曲]歌曲id
    private long id;
    private String name;

    // 音乐标题
    private String title;
    // 专辑
    private String album;
    // 艺术家
    private String artist;
    // 音乐路径
    private String path;
    // 文件名
    private String fileName;



    // 文件大小
    private long fileSize;
    private String url;
    protected long duration;
    private long size;

    // 专辑封面路径
    private String coverPath;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public void setDuration(long duration) {
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

    public long getId() {
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

    public long getDuration() {
        return duration;
    }

    public String getCoverPath() {
        return coverPath;
    }

    public void setCoverPath(String coverPath) {
        this.coverPath = coverPath;
    }
}
