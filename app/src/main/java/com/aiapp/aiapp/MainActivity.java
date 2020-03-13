package com.aiapp.aiapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.aiapp.aiapp.BaiduAi.AiRequestObject;
import com.aiapp.aiapp.BottomFragment.BtmFragmentPagerAdapter;
import com.aiapp.aiapp.PublicFuc.PermissionManager;
import com.aiapp.aiapp.tts.TtsHelper;

import java.util.ArrayList;

public class MainActivity  extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener,
        ViewPager.OnPageChangeListener{

    private RadioGroup rg_tab_bar;
    private RadioButton btn_Image;
    private RadioButton btn_Car;
    private RadioButton btn_Face;
    private RadioButton btn_Text;
    private ViewPager vpager;
    private BtmFragmentPagerAdapter mAdapter;
    private PermissionManager mPermissionMgr;

    //几个代表页面的常量
    public static final int PAGE_ONE = 0;
    public static final int PAGE_TWO = 1;
    public static final int PAGE_THREE = 2;
    public static final int PAGE_FOUR = 3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TtsHelper.getInstance().Init(this);

        mAdapter = new BtmFragmentPagerAdapter(getSupportFragmentManager());
        bindViews();
        btn_Image.setChecked(true);
        AiRequestObject.getInstance().GetToken();
        initPermission();

    }
    private void bindViews() {

        rg_tab_bar = (RadioGroup) findViewById(R.id.rg_tab_bar);
        btn_Image = (RadioButton) findViewById(R.id.Radiobtn_Image);
        btn_Car = (RadioButton) findViewById(R.id.Radiobtn_Car);
        btn_Face = (RadioButton) findViewById(R.id.Radiobtn_Face);
        btn_Text = (RadioButton) findViewById(R.id.Radiobtn_Text);
        rg_tab_bar.setOnCheckedChangeListener(this);

        vpager = (ViewPager) findViewById(R.id.vpager);
        vpager.setAdapter(mAdapter);
        vpager.setCurrentItem(0);
        vpager.addOnPageChangeListener(this);
    }
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.Radiobtn_Image:
                vpager.setCurrentItem(PAGE_ONE);
                break;
            case R.id.Radiobtn_Car:
                vpager.setCurrentItem(PAGE_TWO);
                break;
            case R.id.Radiobtn_Face:
                vpager.setCurrentItem(PAGE_THREE);
                break;
            case R.id.Radiobtn_Text:
                vpager.setCurrentItem(PAGE_FOUR);
                break;
        }
    }



    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {
        //state的状态有三个，0表示什么都没做，1正在滑动，2滑动完毕
        if (state == 2) {
            switch (vpager.getCurrentItem()) {
                case PAGE_ONE:
                    btn_Image.setChecked(true);
                    break;
                case PAGE_TWO:
                    btn_Car.setChecked(true);
                    break;
                case PAGE_THREE:
                    btn_Face.setChecked(true);
                    break;
                case PAGE_FOUR:
                    btn_Text.setChecked(true);
                    break;
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
                Manifest.permission.CHANGE_WIFI_STATE,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.MODIFY_AUDIO_SETTINGS
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
