package com.example.hw9.myfirstapp.utility;

/**
 * Created by Jiayi Lai on 2016-04-21.
 */
import com.loopj.android.http.*;

public class StockRestClient {
    private static final String BASE_URL = "http://stocksearchback.appspot.com/index.php";

    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }
}