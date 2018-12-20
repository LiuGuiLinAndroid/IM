package com.liuguilin.im.http;

import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.kymjs.rxvolley.client.HttpParams;
import com.liuguilin.im.entity.Constants;


/**
 * FileName: HttpHelper
 * Founder: LiuGuiLin
 * Create Date: 2018/12/20 16:42
 * Email: lgl@szokl.com.cn
 * Profile: Http
 */
public class HttpHelper {

    private static final String BASE_URL = "http://v.juhe.cn/toutiao/index?";

    public static void get(String url, HttpCallback callback) {
        RxVolley.get(url, callback);
    }

    public static void post(String url, HttpParams params, HttpCallback callback) {
        RxVolley.post(url, params, callback);
    }

    /**
     * 请求新闻
     * @param type
     * @param callback
     */
    public static void requestNews(String type, HttpCallback callback) {
        HttpParams httpParams = new HttpParams();
        httpParams.put("key", Constants.NEWS_KEY);
        httpParams.put("type", type);
        post(BASE_URL, httpParams, callback);
    }
}
