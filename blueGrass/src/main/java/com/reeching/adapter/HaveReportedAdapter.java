package com.reeching.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.reeching.bean.ZhanlanBean;
import com.reeching.bluegrass.R;
import com.reeching.utils.HttpApi;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/8.
 */

public class HaveReportedAdapter extends BaseAdapter {

    private List<ZhanlanBean.Infos> lists;
    private Context context;
    private List<String> list = new ArrayList<String>();

    public HaveReportedAdapter(List<ZhanlanBean.Infos> lists, Context context) {
        this.lists = lists;
        this.context = context;

    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return null == lists ? 0 : lists.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return lists.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ZhanlanBean.Infos infos = lists.get(position);
        HaveReportedAdapter.viewholder vh = null;
        if (convertView == null) {
            vh = new HaveReportedAdapter.viewholder();
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.history_item, null);
            vh.tvtheme = (TextView) convertView
                    .findViewById(R.id.history_theme);
            vh.tvpeople = (TextView) convertView
                    .findViewById(R.id.history_cezhanren);
            vh.tvtimes = (TextView) convertView
                    .findViewById(R.id.history_times);
            vh.tvtimee = (TextView) convertView
                    .findViewById(R.id.history_timee);
            vh.lin = (LinearLayout) convertView.findViewById(R.id.history_lin);
            vh.zuoZhe = (TextView) convertView.findViewById(R.id.history_zuozhe);
            convertView.setTag(vh);
        } else {
            vh = (HaveReportedAdapter.viewholder) convertView.getTag();
        }
        vh.tvtheme.setText(infos.getTheme());
        vh.tvpeople.setText(infos.getManager());
        vh.zuoZhe.setText(infos.getAuthor());
        String beginTime = infos.getDateBegin();
        vh.tvtimes.setText(beginTime);
        String endTime = infos.getDateEnd();
        vh.tvtimee.setText(endTime);
        if (!"".equals(infos.getSmallPhoto())) {
            String sourceStr = infos.getSmallPhoto();
            String[] sourceStrArray = sourceStr.split("\\|");
            for (int ii = 1; ii < sourceStrArray.length; ii++) {
                list.add(sourceStrArray[ii]);
            }
        }
        for (int i = 0; i < (list.size()>3?3:list.size()); i++) {
            final ImageView mImageView = new ImageView(context);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    150, 150);
            params.setMargins(5, 5, 5, 5);
            mImageView.setLayoutParams(params);
            if(list.size()!=0){
                final String url = HttpApi.picip + list.get(i);
//			Picasso.with(context)
//			.load(url).into(mImageView);
                Picasso.with(context)
                        .load(url)
                        .resize(40, 40)
                        .placeholder(R.drawable.downing)              //添加占位图片
                        .error(R.drawable.error)
                        .centerCrop()
                        .into(mImageView);
                vh.lin.removeAllViews();
                vh.lin.addView(mImageView);}
        }
        return convertView;
    }

    static class viewholder {
        TextView tvtheme, tvpeople, tvtimes, tvtimee, zuoZhe;
        LinearLayout lin;
    }
}
