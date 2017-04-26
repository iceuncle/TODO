package com.todo.ui.set;

import android.databinding.DataBindingUtil;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.databinding.library.baseAdapters.BR;
import com.loonggg.lib.alarmmanager.clock.SPUtils;
import com.todo.R;
import com.todo.data.bean.Mp3Info;
import com.todo.databinding.FragmentMusicBinding;
import com.todo.ui.base.BaseFragment;
import com.todo.ui.base.EmptyState;
import com.todo.ui.base.StateModel;
import com.todo.utils.IsEmpty;
import com.todo.utils.LogUtil;
import com.todo.utils.MediaUtil;
import com.todo.vendor.recyleradapter.BaseViewAdapter;
import com.todo.vendor.recyleradapter.SingleTypeAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tianyang on 2017/3/17.
 */
public class MusicFragment extends BaseFragment {
    private List<Mp3Info> mp3InfoList = new ArrayList<>();
    private SingleTypeAdapter<Mp3Info> mAdapter;
    private MediaPlayer mMediaPlayer;
    // 标志位，标志已经初始化完成。
    private boolean isPrepared;
    private FragmentMusicBinding mBinding;

    private StateModel stateModel;

    public static MusicFragment newInstance() {
        MusicFragment fragment = new MusicFragment();
        return fragment;
    }


    @Override
    protected void lazyLoad() {
        if (!isPrepared || !isVisible) {
            return;
        }
        initDatas();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_music, container, false);
        LogUtil.d("MusicFragment  onCreateView...");
        initView();
        isPrepared = true;
        return mBinding.getRoot();
    }


    private void initView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mBinding.recyclerview.setLayoutManager(layoutManager);
        mAdapter = new SingleTypeAdapter<>(getActivity(), R.layout.item_music);
        mAdapter.setPresenter(new ItemPresenter());
        mBinding.recyclerview.setAdapter(mAdapter);

        if (stateModel == null)
            stateModel = new StateModel();
        mBinding.setVariable(BR.stateModel, stateModel);
    }

    private void initDatas() {
        LogUtil.d("MusicFragment  initDatas...");
        mp3InfoList = MediaUtil.getMp3Infos(getActivity());
        if (IsEmpty.list(mp3InfoList)) {
            stateModel.setEmptyState(EmptyState.EMPTY_MUSIC);
            return;
        }
        stateModel.setEmptyState(EmptyState.NORMAL);
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
    protected void onInvisible() {
        super.onInvisible();
        LogUtil.d("music   onInvisible...");
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }


    @Override
    public void onStop() {
        super.onStop();
        LogUtil.d("music   stop...");
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }


}
