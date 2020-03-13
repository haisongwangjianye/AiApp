package com.aiapp.aiapp;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aiapp.aiapp.PublicFuc.PublicFuc;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wjy on 2019/10/27.
 */

public class ImageProcActivity extends AppCompatActivity implements View.OnClickListener {
    public static final int TAKE_PHOTO = 1;

    public static final int CHOOSE_PHOTO = 2;

    private AlertDialog alertDialog2; //单选框
    private ImageView picture;
    private Context mContext;
    private TextView text_Title;
    private Uri imageUri;
    private Button btnStartAnalysisy;
    private Button btnChooseFromAlbum;
    private Button btnTakePhoto;
    private String strImagePath;
    private String strPrcType;
    private String strMainType;
    private boolean bIdCardPositive = true;//正面 true 反面 false
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_proc_activity);
        bindView();
        GetParamInfo();

       // Request();
        initPermission();
    }
    void bindView()
    {
        btnTakePhoto = (Button) findViewById(R.id.btn_take_photo);
        btnChooseFromAlbum = (Button) findViewById(R.id.btn_choose_from_album);
        btnStartAnalysisy = (Button)findViewById(R.id.btn_get_ImageInfo);
        picture = (ImageView) findViewById(R.id.picture);
        text_Title = (TextView)findViewById(R.id.text_Title);
        btnTakePhoto.setOnClickListener(this);
        btnStartAnalysisy.setOnClickListener(this);
        btnChooseFromAlbum.setOnClickListener(this);
    }
    void GetParamInfo()
    {
        Intent intent = getIntent();
        strMainType = intent.getStringExtra("MianType");
        strPrcType = intent.getStringExtra("Subtype");
        text_Title.setText(strPrcType);
        if(strMainType.equals("text") && strPrcType.equals("身份证"))
        {
            showSingleAlertDialog();
        }

    }
    public void showSingleAlertDialog(){
        final String[] items = {"正面", "反面"};
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setTitle("身份证");
        alertBuilder.setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //Toast.makeText(ImageProcActivity.this, "您选择了" + i + " 项", Toast.LENGTH_SHORT).show();
                //Toast.makeText(ImageProcActivity.this, items[i], Toast.LENGTH_SHORT).show();
                if (i == 0)
                {
                    bIdCardPositive = true;
                }
                else
                {
                    bIdCardPositive = false;
                }
            }
        });

        alertBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                alertDialog2.dismiss();
            }
        });

//        alertBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                alertDialog2.dismiss();
//            }
//        });

        alertDialog2 = alertBuilder.create();
        alertDialog2.show();
    }

    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, CHOOSE_PHOTO); // 打开相册
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openAlbum();
                } else {
                    Toast.makeText(this, "You denied the permission", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
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
                        picture.setImageBitmap(take);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    StartRequest();
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
            picture.setImageBitmap(take);
            StartRequest();
        } else {
            Toast.makeText(this, "failed to get image", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btn_take_photo:
            {
                // 创建File对象，用于存储拍照后的图片
                File outputImage = new File(getExternalCacheDir(), "output_image.jpg");
                try {
                    if (outputImage.exists()) {
                        outputImage.delete();
                    }
                    outputImage.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                strImagePath = outputImage.getPath();
                if (Build.VERSION.SDK_INT < 24) {
                    imageUri = Uri.fromFile(outputImage);
                } else {
                    imageUri = FileProvider.getUriForFile(ImageProcActivity.this, "com.aiapp.aiapp.fileprovider", outputImage);
                }
                // 启动相机程序
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent, TAKE_PHOTO);
            }
            break;
            case R.id.btn_choose_from_album:
            {
                if (ContextCompat.checkSelfPermission(ImageProcActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(ImageProcActivity.this, new String[]{ Manifest.permission. WRITE_EXTERNAL_STORAGE }, 1);
                } else {
                    openAlbum();
                }
            }
            break;
            case R.id.btn_get_ImageInfo:
            {
                if(strImagePath.isEmpty())
                {
                    Toast.makeText(this, "请先选择图片", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    StartRequest();
                }

            }
            break;
            default:
                break;
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    void Request() {
        //获取相机拍摄读写权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //版本判断
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA}, 1);
            }
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
    private void StartRequest()
    {
        Intent intent= new Intent(ImageProcActivity.this,AnalysisyResultActivity.class);
        intent.putExtra("MainType",strMainType);
        intent.putExtra("SubType",strPrcType);
        intent.putExtra("imagePath",strImagePath);
        if(strMainType.equals("text") && strPrcType.equals("身份证"))
        {
            intent.putExtra("IdCardFace",bIdCardPositive);
        }
        startActivity(intent);
    }
}
