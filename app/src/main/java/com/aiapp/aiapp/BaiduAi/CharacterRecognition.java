package com.aiapp.aiapp.BaiduAi;

import android.graphics.Bitmap;
import android.widget.TextView;

import com.aiapp.aiapp.HttpHelper.HttpHelper;
import com.aiapp.aiapp.PublicFuc.PublicFuc;
import com.aiapp.aiapp.tts.TtsHelper;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by wjy on 2019/11/7.
 */

public class CharacterRecognition {
    private String strResult;
    private String strBaseurl="https://aip.baidubce.com";

    //通用文字
    private String strGeneral_basicUrl="/rest/2.0/ocr/v1/general_basic";
    //身份证
    private String strIdcardUrl = "/rest/2.0/ocr/v1/idcard";
    //银行卡
    private String strBankcardUrl = "/rest/2.0/ocr/v1/bankcard";
    //驾驶证
    private String strDriving_licenseUrl = "/rest/2.0/ocr/v1/driving_license";
    //行驶证
    private String strVehicle_licenseUrl = "/rest/2.0/ocr/v1/vehicle_license";
    //车牌
    private String strLicense_plateUrl = "/rest/2.0/ocr/v1/license_plate";
    //通用票据
    private String strReceiptUrl = "/rest/2.0/ocr/v1/receipt";

    protected String appId = "16126270";
    protected String appKey = "GTbPQx3LL52LkRDtAShmAq6c";
    protected String secretKey = "btVqOuTEIHvIHldj8afZCQICG9rBnKy3";

    private String strToken;
    private String strReturn ="not return";
    private TextView resultShowTextView;
    private boolean bIdCardFront = true;
    public void SetTextView(TextView tempTextView)
    {
        resultShowTextView =tempTextView;
    }
    public CharacterRecognition() {
        strToken = AiRequestObject.getInstance().getStrCharToken();
    }

    public void ProcGeneral_basic(String strFilePath)
    {
        //通用文字
        String strBase64 = PublicFuc.GetImageBase64String(strFilePath,320);
        RequestParams params=new RequestParams();
        params.add("access_token",strToken);
        params.add("image",strBase64);
        RequestBaidu(strGeneral_basicUrl,params,"General_basic");
    }
    public void ProcIdcard(String strFilePath,boolean bIsSide)
    {
        bIdCardFront = bIsSide;
        //身份证
        String strBase64 = PublicFuc.GetImageBase64String(strFilePath,320);
        RequestParams params=new RequestParams();
        params.add("access_token",strToken);
        params.add("image",strBase64);
        String strSide =bIsSide?"front":"back";
        params.add("id_card_side",strSide);
        RequestBaidu(strIdcardUrl,params,"Idcard");
    }
    public void ProcBankcard(String strFilePath)
    {
        //银行卡
        String strBase64 = PublicFuc.GetImageBase64String(strFilePath,320);
        RequestParams params=new RequestParams();
        params.add("access_token",strToken);
        params.add("image",strBase64);
        RequestBaidu(strBankcardUrl,params,"Bankcard");
    }
    public void ProcDriving_license(String strFilePath)
    {
        //驾驶证
        String strBase64 = PublicFuc.GetImageBase64String(strFilePath,320);
        RequestParams params=new RequestParams();
        params.add("access_token",strToken);
        params.add("image",strBase64);
        RequestBaidu(strDriving_licenseUrl,params,"Driving_license");
    }
    public void ProcVehicle_license(String strFilePath)
    {
        //行驶证
        String strBase64 = PublicFuc.GetImageBase64String(strFilePath,320);
        RequestParams params=new RequestParams();
        params.add("access_token",strToken);
        params.add("image",strBase64);
        RequestBaidu(strVehicle_licenseUrl,params,"Vehicle_license");
    }
    public void ProcLicense_plate(String strFilePath)
    {
        //车牌
        String strBase64 = PublicFuc.GetImageBase64String(strFilePath,320);
        RequestParams params=new RequestParams();
        params.add("access_token",strToken);
        params.add("image",strBase64);
        RequestBaidu(strLicense_plateUrl,params,"License_plate");
    }
    public void ProcReceipt(String strFilePath)
    {
        //通用票据
        String strBase64 = PublicFuc.GetImageBase64String(strFilePath,320);
        RequestParams params=new RequestParams();
        params.add("access_token",strToken);
        params.add("image",strBase64);
        RequestBaidu(strReceiptUrl,params,"Receipt");
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
                if(strType.equals("General_basic"))
                {
                   AnalysisGeneral_basic(strOut);
                }
                else if(strType.equals("Idcard"))
                {
                    if(bIdCardFront)
                    {
                        AnalysisIdCardFront(strOut);
                    }
                    else
                    {
                        AnalysisIdCardBack(strOut);
                    }
                }
                else if(strType.equals("Bankcard"))
                {
                    AnalysisBankCardResult(strOut);
                }
                else if(strType.equals("License_plate"))
                {
                    AnalysisLicense_plat(strOut);
                }
                else if(strType.equals("Driving_license"))
                {
                    AnalysisDriving_license(strOut);
                }
            }

            @Override
            public void onFailure(int i, cz.msebera.android.httpclient.Header[] headers, byte[] bytes, Throwable throwable) {
                resultShowTextView.setText("http 访问失败 请检查网络");
            }
        });
    }
    private void AnalysisGeneral_basic(String strInResult)
    {
        strResult = "";
        strResult = strInResult;
        String strRet = "";
        try{
            JSONObject jsonObject1 = new JSONObject(strInResult);
            JSONArray jsonArray = jsonObject1.getJSONArray("words_result");
            resultShowTextView.append("识别结果如下:\r\n");
            for(int i = 0;i < jsonArray.length();i++){
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);

                String strName = jsonObject.getString("words");
                String strShow =strName +"\r\n";
                resultShowTextView.append(strShow);
            }
            String strOut = resultShowTextView.getText().toString();
            TtsHelper.getInstance().speak(strOut);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    private void AnalysisIdCardFront(String strInResult)
    {
        strResult = "";
        strResult = strInResult;
        String strRet = "";
        try{
            JSONObject jsonObject1 = new JSONObject(strInResult);

            JSONObject jsonObject2 = jsonObject1.getJSONObject("words_result");
            //姓名
            JSONObject jsonObjectName = jsonObject2.getJSONObject("姓名");
            String strName = jsonObjectName.getString("words");
            String strShow ="姓名："+ strName +"\r\n";
            //性别
            JSONObject jsonObjectSex = jsonObject2.getJSONObject("性别");
            String strSex = jsonObjectSex.getString("words");
            String strTemp1 = "性别: " + strSex + "\r\n";
            strShow+=strTemp1;
            //民族
            JSONObject jsonObjectMinzhu = jsonObject2.getJSONObject("民族");
            String strMinzhu = jsonObjectMinzhu.getString("words");
            strTemp1 = "民族: " + strMinzhu + "\r\n";
            strShow+=strTemp1;
            //出生
            JSONObject jsonObjectDate = jsonObject2.getJSONObject("出生");
            String strDate = jsonObjectDate.getString("words");
            strTemp1 = "出生日期: " + strDate + "\r\n";
            strShow+=strTemp1;
            //公民身份号码
            JSONObject jsonObjectNo = jsonObject2.getJSONObject("公民身份号码");
            String strNo = jsonObjectNo.getString("words");
            strTemp1 = "公民身份号码: " + strNo + "\r\n";
            strShow+=strTemp1;
            //住址
            JSONObject jsonObjectzhuzhi = jsonObject2.getJSONObject("住址");
            String strZhuzhi = jsonObjectzhuzhi.getString("words");
            strTemp1 = "住址   : " + strZhuzhi + "\r\n";
            strShow+=strTemp1;

            resultShowTextView.append("识别结果如下:\r\n");
            resultShowTextView.append(strShow);

            String strOut = resultShowTextView.getText().toString();
            TtsHelper.getInstance().speak(strOut);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    private void AnalysisLicense_plat(String strInResult)
    {
        strResult = "";
        strResult = strInResult;
        String strRet = "";
        try{
            JSONObject jsonObject1 = new JSONObject(strInResult);
//            JSONObject jsonObject2 = jsonObject1.getJSONObject("data");
            JSONObject jsonObject3 = jsonObject1.getJSONObject("words_result");
            //车牌颜色
            String strQianfa = jsonObject3.getString("color");
            String strShow = "车牌颜色: " + strQianfa + "\r\n";
            //车牌号
            String strNum = jsonObject3.getString("number");
            String strTemp1 ="车牌号  : " + strNum + "  ";
            strShow+=strTemp1;

            resultShowTextView.append(strShow);
            String strOut = resultShowTextView.getText().toString();
            TtsHelper.getInstance().speak(strOut);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void AnalysisDriving_license(String strInResult)
    {
        strResult = "";
        strResult = strInResult;
        String strRet = "";
        try{
            JSONObject jsonObject1 = new JSONObject(strInResult);
            JSONObject jsonObject2 = jsonObject1.getJSONObject("words_result");
            //姓名
            JSONObject jsonObjecOutTemp = jsonObject2.getJSONObject("姓名");
            String strTemp = jsonObjecOutTemp.getString("words");
            String strTemp1 ="姓名："+ strTemp +"\r\n";
            String strShow = strTemp1;
            //性别
            jsonObjecOutTemp = jsonObject2.getJSONObject("性别");
            strTemp = jsonObjecOutTemp.getString("words");
            strTemp1 ="性别："+ strTemp +"\r\n";
            strShow+=strTemp1;
            //国籍
            jsonObjecOutTemp = jsonObject2.getJSONObject("国籍");
            strTemp = jsonObjecOutTemp.getString("words");
            strTemp1 ="国籍："+ strTemp +"\r\n";
            strShow+=strTemp1;
            //出生日期
            jsonObjecOutTemp = jsonObject2.getJSONObject("出生日期");
            strTemp = jsonObjecOutTemp.getString("words");
            strTemp1 ="出生日期："+ strTemp +"\r\n";
            strShow+=strTemp1;
            //证号
            jsonObjecOutTemp = jsonObject2.getJSONObject("证号");
            String strQianfa = jsonObjecOutTemp.getString("words");
            strTemp1 = "身份证号: " + strQianfa + "\r\n";
            strShow+=strTemp1;
            //住址
            jsonObjecOutTemp = jsonObject2.getJSONObject("住址");
            strTemp = jsonObjecOutTemp.getString("words");
            strTemp1 ="住址："+ strTemp +"\r\n";
            strShow+=strTemp1;
            //准驾车型
            JSONObject jsonObjecOutTIme = jsonObject2.getJSONObject("准驾车型");
            String strOutTIme = jsonObjecOutTIme.getString("words");
            strTemp1 ="准驾车型："+ strOutTIme +"\r\n";
            strShow+=strTemp1;
            //初次领证日期
            jsonObjecOutTemp = jsonObject2.getJSONObject("初次领证日期");
            strTemp = jsonObjecOutTemp.getString("words");
            strTemp1 ="初次领证日期："+ strTemp +"\r\n";
            strShow+=strTemp1;
            //有效期限
             jsonObjecOutTemp = jsonObject2.getJSONObject("有效期限");
            String strStartDate = jsonObjecOutTemp.getString("words");
            strTemp1 = "有效期限: " + strStartDate + " ";
            strShow+=strTemp1;
            //到期时间
             jsonObjecOutTemp = jsonObject2.getJSONObject("至");
            strTemp = jsonObjecOutTemp.getString("words");
            strTemp1 =" 至 "+ strTemp +"\r\n";
            strShow+=strTemp1;

            resultShowTextView.append(strShow);
            TtsHelper.getInstance().speak(strShow);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    private void AnalysisIdCardBack(String strInResult)
    {
        strResult = "";
        strResult = strInResult;
        String strRet = "";
        try{
            JSONObject jsonObject1 = new JSONObject(strInResult);
            JSONObject jsonObject2 = jsonObject1.getJSONObject("words_result");
            //签发机关
            JSONObject jsonObjectQianfa = jsonObject2.getJSONObject("签发机关");
            String strQianfa = jsonObjectQianfa.getString("words");
            String strShow = "签发机关: " + strQianfa + "\r\n";
            //签发日期
            JSONObject jsonObjectStartDate = jsonObject2.getJSONObject("签发日期");
            String strStartDate = jsonObjectStartDate.getString("words");
            String strTemp1 = "签发日期: " + strStartDate + "  ";
            strShow+=strTemp1;
            //失效日期
            JSONObject jsonObjecOutTIme = jsonObject2.getJSONObject("失效日期");
            String strOutTIme = jsonObjecOutTIme.getString("words");
            strTemp1 ="失效日期："+ strOutTIme +"\r\n";
            strShow+=strTemp1;

            resultShowTextView.append("识别结果如下:\r\n");
            resultShowTextView.append(strShow);
            String strOut = resultShowTextView.getText().toString();
            TtsHelper.getInstance().speak(strOut);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    private String AnalysisBankCardResult(String strInResult)
    {
        strResult = "";
        strResult = strInResult;
        String strRet = "";
        try{
            JSONObject jsonObject1 = new JSONObject(strInResult);
            JSONObject jsonObject2 = jsonObject1.getJSONObject("result");
            //银行名称
            String strBankName = jsonObject2.getString("bank_name");
            String strTemp1 = "银行名称: " + strBankName + "\r\n";
            String strShow =strTemp1;
            //卡类型
            int strCardType = jsonObject2.getInt("bank_card_type");
            if(strCardType == 0)//0:不能识别; 1: 借记卡; 2: 信用卡
            {
                strTemp1 = "卡类型  : " + "未知" + "\r\n";
            }
            if(strCardType == 1)
            {
                strTemp1 = "卡类型  : " + "借记卡" + "\r\n";
            }
            if(strCardType == 2)
            {
                strTemp1 = "卡类型  : " + "信用卡" + "\r\n";
            }
            strShow+=strTemp1;
            //有效日期
            String strStartDate = jsonObject2.getString("valid_date");
            strTemp1 = "有效期  : " + strStartDate + "\r\n";
            strShow+=strTemp1;
            //卡号
            String strCardNo = jsonObject2.getString("bank_card_number");
            strTemp1 = "卡号   : " + strCardNo;
            strShow+=strTemp1;

            resultShowTextView.append(strShow);
            TtsHelper.getInstance().stop();
            TtsHelper.getInstance().speak("识别结果:"+strShow);
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
