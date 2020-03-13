package com.aiapp.aiapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aiapp.aiapp.BaiduAi.CarRecognition;
import com.aiapp.aiapp.BaiduAi.CharacterRecognition;
import com.aiapp.aiapp.BaiduAi.FaceRecognition;
import com.aiapp.aiapp.BaiduAi.ImageRecognition;
import com.aiapp.aiapp.HttpHelper.HttpHelper;
import com.aiapp.aiapp.tts.TtsHelper;
import com.loopj.android.http.AsyncHttpResponseHandler;


/**
 * Created by wjy on 2019/11/6.
 */

public class AnalysisyResultActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView picture;
    private TextView resultTextView;
    private Button btnVoiceRead;
    private Button btnGetToken;
//    private Button btnRequest;
    private String strPath;

    private String strMainType;
    private String strSubType;
    private ImageRecognition imageRecognition;
    private CharacterRecognition characterRecognition;
    private FaceRecognition faceRecognition;
    private CarRecognition carRecognition;
    private Handler m_handler;
    private boolean bIdCardFacce = true;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.analysis_result_activity);

        Intent intent = getIntent();
        strMainType = intent.getStringExtra("MainType");
        strSubType = intent.getStringExtra("SubType");
        strPath = intent.getStringExtra("imagePath");
        if(strMainType.equals("text") && strSubType.equals("身份证"))
        {
            bIdCardFacce = intent.getBooleanExtra("IdCardFace",bIdCardFacce);
        }
        bindView();
        ShowImage();
        ProcRequest();
    }


    public void bindView()
    {
        imageRecognition = new ImageRecognition();
        characterRecognition = new CharacterRecognition();
        faceRecognition = new FaceRecognition();
        carRecognition = new CarRecognition();
        picture =  (ImageView)findViewById(R.id.image_org_picture);
        resultTextView = (TextView)findViewById(R.id.textView_result);
        btnVoiceRead = (Button)findViewById(R.id.btn_YuyinBofang);
        btnGetToken = (Button)findViewById(R.id.btn_take_token);
        //btnRequest = (Button)findViewById(R.id.btn_take_request);
        //btnRequest.setOnClickListener(this);
        btnVoiceRead.setOnClickListener(this);
        btnGetToken.setOnClickListener(this);
        carRecognition.SetTextView(resultTextView);
        imageRecognition.SetTextView(resultTextView);
        characterRecognition.SetTextView(resultTextView);
        faceRecognition.SetTextView(resultTextView);
        faceRecognition.SetContent(this);
    }
    private void ShowImage()
    {
        Bitmap bitmap = BitmapFactory.decodeFile(strPath);
        picture.setImageBitmap(bitmap);
        picture.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
    }

    @Override
    protected void onStop() {
        TtsHelper.getInstance().stop();
        super.onStop();
    }

    private boolean ProcRequest()
    {
        if(strMainType.equals("image"))
        {
            if(strSubType.equals("动物"))
            {
                imageRecognition.GetAnimal(strPath);
            }
            if(strSubType.equals("植物"))
            {
                imageRecognition.GetPlant(strPath);
            }
            if(strSubType.equals("果蔬"))
            {
                imageRecognition.GetIngredient(strPath);
            }
            if(strSubType.equals("菜品"))
            {
                imageRecognition.GetDish(strPath);
            }
            if(strSubType.equals("地标"))
            {
                imageRecognition.GetLandmarkdient(strPath);
            }
            if(strSubType.equals("通用物体"))
            {
                imageRecognition.GetAdvanced_general(strPath);
            }
        }
        else if(strMainType.equals("text"))
        {
            if(strSubType.equals("通用文字"))
            {
                characterRecognition.ProcGeneral_basic(strPath);
            }
            if(strSubType.equals("身份证"))
            {
                characterRecognition.ProcIdcard(strPath,bIdCardFacce);
            }
            if(strSubType.equals("银行卡"))
            {
                characterRecognition.ProcBankcard(strPath);
            }
            if(strSubType.equals("驾驶证"))
            {
                characterRecognition.ProcDriving_license(strPath);
            }
            if(strSubType.equals("行驶证"))
            {
                characterRecognition.ProcVehicle_license(strPath);
            }
            if(strSubType.equals("车牌"))
            {
                characterRecognition.ProcLicense_plate(strPath);
            }
            if(strSubType.equals("通用票据"))
            {
                characterRecognition.ProcReceipt(strPath);
            }
        }
        else if(strMainType.equals("face"))
        {
            if(strSubType.equals("人脸检测"))
            {
                faceRecognition.FaceDetection(strPath);
            }
        }
        else if (strMainType.equals("car"))
        {
            if(strSubType.equals("车型识别"))
            {
                carRecognition.CarDetection(strPath);
            }
            if(strSubType.equals("车辆检测"))
            {
                carRecognition.Vehicle_detect(strPath);
            }
            if(strSubType.equals("车辆外观损伤识别"))
            {
                carRecognition.Vehicle_damage(strPath);
            }
        }
        return true;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btn_YuyinBofang:
                String strOut = resultTextView.getText().toString();
                TtsHelper.getInstance().speak(strOut);
                break;
            case R.id.btn_take_token:
                if(strMainType.equals("text"))
                {
                    characterRecognition.ShowOrgResult();
                }
                else if(strMainType.equals("image"))
                {
                    imageRecognition.ShowOrgResult();
                }
                else if(strMainType.equals("car"))
                {
                    carRecognition.ShowOrgResult();
                }

                break;
//            case R.id.btn_take_request:
//                ProcRequest();
//                break;
            default:
                break;
        }
    }
}
