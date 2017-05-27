package com.reeching.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.reeching.bean.HuaLangShowing;
import com.reeching.bluegrass.HualangHistoryActivity;
import com.reeching.bluegrass.R;
import com.reeching.bluegrass.XiaJiaActivity;

import java.util.List;

/**
 * Created by Administrator on 2017/3/13.
 */

public class DeleteHuaLangAdapter extends BaseAdapter {

    private List<HuaLangShowing.Infos> infos;
    private Context context;

    public DeleteHuaLangAdapter(List<HuaLangShowing.Infos> infos, Context context) {
        this.context = context;
        this.infos = infos;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return infos.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return infos.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        final HuaLangShowing.Infos info = infos.get(position);
        DeleteHuaLangAdapter.ViewHolder vh = new DeleteHuaLangAdapter.ViewHolder();
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.delhualanginfo_item, null);
            vh.tvname = (TextView) convertView
                    .findViewById(R.id.hualanginfo_tv1);
            vh.tvlocation = (TextView) convertView
                    .findViewById(R.id.hualanginfo_tv2);
            vh.ivlever = (ImageView) convertView
                    .findViewById(R.id.hualanginfo_iv);
            vh.tvtime = (TextView) convertView
                    .findViewById(R.id.hualanginfo_tv7);
            vh.tvgothere = (TextView) convertView
                    .findViewById(R.id.hualanginfo_tv5);
            vh.tvcheck = (TextView) convertView
                    .findViewById(R.id.hualanginfo_tv4);
            convertView.setTag(vh);
        } else {
            vh = (DeleteHuaLangAdapter.ViewHolder) convertView.getTag();
        }

        vh.tvname.setText(info.getName());
        vh.tvlocation.setText("".equals(info.getLinkMan())?"无":info.getLinkMan());
        if (info.getCareLevel().equals("0")) {
            vh.ivlever.setImageResource(R.drawable.green);

        } else if (info.getCareLevel().equals("1")) {
            vh.ivlever.setImageResource(R.drawable.yellow);
        } else {
            vh.ivlever.setImageResource(R.drawable.red);
        }
        vh.tvtime.setText(info.getCreateDate());
        vh.tvcheck.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(context,
                        XiaJiaActivity.class);
                HuaLangShowing.Infos info = infos.get(position);
                intent.putExtra("info", info);
                context.startActivity(intent);

            }
        });

        vh.tvgothere.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, HualangHistoryActivity.class);
                String id = infos.get(position).getId();
                Log.d("xiaoru", "onClick: " + id);
                intent.putExtra("id", id);
                context.startActivity(intent);
                // TODO Auto-generated method stub


            }
        });
        return convertView;

    }

    static class ViewHolder {
        TextView tvname, tvlocation, tvcheck, tvgothere, tvtime;
        ImageView ivlever;
    }

}
