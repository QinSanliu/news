package com.example.parting_soul.news.utils;

import android.util.Log;

import com.example.parting_soul.news.bean.News;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by parting_soul on 2016/10/5.
 */

public class JsonParseTool {
    public static List<News> parseJsonWidthJSONObject(String jsonString) {
        List<News> lists = new ArrayList<News>();
        try {
            JSONObject root = new JSONObject(jsonString);
            int error_code = root.getInt(CommonInfo.NewsAPI.JSONKEY.RESPONSE_JSON_ERROR_CODE_KEY_NAME);
            if (checkResultCode(error_code)) {
                JSONObject result = root.getJSONObject(CommonInfo.NewsAPI.JSONKEY.RESPONSE_JSON_RESULT_KEY_NAME);
                JSONArray dataArray = result.getJSONArray(CommonInfo.NewsAPI.JSONKEY.RESPONSE_JSON_RESULT_DATA_KEY_NAME);
                for (int i = 0; i < dataArray.length(); i++) {
                    JSONObject newsJsonObject = dataArray.getJSONObject(i);
                    News news = new News();
                    String title = newsJsonObject.getString(CommonInfo.NewsAPI.JSONKEY.RESPONSE_JSON_RESULT_NEWS_TITLE);
                    String date = newsJsonObject.getString(CommonInfo.NewsAPI.JSONKEY.RESPONSE_JSON_RESULT_NEWS_DATE);
                    String picPath = newsJsonObject.getString(CommonInfo.NewsAPI.JSONKEY.RESPONSE_JSON_RESULT_NEWS_PICTURE_PATH);
                    String url = newsJsonObject.getString(CommonInfo.NewsAPI.JSONKEY.RESPONSE_JSON_RESULT_NEWS_URL);
  //                  String uniqueKey = newsJsonObject.getString(CommonInfo.NewsAPI.JSONKEY.RESPONSE_JSON_RESULT_NEWS_UNIQUE_KEY);
  //                  String realType = newsJsonObject.getString(CommonInfo.NewsAPI.JSONKEY.RESPONSE_JSON_RESULT_NEWS_REALTYPE);
                    String author_name = newsJsonObject.getString(CommonInfo.NewsAPI.JSONKEY.RESPONSE_JSON_RESULT_NEWS_AUTHOR_NAME);
                    news.setTitle(title);
                    news.setDate(date);
                    news.setPicPath(picPath);
                    news.setUrl(url);
                    news.setAuthor_name(author_name);
                    lists.add(news);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return lists;
    }

    private static boolean checkResultCode(int error_code) {
        switch (error_code) {
            case CommonInfo.NewsAPI.ResponseCode.RESPONSE_JSON_NORMALL_CODE:
                return true;
            case CommonInfo.NewsAPI.ResponseCode.RESPONSE_JSON_API_INTERFACE_MAINTAIN:
                return false;
            case CommonInfo.NewsAPI.ResponseCode.RESPONSE_JSON_API_INTERFACE_STOP:
                return false;
            case CommonInfo.NewsAPI.ResponseCode.RESPONSE_JSON_SERVER_ERROR:
                return false;
            default:
                return false;
        }
    }
}
