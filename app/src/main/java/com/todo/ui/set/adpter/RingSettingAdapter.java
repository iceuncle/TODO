package com.todo.ui.set.adpter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.todo.ui.set.RecorderFragment;
import com.todo.ui.set.MusicFragment;

/**
 * Created by tianyang on 2017/3/17.
 */
public class RingSettingAdapter extends FragmentPagerAdapter {

    final int PAGE_COUNT = 2;
    private String tabTitles[] = new String[]{"音乐", "录音"};
    private Context context;


    public RingSettingAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0)
            return MusicFragment.newInstance();
        else
            return RecorderFragment.newInstance();
    }


    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }

    private int mChildCount = 0;

    @Override
    public void notifyDataSetChanged() {
        // 重写这个方法，取到子Fragment的数量，用于下面的判断，以执行多少次刷新
        mChildCount = getCount();
        super.notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(Object object) {
        if (mChildCount > 0) {
            // 这里利用判断执行若干次不缓存，刷新
            mChildCount--;
            // 返回这个是强制ViewPager不缓存，每次滑动都刷新视图
            return POSITION_NONE;
        }
        // 这个则是缓存不刷新视图
        return super.getItemPosition(object);
    }


}
