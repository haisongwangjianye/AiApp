package com.aiapp.aiapp.faceProc;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aiapp.aiapp.BaiduAi.FaceRecognition;
import com.aiapp.aiapp.PublicFuc.PublicFuc;
import com.aiapp.aiapp.R;
import com.aiapp.aiapp.tts.TtsHelper;
import com.aiapp.aiapp.widgetCustom.CircleAnimation;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Face_Compare extends AppCompatActivity implements View.OnClickListener {
    public static final int TAKE_PHOTO = 1;

    public static final int CHOOSE_PHOTO = 2;
    private CircleAnimation mAnimation;

    private ImageView imageView_one;
    private ImageView imageView_two;
    private TextView resultTextView;
    private Button  btnCompare;
    private Uri imageUri;
    private boolean bSelectImage1 = false;
    private String strImagePath;
    private String strImagePath2;
    private FaceRecognition faceRecognition;

    public Handler mHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face__compare);
        initPermission();
        mHandler = new Handler()
        {
            @Override
            //重写handleMessage方法,根据msg中what的值判断是否执行后续操作
            public void handleMessage(Message msg) {
                if(msg.what == 0x123)
                {
                    double iscore = msg.getData().getDouble("score");
                    int iScore = (int)iscore;
                    double dEnd = 3.6 * iScore;
                    long iEnd = Math.round(dEnd);
                    String strShow = String.format("相似度:%d",iScore);
                    mAnimation.SetText(strShow);
                    TtsHelper.getInstance().stop();
                    TtsHelper.getInstance().speak(strShow);
                    mAnimation.setAngle(0,(int)iEnd);
                    mAnimation.play();

                }
            }
        };

        bindView();
        LinearLayout ll_layout = (LinearLayout) findViewById(R.id.ll_layout);
        mAnimation = new CircleAnimation(this);
        ll_layout.addView(mAnimation);
        mAnimation.render();
        int iLeft = ll_layout.getWidth()/2 - ll_layout.getHeight()/2;
        mAnimation.setAngle(0,0);
        mAnimation.play();
    }
    private void bindView()
    {
        imageView_one = (ImageView)findViewById(R.id.image_picture1);
        imageView_two = (ImageView)findViewById(R.id.image_picture2);
        resultTextView = (TextView)findViewById(R.id.textView_CompareResult);
        btnCompare = (Button)findViewById(R.id.btn_startCompare);

        imageView_one.setOnClickListener(this);
        imageView_two.setOnClickListener(this);
        btnCompare.setOnClickListener(this);

        faceRecognition = new FaceRecognition();
        faceRecognition.SetMsgHandle(mHandler);
        faceRecognition.SetTextView(resultTextView);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.image_picture1:
                bSelectImage1 = true;
                StartCamera(bSelectImage1);
                break;
            case R.id.image_picture2:
                bSelectImage1 = false;
                StartCamera(bSelectImage1);
                break;
            case R.id.btn_startCompare:
                faceRecognition.FaceCompare(strImagePath,strImagePath2);
                break;
            default:
                break;
        }
    }

    private  void StartCamera(boolean bIsSelect1)
    {
        String strFileName="output_image.jpg";
        if(!bIsSelect1)
        {
            strFileName="output_image1.jpg";
        }
        // 创建File对象，用于存储拍照后的图片
        File outputImage = new File(getExternalCacheDir(), strFileName);
        try {
            if (outputImage.exists()) {
                outputImage.delete();
            }
            outputImage.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (bIsSelect1)
        {
            strImagePath = outputImage.getPath();
        }
        else
        {
            strImagePath2 = outputImage.getPath();
        }
        if (Build.VERSION.SDK_INT < 24) {
            imageUri = Uri.fromFile(outputImage);
        } else {
            imageUri = FileProvider.getUriForFile(Face_Compare.this, "com.aiapp.aiapp.fileprovider", outputImage);
        }
        // 启动相机程序
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, TAKE_PHOTO);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {
                        // 将拍摄的照片显示出来
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        Bitmap take = PublicFuc.ResizeBitmap(bitmap, 640);
                        if (bSelectImage1)
                        {
                            imageView_one.setImageBitmap(take);
                        }
                        else
                        {
                            imageView_two.setImageBitmap(take);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case CHOOSE_PHOTO:
                if (resultCode == RESULT_OK) {
                    // 判断手机系统版本号
                    if (Build.VERSION.SDK_INT >= 19) {
                        // 4.4及以上系统使用这个方法处理图片
                        handleImageOnKitKat(data);
                    } else {
                        // 4.4以下系统使用这个方法处理图片
                        handleImageBeforeKitKat(data);
                    }
                }
                break;
            default:
                break;
        }
    }

    @TargetApi(19)
    private void handleImageOnKitKat(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        Log.d("TAG", "handleImageOnKitKat: uri is " + uri);
        if (DocumentsContract.isDocumentUri(this, uri)) {
            // 如果是document类型的Uri，则通过document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1]; // 解析出数字格式的id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // 如果是content类型的Uri，则使用普通方式处理
            imagePath = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            // 如果是file类型的Uri，直接获取图片路径即可
            imagePath = uri.getPath();
        }
        displayImage(imagePath); // 根据图片路径显示图片
    }

    private void handleImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
        displayImage(imagePath);
    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        // 通过Uri和selection来获取真实的图片路径
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    private void displayImage(String imagePath) {
        strImagePath = imagePath;
        if (imagePath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            Bitmap take = PublicFuc.ResizeBitmap(bitmap, 640);
            if (bSelectImage1)
            {
                imageView_one.setImageBitmap(take);
            }
            else
            {
                imageView_two.setImageBitmap(take);
            }
        } else {
            Toast.makeText(this, "failed to get image", Toast.LENGTH_SHORT).show();
        }

    }
    /**
     * android 6.0 以上需要动态申请权限
     */
    private void initPermission() {
        String permissions[] = {
                Manifest.permission.CAMERA,
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.MODIFY_AUDIO_SETTINGS,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_SETTINGS,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.CHANGE_WIFI_STATE
        };

        ArrayList<String> toApplyList = new ArrayList<String>();

        for (String perm :permissions){
            if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(this, perm)) {
                toApplyList.add(perm);
                //进入到这里代表没有权限.

            }
        }
        String tmpList[] = new String[toApplyList.size()];
        if (!toApplyList.isEmpty()){
            ActivityCompat.requestPermissions(this
                    , toApplyList.toArray(tmpList), 123);
        }

    }
}
