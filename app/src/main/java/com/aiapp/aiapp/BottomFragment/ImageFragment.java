package com.aiapp.aiapp.BottomFragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

public class ImageFragment extends Fragment {
    private Context mContext;
    private GridView grid_photo;
    private BaseAdapter mAdapter = null;
    private ArrayList<Icon> mData = null;

    public ImageFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.image_recognition_fragment, container, false);
        mContext = getActivity();
        grid_photo = (GridView)view.findViewById(R.id.grid_photo);

        mData = new ArrayList<Icon>();
        mData.add(new Icon(R.mipmap.iv_icon_1, "动物"));
        mData.add(new Icon(R.mipmap.iv_icon_2, "植物"));
        mData.add(new Icon(R.mipmap.iv_icon_3, "果蔬"));
        mData.add(new Icon(R.mipmap.iv_icon_4, "菜品"));
        mData.add(new Icon(R.mipmap.iv_icon_5, "地标"));
        mData.add(new Icon(R.mipmap.iv_icon_6, "通用物体"));
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
                intent.putExtra("MianType","image");
                String strSubType = "";
                switch (position)
                {
                    case 0:
                        strSubType = "动物";
                        break;
                    case 1:
                        strSubType = "植物";
                        break;
                    case 2:
                        strSubType = "果蔬";
                        break;
                    case 3:
                        strSubType = "菜品";
                        break;
                    case 4:
                        strSubType = "地标";
                        break;
                    case 5:
                        strSubType = "通用物体";
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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
}
