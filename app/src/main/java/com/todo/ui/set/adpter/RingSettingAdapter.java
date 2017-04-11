package com.todo.ui.set.adpter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.todo.ui.set.MusicFragment;
import com.todo.ui.set.RecorderFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tianyang on 2017/3/17.
 */
public class RingSettingAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragmentList = new ArrayList<>();

    final int PAGE_COUNT = 2;
    private String tabTitles[] = new String[]{"音乐", "录音"};
    private Context context;


    public RingSettingAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
        fragmentList.add(null);
        fragmentList.add(null);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = fragmentList.get(position);
        if (fragment == null) {
            if (position == 0)
                fragment = MusicFragment.newInstance();
            else
                fragment = RecorderFragment.newInstance();
            fragmentList.set(position, fragment);
        }
        return fragment;
    }


    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }


}
