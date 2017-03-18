package com.todo.ui.set;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.markzhai.recyclerview.BaseViewAdapter;
import com.github.markzhai.recyclerview.SingleTypeAdapter;
import com.loonggg.lib.alarmmanager.clock.SPUtils;
import com.todo.R;
import com.todo.data.bean.Mp3Info;
import com.todo.utils.MediaUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tianyang on 2017/3/17.
 */
public class MusicFragment extends Fragment {
    private List<Mp3Info> mp3InfoList = new ArrayList<>();
    private RecyclerView recyclerView;
    private View mView;
    private SingleTypeAdapter<Mp3Info> mAdapter;
    private MediaPlayer mMediaPlayer;

    public static MusicFragment newInstance() {
        MusicFragment fragment = new MusicFragment();
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_music, container, false);
        initView();
        initDatas();
        return mView;

    }

    private void initView() {
        recyclerView = (RecyclerView) mView.findViewById(R.id.recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new SingleTypeAdapter<>(getActivity(), R.layout.item_music);
        mAdapter.setPresenter(new ItemPresenter());
        recyclerView.setAdapter(mAdapter);
    }

    private void initDatas() {
        mp3InfoList = MediaUtil.getMp3Infos(getActivity());
        String type = (String) SPUtils.get(getActivity(), SPUtils.RING_TYPE_KEY, "");
        String contentUri = (String) SPUtils.get(getActivity(), SPUtils.MUSIC_NAME_KEY, "");
        if (type != null && type.equals(SPUtils.MUSIC_NAME_KEY) && contentUri != null)
            for (Mp3Info mp3Info : mp3InfoList) {
                if (contentUri.equals(mp3Info.getContentUrl().toString()))
                    mp3Info.setCheck(true);
            }
        mAdapter.addAll(mp3InfoList);
    }

    public class ItemPresenter implements BaseViewAdapter.Presenter {

        public void onItemClick(Mp3Info mp3Info) {
            for (Mp3Info bean : mp3InfoList)
                bean.setCheck(false);
            mp3Info.setCheck(true);
            mAdapter.set(mp3InfoList);
            if (mMediaPlayer != null) {
                mMediaPlayer.release();
                mMediaPlayer = null;
            }
            playMusic(mp3Info);
            SPUtils.put(getActivity(), SPUtils.RING_TYPE_KEY, SPUtils.MUSIC_NAME_KEY);
            SPUtils.put(getActivity(), SPUtils.MUSIC_NAME_KEY, mp3Info.getContentUrl().toString());
        }

    }

    private void playMusic(Mp3Info mp3Info) {
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mMediaPlayer.setDataSource(getActivity(), mp3Info.getContentUrl());
            mMediaPlayer.prepare(); // might take long! (for buffering, etc)
            mMediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mMediaPlayer != null)
            mMediaPlayer.release();
        mMediaPlayer = null;
    }

}
