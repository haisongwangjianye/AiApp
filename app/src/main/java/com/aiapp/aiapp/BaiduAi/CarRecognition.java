package com.aiapp.aiapp.BaiduAi;

import android.widget.TextView;

import com.aiapp.aiapp.HttpHelper.HttpHelper;
import com.aiapp.aiapp.PublicFuc.PublicFuc;
import com.aiapp.aiapp.tts.TtsHelper;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by wjy on 2019/11/13.
 */

public class CarRecognition {
    private String strResult;
    private String strBaseurl="https://aip.baidubce.com";
    private String strToken;
    private TextView resultShowTextView;
    //车型识别
    private String strCarUrl="/rest/2.0/image-classify/v1/car";
    //车辆检测
    private String strVehicle_detectUrl = "/rest/2.0/image-classify/v1/vehicle_detect";
    //车辆外观损伤识别
    private String strVehicle_damageUrl = "/rest/2.0/image-classify/v1/vehicle_damage";

    public void SetTextView(TextView tempTextView)
    {
        resultShowTextView =tempTextView;
    }
    public CarRecognition() {
        strToken = AiRequestObject.getInstance().getStrImageToken();
    }

    //车型识别
    public void CarDetection(String strPath)
    {
        TtsHelper.getInstance().speak("正在车型识别，请稍等");
        String strBase64 = PublicFuc.GetImageBase64String(strPath,320);
        RequestParams params=new RequestParams();
        params.add("access_token",strToken);
        params.add("image",strBase64);
        params.add("image_type","BASE64");
        params.put("baike_num",1);
        RequestBaidu(strCarUrl,params,"Car");
    }
    //车辆检测
    public void Vehicle_detect(String strPath)
    {
        TtsHelper.getInstance().speak("正在车辆检测，请稍等");
        String strBase64 = PublicFuc.GetImageBase64String(strPath,320);
        RequestParams params=new RequestParams();
        params.add("access_token",strToken);
        params.add("image",strBase64);
        params.add("image_type","BASE64");
        RequestBaidu(strVehicle_detectUrl,params,"Vehicle_detect");
    }
    //车辆外观损伤识别
    public void Vehicle_damage(String strPath)
    {
        TtsHelper.getInstance().speak("正在进行,车辆外观损伤识别，请稍等");
        String strBase64 = PublicFuc.GetImageBase64String(strPath,320);
        RequestParams params=new RequestParams();
        params.add("access_token",strToken);
        params.add("image",strBase64);
        params.add("image_type","BASE64");
        RequestBaidu(strVehicle_damageUrl,params,"Vehicle_damage");
    }
    private void RequestBaidu(String strUrl, RequestParams params, final String strType)
    {
        String strTempUrl = strBaseurl+ strUrl;
       // resultShowTextView.setText(strTempUrl);
        HttpHelper.HttpAsyncPostEx(strTempUrl,params,new AsyncHttpResponseHandler(){
            String strOut;
            @Override
            public void onSuccess(int i, cz.msebera.android.httpclient.Header[] headers, byte[] bytes) {
                strOut = new String(bytes);
                if(strType.equals("Car"))
                {
                    AnalysisCarResult(strOut);
                }
                else if(strType.equals("Vehicle_detect"))
                {
                    AnalysisVehicle_detectResult(strOut);
                }
                else if(strType.equals("Vehicle_damage"))
                {
                    AnalysisVehicle_damageResult(strOut);
                }
            }

            @Override
            public void onFailure(int i, cz.msebera.android.httpclient.Header[] headers, byte[] bytes, Throwable throwable) {
                resultShowTextView.setText("http 访问失败 请检查网络");
            }
        });
    }
    private String AnalysisVehicle_damageResult(String strInResult)
    {
        strResult = "";
        strResult = strInResult;
        resultShowTextView.append(strInResult);
        String strRet = "";
        try{
            JSONObject jsonObject1 = new JSONObject(strInResult);
            JSONObject jsonObject2 = jsonObject1.getJSONObject("result");
            String strDescription = jsonObject2.getString("description");
            resultShowTextView.append(strDescription);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return strRet;
    }
    private String AnalysisCarResult(String strInResult)
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
    private String AnalysisVehicle_detectResult(String strInResult)
    {
        strResult = "";
        strResult = strInResult;
        String strRet = "";
        try{
            JSONObject jsonObject1 = new JSONObject(strInResult);
            JSONObject jsonObject2 = jsonObject1.getJSONObject("vehicle_num");

            int iCarNum = jsonObject2.getInt("car");
            String strTemp="";
            String strShow="";
            boolean bIsHave = false;
            if(iCarNum > 0)
            {
                strTemp = String.format("%d 辆小汽车，\n",iCarNum);
                strShow += strTemp;
                bIsHave = true;
            }

            int itruckNum = jsonObject2.getInt("truck");
            if(itruckNum > 0)
            {
                bIsHave = true;
                strTemp = String.format("%d 辆卡车，\n",itruckNum);
                strShow += strTemp;
            }

            int ibusNum = jsonObject2.getInt("bus");
            if(ibusNum > 0)
            {
                bIsHave = true;
                strTemp = String.format("%d 辆公共汽车，\n",ibusNum);
                strShow += strTemp;
            }

            int imotorbikeNum = jsonObject2.getInt("motorbike");
            if(imotorbikeNum > 0)
            {
                bIsHave = true;
                strTemp = String.format("%d 辆摩托车，\n",imotorbikeNum);
                strShow += strTemp;
            }

            int itricycleNum = jsonObject2.getInt("tricycle");
            if(itricycleNum > 0)
            {
                bIsHave = true;
                strTemp = String.format("%d 辆三轮车，\n",itricycleNum);
                strShow += strTemp;
            }
            if(bIsHave)
            {
                resultShowTextView.append(strShow);
            }
            else
            {
                resultShowTextView.append("没有检测到车辆，\r\n目前只能检测：小汽车,卡车,公共汽车,摩托车,三轮车");
            }

            TtsHelper.getInstance().stop();
            String strOut = resultShowTextView.getText().toString();
            TtsHelper.getInstance().speak("识别结果:"+strOut);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return strRet;
    }
    public void ShowOrgResult()
    {
        resultShowTextView.setText(strResult);
    }
}
