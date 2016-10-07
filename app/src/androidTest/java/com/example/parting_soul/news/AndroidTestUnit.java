package com.example.parting_soul.news;

import android.test.AndroidTestCase;
import android.util.Log;

import com.example.parting_soul.news.bean.NewsKinds;
import com.example.parting_soul.news.utils.CommonInfo;
import com.example.parting_soul.news.utils.DiskLruCacheHelper;
import com.example.parting_soul.news.utils.LogUtils;
import com.example.parting_soul.news.utils.MD5Utils;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static com.example.parting_soul.news.utils.MD5Utils.hashKeyForDisk;

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

    @Test
    public void testMD5() {
        String key = MD5Utils.hashKeyForDisk("http://img.my.csdn.net/uploads/201309/01/1378037235_7476.jpg");
        LogUtils.d(CommonInfo.TAG, "-->" + key);
    }

    @Test
    public void testVersion() {
        int version = DiskLruCacheHelper.getVersion(getContext());
        LogUtils.d(CommonInfo.TAG, "-->" + version);
    }

}
