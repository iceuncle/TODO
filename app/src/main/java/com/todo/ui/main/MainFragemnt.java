package com.todo.ui.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.todo.R;

/**
 * Created by tianyang on 2017/2/14.
 */
public class MainFragemnt extends Fragment {
    private static final String ARG_POSITION = "position";
    private int position;
    private TextView textView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, null);

        textView = (TextView) view.findViewById(R.id.text);
        textView.setText("这是第"+position+"个fragment");
        return view;

    }

    public static MainFragemnt newInstance(int position) {
        MainFragemnt mainFragemnt = new MainFragemnt();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        mainFragemnt.setArguments(b);
        return mainFragemnt;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = getArguments().getInt(ARG_POSITION);
    }

}
