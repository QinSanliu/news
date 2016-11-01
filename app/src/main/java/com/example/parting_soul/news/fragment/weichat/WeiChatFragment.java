package com.example.parting_soul.news.fragment.weichat;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.parting_soul.news.R;

/**
 * Created by parting_soul on 2016/11/1.
 */

public class WeiChatFragment extends Fragment {
    public static final String NAME = "weichatfragment";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.weichat_fragment_layout, container, false);
        return view;
    }

}
