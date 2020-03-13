package com.aiapp.aiapp.BaiduAi;

import com.aiapp.aiapp.HttpHelper.HttpHelper;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONObject;

/**
 * Created by wjy on 2019/11/12.
 */

public class AiRequestObject {
    private String strBaseurl="https://aip.baidubce.com";
    private String strTokenUrl = "/oauth/2.0/token";

    private String ImageAppKey = "spzBhP8ognC50XWZYuwGP8L9";
    private String ImageSecretKey = "vDDHaQus9SEXy8C7WAttL6t2OqQwrBCq";


    private String FaceAppKey = "AhfzkCxq0RdxzQpBCNULZkS5";
    private String FaceSecretKey = "sk5FTMXxICBotGDLnUNylkPMGR8BTcEZ";

    private String CharAppKey = "GTbPQx3LL52LkRDtAShmAq6c";
    private String CharSecretKey = "btVqOuTEIHvIHldj8afZCQICG9rBnKy3";

    private String strImageToken;
    private String strFaceToken;
    private String strCharToken;
    private static final AiRequestObject ourInstance = new AiRequestObject();

    public static AiRequestObject getInstance() {
        return ourInstance;
    }

    private AiRequestObject() {

    }
    public void GetToken()
    {
        GetImageToken();
        GetFaceToken();
        GetCharToken();
    }
    public String getStrImageToken() {
        return strImageToken;
    }

    public String getStrFaceToken() {
        return strFaceToken;
    }

    public String getStrCharToken() {
        return strCharToken;
    }

    private String GetImageToken()
    {
        String strParam = "?grant_type=client_credentials&client_id=" +
                ImageAppKey + "&client_secret=" + ImageSecretKey;
        String strTempUrl = strBaseurl+ strTokenUrl +strParam;
        HttpHelper.HttpAsyncGet(strTempUrl,null,new AsyncHttpResponseHandler(){
            String strOut;
            @Override
            public void onSuccess(int i, cz.msebera.android.httpclient.Header[] headers, byte[] bytes) {
                strOut = new String(bytes);
                String strToken1 = AnalysisTokenResult(strOut);
                strImageToken = strToken1;
            }

            @Override
            public void onFailure(int i, cz.msebera.android.httpclient.Header[] headers, byte[] bytes, Throwable throwable) {

            }
        });
        return strTempUrl;
    }
    private String AnalysisTokenResult(String strInResult)
    {
        String strRet = "";
        try{
            JSONObject jsonObject1 = new JSONObject(strInResult);
            strRet = jsonObject1.getString("access_token");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return strRet;
    }
    private String GetFaceToken()
    {
        String strParam = "?grant_type=client_credentials&client_id=" +
                FaceAppKey + "&client_secret=" + FaceSecretKey;
        String strTempUrl = strBaseurl+ strTokenUrl +strParam;
        HttpHelper.HttpAsyncGet(strTempUrl,null,new AsyncHttpResponseHandler(){
            String strOut;
            @Override
            public void onSuccess(int i, cz.msebera.android.httpclient.Header[] headers, byte[] bytes) {
                strOut = new String(bytes);
                String strToken1 = AnalysisTokenResult(strOut);
                strFaceToken = strToken1;
            }

            @Override
            public void onFailure(int i, cz.msebera.android.httpclient.Header[] headers, byte[] bytes, Throwable throwable) {

            }
        });
        return strTempUrl;
    }
    private String GetCharToken()
    {
        String strParam = "?grant_type=client_credentials&client_id=" +
                CharAppKey + "&client_secret=" + CharSecretKey;
        String strTempUrl = strBaseurl+ strTokenUrl +strParam;
        HttpHelper.HttpAsyncGet(strTempUrl,null,new AsyncHttpResponseHandler(){
            String strOut;
            @Override
            public void onSuccess(int i, cz.msebera.android.httpclient.Header[] headers, byte[] bytes) {
                strOut = new String(bytes);
                String strToken1 = AnalysisTokenResult(strOut);
                strCharToken = strToken1;
            }

            @Override
            public void onFailure(int i, cz.msebera.android.httpclient.Header[] headers, byte[] bytes, Throwable throwable) {
            }
        });
        return strTempUrl;
    }
}
