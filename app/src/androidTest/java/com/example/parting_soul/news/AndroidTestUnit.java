package com.example.parting_soul.news;

import android.test.AndroidTestCase;
import android.util.Log;

import com.example.parting_soul.news.bean.News;
import com.example.parting_soul.news.bean.NewsKinds;
import com.example.parting_soul.news.utils.CommonInfo;
import com.example.parting_soul.news.utils.LogUtils;
import com.example.parting_soul.news.utils.MD5Utils;
import com.example.parting_soul.news.utils.cache.DiskLruCacheHelper;
import com.example.parting_soul.news.utils.cache.database.DBManager;
import com.example.parting_soul.news.utils.cache.database.SQLiteDatabaseHelper;

import org.junit.Test;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
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

    @Test
    public void testMD5() {
        String[] strs = {
                "http://mini.eastday.com/mobile/161005161429505.html?qid=juheshuju",
                "http://mini.eastday.com/mobile/161005133918038.html?qid=juheshuju",
                "http://mini.eastday.com/mobile/161005132413174.html?qid=juheshuju",
                "http://mini.eastday.com/mobile/161005164957605.html?qid=juheshuju",
                "http://mini.eastday.com/mobile/161005064102539.html?qid=juheshuju",
                "http://mini.eastday.com/mobile/161005071801284.html?qid=juheshuju",
                "http://mini.eastday.com/mobile/161005142541020.html?qid=juheshuju"
        };
        for (int i = 0; i < strs.length; i++) {
            String key = MD5Utils.hashKeyForDisk(strs[i]);
            LogUtils.d(CommonInfo.TAG, "-->" + key);
        }
    }

    @Test
    public void testVersion() {
        int version = DiskLruCacheHelper.getVersion(getContext());
        LogUtils.d(CommonInfo.TAG, "-->" + version);
    }

    @Test
    public void testCacheSize() {
        double size = 22 * 1.0 / 1024 / 1024;
        DecimalFormat format = new DecimalFormat("#.00");
        LogUtils.d(CommonInfo.TAG, " -->" + Double.parseDouble(format.format(size)));
    }

    @Test
    public void testDatabase() {
        LogUtils.d(CommonInfo.TAG, "--->" + SQLiteDatabaseHelper.CREATE_NEWS_TABLE);
        DBManager manager = DBManager.getDBManager(getContext());
        List<News> lists = new ArrayList<News>();

        News news = new News();
        news.setTitle("南京一公交司机开车途中突发病 瘫倒前紧急停车");
        news.setAuthor_name("新浪");
        news.setPicPath("http://05.imgmini.eastday.com/mobile/20161009/20161009084016_8446ce474d6f7f3d7b503d085a82eb10_1_mwpm_03200403.jpeg");
        news.setUrl("http://mini.eastday.com/mobile/161009084016178.html?qid=juheshuju");
        news.setDate("2016-10-09 08:40");
        lists.add(news);

        news = new News();
        news.setTitle("南京一公交司机开车途中突发病 瘫倒前紧急停车222");
        news.setAuthor_name("新浪22");
        news.setPicPath("http://05.imgmini.eastday.com/mobile/20161009/20161009084016_8446ce474d6f7f3d7b503d085a82eb10_1_mwpm_03200403.jpeg");
        news.setUrl("http://mini.eastday.com/mobile/161009084016178.html?qid=juheshuju");
        news.setDate("2016-10-09 08:40");
        lists.add(news);

        news = new News();
        news.setTitle("南京一公交司机开车途中突发病 瘫倒前紧急停车33");
        news.setAuthor_name("新浪33");
        news.setPicPath("http://05.imgmini.eastday.com/mobile/20161009/20161009084016_8446ce474d6f7f3d7b503d085a82eb10_1_mwpm_03200403.jpeg");
        news.setUrl("http://mini.eastday.com/mobile/161009084016178.html?qid=juheshuju");
        news.setDate("2016-10-09 08:40");
        lists.add(news);

        news = new News();
        news.setTitle("南京一公交司机开车途中突发病 瘫倒前紧急停车44");
        news.setAuthor_name("新浪44");
        news.setPicPath("http://05.imgmini.eastday.com/mobile/20161009/20161009084016_8446ce474d6f7f3d7b503d085a82eb10_1_mwpm_03200403.jpeg");
        news.setUrl("http://mini.eastday.com/mobile/161009084016178.html?qid=juheshuju");
        news.setDate("2016-10-09 08:40");
        lists.add(news);

//              manager.addNewsCacheToDataBase(lists, "top");
//        List<News> l = manager.readNewsCacheFromDatabase("top");
//        for (int i = 0; i < l.size(); i++) {
//            News news1 = l.get(i);
//            LogUtils.d(CommonInfo.TAG, news1.getTitle() + " " + news1.getAuthor_name() + " " + news1.getPicPath() + " " + news1.getDate());
//        }
        manager.deleteNewsCacheFromDataBase("top");
    }

    @Test
    public void testDatabaseTest() {
        File file = getContext().getDatabasePath(SQLiteDatabaseHelper.DATABASE_NAME);
        File root = file.getParentFile();
        long le = root.length();
        boolean i = root.delete();
        LogUtils.d(CommonInfo.TAG, "--" + le + " " + i);
    }
}
