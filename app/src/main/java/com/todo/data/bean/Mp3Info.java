package com.todo.data.bean;

import android.net.Uri;

import java.io.Serializable;

/**
 * Created by tianyang on 2017/3/15.
 */
public class Mp3Info implements Serializable {

    private long id;
    private String title;
    private String artist;
    private String album;
    private long albumId;
    private long duration;
    private long size;
    private String url;
    private Uri contentUrl;
    private boolean isCheck = false;

    public Mp3Info() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public long getAlbumId() {
        return albumId;
    }

    public void setAlbumId(long albumId) {
        this.albumId = albumId;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setContentUrl(Uri contentUrl) {
        this.contentUrl = contentUrl;
    }

    public Uri getContentUrl() {
        return contentUrl;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public boolean getIsCheck() {
        return isCheck;
    }


}
