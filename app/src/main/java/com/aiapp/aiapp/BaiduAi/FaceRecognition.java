package com.aiapp.aiapp.BaiduAi;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.aiapp.aiapp.HttpHelper.HttpHelper;
import com.aiapp.aiapp.PublicFuc.PublicFuc;
import com.aiapp.aiapp.tts.TtsHelper;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import cz.msebera.android.httpclient.util.TextUtils;


/**
 * Created by wjy on 2019/11/7.
 */

public class FaceRecognition extends AppCompatActivity{
    private Context context;
    private String strResult;
    private String strBaseurl="https://aip.baidubce.com";
    private Handler mHandler;
    //人脸检测
    private String strFaceDetectionUrl="/rest/2.0/face/v3/detect";
    //人脸对比
    private String strFaceCompareUrl = "/rest/2.0/face/v3/match";

    private String strToken;
    private TextView resultShowTextView;
    public void SetTextView(TextView tempTextView)
    {
        resultShowTextView =tempTextView;
    }
    public void SetMsgHandle(Handler inHandler){mHandler = inHandler;}
    public FaceRecognition() {
        strToken = AiRequestObject.getInstance().getStrFaceToken();
    }
    public void SetContent(Context ncontext){context = ncontext;};

    //人脸检测
    public void FaceDetection(String strPath)
    {
        String strBase64 = PublicFuc.GetImageBase64String(strPath,320);
        RequestParams params=new RequestParams();
        params.add("access_token",strToken);
        params.add("image",strBase64);
        params.add("image_type","BASE64");
        params.add("face_field","age,beauty,expression,gender,glasses,race,quality,eye_status,emotion");
        RequestBaidu(strFaceDetectionUrl,params,"detection");
    }
    //人脸对比
    public void FaceCompare(String strFace1,String strFace2)
    {
        String strBase641 = PublicFuc.GetImageBase64String(strFace1,160);
        String strBase642 = PublicFuc.GetImageBase64String(strFace2,160);
        String strOut = null;
        JSONObject jsonObj = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonobject1 = new JSONObject();
        try {
            jsonobject1.put("image",strBase641);
            jsonobject1.put("image_type","BASE64");
            JSONObject jsonobject2 = new JSONObject();
            jsonobject2.put("image",strBase642);
            jsonobject2.put("image_type","BASE64");
            jsonArray.put(0,jsonobject1);
            jsonArray.put(1,jsonobject2);

            jsonObj.put("studentArr", jsonArray);
             strOut = jsonArray.toString();
            String strTempUrl = strBaseurl+ strFaceCompareUrl+ "?access_token=" + strToken;

            HttpHelper.HttpAysncPostJsonEx2(context,strTempUrl,strOut
                    ,new AsyncHttpResponseHandler()
            {
                String strOut;
                @Override
                public void onSuccess(int i, cz.msebera.android.httpclient.Header[] headers, byte[] bytes) {
                    strOut = new String(bytes);
                    AnalysisFaceCompareRequest(strOut);
                }

                @Override
                public void onFailure(int i, cz.msebera.android.httpclient.Header[] headers, byte[] bytes, Throwable throwable) {
                    resultShowTextView.setText("http 访问失败 请检查网络");
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void AnalysisFaceDetectionRequest(String strInResult)
    {
        strResult = "";
        strResult = strInResult;
        String strRet = "";
        try{
            String strShow = null;
            JSONObject jsonObject1 = new JSONObject(strInResult);
            int iErrorCode = jsonObject1.getInt("error_code");
            String strErrorInfo = jsonObject1.getString("error_msg");
            resultShowTextView.append(strErrorInfo);
            if(0 == iErrorCode)
            {
                JSONObject jsonObject2 = jsonObject1.getJSONObject("result");
                int ifaceNum =jsonObject2.getInt("face_num");
                if (ifaceNum > 0)
                {
                    JSONArray  jsonArray = jsonObject2.getJSONArray("face_list");
                    for(int i = 0;i < jsonArray.length();i++){
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                        String strTemp;
                        int iAge = jsonObject.getInt("age");
                        strTemp = String.format("年龄：%d 岁\n",iAge );
                        strShow =  strTemp;
                        float fbeauty = jsonObject.getInt("beauty");
                        strTemp = String.format("漂亮系数：%f、\n ",fbeauty );
                        strShow += strTemp;
                        //性别
                        JSONObject jsonObject3 = (JSONObject) jsonObject.getJSONObject("gender");
                        String strType = jsonObject3.getString("type");
                        if(strType.equals("male"))
                        {
                            strTemp = "性别: 男\r\n";
                        }
                        else
                        {
                            strTemp = "性别: 女\n";
                        }
                        strShow += strTemp;
                        //表情
                        jsonObject3 = (JSONObject) jsonObject.getJSONObject("expression");
                        strType = jsonObject3.getString("type");//none:不笑；smile:微笑；laugh:大笑
                        if(strType.equals("none"))
                        {
                            strTemp = "表情: 没有笑\n";
                        }
                        else if(strType.equals("smile"))
                        {
                            strTemp = "表情: 微笑\n";
                        }
                        else if(strType.equals("laugh"))
                        {
                            strTemp = "表情: 大笑\n";
                        }
                        strShow += strTemp;
                        //人种
                        jsonObject3 = (JSONObject) jsonObject.getJSONObject("race");
                        strType = jsonObject3.getString("type");
                        if(strType.equals("yellow"))//yellow: 黄种人 white: 白种人 black:黑种人 arabs: 阿拉伯人
                        {
                            strTemp = "黄种人\r\n";
                        }
                        if(strType.equals("white"))
                        {
                            strTemp = "白种人\n";
                        }
                        if(strType.equals("black"))
                        {
                            strTemp = "黑种人\n";
                        }
                        if(strType.equals("arabs"))
                        {
                            strTemp = "阿拉伯人\n";
                        }
                        strShow += strTemp;
                        //情绪
                        jsonObject3 = (JSONObject) jsonObject.getJSONObject("emotion");
                        strType = jsonObject3.getString("type");//angry:愤怒 disgust:厌恶 fear:恐惧 happy:高兴 sad:伤心 surprise:惊讶
                        // neutral:无表情 pouty: 撅嘴 grimace:鬼脸
                        if(strType.equals("angry"))
                        {
                            strTemp = "您是愤怒的小鸟么？\r\n";
                        }
                        if(strType.equals("disgust"))
                        {
                            strTemp = "厌恶什么呢\n";
                        }
                        if(strType.equals("fear"))
                        {
                            strTemp = "什么吓到了您，使您如此恐惧。\n";
                        }
                        if(strType.equals("happy"))
                        {
                            strTemp = "您此时很高兴呀\n";
                        }
                        if(strType.equals("sad"))
                        {
                            strTemp = "为什么伤心呢\n";
                        }
                        if(strType.equals("surprise"))
                        {
                            strTemp = "因为什么惊讶呢\n";
                        }
                        if(strType.equals("neutral"))
                        {
                            strTemp = "面无任何表情哟\n";
                        }
                        if(strType.equals("pouty"))
                        {
                            strTemp = "干嘛撅嘴，生气了吗\n";
                        }
                        if(strType.equals("grimace"))
                        {
                            strTemp = "为什么做鬼脸呢\n";
                        }
                        strShow +=  strTemp;
                        //是否戴眼镜
                        jsonObject3 = (JSONObject) jsonObject.getJSONObject("glasses");
                        strType = jsonObject3.getString("type");
                        if(strType.equals("none"))//none:无眼镜，common:普通眼镜，sun:墨镜
                        {
                            strTemp = "没有带眼镜\r\n";
                        }
                        if(strType.equals("common"))
                        {
                            strTemp = "带普通眼镜，您近视眼么\n";
                            strShow +=  strTemp;
                        }
                        if(strType.equals("sun"))
                        {
                            strTemp = "为什么带墨镜呢？\n";
                            strShow +=  strTemp;
                        }

                        TtsHelper.getInstance().stop();
                        TtsHelper.getInstance().speak(strShow);
                        break;
                    }
                }
            }
            else
            {
                strShow = "error_code:" + iErrorCode + "\nerror_msg:" + strErrorInfo;
            }
            resultShowTextView.setText(strShow);
            TtsHelper.getInstance().stop();
            TtsHelper.getInstance().speak(strShow);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    private void AnalysisFaceCompareRequest(String strInResult)
    {
        strResult = "";
        strResult = strInResult;
        String strRet = "";
        try{
            String strShow = null;
            JSONObject jsonObject1 = new JSONObject(strInResult);
            int iErrorCode = jsonObject1.getInt("error_code");
            String strErrorInfo = jsonObject1.getString("error_msg");
            //resultShowTextView.append(strErrorInfo);
            if(0 == iErrorCode)
            {
                JSONObject jsonObject2 = jsonObject1.getJSONObject("result");
                Double iscore =jsonObject2.getDouble("score");
                Message msg = new Message();
                msg.what = 0x123;
                Bundle bundle = new Bundle();
                bundle.putDouble("score",iscore);  //往Bundle中存放数据
                msg.setData(bundle);//mes利用Bundle传递数据
                mHandler.sendMessage(msg);//用activity中的handler发送消息

               // strShow = String.format("相似度:%f",iscore);
            }
            else
            {
                strShow = "error_code:" + iErrorCode + "\nerror_msg:" + strErrorInfo;
            }
            //resultShowTextView.setText(strShow);
//            TtsHelper.getInstance().stop();
//            TtsHelper.getInstance().speak(strShow);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void RequestBaidu(String strUrl, RequestParams params, final String strType)
    {
        TtsHelper.getInstance().speak("正在识别，请稍等");
        String strTempUrl = strBaseurl+ strUrl;
        HttpHelper.HttpAsyncPostJson(strTempUrl,params,new AsyncHttpResponseHandler(){
            String strOut;
            @Override
            public void onSuccess(int i, cz.msebera.android.httpclient.Header[] headers, byte[] bytes) {
                strOut = new String(bytes);
                if(strType.equals("detection"))
                {
                    AnalysisFaceDetectionRequest(strOut);
                }
            }

            @Override
            public void onFailure(int i, cz.msebera.android.httpclient.Header[] headers, byte[] bytes, Throwable throwable) {
                resultShowTextView.setText("http 访问失败 请检查网络");
            }
        });
    }
}
