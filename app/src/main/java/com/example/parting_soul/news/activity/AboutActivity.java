package com.example.parting_soul.news.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.example.parting_soul.news.R;
import com.example.parting_soul.news.fragment.about.AboutFragment;
import com.example.parting_soul.news.utils.style.LanguageChangeManager;
import com.example.parting_soul.news.utils.style.ThemeChangeManager;

/**
 * Created by parting_soul on 2016/10/29.
 */

public class AboutActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageButton mBackView;
    private LinearLayout mNotificationHead;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeChangeManager.changeThemeMode(this);
        LanguageChangeManager.changeLanguage();
        setContentView(R.layout.activity_about);
        mNotificationHead = (LinearLayout) findViewById(R.id.notify);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            mNotificationHead.setVisibility(View.VISIBLE);
        }
        mBackView = (ImageButton) findViewById(R.id.back_forward);
        getFragmentManager().beginTransaction().add(R.id.about_fragment, new AboutFragment()).commit();
        mBackView.setOnClickListener(this);
    }

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, AboutActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        finish();
    }
}
