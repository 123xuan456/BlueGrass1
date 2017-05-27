package com.reeching.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.reeching.bean.ZhanlanBean.Infos;
import com.reeching.bluegrass.R;
import com.reeching.utils.HttpApi;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ZhanLanAdapter extends BaseAdapter {
    private List<Infos> infos;
    private Context context;

    public ZhanLanAdapter(List<Infos> infos, Context context) {
        this.context = context;
        this.infos = infos;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return 0 == infos.size() ? 0 : infos.size();
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
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Infos info = infos.get(position);
        viewHolder vh = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.detaireported_item, null);
            vh = new viewHolder();
            vh.tvname = (TextView) convertView.findViewById(R.id.item_name);
            vh.lin= (LinearLayout) convertView.findViewById(R.id.item_lin);
            vh.tvlocation = (TextView) convertView
                    .findViewById(R.id.item_location);
            vh.tvpeople = (TextView) convertView.findViewById(R.id.item_people);
            vh.tvstarttime = (TextView) convertView
                    .findViewById(R.id.item_starttime);
            vh.tvendtime = (TextView) convertView
                    .findViewById(R.id.item_endtime);
            vh.zuoZhe = ((TextView) convertView.findViewById(R.id.history_zuozhe));
            vh.currentTime = ((TextView) convertView.findViewById(R.id.history_currenttime));
            convertView.setTag(vh);
        } else {
            vh = (viewHolder) convertView.getTag();
        }
        vh.lin.removeAllViews();
        vh.list.clear();
        if (!"".equals(info.getSmallPhoto())) {
            String sourceStr = info.getSmallPhoto();
            String[] sourceStrArray = sourceStr.split("\\|");
            for (int ii = 1; ii < sourceStrArray.length; ii++) {
                vh.list.add(sourceStrArray[ii]);
            }
        }
        vh.tvname.setText(info.getTheme());
        vh.tvlocation.setText(info.getGalleryName());
        vh.tvpeople.setText(info.getManager());
        vh.tvstarttime.setText("展览开始：" + info.getDateBegin());
        vh.tvendtime.setText("展览结束：" + info.getDateEnd());
        vh.zuoZhe.setText(info.getAuthor());
        vh.currentTime.setText(info.getCreateDate());
        for (int i = 0; i < (vh.list.size()>3?3:vh.list.size()); i++) {
            final ImageView mImageView = new ImageView(context);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    150, 150);
            params.setMargins(5, 5, 5, 5);
            mImageView.setLayoutParams(params);
            if(vh.list.size()!=0){
                final String url = HttpApi.picip + vh.list.get(i);
                Picasso.with(context)
                        .load(url)
                        .resize(40, 40)
                        .placeholder(R.drawable.downing)              //添加占位图片
                        .error(R.drawable.error)
                        .centerCrop()
                        .into(mImageView);
                vh.lin.addView(mImageView);}
        }
        return convertView;
    }

    static class viewHolder {
        TextView tvname, tvlocation, tvpeople, tvstarttime, tvendtime, zuoZhe, currentTime;
        LinearLayout lin;
        List<String> list = new ArrayList<String>();
    }
}
