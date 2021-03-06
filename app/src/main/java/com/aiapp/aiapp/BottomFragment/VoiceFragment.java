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

import com.aiapp.aiapp.R;
import com.aiapp.aiapp.voiceProc.VoiceTts;
import com.aiapp.aiapp.voiceProc.Voice_Recognition;

import java.util.ArrayList;

/**
 * Created by wjy on 2019/10/26.
 */

public class VoiceFragment extends Fragment {
    private Context mContext;
    private GridView grid_photo;
    private BaseAdapter mAdapter = null;
    private ArrayList<Icon> mData = null;

    public VoiceFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.voice_fragment, container, false);
        mContext = getActivity();
        grid_photo = (GridView)view.findViewById(R.id.grid_voice_photo);

        mData = new ArrayList<Icon>();
        mData.add(new Icon(R.mipmap.voice, "语音识别"));
        mData.add(new Icon(R.mipmap.voice2, "语音合成"));

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
                        String strSubType = "语音识别";
                        Intent intent= new Intent(getActivity(),Voice_Recognition.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                        startActivity(intent);
                    }
                    break;
                    case 1:
                    {
                        String strSubType = "语音合成";
                        Intent intent= new Intent(getActivity(),VoiceTts.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
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
