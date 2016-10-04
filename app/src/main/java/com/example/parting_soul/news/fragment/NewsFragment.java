package com.example.parting_soul.news.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.parting_soul.news.R;



/**
 * Created by parting_soul on 2016/10/4.
 * 新闻碎片类
 */

public class NewsFragment extends Fragment {
    private String msg;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        msg = getArguments().getString("msg");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.news_fragment,container,false);
        TextView textView = (TextView) view.findViewById(R.id.textView);
        textView.setText(msg);
        return view;
    }

}
