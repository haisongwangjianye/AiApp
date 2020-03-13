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


public class CarFragment extends Fragment {

    private Context mContext;
    private GridView grid_photo;
    private BaseAdapter mAdapter = null;
    private ArrayList<Icon> mData = null;
    public CarFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_car, container, false);

        mContext = getActivity();
        grid_photo = (GridView)view.findViewById(R.id.grid_voice_photo);

        mData = new ArrayList<Icon>();
        mData.add(new Icon(R.mipmap.car, "车型识别"));
        mData.add(new Icon(R.mipmap.vehicle_detect, "车辆检测"));
       // mData.add(new Icon(R.mipmap.voice2, "车辆外观损伤识别"));

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
                Intent intent= new Intent(getActivity(),ImageProcActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                intent.putExtra("MianType","car");
                String strSubType = "";
                switch (position)
                {
                    case 0:
                        strSubType = "车型识别";
                        break;
                    case 1:
                        strSubType = "车辆检测";
                        break;
                    case 2:
                        strSubType = "车辆外观损伤识别";
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
