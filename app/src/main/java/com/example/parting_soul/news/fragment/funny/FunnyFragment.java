package com.example.parting_soul.news.fragment.funny;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.parting_soul.news.R;
import com.example.parting_soul.news.activity.MainActivity;

/**
 * Created by parting_soul on 2016/11/1.
 */

public class FunnyFragment extends Fragment {
    public static final String NAME = "funnyFragment";


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MainActivity) getActivity()).setTitleName(R.string.funny);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.funny_fragment_layout, container, false);
        return view;
    }
}
