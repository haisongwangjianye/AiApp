package com.aiapp.aiapp.HttpHelper;

import android.content.Context;

import com.aiapp.aiapp.PublicFuc.StreamTool;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import cz.msebera.android.httpclient.entity.ByteArrayEntity;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;

/**
 * Created by wjy on 2018/11/27.
 */

public class HttpHelper {
    private static AsyncHttpClient AsyncClient = new AsyncHttpClient();
    public static void HttpAsyncGet(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        AsyncClient.setConnectTimeout(1000*10);
        AsyncClient.setResponseTimeout(1000*5);
        AsyncClient.get(url, params, responseHandler);
    }

    public static void HttpAsyncPost(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        AsyncClient.setConnectTimeout(1000*10);
        AsyncClient.setResponseTimeout(1000*5);
        AsyncClient.post(url, params, responseHandler);
    }
    public static void HttpAsyncPostEx(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        AsyncClient.setConnectTimeout(1000*10);
        AsyncClient.setResponseTimeout(1000*5);
        AsyncClient.addHeader("Content-Type","application/x-www-form-urlencoded");
        AsyncClient.post(url, params, responseHandler);
    }
    public static void HttpAsyncPostJson(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        AsyncClient.setConnectTimeout(1000*10);
        AsyncClient.setResponseTimeout(1000*5);
        AsyncClient.addHeader("Content-Type","application/json");
        AsyncClient.post(url, params, responseHandler);
    }
     public static  void HttpAysncPostJsonEx(String url, RequestParams params, AsyncHttpResponseHandler responseHandler)
     {
         AsyncClient.setConnectTimeout(1000*10);
         AsyncClient.setResponseTimeout(1000*5);
         AsyncClient.addHeader("Content-Type","application/json");
         AsyncClient.post(url, params, responseHandler);
     }
    public static  void HttpAysncPostJsonEx2(Context mContext,String url, String strPostJsonData, AsyncHttpResponseHandler responseHandler)
    {
        AsyncClient.setConnectTimeout(1000*10);
        AsyncClient.setResponseTimeout(1000*5);
        ByteArrayEntity entity = null;
        try {
            entity = new ByteArrayEntity(strPostJsonData.getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        AsyncClient.post(mContext,url,entity,"application/json",responseHandler);
    }

}
