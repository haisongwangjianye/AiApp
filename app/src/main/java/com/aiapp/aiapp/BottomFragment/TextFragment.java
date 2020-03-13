package com.aiapp.aiapp.BottomFragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;

import com.aiapp.aiapp.ImageProcActivity;
import com.aiapp.aiapp.R;

import java.util.ArrayList;

/**
 * Created by wjy on 2019/10/26.
 */

public class TextFragment extends Fragment {
    private Context mContext;
    private GridView grid_photo;
    private BaseAdapter mAdapter = null;
    private ArrayList<Icon> mData = null;

    public TextFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.text_recognition_fragment, container, false);
        mContext = getActivity();
        grid_photo = (GridView)view.findViewById(R.id.grid_text_photo);

        mData = new ArrayList<Icon>();
        mData.add(new Icon(R.mipmap.txt, "通用文字"));
        mData.add(new Icon(R.mipmap.idcard, "身份证"));
        mData.add(new Icon(R.mipmap.bandcard, "银行卡"));
        mData.add(new Icon(R.mipmap.jiashizheng, "驾驶证"));
        mData.add(new Icon(R.mipmap.xingshizheng, "行驶证"));
        mData.add(new Icon(R.mipmap.chepai, "车牌"));
        //mData.add(new Icon(R.mipmap.iv_icon_7, "通用票据"));

        mAdapter = new GridAdapter<Icon>(mData, R.layout.item_grid_icon) {
            @Override
            public void bindView(ViewHolder holder, Icon obj) {
                holder.setImageResource(R.id.img_icon, obj.getiId());
                holder.setText(R.id.txt_icon, obj.getiName());
            }
        };

        grid_photo.setAdapter(mAdapter);

        grid_photo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(mContext, "你点击了~" + position + "~项", Toast.LENGTH_SHORT).show();
                Intent intent= new Intent(getActivity(),ImageProcActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                intent.putExtra("MianType","text");
                String strSubType = "";
                switch (position)
                {
                    case 0:
                        strSubType = "通用文字";
                        break;
                    case 1:
                        strSubType = "身份证";
                        break;
                    case 2:
                        strSubType = "银行卡";
                        break;
                    case 3:
                        strSubType = "驾驶证";
                        break;
                    case 4:
                        strSubType = "行驶证";
                        break;
                    case 5:
                        strSubType = "车牌";
                        break;
                    case 7:
                        strSubType = "通用票据";
                        break;
                    default:
                        break;
                }
                intent.putExtra("Subtype",strSubType);
                startActivity(intent);
            }
        });
        return view;
    }
}
