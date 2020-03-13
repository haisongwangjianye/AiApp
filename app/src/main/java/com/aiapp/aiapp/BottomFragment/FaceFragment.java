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
import com.aiapp.aiapp.faceProc.Face_Compare;

import java.util.ArrayList;

/**
 * Created by wjy on 2019/10/26.
 */

public class FaceFragment extends Fragment {
    private Context mContext;
    private GridView grid_photo;
    private BaseAdapter mAdapter = null;
    private ArrayList<Icon> mData = null;

    public FaceFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.face_recognition_fragment, container, false);
        mContext = getActivity();
        grid_photo = (GridView)view.findViewById(R.id.grid_face_photo);

        mData = new ArrayList<Icon>();
        mData.add(new Icon(R.mipmap.face1, "人脸检测"));
        mData.add(new Icon(R.mipmap.face2, "人脸对比"));

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


                switch (position)
                {
                    case 0:
                    {
                        String strSubType = "人脸检测";
                        Intent intent= new Intent(getActivity(),ImageProcActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                        intent.putExtra("MianType","face");
                        intent.putExtra("Subtype",strSubType);
                        startActivity(intent);
                    }
                    break;
                    case 1:
                    {
                        String strSubType = "人脸对比";
                        Intent intent= new Intent(getActivity(),Face_Compare.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                        intent.putExtra("MianType","face");
                        intent.putExtra("Subtype",strSubType);
                        startActivity(intent);
                    }
                    break;
                    default:
                        break;
                }

            }
        });
        return view;
    }


}
