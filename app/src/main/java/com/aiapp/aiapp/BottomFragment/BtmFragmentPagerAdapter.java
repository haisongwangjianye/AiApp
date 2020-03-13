package com.aiapp.aiapp.BottomFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.aiapp.aiapp.BottomFragment.CarFragment;
import com.aiapp.aiapp.BottomFragment.FaceFragment;
import com.aiapp.aiapp.BottomFragment.ImageFragment;
import com.aiapp.aiapp.BottomFragment.TextFragment;
import com.aiapp.aiapp.MainActivity;

/**
 * Created by wjy on 2019/10/26.
 */

public class BtmFragmentPagerAdapter extends FragmentPagerAdapter
{
    private final int PAGER_COUNT = 4;
    private ImageFragment imgaeFragment = null;
    //private VoiceFragment voiceFragment = null;
    private FaceFragment faceFragment = null;
    private TextFragment textFragment = null;
    private CarFragment carFragent = null;
    public BtmFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
        imgaeFragment = new ImageFragment();
        //voiceFragment = new VoiceFragment();
        faceFragment = new FaceFragment();
        textFragment = new TextFragment();
        carFragent = new CarFragment();
    }

    @Override
    public Object instantiateItem(ViewGroup vg, int position) {
        return super.instantiateItem(vg, position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        System.out.println("position Destory" + position);
        super.destroyItem(container, position, object);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case MainActivity.PAGE_ONE:
                fragment = imgaeFragment;
                break;
            case MainActivity.PAGE_TWO:
                fragment = carFragent;
                break;
            case MainActivity.PAGE_THREE:
                fragment = faceFragment;
                break;
            case MainActivity.PAGE_FOUR:
                fragment = textFragment;
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return PAGER_COUNT;
    }
}
