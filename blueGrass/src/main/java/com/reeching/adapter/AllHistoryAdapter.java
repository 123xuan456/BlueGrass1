package com.reeching.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.reeching.bean.AllHuaLangHistory;
import com.reeching.bluegrass.R;
import com.reeching.utils.HttpApi;
import com.squareup.picasso.Picasso;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/3/7.
 */

public class AllHistoryAdapter extends BaseAdapter{
    private List<AllHuaLangHistory.Infos> lists;
    private Context context;


    public AllHistoryAdapter(List<AllHuaLangHistory.Infos> lists, Context context) {
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
        AllHuaLangHistory.Infos infos = lists.get(position);
        AllHistoryAdapter.viewholder vh = null;
        if (convertView == null) {
            vh = new AllHistoryAdapter.viewholder();
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
            vh.infoHuaLang = ((TextView) convertView.findViewById(R.id.history_hualangname));
            vh.zuoZhe = (TextView) convertView.findViewById(R.id.history_zuozhe);
            convertView.setTag(vh);
        } else {
            vh = (AllHistoryAdapter.viewholder) convertView.getTag();
        }
        vh.tvtheme.setText(infos.getTheme());
        vh.tvpeople.setText(infos.getManager());
        vh.infoHuaLang.setText(infos.getGalleryName());
        vh.zuoZhe.setText(infos.getAuthor());
        Date dateBegin = infos.getDateBegin();
        SimpleDateFormat formatter   =
                new SimpleDateFormat( "yyyy-MM-dd ");
        String beginTime = formatter.format(dateBegin);

        vh.tvtimes.setText(beginTime);
        Date dateEnd = infos.getDateEnd();
        String endTime = formatter.format(dateEnd);
        vh.tvtimee.setText(endTime);
        vh.lin.removeAllViews();
        vh.list.clear();
        if (!"".equals(infos.getSmallPhoto())) {
            String sourceStr = infos.getSmallPhoto();
            String[] sourceStrArray = sourceStr.split("\\|");
            for (int ii = 1; ii < sourceStrArray.length; ii++) {
                vh.list.add(sourceStrArray[ii]);
            }
        }
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

    static class viewholder {
        TextView tvtheme, tvpeople, tvtimes, tvtimee, infoHuaLang, zuoZhe;
        LinearLayout lin;
         List<String> list = new ArrayList<String>();
    }
}
