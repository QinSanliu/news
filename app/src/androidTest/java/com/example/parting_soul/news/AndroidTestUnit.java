package com.example.parting_soul.news;

import android.test.AndroidTestCase;
import android.util.Log;

import com.example.parting_soul.news.bean.NewsKinds;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by parting_soul on 2016/10/5.
 */

public class AndroidTestUnit extends AndroidTestCase {


    @Test
    public void testNewsTypeMap() {
        Map<String, String> map = NewsKinds.getNewsTypeMap();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            Log.d("MyTest", "MyTest: " + " key = " + entry.getKey() + " value = " + entry.getValue());
        }
    }

}
