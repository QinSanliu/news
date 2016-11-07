package com.example.parting_soul.news.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.parting_soul.news.R;
import com.example.parting_soul.news.bean.Joke;
import com.example.parting_soul.news.utils.style.FontChangeManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by parting_soul on 2016/11/5.
 */

public class JokeFragmentAdapter extends BaseFragmentAdapter<Joke> {

    private Context mContext;

    private int textStyleId;

    private Map<Integer, Joke> map;

    public JokeFragmentAdapter(Context context, List<Joke> data) {
        mContext = context;
        this.mLists = data;
        textStyleId = FontChangeManager.changeJokeFontSize();
        map = new HashMap<Integer, Joke>();
    }

    @Override
    public void getPicUrl() {

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = null;
        JokeHolder holder = null;
        if (convertView == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.funny_listview_item, null);
            holder = new JokeHolder();
            holder.content = (TextView) view.findViewById(R.id.content);
            holder.collection = (CheckBox) view.findViewById(R.id.is_collectedBox);
            holder.date = (TextView) view.findViewById(R.id.update_date);
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (JokeHolder) view.getTag();
        }
        holder.content.setText(mLists.get(position).getContent());
        holder.date.setText(mLists.get(position).getDate());
        holder.content.setTextAppearance(mContext, textStyleId);
//        holder.collection.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (position >= map.size()) {
//                    holder.collection.setChecked(mLists.get(position).is_collected());
//                    map.put(position, mLists.get(position));
//                } else {
//                    for (int i = 0; i < map.size(); i++) {
//                        map.get()
//                    }
//                }
//            }
//        });
        holder.collection.setChecked(mLists.get(position).is_collected());
        return view;
    }

    class JokeHolder {
        TextView content;
        TextView date;
        CheckBox collection;
    }

}
