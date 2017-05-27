package com.reeching.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.reeching.bean.HechaInfobean.Infos;
import com.reeching.bluegrass.BeginToHecha;
import com.reeching.bluegrass.GoHere;
import com.reeching.bluegrass.R;

public class HechaAdapter extends BaseAdapter {
	private List<Infos> lists;
	private Context context;

	public HechaAdapter(List<Infos> lists, Context context) {
		this.context = context;
		this.lists = lists;
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
		final Infos infos = lists.get(position);
		viewholder vh = null;
		if (convertView == null) {
			vh = new viewholder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.waitforhecha, null);
			vh.tvgothere = (TextView) convertView
					.findViewById(R.id.waitforhecha_tv5);
			vh.tvlocation = (TextView) convertView
					.findViewById(R.id.waitforhecha_tv2);
			vh.tvtime = (TextView) convertView
					.findViewById(R.id.waitforhecha_tv7);
			vh.tvtheme = (TextView) convertView
					.findViewById(R.id.waitforhecha_tv1);
			vh.tvbeginhecha = (TextView) convertView
					.findViewById(R.id.waitforhecha_tv4);
			vh.tvgallery= (TextView) convertView.findViewById(R.id.waitforhecha_tv0);
			vh.iv = (ImageView) convertView.findViewById(R.id.waitforhecha_iv);
			convertView.setTag(vh);
		}
		vh = (viewholder) convertView.getTag();
		vh.tvgothere.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(context, GoHere.class);
				intent.putExtra("lat", infos.getExhibition().getMapLat());
				intent.putExtra("lng", infos.getExhibition().getMapLng());
				context.startActivity(intent);
			}
		});
		vh.tvbeginhecha.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(context, BeginToHecha.class);
				intent.putExtra("info", infos);
				context.startActivity(intent);
			}
		});
		vh.tvtheme.setText(infos.getExhibition().getTheme());
		vh.tvlocation.setText(infos.getExhibition().getAddress());
		vh.tvtime.setText(infos.getExhibition().getUpdateDate());
		vh.tvgallery.setText(infos.getExhibition().getGalleryName());
		if (infos.getExhibition().getCareLevel().equals("0")) {
			vh.iv.setImageResource(R.drawable.green);
		} else if (infos.getExhibition().getCareLevel().equals("1")) {
			vh.iv.setImageResource(R.drawable.yellow);

		} else {
			vh.iv.setImageResource(R.drawable.red);
		}
		return convertView;
	}

	static class viewholder {
		TextView tvtheme, tvlocation, tvtime, tvgothere, tvbeginhecha,tvgallery;
		ImageView iv;

	}
}
