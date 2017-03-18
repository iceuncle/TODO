package com.todo.utils;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.todo.data.bean.Mp3Info;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tianyang on 2017/3/15.
 */
public class MediaUtil {
    /**
     * 用于从数据库中查询歌曲的信息，保存在List当中
     *
     * @return
     */
    public static List<Mp3Info> getMp3Infos(Context context) {
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null,
                MediaStore.Audio.Media.DEFAULT_SORT_ORDER);

        List<Mp3Info> mp3Infos = new ArrayList<Mp3Info>();

        if (cursor != null) {
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToNext();
                Mp3Info mp3Info = new Mp3Info();
                long id = cursor.getLong(cursor
                        .getColumnIndex(MediaStore.Audio.Media._ID)); // 音乐id
                String title = cursor.getString((cursor
                        .getColumnIndex(MediaStore.Audio.Media.TITLE))); // 音乐标题
                String artist = cursor.getString(cursor
                        .getColumnIndex(MediaStore.Audio.Media.ARTIST)); // 艺术家
                String album = cursor.getString(cursor
                        .getColumnIndex(MediaStore.Audio.Media.ALBUM)); // 专辑
                long albumId = cursor.getInt(cursor
                        .getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
                long duration = cursor.getLong(cursor
                        .getColumnIndex(MediaStore.Audio.Media.DURATION)); // 时长
                long size = cursor.getLong(cursor
                        .getColumnIndex(MediaStore.Audio.Media.SIZE)); // 文件大小
                String url = cursor.getString(cursor
                        .getColumnIndex(MediaStore.Audio.Media.DATA)); // 文件路径
                int isMusic = cursor.getInt(cursor
                        .getColumnIndex(MediaStore.Audio.Media.IS_MUSIC)); // 是否为音乐
                Uri contentUrl = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id);
                //.awa
                if (isMusic != 0 && url.endsWith(".mp3")) { // 只把音乐添加到集合当中
                    mp3Info.setId(id);
                    mp3Info.setTitle(title);
                    mp3Info.setArtist(artist);
                    mp3Info.setAlbum(album);
                    mp3Info.setAlbumId(albumId);
                    mp3Info.setDuration(duration);
                    mp3Info.setSize(size);
                    mp3Info.setUrl(url);
                    mp3Info.setContentUrl(contentUrl);
                    mp3Infos.add(mp3Info);
                }
            }
            cursor.close();
        }
        return mp3Infos;
    }


}
