package com.todo.ui.main.adpters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.todo.ui.main.MainFragment;

import java.util.ArrayList;
import java.util.List;


public class ViewPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragmentList = new ArrayList<>();

    final int PAGE_COUNT = 3;
    private String tabTitles[] = new String[]{"待办", "过期", "完成"};
    private Context context;


    public ViewPagerAdapter(FragmentManager fm, Context context, List<Fragment> fragmentList) {
        super(fm);
        this.context = context;
        this.fragmentList = fragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = fragmentList.get(position);
        if (fragment == null) {
            Log.d("qqq", "new fragment  " + position);
            fragment = MainFragment.newInstance(position);
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

//    private int mChildCount = 0;

//    @Override
//    public void notifyDataSetChanged() {
//        // 重写这个方法，取到子Fragment的数量，用于下面的判断，以执行多少次刷新
//        mChildCount = getCount();
//        super.notifyDataSetChanged();
//    }

//    @Override
//    public int getItemPosition(Object object) {
//        if (mChildCount > 0) {
//            // 这里利用判断执行若干次不缓存，刷新
//            mChildCount--;
//            // 返回这个是强制ViewPager不缓存，每次滑动都刷新视图
//            return POSITION_NONE;
//        }
//        // 这个则是缓存不刷新视图
//        return super.getItemPosition(object);
//    }


}