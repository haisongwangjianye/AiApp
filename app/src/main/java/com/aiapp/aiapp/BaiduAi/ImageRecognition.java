package com.aiapp.aiapp.BaiduAi;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.aiapp.aiapp.HttpHelper.HttpHelper;
import com.aiapp.aiapp.PublicFuc.PublicFuc;
import com.aiapp.aiapp.tts.TtsHelper;
import com.aiapp.aiapp.tts.util.FileUtil;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;

import static com.loopj.android.http.AsyncHttpClient.log;

/**
 * Created by wjy on 2019/11/7.
 */

public class ImageRecognition {
    private String strResult;
    private String strBaseurl="https://aip.baidubce.com";

    //通用物体
    private String strAdvanced_generalUrl="/rest/2.0/image-classify/v2/advanced_general";
    //菜品
    private String strDishUrl = "/rest/2.0/image-classify/v2/dish";
    //动物
    private String strAnimalUrl = "/rest/2.0/image-classify/v1/animal";
    //植物
    private String strPlantUrl = "/rest/2.0/image-classify/v1/plant";
    //果蔬类食材
    private String strIngredientUrl = "/rest/2.0/image-classify/v1/classify/ingredient";
    //地标
    private String strLandmarkdientUrl = "/rest/2.0/image-classify/v1/landmark";

    protected String appId = "17763711";
    protected String appKey = "spzBhP8ognC50XWZYuwGP8L9";
    protected String secretKey = "vDDHaQus9SEXy8C7WAttL6t2OqQwrBCq";

    private String strToken;
    private String strReturn ="not return";
    private TextView resultShowTextView;
    public void SetTextView(TextView tempTextView)
    {
        resultShowTextView =tempTextView;
    }
    public ImageRecognition() {
        strToken = AiRequestObject.getInstance().getStrImageToken();
    }

    //通用物体
    public String GetAdvanced_general(String strPath)
    {
        String strBase64 = PublicFuc.GetImageBase64String(strPath,320);
        RequestParams params=new RequestParams();
        params.add("access_token",strToken);
        params.add("image",strBase64);
        RequestBaidu(strAdvanced_generalUrl,params,"Advanced_general");
        return "";
    }
    //菜品
    public String GetDish(String strPath)
    {
        String strBase64 = PublicFuc.GetImageBase64String(strPath,320);
        RequestParams params=new RequestParams();
        params.add("access_token",strToken);
        params.add("image",strBase64);
        params.put("baike_num",1);
        RequestBaidu(strDishUrl,params,"Dish");
        return "";
    }
    //动物
    public String GetAnimal(String strPath)
    {
        String strBase64 = PublicFuc.GetImageBase64String(strPath,320);
        RequestParams params=new RequestParams();
        params.add("access_token",strToken);
        params.add("image",strBase64);
        params.put("baike_num",1);
        RequestBaidu(strAnimalUrl,params,"Animal");
        return "";
    }
    private String AnalysisAnimalResult(String strInResult)
    {
        strResult = "";
        strResult = strInResult;
        String strRet = "";
        try{
            JSONObject jsonObject1 = new JSONObject(strInResult);
            JSONArray jsonArray = jsonObject1.getJSONArray("result");
           // resultShowTextView.append("识别结果如下:\r\n");
            for(int i = 0;i < jsonArray.length();i++){
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);

                String strScore = jsonObject.getString("score");
                String strName = jsonObject.getString("name");

               // String strShow = "名称:"+strName +" 评分:" +strScore + "\r\n";
                resultShowTextView.append(strName);
                TtsHelper.getInstance().speak("识别结果:"+strName);
                break;

            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return strRet;
    }
    //植物
    public String GetPlant(String strPath)
    {
        String strBase64 = PublicFuc.GetImageBase64String(strPath,320);
        RequestParams params=new RequestParams();
        params.add("access_token",strToken);
        params.add("image",strBase64);
        params.put("baike_num",1);
        RequestBaidu(strPlantUrl,params,"Plant");
        return "";
    }
    //果蔬类食材
    public String GetIngredient(String strPath)
    {
        String strBase64 = PublicFuc.GetImageBase64String(strPath,320);
        RequestParams params=new RequestParams();
        params.add("access_token",strToken);
        params.add("image",strBase64);
        RequestBaidu(strIngredientUrl,params,"Ingredient");
        return "";
    }
    //地标
    public String GetLandmarkdient(String strPath)
    {
        String strBase64 = PublicFuc.GetImageBase64String(strPath,320);
        RequestParams params=new RequestParams();
        params.add("access_token",strToken);
        params.add("image",strBase64);
        RequestBaidu(strLandmarkdientUrl,params,"Landmark");
        return "";
    }

    private void RequestBaidu(String strUrl, RequestParams params, final String strType)
    {
        TtsHelper.getInstance().speak("正在识别，请稍等");
        String strTempUrl = strBaseurl+ strUrl;
        HttpHelper.HttpAsyncPostEx(strTempUrl,params,new AsyncHttpResponseHandler(){
            String strOut;
            @Override
            public void onSuccess(int i, cz.msebera.android.httpclient.Header[] headers, byte[] bytes) {
                strOut = new String(bytes);
                if(strType.equals("Advanced_general"))
                {
                    AnalysisAdvanced_generalResult(strOut);
                }
                else if(strType.equals("Landmark"))
                {
                    AnalysisLandmarkdientResult(strOut);
                }
                else if(strType.equals("Dish"))
                {
                    AnalysisResult(strOut);
                }
                else
                {
                    AnalysisResult(strOut);
                }

            }

            @Override
            public void onFailure(int i, cz.msebera.android.httpclient.Header[] headers, byte[] bytes, Throwable throwable) {
                resultShowTextView.setText("http 访问失败 请检查网络");
            }
        });
    }
    public void ShowOrgResult()
    {
        resultShowTextView.setText(strResult);
    }
    private String AnalysisResult(String strInResult)
    {
        strResult = "";
        strResult = strInResult;
        String strRet = "";
        try{
            JSONObject jsonObject1 = new JSONObject(strInResult);
            JSONArray jsonArray = jsonObject1.getJSONArray("result");
            for(int i = 0;i < jsonArray.length();i++){
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);

                String strName = jsonObject.getString("name");
                String strShow = strName;
                if (!jsonObject.isNull("baike_info"))
                {
                    JSONObject object2 = jsonObject.getJSONObject("baike_info");
                    if (!object2.isNull("description"))
                    {
                        String strDescrip = object2.getString("description");
                        strShow += "\r\n";
                        strShow += strDescrip;
                    }
                }

                resultShowTextView.append(strShow);
                TtsHelper.getInstance().stop();
                TtsHelper.getInstance().speak("识别结果:"+strShow);
                break;

            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return strRet;
    }
    private String AnalysisAdvanced_generalResult(String strInResult)
    {
        strResult = "";
        strResult = strInResult;
        String strRet = "";
        try{
            JSONObject jsonObject1 = new JSONObject(strInResult);
            JSONArray jsonArray = jsonObject1.getJSONArray("result");
            //resultShowTextView.append("识别结果如下:\r\n");
            for(int i = 0;i < jsonArray.length();i++){
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);

                String strScore = jsonObject.getString("score");
                String strName = jsonObject.getString("root");
                //String strShow = "名称:"+strName +" 评分:" +strScore + "\r\n";
                resultShowTextView.append(strName);
                TtsHelper.getInstance().stop();
                TtsHelper.getInstance().speak("识别结果:"+strName);
                break;

            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return strRet;
    }
    private String AnalysisLandmarkdientResult(String strInResult)
    {
        strResult = "";
        strResult = strInResult;
        String strRet = "";
        try{
            JSONObject jsonObject1 = new JSONObject(strInResult);
            JSONObject jsonObject2 = jsonObject1.getJSONObject("result");
            String strName = jsonObject2.getString("landmark");
            resultShowTextView.append(strName);
            TtsHelper.getInstance().stop();
            TtsHelper.getInstance().speak("识别结果:"+strName);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return strRet;
    }
}
