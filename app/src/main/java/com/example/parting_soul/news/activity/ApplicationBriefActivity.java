package com.example.parting_soul.news.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.parting_soul.news.R;
import com.example.parting_soul.news.utils.style.LanguageChangeManager;
import com.example.parting_soul.news.utils.style.ThemeChangeManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by parting_soul on 2016/10/30.
 */

public class ApplicationBriefActivity extends AppCompatActivity {
    private TextView mContent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeChangeManager.changeThemeMode(this);
        LanguageChangeManager.changeLanguage();
        setContentView(R.layout.application_brief);
        mContent = (TextView) findViewById(R.id.application_content);
        mContent.setText(readTXTFileFromAssets(this));
    }

    /***
     * 从asserts目录读取文件
     *
     * @param context
     * @return
     */
    public static String readTXTFileFromAssets(Context context) {
        InputStream in = null;
        StringBuilder builder = new StringBuilder();
        try {
            in = context.getAssets().open("application.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String result = null;
            while ((result = reader.readLine()) != null) {
                builder.append(result);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    in = null;
                }
            }
        }
        return builder.toString();
    }

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, ApplicationBriefActivity.class);
        context.startActivity(intent);
    }
}
