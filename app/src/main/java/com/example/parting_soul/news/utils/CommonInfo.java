package com.example.parting_soul.news.utils;

/**
 * Created by parting_soul on 2016/10/5.
 * 可供所有类访问的一些公共信息
 */

public class CommonInfo {

    /**
     * 用于调试标志
     */
    public static final String TAG = "MyTest";

    /**
     * 默认的编码方式
     */
    public static final String ENCODE_TYPE = "utf-8";


    /**
     * 新闻api接口有关的信息
     */
    public class NewsAPI {

        /**
         * 接口api的有关请求参数，地址等
         */
        public class Params {
            /**
             * activity与fragment间通信的Bundle中的键<br>
             * 用于得到向网络请求的新闻类参数
             */
            public static final String REQUEST_TYPE_PARAM_NAME = "type_param_name";

            /**
             * 新闻接口地址
             */
            public static final String REQUEST_URL = "http://v.juhe.cn/toutiao/index";

            /**
             * 新闻接口地址参数名
             */
            public static final String REQUEST_URL_TYPR_NAME = "type";

            /**
             * 新闻接口地址参数名
             */
            public static final String REQUEST_URL_KEY_NAME = "key";

            /**
             * 新闻接口参数值
             */
            public static final String REQUEST_URL_KEY_VALUE = "9a21b88b1793ce50d8403ae0abfbbb90";
        }

        /**
         * 解析接口返回的json数据中所需要的键，以便获取相应的值
         */
        public class JSONKEY {

            /**
             * json数据返回状态码的名字
             */
            public static final String RESPONSE_JSON_ERROR_CODE_KEY_NAME = "error_code";

            /**
             * json数据返回结果的键
             */
            public static final String RESPONSE_JSON_RESULT_KEY_NAME = "result";

            /**
             * json数据数据集的键
             */
            public static final String RESPONSE_JSON_RESULT_DATA_KEY_NAME = "data";

            /**
             * 新闻的标题键
             */
            public static final String RESPONSE_JSON_RESULT_NEWS_TITLE = "title";

            /**
             * 新闻的日期键
             */
            public static final String RESPONSE_JSON_RESULT_NEWS_DATE = "date";

            /**
             * 新闻的作者键
             */
            public static final String RESPONSE_JSON_RESULT_NEWS_AUTHOR_NAME = "author_name";

            /**
             * 新闻图片地址键
             */
            public static final String RESPONSE_JSON_RESULT_NEWS_PICTURE_PATH = "thumbnail_pic_s";

            /**
             * 新闻地址键
             */
            public static final String RESPONSE_JSON_RESULT_NEWS_URL = "url";

            /**
             * 新闻唯一识别码键
             */
            public static final String RESPONSE_JSON_RESULT_NEWS_UNIQUE_KEY = "uniquekey";

            /**
             * 新闻真正的类型键
             */
            public static final String RESPONSE_JSON_RESULT_NEWS_REALTYPE = "realtype";
        }

        /**
         * 接口返回的状态码
         */
        public class ResponseCode {
            /**
             * json数据正常返回码
             */
            public static final int RESPONSE_JSON_NORMALL_CODE = 0;

            /**
             * 接口维护返回码
             */
            public static final int RESPONSE_JSON_API_INTERFACE_MAINTAIN = 10020;

            /**
             * 接口停用返回码
             */
            public static final int RESPONSE_JSON_API_INTERFACE_STOP = 10021;

            /**
             * 返回码大于200000,服务器发生错误
             */
            public static final int RESPONSE_JSON_SERVER_ERROR = 200000;
        }
    }

}
