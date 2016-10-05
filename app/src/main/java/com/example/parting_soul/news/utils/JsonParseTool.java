package com.example.parting_soul.news.utils;

import android.text.Html;
import android.util.Log;

import com.example.parting_soul.news.bean.News;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by parting_soul on 2016/10/5.
 * json数据解析类
 */

public class JsonParseTool {
    /**
     * 将json数据解析为对应的新闻数组
     *
     * @param jsonString json数据
     * @return List<News> 新闻类的数组
     */
    public static List<News> parseJsonWidthJSONObject(String jsonString) {
        List<News> lists = new ArrayList<News>();
        try {
            //得到根json对象
            JSONObject root = new JSONObject(jsonString);
            //得到json对象返回的状态码
            int error_code = root.getInt(CommonInfo.NewsAPI.JSONKEY.RESPONSE_JSON_ERROR_CODE_KEY_NAME);
            if (checkResultCode(error_code)) {
                JSONObject result = root.getJSONObject(CommonInfo.NewsAPI.JSONKEY.RESPONSE_JSON_RESULT_KEY_NAME);
                //得到新闻数据数组的json对象数组
                JSONArray dataArray = result.getJSONArray(CommonInfo.NewsAPI.JSONKEY.RESPONSE_JSON_RESULT_DATA_KEY_NAME);
                //遍历每个json对象，并生成对应的新闻类
                for (int i = 0; i < dataArray.length(); i++) {
                    JSONObject newsJsonObject = dataArray.getJSONObject(i);
                    News news = new News();
                    //根据各json的键值得到对用的数据值
                    String title = newsJsonObject.getString(CommonInfo.NewsAPI.JSONKEY.RESPONSE_JSON_RESULT_NEWS_TITLE);
                    String date = newsJsonObject.getString(CommonInfo.NewsAPI.JSONKEY.RESPONSE_JSON_RESULT_NEWS_DATE);
                    String picPath = newsJsonObject.getString(CommonInfo.NewsAPI.JSONKEY.RESPONSE_JSON_RESULT_NEWS_PICTURE_PATH);
                    String url = newsJsonObject.getString(CommonInfo.NewsAPI.JSONKEY.RESPONSE_JSON_RESULT_NEWS_URL);
                    String author_name = newsJsonObject.getString(CommonInfo.NewsAPI.JSONKEY.RESPONSE_JSON_RESULT_NEWS_AUTHOR_NAME);
                    //                  String uniqueKey = newsJsonObject.getString(CommonInfo.NewsAPI.JSONKEY.RESPONSE_JSON_RESULT_NEWS_UNIQUE_KEY);
                    //                  String realType = newsJsonObject.getString(CommonInfo.NewsAPI.JSONKEY.RESPONSE_JSON_RESULT_NEWS_REALTYPE);

                    news.setTitle(title);
                    news.setDate(date);
                    news.setPicPath(picPath);
                    news.setUrl(url);
                    news.setAuthor_name(author_name);
                    //添加到数组中
                    lists.add(news);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return lists;
    }

    /**
     * 根据不同的返回码返回不同的状态
     *
     * @param error_code json数据中返回的状态码
     * @return boolean 返回正常为true
     */
    private static boolean checkResultCode(int error_code) {
        switch (error_code) {
            //返回成功
            case CommonInfo.NewsAPI.ResponseCode.RESPONSE_JSON_NORMALL_CODE:
                return true;
            //接口维护
            case CommonInfo.NewsAPI.ResponseCode.RESPONSE_JSON_API_INTERFACE_MAINTAIN:
                return false;
            //接口停止服务
            case CommonInfo.NewsAPI.ResponseCode.RESPONSE_JSON_API_INTERFACE_STOP:
                return false;
            //服务器异常
            case CommonInfo.NewsAPI.ResponseCode.RESPONSE_JSON_SERVER_ERROR:
                return false;
            default:
                return false;
        }
    }
}
