package com.example.parting_soul.news.utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * Created by parting_soul on 2016/10/4.
 * Http工具类
 */

public class HttpUtils {
    /**
     * 采用http协议的Get方法从网络获取数据
     *
     * @param path http路径
     * @return byte[] 返回下载的字符数组
     */
    public static byte[] HttpGetMethod(String path) {
        byte[] result = null;
        HttpURLConnection conn = null;
        InputStream in = null;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            URL url = new URL(path);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
//            conn.setConnectTimeout(10 * 1000);
//            conn.setReadTimeout(10 * 1000);
            in = conn.getInputStream();

            byte[] buffer = new byte[1024];
            int len = -1;
            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
            result = out.toByteArray();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            conn.disconnect();
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    in = null;
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    out = null;
                }
            }
        }
        return result;
    }

    /**
     * 利用Http的Post方法获取资源
     *
     * @param path   网络地址
     * @param param  网络地址代封装中的参数
     * @param encode 请求的编码方式
     * @return String 返回字符串数据
     */
    public static String HttpPostMethod(String path, String param, String encode) {
        StringBuilder result = new StringBuilder();
        HttpURLConnection conn = null;
        BufferedReader reader = null;
        try {
            URL url = new URL(path);
            conn = (HttpURLConnection) url.openConnection();
            //设置可以写入输出流
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            //设置请求头
            conn.setRequestProperty("Accept-Charset", encode);
            conn.setRequestProperty("contentType", encode);
            conn.setConnectTimeout(10 * 1000);
            conn.setReadTimeout(10 * 1000);

            //将请求参数写入输出流
            PrintWriter writer = new PrintWriter(conn.getOutputStream());
            writer.print(param);
            writer.flush();
            writer.close();

            //从输入流读出数据
            reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), encode));
            String line = null;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            conn.disconnect();
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    reader = null;
                }
            }
        }
        return result.toString();
    }

}